package com.vone.medisafe.satusehat;

import com.vone.medisafe.mapping.SatuSehatToken;
import com.vone.medisafe.mapping.SatuSehatTokenDAO;

public class SatuSehatManagerImpl implements SatuSehatManager{

    private SatuSehatTokenDAO dao;

    public SatuSehatTokenDAO getDao() {
        return dao;
    }

    public void setDao(SatuSehatTokenDAO dao) {
        this.dao = dao;
    }

    @Override
    public SatuSehatToken getToken() {
        return dao.getToken();
    }

    @Override
    public void saveToken(SatuSehatToken token) {
        this.dao.save(token);
    }
}
