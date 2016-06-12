package org.whatsmart.smartapp.ui;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.SmartApp;

/**
 * Created by blue on 2016/6/2.
 */
public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupToolbar();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.fragment_background));
    }

    public void setupToolbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        //toolbar.setTitle("设置");
        //toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_Title);
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("设置");

        toolbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("SETTING", "setting changed: " + key + ": " + sharedPreferences.getString(key, null));
        if ("gateway_address".equalsIgnoreCase(key)) {
            String gateway_address = sharedPreferences.getString("gateway_address", "");
            ((SmartApp)getActivity().getApplication()).gateway_url = "http://" + gateway_address;
        }
    }
}
