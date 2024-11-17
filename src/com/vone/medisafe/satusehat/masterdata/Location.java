package com.vone.medisafe.satusehat.masterdata;

public class Location {

    private String code;
    private String parent_code;
    private String bps_code;
    private String name;

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public void setBps_code(String bps_code) {
        this.bps_code = bps_code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getParent_code() {
        return parent_code;
    }

    public String getBps_code() {
        return bps_code;
    }

    public String getName() {
        return name;
    }
}
