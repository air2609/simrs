package com.vone.medisafe.satusehat;

import com.vone.medisafe.mapping.SatuSehatToken;

public interface SatuSehatManager {

    public SatuSehatToken getToken();
    public void saveToken(SatuSehatToken token);
}
