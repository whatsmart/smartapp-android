package org.whatsmart.smartapp.server.smarthub;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import org.whatsmart.smartapp.base.device.Device;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/**
 * Created by blue on 2016/3/16.
 */
public class DeviceManager {
    private ArrayList<Device> devices;
    private Handler handler;
    private String apiAddress;

    public DeviceManager(Handler handler, String apiAddress) {
        this.handler = handler;
        this.apiAddress = apiAddress;
    }

    public void getDevices() {
        try {
            JSONRPC2Session mySession = new JSONRPC2Session(new URL(apiAddress));
            JSONRPC2Request request = new JSONRPC2Request("get_devices", 1);
            // Send request
            JSONRPC2Response response = null;
            try {
                response = mySession.send(request);
            } catch (JSONRPC2SessionException e) {
                System.err.println(e.getMessage());
                // handle exception...
            }
            // Print response result / error
            if (response.indicatesSuccess())
                System.out.println(response.getResult());
            else
                System.out.println(response.getError().getMessage());

            Message msg = new Message();
            msg.what = DeviceHandler.GET_ONE_ITEM;
            Bundle bundle = new Bundle();
            bundle.putInt("id", 2);
            bundle.putString("name", "阿凡达");
            bundle.putString("type", "lighting");
            msg.setData(bundle);
            handler.sendMessage(msg);
        }catch(Exception e) {

        }
    }

    public void getLatestDevices() {

    }

    public void getDevice(int id) {


    }

    public void getLatestDevice() {

    }
}
