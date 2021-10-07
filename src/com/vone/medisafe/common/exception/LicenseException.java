package com.vone.medisafe.common.exception;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Isyak
 * Date: Aug 23, 2005
 * Time: 3:52:18 PM
 */

public class LicenseException extends Exception
{
    private static final Logger logger = Logger.getLogger(LicenseException.class);
    private String code;

    public LicenseException(String code)
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
        return "License Exception : " + code;
    }
}
