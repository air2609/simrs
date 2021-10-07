package com.vone.medisafe.common.services;

import org.apache.log4j.Logger;

import com.vone.medisafe.common.exception.LicenseException;
import com.vone.medisafe.common.util.MD5;
import com.vone.medisafe.common.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Isyak
 * Date: Aug 23, 2005
 * Time: 1:13:22 PM
 */

public class LicenseManager
{
    private static Logger logger = Logger.getLogger(LicenseManager.class);

    private static LicenseManager instance = null;

    private Properties properties = new Properties();

    private String licensee;
    private String expiration;
    private String units;
    private String serverIpAddr;
    private String signature;

    private int unitsInInteger;

    public static LicenseManager getInstance()
        throws LicenseException
    {
        if (instance == null)
        {
            instance = new LicenseManager();
        }

        return instance;
    }

    protected LicenseManager()
        throws LicenseException
    {
        try
        {
            String licensePath = ConfigurationManager.getKey("file.license");

            if (StringUtils.hasValue(licensePath))
            {
                logger.debug("Loading license file from : " + licensePath);
                properties.load(new FileInputStream(licensePath));
            }
            else
            {
                throw new LicenseException(MessagesService.getKey("license.missing_license_file"));
            }

        }
        catch (FileNotFoundException e)
        {
            throw new LicenseException(MessagesService.getKey("license.missing_license_file"));
        }
        catch (Exception e)
        {
            throw new LicenseException(MessagesService.getKey("license.fatal_error"));
        }

        licensee = properties.getProperty("licensee");
        expiration = properties.getProperty("expiration");
        serverIpAddr = properties.getProperty("ip");
        units = properties.getProperty("units");
        signature = properties.getProperty("signature");

        try
        {
            unitsInInteger = units.equals("unlimited") ? Integer.MAX_VALUE : Integer.parseInt(units);
        }
        catch (Exception e)
        {
            throw new LicenseException(MessagesService.getKey("license.invalid_license"));
        }

        // License checking
        logger.debug("Checking License Validity");
        if (!isValidLicense())
        {
            throw new LicenseException(MessagesService.getKey("license.invalid_license"));
        }

        logger.debug("Checking Expired License");
        if (isLicenseExpired())
        {
            throw new LicenseException(MessagesService.getKey("license.expired"));
        }

        logger.debug("Checking Server IP");
        if (!isValidIp())
        {
            throw new LicenseException(MessagesService.getKey("license.invalid_ip"));
        }
    }

    private boolean isValidLicense()
        throws LicenseException
    {
        if (!StringUtils.hasValue(licensee) || !StringUtils.hasValue(expiration) || !StringUtils.hasValue(serverIpAddr) || !StringUtils.hasValue(units) || !StringUtils.hasValue(signature))
        {
            throw new LicenseException(MessagesService.getKey("license.invalid_license"));
        }

        StringBuffer sb = new StringBuffer("Medisafe License : ");
        sb.append(licensee);
        sb.append(" ");
        sb.append(expiration);
        sb.append(" ");
        sb.append(units);
        sb.append(" ");
        sb.append(serverIpAddr);

        String licenseKey = new MD5(sb.toString()).toString();
        logger.debug(licenseKey);

        return licenseKey.equals(signature);
    }

    private boolean isLicenseExpired()
        throws LicenseException
    {
        if (!expiration.equals("never"))
        {
            logger.debug("Checking Expired Date");
            Calendar todayCalendar = new GregorianCalendar();

            StringTokenizer st = new StringTokenizer(expiration, "-");

            int year, month, day;
            try
            {
                year = Integer.parseInt(st.nextToken());
                month = Integer.parseInt(st.nextToken()) - 1;
                day = Integer.parseInt(st.nextToken());
            }
            catch (Exception e)
            {
                throw new LicenseException(MessagesService.getKey("license.invalid_expired_date"));
            }

            Calendar expiredCalendar = new GregorianCalendar(year, month, day);

            return todayCalendar.after(expiredCalendar);
        }
        else
        {
            return false;
        }
    }

    private boolean isValidIp()
        throws LicenseException
    {
        if (serverIpAddr.equals("any"))
        {
            return true;
        }

        try
        {
            InetAddress addr = InetAddress.getLocalHost();

            // Get IP Address
            byte[] ipAddr = addr.getAddress();
            String ipAddrString = "";
            for (int a = 0; a < ipAddr.length; a++)
            {
                if (a > 0)
                {
                    ipAddrString += ".";
                }
                ipAddrString += ipAddr[a] & 0xFF;
            }
            logger.debug("Server IP Address = " + ipAddrString);

            return serverIpAddr.equals(ipAddrString);
        }
        catch (UnknownHostException e)
        {
            throw new LicenseException(MessagesService.getKey("license.ip_address_error"));
        }
    }

    public int getMaxConnection()
    {
        return unitsInInteger;
    }
}
