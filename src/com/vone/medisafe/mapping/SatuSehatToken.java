package com.vone.medisafe.mapping;

import java.io.Serializable;
import java.util.Date;

public class SatuSehatToken implements Serializable {
    private Integer id;
    private String accessToken;
    private Date lastAccess;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }
}
