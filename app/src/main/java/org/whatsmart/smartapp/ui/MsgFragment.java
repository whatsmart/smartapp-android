package org.whatsmart.smartapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.whatsmart.smartapp.R;

/**
 * Created by blue on 2016/3/7.
 */
public class MsgFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toobar_msg);
        toolbar.setTitle("消息");
        toolbar.setTitleTextColor(0xffffffff);

        final EditText msgText = (EditText) view.findViewById(R.id.edittext_msg);
        msgText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    System.out.println("TEXT:" + msgText.getText().toString());
                }
                return true;
            }
        });

        return view;
    }
}
