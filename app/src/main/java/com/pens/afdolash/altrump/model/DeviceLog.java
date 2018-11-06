package com.pens.afdolash.altrump.model;

import java.util.Date;

public class DeviceLog {
    private Date tgl;
    private String status;

    public DeviceLog(Date tgl, String status) {
        this.tgl = tgl;
        this.status = status;
    }

    public Date getTgl() {
        return tgl;
    }

    public String getStatus() {
        return status;
    }

    public DeviceLog(Date tgl) {
        this.tgl = tgl;
    }
}
