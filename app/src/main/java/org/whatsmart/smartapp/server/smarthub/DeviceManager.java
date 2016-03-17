package org.whatsmart.smartapp.server.smarthub;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import org.whatsmart.smartapp.base.device.Device;

import java.net.HttpURLConnection;
import java.util.ArrayList;

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
        Message msg = new Message();
        msg.what = DeviceHandler.GET_ONE_ITEM;
        Bundle bundle = new Bundle();
        bundle.putInt("id", 2);
        bundle.putString("name", "阿凡达");
        bundle.putString("type", "lighting");
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void getLatestDevices() {

    }

    public void getDevice(int id) {


    }

    public void getLatestDevice() {

    }
}
