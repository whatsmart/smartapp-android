package org.whatsmart.smartapp;

import android.app.Application;

import org.whatsmart.smartapp.base.device.Device;

import java.util.ArrayList;

/**
 * Created by blue on 2016/3/16.
 */
public class SmartApp extends Application {
    private ArrayList<Device> devices = new ArrayList<>();

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }
}
