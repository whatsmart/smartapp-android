package org.whatsmart.smartapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.whatsmart.smartapp.SmartApp;
import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.base.device.Device;
import org.whatsmart.smartapp.server.gateway.APIDevice;
import org.whatsmart.smartapp.ui.control.Lighting;

import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by blue on 2016/3/9.
 */
public class DeviceFragment extends Fragment {
    private APIDevice deviceManager;
    private List<org.whatsmart.smartapp.base.device.Device> devices = null;
    private Handler handler;
    private DeviceListAdapter devListAdapter;
    private ListView lv_devices;
    private Toolbar toolbar;
    private String apiPrefix;
    private PushReceiver pushReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //toolbar_main
        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);

        //menu
        //setHasOptionsMenu(true);

        SmartApp smartApp = (SmartApp) getActivity().getApplication();
        devices = smartApp.getDevices();
        apiPrefix = ((SmartApp) getActivity().getApplication()).gateway_url + "/jsonrpc/v1.0";

        try {
            new UIGetDevicesTask().execute(apiPrefix + "/device");
        } catch (Exception e) {
            e.printStackTrace();
        }

        pushReceiver = new PushReceiver();
        IntentFilter intentFilter = new IntentFilter(JPushInterface.ACTION_MESSAGE_RECEIVED);
        intentFilter.addCategory(getContext().getPackageName());
        getContext().registerReceiver(pushReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();

        setupToolbar();

        try {
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.container_device_list);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new UIRefreshGetDevicesTask().execute(apiPrefix + "/device");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UIRefreshGetDevicesTask extends APIDevice.GetDevicesTask {
        @Override
        protected void onPostExecute(Object result) {
            SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.container_device_list);
            if (result != null) {
                devices.clear();
                devices.addAll((List<org.whatsmart.smartapp.base.device.Device>) result);
                //@todo 重新设置adapter，数据的内容更新，但getitemid返回相同，否则不能更新
                lv_devices.setAdapter(devListAdapter);
                devListAdapter.notifyDataSetChanged();
            }
            refreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    class UIGetDevicesTask extends APIDevice.GetDevicesTask {
        @Override
        protected void onPostExecute(Object o) {
            if (o != null) {
                devices.clear();
                devices.addAll((List<org.whatsmart.smartapp.base.device.Device>) o);
                devListAdapter.notifyDataSetChanged();
            }
            super.onPostExecute(o);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_device, container, false);

        //setup listview of device
        lv_devices = (ListView) view.findViewById(R.id.listview_device);
        devListAdapter = new DeviceListAdapter();
        lv_devices.setAdapter(devListAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getContext().unregisterReceiver(pushReceiver);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.fragment_background));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.device_menu, menu);
    }

    private void setupToolbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        //toolbar.setTitle("设备");
        //toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_Title);
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("设备");

        toolbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
        }
    }


    private class DeviceListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
//            System.out.println("size:" + devices.size());
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            org.whatsmart.smartapp.base.device.Device dev = devices.get(position);
            return dev;
        }

        @Override
        public long getItemId(int position) {
            org.whatsmart.smartapp.base.device.Device dev = devices.get(position);
            return dev.getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                //@todo 更新信息
                view = convertView;
            } else {
                view = getActivity().getLayoutInflater().inflate(R.layout.list_item_device, null);
                LinearLayout state = (LinearLayout) view.findViewById(R.id.device_state);
                View lighting_state = getActivity().getLayoutInflater().inflate(R.layout.list_item_device_info_lighting, state);
                org.whatsmart.smartapp.base.device.Device device = devices.get(position);

                ImageView icon = (ImageView) view.findViewById(R.id.device_icon);
                TextView name = (TextView) view.findViewById(R.id.device_name);

                TextView tv_power = (TextView) lighting_state.findViewById(R.id.tv_power);
                TextView tv_brightness = (TextView) lighting_state.findViewById(R.id.tv_brightness);
                TextView tv_color = (TextView) lighting_state.findViewById(R.id.tv_color);
                try {
                    if ("on".equalsIgnoreCase(device.getState().getPower())) {
                        tv_power.setText(R.string.on);
                    } else {
                        tv_power.setText(R.string.off);
                    }
                    tv_brightness.setText(String.format("%d", device.getState().getBrightness()));
                    try {
                        int c = 0xff << 30;
                        int color = Integer.valueOf(device.getState().getColor().substring(2), 16);
                        color = Common.removeLightness(color);
                        Log.d("DEVICE_LIST", String.format("%08x", c | color));
                        tv_color.setBackgroundColor(c | color);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if ("lighting".equalsIgnoreCase(device.getType())) {
                    icon.setImageResource(R.drawable.device_lighting_icon);
                }
                name.setText(device.getName() + "（" + device.getPosition() + "）");

                view.setOnClickListener(new DeviceItemOnClickListener(position));
                view.setOnLongClickListener(new DeviceItemOnLongClickListener(position));
            }
            return view;
        }
    }

    class DeviceItemOnClickListener implements View.OnClickListener {
        private int id;
        public DeviceItemOnClickListener(int id) {
            this.id = id;
        }
        @Override
        public void onClick(View v) {
            org.whatsmart.smartapp.base.device.Device dev = devices.get(id);
            String type = dev.getType();
            if ("lighting".equals(type)) {
                Lighting ltconfig = new Lighting((AppCompatActivity) getActivity(),lv_devices, apiPrefix, dev);
                ltconfig.show();
            }
        }
    }

    class DeviceItemOnLongClickListener implements View.OnLongClickListener {
        public int position;
        public DeviceItemOnLongClickListener (int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            final org.whatsmart.smartapp.base.device.Device device = devices.get(position);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            final View view = getActivity().getLayoutInflater().inflate(R.layout.device_setting_info, null);
            ((TextView)view.findViewById(R.id.title_device_setting)).setText("设备信息");
            ((EditText)view.findViewById(R.id.et_name)).setText(device.getName());
            ((EditText)view.findViewById(R.id.et_position)).setText(device.getPosition());
            builder.setView(view);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        EditText et_name = (EditText) view.findViewById(R.id.et_name);
                        EditText et_position = (EditText) view.findViewById(R.id.et_position);
                        final String name = et_name.getText().toString();
                        final String position = et_position.getText().toString();
                        TreeMap<String, Object> map = new TreeMap<String, Object>();
                        map.put("name", name);
                        map.put("position", position);

                        new APIDevice.SetInfoTask() {
                            @Override
                            protected void onPostExecute(Object o) {
                                super.onPostExecute(o);
                                Boolean result = (Boolean) o;
                                if (result) {
                                    device.setName(name);
                                    device.setPosition(position);
                                    lv_devices.setAdapter(devListAdapter);
                                    devListAdapter.notifyDataSetChanged();
                                }
                            }
                        }.execute(apiPrefix + "/device/" + device.getId(), map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setCancelable(true);
            builder.show();

            return true;
        }
    }

    class PushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            try {
                if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                    String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                    JsonParser parser = new JsonParser();
                    JsonElement msg = parser.parse(message);
                    Log.d("STATE_CHANGED", message);
                    if (msg instanceof JsonObject) {
                        JsonObject stateChanged = (JsonObject) ((JsonObject) msg).get("device_state_changed");
                        if (((JsonObject) msg).has("device_state_changed")) {
                            int id = stateChanged.get("id").getAsInt();
                            Log.d("STATE_CHANGED", "device id: " + id);
                            Device  device = null;
                            for (int i=0; i<devices.size(); i++) {
                                if (id == devices.get(i).getId()) {
                                    device = devices.get(i);
                                    break;
                                }
                            }
                            if (device != null) {
                                if ("lighting".equalsIgnoreCase(device.getType())) {
                                    JsonObject states = stateChanged.getAsJsonObject("state");
                                    if (states.has("power")) {
                                        device.getState().setPower(states.get("power").getAsString());
                                    }
                                    if (states.has("brightness")) {
                                        device.getState().setBrightness(states.get("brightness").getAsInt());
                                    }
                                    if (states.has("color")) {
                                        device.getState().setColor(states.get("color").getAsString());
                                    }
                                }
                                Log.d("STATE_CHANGED", "color: " + device.getState().getColor());
                                //@todo 更好的方法更新
                                lv_devices.setAdapter(lv_devices.getAdapter());
                            }
                        }
                    }

                } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
