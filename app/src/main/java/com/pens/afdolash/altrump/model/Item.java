package com.pens.afdolash.altrump.model;

public class Item {
    private String idMachine, tv_namaSPBU, tv_jmlPbaruAtas, tv_jmlTBerhasil, tv_jmlTGagal, tv_jmlPbaru, tv_jmlPulang;

    public String getIdMachine() {
        return idMachine;
    }

    public String getTv_namaSPBU() {
        return tv_namaSPBU;
    }

    public String getTv_jmlPbaruAtas() {
        return tv_jmlPbaruAtas;
    }

    public String getTv_jmlTBerhasil() {
        return tv_jmlTBerhasil;
    }

    public String getTv_jmlTGagal() {
        return tv_jmlTGagal;
    }

    public String getTv_jmlPbaru() {
        return tv_jmlPbaru;
    }

    public String getTv_jmlPulang() {
        return tv_jmlPulang;
    }

    public Item(String tv_namaSPBU, String tv_jmlPbaruAtas, String tv_jmlTBerhasil, String tv_jmlTGagal, String tv_jmlPbaru, String tv_jmlPulang) {
        this.tv_namaSPBU = tv_namaSPBU;
        this.tv_jmlPbaruAtas = tv_jmlPbaruAtas;
        this.tv_jmlTBerhasil = tv_jmlTBerhasil;
        this.tv_jmlTGagal = tv_jmlTGagal;
        this.tv_jmlPbaru = tv_jmlPbaru;
        this.tv_jmlPulang = tv_jmlPulang;
    }
}
