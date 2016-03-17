package org.whatsmart.smartapp.base.device;

/**
 * Created by blue on 2016/3/16.
 */
public interface PowerControl {
    static final int RESULT_SUCCESS = 0;
    static final int RESULT_FAIL = 0;
    int powerOn();
    int powerOff();
}
