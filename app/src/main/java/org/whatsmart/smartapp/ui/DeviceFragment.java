package org.whatsmart.smartapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import org.whatsmart.smartapp.server.gateway.DeviceHandler;
import org.whatsmart.smartapp.server.gateway.DeviceManager;

import java.util.List;

/**
 * Created by blue on 2016/3/9.
 */
public class DeviceFragment extends Fragment {
    private DeviceManager deviceManager;
    private List<Device> devices = null;
    private Handler handler;
    private DeviceListAdapter devListAdapter;
    private ListView lv_devices;
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

        String apiAddress = smartApp.gateway_url + "/jsonrpc/v1.0/device";
        DeviceManager.setup(apiAddress);

        try {
            new UIGetDevicesTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.container_device_list);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new UIRefreshGetDevicesTask(refreshLayout).execute();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UIRefreshGetDevicesTask extends DeviceManager.GetDevicesTask {
        public SwipeRefreshLayout refreshLayout;
        public UIRefreshGetDevicesTask (SwipeRefreshLayout refreshLayout) {
            this.refreshLayout = refreshLayout;
        }
        @Override
        protected void onPostExecute(Object o) {
            if (o != null) {
                devices.clear();
                devices.addAll((List<Device>) o);
                //@todo 重新设置adapter，数据的内容更新，但getitemid返回相同，否则不能更新
                lv_devices.setAdapter(devListAdapter);
                devListAdapter.notifyDataSetChanged();
            }
            refreshLayout.setRefreshing(false);
            super.onPostExecute(o);
        }
    }

    class UIGetDevicesTask extends DeviceManager.GetDevicesTask {
        @Override
        protected void onPostExecute(Object o) {
            if (o != null) {
                devices.addAll((List<Device>) o);
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
            if (convertView != null) {
                view = convertView;
            } else {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_device_list, null);
                Device device = devices.get(position);

                ImageView icon = (ImageView) view.findViewById(R.id.device_icon);
                TextView name = (TextView) view.findViewById(R.id.device_name);
                TextView status = (TextView) view.findViewById(R.id.device_status);

                if ("lighting".equalsIgnoreCase(device.getType())) {
                    icon.setImageResource(R.drawable.device_lighting_icon);
                }
                name.setText(device.getName() + "(" + device.getPosition() + ")");
                Resources res = getResources();
                try {
                    status.setText(String.format(res.getString(R.string.lighting_state), device.getState().get("power"),
                                   device.getState().get("brightness"), device.getState().get("color")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private class DeviceManagerHandler extends DeviceHandler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GET_ONE_DEVICE) {
                Device dev = new Device();
                dev.setId(msg.getData().getInt("id"));
                dev.setName(msg.getData().getString("name"));
                dev.setType(msg.getData().getString("type"));
                devices.add(dev);
                devListAdapter.notifyDataSetChanged();
            } if (msg.what == GET_ALL_DEVICES) {

            }

            super.handleMessage(msg);
        }
    }
}
