package com.pens.afdolash.altrump.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Machine implements Serializable{

    private String id_mesin;
    private String alamat;
    private String tanggal_maintenance;
    private String tanggal_pasang;
    private String user_key;
    private HashMap<String, String> price;

    public Machine(){

    }

    public String getId_mesin() {
        return id_mesin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTanggal_maintenance() {
        return tanggal_maintenance;
    }

    public String getTanggal_pasang() {
        return tanggal_pasang;
    }

    public HashMap<String, String> getPrice() {
        return price;
    }

    public String getUser_key() {
        return user_key;
    }

    public Machine(String id_mesin, String alamat, HashMap<String, String> price,String tanggal_pasang, String user_key) {
        this.alamat = alamat;
        this.id_mesin = id_mesin;
        this.price = price;
        this.tanggal_maintenance = "";
        this.tanggal_pasang = tanggal_pasang;
        this.user_key = user_key;
    }
}
