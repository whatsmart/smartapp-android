package org.whatsmart.smartapp.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.whatsmart.smartapp.R;

/**
 * Created by blue on 2016/3/7.
 */
public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ImageButton voice_Button = (ImageButton) view.findViewById(R.id.bt_voice);
        ImageButton voice_Button1 = (ImageButton) view.findViewById(R.id.bt_keyboard);

        final RelativeLayout sendtext = (RelativeLayout) view.findViewById(R.id.send_text);
        final RelativeLayout sendvoice = (RelativeLayout) view.findViewById(R.id.send_voice);

        sendtext.setVisibility(View.VISIBLE);
        sendvoice.setVisibility(View.GONE);

        voice_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtext.setVisibility(View.GONE);
                sendvoice.setVisibility(View.VISIBLE);
            }
        });

        voice_Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtext.setVisibility(View.VISIBLE);
                sendvoice.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
