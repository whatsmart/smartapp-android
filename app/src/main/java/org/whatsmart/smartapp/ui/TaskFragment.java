package org.whatsmart.smartapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_task);
        setupToolbar();

        return view;
    }

    public void setupToolbar() {
        toolbar.setTitle("任务");
        toolbar.setTitleTextAppearance(getContext(), R.style.Toolbar_Title);

        toolbar.setPadding(0, 0, (int) (getContext().getResources().getDisplayMetrics().density * 10), 0);

        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
    }
}
