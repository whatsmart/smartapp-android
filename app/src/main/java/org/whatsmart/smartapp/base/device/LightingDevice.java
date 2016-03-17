package org.whatsmart.smartapp.base.device;

/**
 * Created by blue on 2016/3/16.
 */
public class LightingDevice extends Device implements PowerControl, BrightnessControl{
    public int powerOn() {

        return PowerControl.RESULT_SUCCESS;
    }

    public int powerOff() {

        return PowerControl.RESULT_SUCCESS;
    }

    public void setBrightness(int brightness) {

    }

    public int getBrightness() {

        return 100;
    }
}
