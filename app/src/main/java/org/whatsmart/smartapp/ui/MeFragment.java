package org.whatsmart.smartapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.whatsmart.smartapp.R;

/**
 * Created by blue on 2016/3/15.
 */
public class MeFragment extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        //toobar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toobar_me);
        toolbar.setTitle("我的");
        toolbar.setTitleTextColor(0xffffffff);

        return view;
    }
}
