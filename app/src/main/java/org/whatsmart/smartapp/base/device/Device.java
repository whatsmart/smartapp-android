package org.whatsmart.smartapp.base.device;

/**
 * Created by blue on 2016/3/16.
 */
public class Device {
    private int id; //the id in the smarthub(smart gateway)
    private String name;
    private String type;
    private String postion;
    private String vender;
    private String hwversion;
    private String swversion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = new String(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = new String(type);
    }

    public String getPostion() {
        return postion;
    }

    public void setPostion(String postion) {
        this.postion = new String(postion);
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = new String(vender);
    }

    public String getHwversion() {
        return hwversion;
    }

    public void setHwversion(String hwversion) {
        this.hwversion = new String(hwversion);
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = new String(swversion);
    }
}
