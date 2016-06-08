package org.whatsmart.smartapp.server.gateway;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;


/**
 * Created by blue on 2016/3/16.
 */
public class APIDevice {
    public static URL getApiUrl(String apiAddress) {
        try {
            return new URL(apiAddress);
        } catch (MalformedURLException e) {
            // handle exception...
        }
        return null;
    }

    public static class GetDevicesTask extends AsyncTask {
        @Override
        public List<org.whatsmart.smartapp.base.device.Device> doInBackground(Object... args) {
            try {
                String apiAddress = (String) args[0];
                URL apiUrl = getApiUrl(apiAddress);
                if (apiUrl == null) {
                    return null;
                }
                JSONRPC2Session session = new JSONRPC2Session(apiUrl);
                JSONRPC2Request request = new JSONRPC2Request("get_devices", 1);
                JSONRPC2Response response = null;
                try {
                    response = session.send(request);
                } catch (JSONRPC2SessionException e) {
                    System.err.println(e.getMessage());
                    // handle exception...
                }
                if (response.indicatesSuccess()) {
                    JSONArray result = (JSONArray) response.getResult();
                    String json = result.toJSONString();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<org.whatsmart.smartapp.base.device.Device>>() {}.getType();
                    List<org.whatsmart.smartapp.base.device.Device> devices = gson.fromJson(json, type);
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

    public static class SetInfoTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... args) {
            try {
                String apiAddress = (String) args[0];
                URL apiUrl = getApiUrl(apiAddress);
                if (apiUrl == null) {
                    return false;
                }
                TreeMap<String, Object> params = (TreeMap<String, Object>) args[1];
                JSONRPC2Session session = new JSONRPC2Session(apiUrl);
                JSONRPC2Request request = new JSONRPC2Request("set_info", params, 1);
                JSONRPC2Response response = null;
                try {
                    response = session.send(request);
                } catch (JSONRPC2SessionException e) {
                    System.err.println(e.getMessage());
                    // handle exception...
                }
                if (response.indicatesSuccess()) {
                    Boolean result = (Boolean) response.getResult();
                    return result;
                } else {
                    System.out.println(response.getError().getMessage());
                    return false;
                }
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
