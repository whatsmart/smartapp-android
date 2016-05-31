package org.whatsmart.smartapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.whatsmart.smartapp.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by blue on 2016/3/7.
 */
public class MsgFragment extends Fragment {
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_msg);
        setupToolbar();


        return view;
    }

    public void setupToolbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        toolbar.setTitle("消息");
        toolbar.setTitleTextAppearance(getContext(), R.style.Toolbar_Title);

        toolbar.setPadding(0, config.getPixelInsetTop(false), (int) (getContext().getResources().getDisplayMetrics().density * 10), 0);

        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(toolbar);
    }
}
