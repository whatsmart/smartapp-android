package org.whatsmart.smartapp.ui.control;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.larswerkman.holocolorpicker.ColorPicker;

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
    private ListView lv_devices;
    private JSONRPCHTTPClient client;
    private Device device;
    private Switch sw_power;
    private SeekBar sb_brightness;
    private ColorPicker cp_color;

    public Lighting(AppCompatActivity activity, ListView lv_devices, String apiPrefix, Device dev) {
        this.activity = activity;
        device = dev;
        this.client = new JSONRPCHTTPClient(apiPrefix);
        this.lv_devices = lv_devices;
    }

    public void show() {
        try {
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.device_control_lighting, null);

            sw_power = (Switch) view.findViewById(R.id.sw_power);
            sb_brightness = (SeekBar) view.findViewById(R.id.sb_brightness);
            //@todo 实现饱和度和亮度
            cp_color = (ColorPicker) view.findViewById(R.id.cp_color);
            cp_color.setShowOldCenterColor(false);

            if ("on".equalsIgnoreCase(device.getState().getPower())) {
                sw_power.setChecked(true);
            } else {
                sw_power.setChecked(false);
            }

            sb_brightness.setProgress(device.getState().getBrightness());

            int c = 0xff << 30;
            int color = Integer.valueOf(device.getState().getColor().substring(2), 16);
            cp_color.setColor(c | color);

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
            final int brightness = sb_brightness.getProgress();
            final String power;
            if (sw_power.isChecked()) {
                power = "on";
            } else {
                power = "off";
            }

            int color = cp_color.getColor();
            color = color & 0x00ffffff;
            final String colorStr = "0x" + String.format("%06x", color);

            if (brightness == device.getState().getBrightness() &&
                    power.equalsIgnoreCase(device.getState().getPower()) &&
                    colorStr.equalsIgnoreCase(device.getState().getColor())) {
                return;
            }
            if (brightness != device.getState().getBrightness()) {
                params.addProperty("brightness", brightness);
            }
            if (!power.equalsIgnoreCase(device.getState().getPower())) {
                params.addProperty("power", power);
            }

            if (!colorStr.equalsIgnoreCase(device.getState().getColor())) {
                params.addProperty("color", colorStr);
            }

            client.call("/control/" + device.getId(), "set_state", params, new JSONRPCResponse.ResponseCallback() {
                @Override
                public void onResult(JsonElement result) {
                    if (result.getAsBoolean() == true) {
                        device.getState().setBrightness(brightness);
                        device.getState().setPower(power);
                        device.getState().setColor(colorStr);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lv_devices.setAdapter(lv_devices.getAdapter());
                            }
                        });
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
