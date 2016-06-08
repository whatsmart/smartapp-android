package org.whatsmart.smartapp.ui;

import android.content.Context;

/**
 * Created by blue on 2016/3/17.
 */
public class Common {
    public static int dp2px(Context context, int dp) {
        return (int) context.getResources().getDisplayMetrics().density * dp;
    }
}
