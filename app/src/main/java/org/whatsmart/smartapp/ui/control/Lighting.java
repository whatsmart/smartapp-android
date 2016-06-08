package org.whatsmart.smartapp.ui.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.base.device.Device;
import org.whatsmart.smartapp.server.jsonrpc.JSONRPCError;
import org.whatsmart.smartapp.server.jsonrpc.JSONRPCHTTPClient;
import org.whatsmart.smartapp.server.jsonrpc.JSONRPCResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by blue on 2016/3/16.
 */
public class Lighting {
    private AppCompatActivity activity;
    private JSONRPCHTTPClient client;
    private Device device;
    private Switch sw_power;
    private SeekBar sb_brightness;

    public Lighting(AppCompatActivity activity, String apiPrefix, Device dev) {
        this.activity = activity;
        device = dev;
        this.client = new JSONRPCHTTPClient(apiPrefix);
    }

    public void show() {
        try {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.device_control_lighting, null);

            sw_power = (Switch) view.findViewById(R.id.sw_power);
            sb_brightness = (SeekBar) view.findViewById(R.id.sb_brightness);

            if (device.getState().getPower() == "on") {
                sw_power.setChecked(true);
            } else {
                sw_power.setChecked(false);
            }

            sb_brightness.setProgress(device.getState().getBrightness());

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(view);
            builder.setPositiveButton("确定", new PositiveButtonClickListener());
            builder.setCancelable(true);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            JsonObject params = new JsonObject();
            //params.addProperty();
            client.call("/control/" + device.getId(), "set_state", null, new JSONRPCResponse.ResponseCallback() {
                @Override
                public void onResult(JsonElement result) {
                    Gson gson = new Gson();
                    try {
                        Type type = new TypeToken<List<Device>>(){}.getType();
                        List<Device> devs = gson.fromJson(result, type);

                        System.out.println(devs.get(0).getState().getBrightness());
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onError(JsonElement error) {
                    Gson gson = new Gson();
                    try {
                        final JSONRPCError err = gson.fromJson(error, JSONRPCError.class);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, err.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            });
        }
    }
}
