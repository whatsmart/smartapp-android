package org.whatsmart.smartapp.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.whatsmart.smartapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by blue on 2016/3/7.
 */
public class MainActivity extends FragmentActivity {

    MainSwipeAdapter mAdapter;
    ViewPager mPager;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load layout
        setContentView(R.layout.activity_main);

        //actionbar
        actionBar = getActionBar();
        actionBar.setTitle("消息");

        //viewpager
        mAdapter = new MainSwipeAdapter(getSupportFragmentManager());
        mAdapter.addFragment(0, new MainFragment());
        mAdapter.addFragment(1, new TaskFragment());
        mAdapter.addFragment(2, new DeviceFragment());
        mAdapter.addFragment(3, new SettingFragment());
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("selected");
                switch (position) {
                    case 0:
                        actionBar.setTitle("消息");
                        break;
                    case 1:
                        actionBar.setTitle("任务");
                        break;
                    case 2:
                        actionBar.setTitle("设备");
                        break;
                    case 3:
                        actionBar.setTitle("设置");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(0);

        //********Below is for drawerlayout**********
        DrawerLayout mdrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mdrawerLayout,
                null,
                0,
                0
        );
        mdrawerLayout.setDrawerListener(mDrawerToggle);

        //main menu
        ListView menu = (ListView) findViewById(R.id.main_menu);

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        HashMap<String, Object> msg = new HashMap<String, Object>();
        msg.put("icon", R.drawable.bt_keyboard);
        msg.put("name", "消息");
        data.add(0, msg);

        HashMap<String, Object> task = new HashMap<String, Object>();
        task.put("icon", R.drawable.bt_voice);
        task.put("name", "任务");
        data.add(1, task);

        HashMap<String, Object> device = new HashMap<String, Object>();
        device.put("icon", R.drawable.bt_keyboard);
        device.put("name", "设备");
        data.add(2, device);

        HashMap<String, Object> setting = new HashMap<String, Object>();
        setting.put("icon", R.drawable.bt_keyboard);
        setting.put("name", "设置");
        data.add(3, setting);

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item_main_menu, new String[]{"icon", "name"}, new int[]{R.id.main_menu_item_icon, R.id.main_menu_item_name});

        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPager.setCurrentItem(position);
            }
        });

    }


    /*
    *  onCreateOptionsMenu
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

}
