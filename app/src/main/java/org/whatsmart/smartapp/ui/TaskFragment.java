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
 * Created by blue on 2016/3/8.
 */
public class TaskFragment extends Fragment {
    private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);
        setupToolbar();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.fragment_background));
    }

    public void setupToolbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        toolbar.setTitle("任务");
        toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_Title);
        toolbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);

        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
    }
}
