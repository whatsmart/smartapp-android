package org.whatsmart.smartapp.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.whatsmart.smartapp.SmartApp;
import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.base.device.Device;

import java.util.ArrayList;

/**
 * Created by blue on 2016/3/7.
 */
public class MainActivity extends AppCompatActivity {

    private Fragment curFragment;
    private FragmentManager fragmentManager;

    private LinearLayout taskTab;
    private LinearLayout deviceTab;
    private LinearLayout meTab;
    private LinearLayout settingTab;

    private Fragment taskFagment = null;
    private Fragment deviceFragment = null;
    private Fragment msgFragment = null;
    private Fragment meFragment = null;
    private Fragment settingFragment = null;

    private SmartApp smartApp;
    private LinearLayout tabs;
    private LinearLayout msgTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //application
        smartApp = (SmartApp) getApplication();
        smartApp.setDevices(new ArrayList<Device>());

        //load layout
        setContentView(R.layout.activity_main);

        //system bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintColor(0xff0066cc);

        View view = findViewById(R.id.main_container);
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        view.setPadding(0, config.getPixelInsetTop(false), config.getPixelInsetRight(), config.getPixelInsetBottom());

        //init filed
        fragmentManager = getSupportFragmentManager();

        tabs = (LinearLayout) findViewById(R.id.tabs);

        taskTab = (LinearLayout) findViewById(R.id.task_tab);
        deviceTab = (LinearLayout) findViewById(R.id.device_tab);
        msgTab = (LinearLayout) findViewById(R.id.msg_tab);
        meTab = (LinearLayout) findViewById(R.id.me_tab);
        settingTab = (LinearLayout) findViewById(R.id.setting_tab);

        //setup msg fragment
        msgFragment = new MsgFragment();
        fragmentManager.beginTransaction().replace(R.id.fragment_container_main, msgFragment, "msg").commit();
        curFragment = msgFragment;

        //tabs switch
        taskTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskFagment == null)
                    taskFagment = new TaskFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container_main, taskFagment, "task").commit();
                curFragment = taskFagment;
                ImageView icon = (ImageView) findViewById(R.id.task_tab_icon);
                TextView label = (TextView) findViewById(R.id.task_tab_label);
                setTabUnactived();
                icon.setImageResource(R.drawable.task_icon_actived);
                label.setTextColor(0xff0066cc);
            }
        });

        deviceTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceFragment == null)
                    deviceFragment = new DeviceFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container_main, deviceFragment, "device").commit();
                curFragment = deviceFragment;
                ImageView icon = (ImageView) findViewById(R.id.device_tab_icon);
                TextView label = (TextView) findViewById(R.id.device_tab_label);
                setTabUnactived();
                icon.setImageResource(R.drawable.device_icon_actived);
                label.setTextColor(0xff0066cc);
            }
        });

        meTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (meFragment == null)
                    meFragment = new MeFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container_main, meFragment, "me").commit();
                curFragment = meFragment;
                ImageView icon = (ImageView) findViewById(R.id.me_tab_icon);
                TextView label = (TextView) findViewById(R.id.me_tab_label);
                setTabUnactived();
                icon.setImageResource(R.drawable.me_icon_actived);
                label.setTextColor(0xff0066cc);
            }
        });

        settingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingFragment == null)
                    settingFragment = new SettingFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_container_main, settingFragment, "setting").commit();
                curFragment = settingFragment;
                ImageView icon = (ImageView) findViewById(R.id.setting_tab_icon);
                TextView label = (TextView) findViewById(R.id.setting_tab_label);
                setTabUnactived();
                icon.setImageResource(R.drawable.setting_icon_actived);
                label.setTextColor(0xff0066cc);
            }
        });

        //voice button
        ImageButton btnVoice = (ImageButton) findViewById(R.id.btn_voice);
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curFragment == msgFragment) {
                    System.out.println("clicked, do nothing");
                } else if (curFragment != msgFragment) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_container_main, msgFragment, "msg").commit();
                    curFragment = msgFragment;
                    setTabUnactived();
                }
            }
        });

        btnVoice.setOnTouchListener(new View.OnTouchListener() {
            private long startTime, endTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (curFragment == msgFragment) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        System.out.println("touch");
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        System.out.println("up");
                    }
                }
                //return true, onTouchEvent will not perform ,onclick will not perform, too
                return false;
            }
        });

        /*
        tabs.setOnTouchListener(new View.OnTouchListener() {
            private float origX, origY;
            private float finalX, finalY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    origX = event.getX();
                    origY = event.getY();

                    if (curFragment == msgFragment && isClickMe(origX, origY, msgTab)) {
                        System.out.println("popup voice window");
                    }
//                    System.out.println("tabs Down: origX->" + origX + ", origY->" + origY);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    finalX = event.getX();
                    finalY = event.getY();
//                    System.out.println("tabs UP: finalX->" + finalX + ", finalY->" + finalY);
                    if (origY - finalY >= 35 && finalX - origX <= 35 && !isClickMe(origX, origY, msgTab)) {
                        System.out.println("flip up");
                        //此处向上滑出输入框
                    } else if (Math.abs(origY - finalY) <= 12 && Math.abs(finalX - origX) <= 12) {

                        if (isClickMe(origX, origY, taskTab)) {
                            if (taskFagment == null)
                                taskFagment = new TaskFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_content, taskFagment, "task").commit();
                            curFragment = taskFagment;
                            ImageView icon = (ImageView) findViewById(R.id.task_icon);
                            TextView label = (TextView) findViewById(R.id.task_label);
                            setTabUnactived();
                            icon.setImageResource(R.drawable.task_icon_actived);
                            label.setTextColor(0xff0066cc);
                        } else if (isClickMe(origX, origY, deviceTab)) {
                            if (deviceFragment == null)
                                deviceFragment = new DeviceFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_content, deviceFragment, "device").commit();
                            curFragment = deviceFragment;
                            ImageView icon = (ImageView) findViewById(R.id.device_icon);
                            TextView label = (TextView) findViewById(R.id.device_label);
                            setTabUnactived();
                            icon.setImageResource(R.drawable.device_icon_actived);
                            label.setTextColor(0xff0066cc);
                        } else if (isClickMe(origX, origY, meTab)) {
                            if (meFragment == null)
                                meFragment = new MeFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_content, meFragment, "me").commit();
                            curFragment = meFragment;
                            ImageView icon = (ImageView) findViewById(R.id.me_icon);
                            TextView label = (TextView) findViewById(R.id.me_label);
                            setTabUnactived();
                            icon.setImageResource(R.drawable.me_icon_actived);
                            label.setTextColor(0xff0066cc);
                        } else if (isClickMe(origX, origY, settingTab)) {
                            if (settingFragment == null)
                                settingFragment = new SettingFragment();
                            fragmentManager.beginTransaction().replace(R.id.fragment_content, settingFragment, "setting").commit();
                            curFragment = settingFragment;
                            ImageView icon = (ImageView) findViewById(R.id.setting_icon);
                            TextView label = (TextView) findViewById(R.id.setting_label);
                            setTabUnactived();
                            icon.setImageResource(R.drawable.setting_icon_actived);
                            label.setTextColor(0xff0066cc);
                        } else if (isClickMe(origX, origY, msgTab)) {
                            if (curFragment == msgFragment) {
                                System.out.println("send the voice");
                            } else if (curFragment != msgFragment) {
                                fragmentManager.beginTransaction().replace(R.id.fragment_content, msgFragment, "msg").commit();
                                curFragment = msgFragment;
                                setTabUnactived();
                            }
                        }
                    }
                }
                return true;
            }
        });
        */


    }
/*
    public boolean isClickMe (float x, float y, View view) {
        if (x < view.getX() + view.getHeight() && x > view.getX() && y > view.getY() && y < view.getY() + view.getHeight())
            return true;
        return false;
    }
*/
    public void setTabUnactived() {
        ImageView icon = (ImageView) findViewById(R.id.task_tab_icon);
        icon.setImageResource(R.drawable.task_icon);
        icon = (ImageView) findViewById(R.id.device_tab_icon);
        icon.setImageResource(R.drawable.device_icon);
        icon = (ImageView) findViewById(R.id.me_tab_icon);
        icon.setImageResource(R.drawable.me_icon);
        icon = (ImageView) findViewById(R.id.setting_tab_icon);
        icon.setImageResource(R.drawable.setting_icon);

        TextView label = (TextView) findViewById(R.id.task_tab_label);
        label.setTextColor(0xff333333);
        label = (TextView) findViewById(R.id.device_tab_label);
        label.setTextColor(0xff333333);
        label = (TextView) findViewById(R.id.me_tab_label);
        label.setTextColor(0xff333333);
        label = (TextView) findViewById(R.id.setting_tab_label);
        label.setTextColor(0xff333333);
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
