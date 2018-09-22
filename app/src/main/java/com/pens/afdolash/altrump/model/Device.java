package com.pens.afdolash.altrump.model;

import java.util.Date;

public class Device {

    private String date, machineida, machineidb, machineidc, machineidd, machineide, machineidf, price, statusA,
            statusB, time, type;

    public String getDate() {
        return date;
    }

    public String getMachineida() {
        return machineida;
    }

    public String getMachineidb() {
        return machineidb;
    }

    public String getMachineidc() {
        return machineidc;
    }

    public String getMachineidd() {
        return machineidd;
    }

    public String getMachineide() {
        return machineide;
    }

    public String getMachineidf() {
        return machineidf;
    }

    public String getPrice() {
        return price;
    }

    public String getStatusA() {
        return statusA;
    }

    public String getStatusB() {
        return statusB;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public Device() {
    }

    public Device(String date, String machineida, String machineidb, String machineidc, String machineidd, String machineide, String machineidf, String price, String statusA, String statusB, String time, String type) {
        this.date = date;
        this.machineida = machineida;
        this.machineidb = machineidb;
        this.machineidc = machineidc;
        this.machineidd = machineidd;
        this.machineide = machineide;
        this.machineidf = machineidf;
        this.price = price;
        this.statusA = statusA;
        this.statusB = statusB;
        this.time = time;
        this.type = type;
    }
}
