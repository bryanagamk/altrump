package com.pens.afdolash.altrump.model;

import java.util.List;

public class DataDevice {

    private int month;
    private int years;
    private List<Device> devices;

    public int getMonth() {
        return month;
    }

    public int getYears() {
        return years;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public DataDevice(int month, int years, List<Device> devices) {

        this.month = month;
        this.years = years;
        this.devices = devices;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DataDevice){
            DataDevice devices = (DataDevice) obj;
            return (devices.getMonth() == this.getMonth() && devices.getYears() == this.getYears());
        }
        return false;
    }
}
