package org.whatsmart.smartapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.whatsmart.smartapp.R;

/**
 * Created by blue on 2016/3/9.
 */
public class SettingFragment extends Fragment {
    private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_setting);
        setupToolbar();

        return view;
    }

    public void setupToolbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        toolbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);
        toolbar.setTitle("设置");
        toolbar.setTitleTextAppearance(getContext(), R.style.Toolbar_Title);

        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
    }
}
