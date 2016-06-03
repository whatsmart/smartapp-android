package org.whatsmart.smartapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.whatsmart.smartapp.SmartApp;
import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.base.device.Device;
import org.whatsmart.smartapp.server.smarthub.DeviceHandler;
import org.whatsmart.smartapp.server.smarthub.DeviceManager;

import java.util.ArrayList;

/**
 * Created by blue on 2016/3/9.
 */
public class DeviceFragment extends Fragment {

    private ArrayList<Device> devices = null;
    private Handler handler;
    private DeviceListAdapter devListAdapter;
    private ListView devList;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //toolbar_main
        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        setupToolbar();

        //menu
        //setHasOptionsMenu(true);

        SmartApp smartApp = (SmartApp) getActivity().getApplication();
        devices = smartApp.getDevices();

        Device light = new Device();
        light.setId(1);
        light.setType("lighting");
        light.setName("智能灯");
        devices.add(light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_device, container, false);

        //setup listview of device
        devList = (ListView) view.findViewById(R.id.listview_device);
        devListAdapter = new DeviceListAdapter();
        devList.setAdapter(devListAdapter);

        return view;
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
        ImageView addImg = new ImageView(getActivity());
        addImg.setImageResource(R.drawable.toolbar_device_add);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("add device clicked");
            }
        });

        ImageView refreshImg = new ImageView(getActivity());
        refreshImg.setImageResource(R.drawable.toolbar_device_refresh);
        refreshImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("refresh device clicked");
            }
        });

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Commen.dp2px(getActivity(), 24), Commen.dp2px(getActivity(), 24), Gravity.RIGHT);
        Toolbar.LayoutParams params1 = new Toolbar.LayoutParams(Commen.dp2px(getActivity(), 24), Commen.dp2px(getActivity(), 24), Gravity.RIGHT);

        toolbar.setTitle("设备");
        toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_Title);

        toolbar.addView(addImg, 1, params);

        params1.setMargins(0, 0, 45, 0);
        toolbar.addView(refreshImg, 2, params1);

//        toolbar.setLogo(R.drawable.toolbar_device_logo);
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        toolbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);

        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
    }


    private class DeviceListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
//            System.out.println("size:" + devices.size());
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            Device dev = devices.get(position);
            return dev;
        }

        @Override
        public long getItemId(int position) {
            Device dev = devices.get(position);
            return dev.getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            Device dev = devices.get(position);
            if (convertView != null) {
                view = convertView;
            } else {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_device_list, null);

//                RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.item_device_list);
                ImageView icon = (ImageView) view.findViewById(R.id.device_icon);
                TextView name = (TextView) view.findViewById(R.id.device_name);
                TextView status = (TextView) view.findViewById(R.id.device_status);

//                icon.setImageResource(R.drawable.setting_icon_actived);
                name.setText("智能灯");
                status.setText("电源打开");

                view.setOnClickListener(new DeviceItemOnClickListener(position));
            }
            return view;
        }
    }

    private class DeviceItemOnClickListener implements View.OnClickListener {
        private int id;
        public DeviceItemOnClickListener(int id) {
            this.id = id;
        }
        @Override
        public void onClick(View v) {
            Context context = getContext();
            Device dev = devices.get(id);
            String type = dev.getType();
            if ("lighting".equals(type)) {
                LightingConfig ltconfig = new LightingConfig(context, dev);
                ltconfig.config();
            }
        }
    }

    private class MyDeviceHandler extends DeviceHandler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GET_ONE_ITEM) {
                Device dev = new Device();
                dev.setId(msg.getData().getInt("id"));
                dev.setName(msg.getData().getString("name"));
                dev.setType(msg.getData().getString("type"));
                devices.add(dev);
                devListAdapter.notifyDataSetChanged();
            }
        }
    }
}
