package org.whatsmart.smartapp.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by blue on 2016/3/8.
 */
public class MainSwipeAdapter extends FragmentPagerAdapter {

    private List<Fragment> list = new ArrayList<Fragment>();

    public MainSwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment (int location, Fragment fragment) {
        list.add(location, fragment);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}
