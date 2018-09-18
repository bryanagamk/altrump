package com.pens.afdolash.altrump.model;

import java.io.Serializable;
import java.util.Date;

public class Machine implements Serializable{

    private String id_mesin;
    private String alamat;
    private String tanggal_maintenance;
    private String tanggal_pasang;
    private String user_key;

    // TODO: data dari firebase simpan di SQLite

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

    public String getUser_key() {
        return user_key;
    }

    public Machine(String id_mesin, String alamat, String tanggal_maintenance, String tanggal_pasang, String user_key) {
        this.id_mesin = id_mesin;
        this.alamat = alamat;
        this.tanggal_maintenance = tanggal_maintenance;
        this.tanggal_pasang = tanggal_pasang;
        this.user_key = user_key;
    }
}
