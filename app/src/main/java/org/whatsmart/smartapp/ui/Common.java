package org.whatsmart.smartapp.ui;

import android.content.Context;

/**
 * Created by blue on 2016/3/17.
 */
public class Common {
    public static int dp2px(Context context, int dp) {
        return (int) context.getResources().getDisplayMetrics().density * dp;
    }

    public static int removeLightness (int rgb) {
        float H, S, V;
        int R, G, B, a, b, c;
        int max, min;
        int d;
        float f;

        R = rgb & 0x00ff0000;
        R = R/256/256;
        G = rgb & 0x0000ff00;
        G = G/256;
        B = rgb & 0x000000ff;
        max = Math.max(R, Math.max(G, G));
        min = Math.min(R, Math.min(G, G));
        System.out.println("max: "+ max + ", min:" + min);
        if (max == min) {
            H = 0f;
        } else if (max == R && G >= B) {
            H = 60 * (G-B)/(float)(max-min);
        } else if (max == R && G < B) {
            H = 60 * (G-B)/(float)(max-min) + 360;
        } else if (max == G) {
            H = 60*(B-R)/(float)(max-min) + 120;
        } else {
            H = 60*(R-G)/(float)(max-min) + 240;
        }

        if (max == 0) {
            S = 0f;
        } else {
            S = (max-min)/(float)max;
        }

        V = 1;

//        if (S == 0)
//            R = G = B = (int) (V * 256);
//        else {
        d = (int) H/60;
        f = H/60 - d;
        a = (int)(V*(1 - S)*255);
        b = (int) (V * (1 - S * f)*255);
        c = (int)(V * (1 - S * (1 - f))*255);

        System.out.println("a: " + a + "b: " + b + "c: " + c + ", d:" + d + "H: " + H);
        switch (d) {
            case 0:
                R = (int) (V * 255);
                G = c;
                B = a;
                break;
            case 1:
                R = b;
                G = (int) (V * 255);
                B = a;
                break;
            case 2:
                R = a;
                G = (int) (V*255);
                B = c;
                break;
            case 3:
                R = a;
                G = b;
                B = (int) (V*255);
                break;
            case 4:
                R = c;
                G = a;
                B = (int) (V*255);
                break;
            case 5:
                R = (int) (V*255);
                G = a;
                B = b;
                break;
        }
//        }
        return 0xff<<24 | R<<16 | G<<8 | B;
    }
}
