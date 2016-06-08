package org.whatsmart.smartapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

        toolbar = (Toolbar) getActivity().findViewById(R.id.main_toolbar);

        final ImageButton bt_switch = (ImageButton) view.findViewById(R.id.bt_switch);
        final Button bt_speak = (Button) view.findViewById(R.id.press_to_speak);
        final EditText et_msginput = (EditText) view.findViewById(R.id.input);
        final Button bt_send = (Button) view.findViewById(R.id.send);

        bt_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_speak.getVisibility() == View.GONE) {
                    et_msginput.setVisibility(View.GONE);
                    bt_send.setVisibility(View.GONE);
                    bt_speak.setVisibility(View.VISIBLE);
                    bt_switch.setImageResource(R.drawable.switch_keyboard);
                } else {
                    et_msginput.setVisibility(View.VISIBLE);
                    bt_send.setVisibility(View.VISIBLE);
                    bt_speak.setVisibility(View.GONE);
                    bt_switch.setImageResource(R.drawable.switch_voice);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupToolbar();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.fragment_background));
    }

    public void setupToolbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

        //toolbar.setTitle("消息");
        //toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_Title);

        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText("消息");

        toolbar.setPadding(0, config.getPixelInsetTop(false), 0, 0);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionbar = activity.getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
        }
    }
}
