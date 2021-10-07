package com.vone.medisafe.common.exception;

import org.apache.log4j.Logger;

public class ScreenImageURLException extends Exception{
	
    private static final Logger logger = Logger.getLogger(LicenseException.class);
    private String code;

    public ScreenImageURLException(String code)
    {
        this.code = code;
        logger.debug(getMessage());
    }

    public String getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return "ScreenImageURL Exception : " + code;
    }
}
