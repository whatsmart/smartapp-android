package org.whatsmart.smartapp.ui;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.whatsmart.smartapp.SmartApp;
import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.base.device.Device;

import java.lang.reflect.Array;
import java.security.KeyStore;
import java.util.ArrayList;
import com.readystatesoftware.systembartint.SystemBarTintManager;
/**
 * Created by blue on 2016/3/7.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView lv_mainMenu;
    private int active_item = -1;
    private ArrayList<MainMenuItem> mainMenuItems;

    private Fragment msgFragment;
    private Fragment deviceFragment = null;
    private Fragment taskFragment = null;
    private Fragment meFragment = null;
    private Fragment settingFragment = null;
    private BaseAdapter mainMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenuItems = new ArrayList<>();
        MainMenuItem menuItem;
        menuItem = new MainMenuItem("msg", R.drawable.navi_msg, R.drawable.navi_msg_active, "消息");
        mainMenuItems.add(menuItem);
        menuItem = new MainMenuItem("device", R.drawable.navi_device,  R.drawable.navi_device_active, "设备管理");
        mainMenuItems.add(menuItem);
        menuItem = new MainMenuItem("task", R.drawable.navi_task, R.drawable.navi_task_active, "任务管理");
        mainMenuItems.add(menuItem);
        menuItem = new MainMenuItem("me", R.drawable.navi_me, R.drawable.navi_me_active, "个人中心");
        mainMenuItems.add(menuItem);
        menuItem = new MainMenuItem("setting", R.drawable.navi_setting, R.drawable.navi_setting_active, "系统设置");
        mainMenuItems.add(menuItem);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lv_mainMenu = (ListView) findViewById(R.id.main_menu);
        RelativeLayout drawer_header = (RelativeLayout) findViewById(R.id.drawer_header);

        // Set the adapter for the list view
        mainMenuAdapter = new MainMenuAdapter();
        lv_mainMenu.setAdapter(mainMenuAdapter);
        // Set the list's click listener
        lv_mainMenu.setOnItemClickListener(new DrawerItemClickListener());
        //drawerLayout.setScrimColor(Color.TRANSPARENT);

        //system bar tint
        setTranslucentStatus(true);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        drawer_header.setPadding(0, config.getPixelInsetTop(false), config.getPixelInsetRight(), config.getPixelInsetBottom());
        tintManager.setTintColor(0x00007fff);
        tintManager.setStatusBarTintEnabled(true);

        //主页显示消息列表
        msgFragment = new MsgFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, msgFragment, "msg")
                .show(msgFragment).commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        public void hideAllFragment() {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (msgFragment != null) {
                fragmentTransaction.hide(msgFragment);
            }
            if (deviceFragment != null) {
                fragmentTransaction.hide(deviceFragment);
            }
            if (taskFragment != null) {
                fragmentTransaction.hide(taskFragment);
            }
            if (meFragment != null) {
                fragmentTransaction.hide(meFragment);
            }
            if (settingFragment != null) {
                fragmentTransaction.hide(settingFragment);
            }
            fragmentTransaction.commit();
        }

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainMenuItem menuItem = mainMenuItems.get(position);

            if (menuItem.tag == "msg") {
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();
                    fragmentTransaction.add(R.id.content_frame, msgFragment, "msg");
                }
                hideAllFragment();
                fragmentTransaction.show(msgFragment).commit();
            } else if (menuItem.tag == "device") {
                if (deviceFragment == null) {
                    deviceFragment = new DeviceFragment();
                    fragmentTransaction.add(R.id.content_frame, deviceFragment, "device");
                }
                hideAllFragment();
                fragmentTransaction.show(deviceFragment).commit();
            } else if (menuItem.tag == "task") {
                if (taskFragment == null) {
                    taskFragment = new TaskFragment();
                    fragmentTransaction.add(R.id.content_frame, taskFragment, "task");
                }
                hideAllFragment();
                fragmentTransaction.show(taskFragment).commit();
            } else if (menuItem.tag == "me") {
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    fragmentTransaction.add(R.id.content_frame, meFragment, "me");
                }
                hideAllFragment();
                fragmentTransaction.show(meFragment).commit();
            } else if (menuItem.tag == "setting"){
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    fragmentTransaction.add(R.id.content_frame, settingFragment, "setting");
                }
                hideAllFragment();
                fragmentTransaction.show(settingFragment).commit();
            }

            // Highlight the selected item and close the drawer
            //lv_mainMenu.setItemChecked(position, true);
            active_item = position;
            mainMenuAdapter.notifyDataSetChanged();

            View leftDrawer = findViewById(R.id.left_drawer);
            drawerLayout.closeDrawer(leftDrawer);
        }
    }

    private class MainMenuItem {
        public String tag;
        public int icon;
        public int active_icon;
        public String label;

        public MainMenuItem(String id, int icon, int active_icon, String label) {
            this.tag = id;
            this.icon = icon;
            this.active_icon = active_icon;
            this.label = label;
        }
    }

    class MainMenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mainMenuItems.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = getLayoutInflater().inflate(R.layout.main_menu_item, null);
            }

            ImageView icon = (ImageView) view.findViewById(R.id.menu_item_icon);
            TextView label = (TextView) view.findViewById(R.id.menu_item_label);
            MainMenuItem menuItem = mainMenuItems.get(position);
            label.setText(menuItem.label);

            if (active_item == position) {
                label.setTextColor(0xff007fff);
                icon.setImageResource(menuItem.active_icon);
            } else {
                icon.setImageResource(menuItem.icon);
                label.setTextColor(0xff000000);
            }

            return view;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return mainMenuItems.get(position);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}