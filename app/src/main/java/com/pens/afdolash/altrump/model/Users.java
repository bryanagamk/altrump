package com.pens.afdolash.altrump.model;

import java.util.Date;

public class Users {

    private String alamat, email, fname, lname, password, tmpt_lahir, nomor_hp, nomor_telp, OID, lastActive, tgl_lahir, isOnline;
    private int id_number;

    public Users() {
    }

    public String getAlamat() {
        return alamat;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }

    public String getTmpt_lahir() {
        return tmpt_lahir;
    }

    public String getNomor_hp() {
        return nomor_hp;
    }

    public String getNomor_telp() {
        return nomor_telp;
    }

    public String getOID() {
        return OID;
    }

    public int getId_number() {
        return id_number;
    }

    public String getLastActive() {
        return lastActive;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public String isOnline() {
        return isOnline;
    }

    public Users(String email, String fname, String password, String nomor_hp) {
        this.alamat = "";
        this.email = email;
        this.fname = fname;
        this.lname = "";
        this.password = password;
        this.tmpt_lahir = "";
        this.nomor_hp = nomor_hp;
        this.nomor_telp = "";
        this.OID = "";
        this.id_number = id_number;
        this.lastActive = "";
        this.tgl_lahir = "";
        this.isOnline = "";
    }

    public Users(String alamat, String email, String fname, String lname, String password, String tmpt_lahir, String nomor_hp, String nomor_telp, String OID, int id_number, String lastActive, String tgl_lahir, String isOnline) {
        this.alamat = alamat;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.tmpt_lahir = tmpt_lahir;
        this.nomor_hp = nomor_hp;
        this.nomor_telp = nomor_telp;
        this.OID = OID;
        this.id_number = id_number;
        this.lastActive = lastActive;
        this.tgl_lahir = tgl_lahir;
        this.isOnline = isOnline;
    }
}
