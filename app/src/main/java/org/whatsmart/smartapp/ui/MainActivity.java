package org.whatsmart.smartapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.whatsmart.smartapp.R;

import java.util.zip.Inflater;

/**
 * Created by blue on 2016/3/7.
 */
public class MainActivity extends AppCompatActivity {

    private Fragment curFragment;
    private FragmentManager fragmentManager;

    private LinearLayout tabs;

    private LinearLayout taskTab;
    private LinearLayout deviceTab;
    private LinearLayout msgTab;
    private LinearLayout meTab;
    private LinearLayout settingTab;

    private Fragment taskFagment = null;
    private Fragment deviceFragment = null;
    private Fragment msgFragment = null;
    private Fragment meFragment = null;
    private Fragment settingFragment = null;

    private PopupWindow popwinVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load layout
        setContentView(R.layout.activity_main);

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
        fragmentManager.beginTransaction().replace(R.id.fragment_content, msgFragment, "msg").commit();
        curFragment = msgFragment;

        //tabs switch
        taskTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskFagment == null)
                    taskFagment = new TaskFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_content, taskFagment, "task").commit();
                curFragment = taskFagment;
                ImageView icon = (ImageView) findViewById(R.id.task_icon);
                TextView label = (TextView) findViewById(R.id.task_label);
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
                fragmentManager.beginTransaction().replace(R.id.fragment_content, deviceFragment, "device").commit();
                curFragment = deviceFragment;
                ImageView icon = (ImageView) findViewById(R.id.device_icon);
                TextView label = (TextView) findViewById(R.id.device_label);
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
                fragmentManager.beginTransaction().replace(R.id.fragment_content, meFragment, "me").commit();
                curFragment = meFragment;
                ImageView icon = (ImageView) findViewById(R.id.me_icon);
                TextView label = (TextView) findViewById(R.id.me_label);
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
                fragmentManager.beginTransaction().replace(R.id.fragment_content, settingFragment, "setting").commit();
                curFragment = settingFragment;
                ImageView icon = (ImageView) findViewById(R.id.setting_icon);
                TextView label = (TextView) findViewById(R.id.setting_label);
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

                } else if (curFragment != msgFragment) {
                    fragmentManager.beginTransaction().replace(R.id.fragment_content, msgFragment, "msg").commit();
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
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    }
                }
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

    public boolean isClickMe (float x, float y, View view) {
        if (x < view.getX() + view.getHeight() && x > view.getX() && y > view.getY() && y < view.getY() + view.getHeight())
            return true;
        return false;
    }

    public void setTabUnactived() {
        ImageView icon = (ImageView) findViewById(R.id.task_icon);
        icon.setImageResource(R.drawable.task_icon);
        icon = (ImageView) findViewById(R.id.device_icon);
        icon.setImageResource(R.drawable.device_icon);
        icon = (ImageView) findViewById(R.id.me_icon);
        icon.setImageResource(R.drawable.me_icon);
        icon = (ImageView) findViewById(R.id.setting_icon);
        icon.setImageResource(R.drawable.setting_icon);

        TextView label = (TextView) findViewById(R.id.task_label);
        label.setTextColor(0xff333333);
        label = (TextView) findViewById(R.id.device_label);
        label.setTextColor(0xff333333);
        label = (TextView) findViewById(R.id.me_label);
        label.setTextColor(0xff333333);
        label = (TextView) findViewById(R.id.setting_label);
        label.setTextColor(0xff333333);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
