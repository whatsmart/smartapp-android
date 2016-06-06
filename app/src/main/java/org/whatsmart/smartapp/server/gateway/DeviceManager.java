package org.whatsmart.smartapp.server.gateway;

import android.os.AsyncTask;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import org.json.JSONArray;
import org.whatsmart.smartapp.base.device.Device;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by blue on 2016/3/16.
 */
public class DeviceManager {
    public static URL serverURL;

    public static void setup(String apiAddress) {
        try {
            serverURL = new URL(apiAddress);
        } catch (MalformedURLException e) {
            // handle exception...
        }
    }

    public static class GetDevicesTask extends AsyncTask {
        @Override
        public List<Device> doInBackground(Object[] params) {
            try {
                JSONRPC2Session session = new JSONRPC2Session(serverURL);
                JSONRPC2Request request = new JSONRPC2Request("get_devices", 1);
                JSONRPC2Response response = null;
                try {
                    response = session.send(request);
                } catch (JSONRPC2SessionException e) {
                    System.err.println(e.getMessage());
                    // handle exception...
                }
                if (response.indicatesSuccess()) {
                    net.minidev.json.JSONArray result = (net.minidev.json.JSONArray) response.getResult();
                    String json = result.toJSONString();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Device>>() {}.getType();
                    List<Device> devices = gson.fromJson(json, type);
                    return devices;
                } else {
                    System.out.println(response.getError().getMessage());
                    return null;
                }
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void getLatestDevices() {

    }

    public void getDevice(int id) {


    }

    public void getLatestDevice() {

    }
}
