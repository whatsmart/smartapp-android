package org.whatsmart.smartapp.base.device;

import java.util.List;
import java.util.Map;

/**
 * Created by blue on 2016/3/16.
 */
public class Device {
    public int id; //the id in the gateway(smart gateway)
    public String uniqid;
    public String name;
    public String type;
    public String position;
    public String vender;
    public String hwversion;
    public String swversion;
    public List<String> operations;
    public State state;

    public class State {
        public String power;
        public int brightness;
        public String color;

        public String getPower() {
            return power;
        }

        public void setPower(String power) {
            this.power = power;
        }

        public int getBrightness() {
            return brightness;
        }

        public void setBrightness(int brightness) {
            this.brightness = brightness;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqid() {
        return uniqid;
    }

    public void setUniqid(String uniqid) {
        this.uniqid = uniqid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public String getHwversion() {
        return hwversion;
    }

    public void setHwversion(String hwversion) {
        this.hwversion = hwversion;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
