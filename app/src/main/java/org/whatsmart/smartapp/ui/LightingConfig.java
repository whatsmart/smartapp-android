package org.whatsmart.smartapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import org.whatsmart.smartapp.R;
import org.whatsmart.smartapp.base.device.Device;

/**
 * Created by blue on 2016/3/16.
 */
public class LightingConfig {
    private Context context;
    private Device device;
    public LightingConfig(Context context, Device dev) {
        this.context = context;
        device = dev;
    }

    void config() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.devconfig_lighting, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        builder.setPositiveButton("确定", new PositiveButtonClickListener());
        builder.setCancelable(true);
        builder.show();
    }

    private class PositiveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            System.out.println("OK clicked");
        }
    }
}
