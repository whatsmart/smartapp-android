package org.whatsmart.smartapp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.whatsmart.smartapp.base.device.Device;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by blue on 2016/3/16.
 */
public class SmartApp extends Application {
    public String gateway_host;
    public String gateway_url;
    private ArrayList<Device> devices = new ArrayList<>();

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gateway_host = sharedPreferences.getString("gateway_address", "121.42.156.167");
        gateway_url = "http://" + gateway_host;

        JPushInterface.init(this);
        JPushInterface.setDebugMode(false);
        JPushInterface.clearAllNotifications(this);
    }
}
