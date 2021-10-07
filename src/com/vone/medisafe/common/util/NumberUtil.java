package com.vone.medisafe.common.util;

import java.text.DecimalFormat;
import java.text.ParseException;

import com.vone.medisafe.common.constant.Constant;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: May 29, 2005
 * Time: 2:26:23 PM
 */
public class NumberUtil {
    private static DecimalFormat df = new DecimalFormat(Constant.stNumberFormat);

    /**
     * Convert stValue to Double.<br>
     * Empty string is converted to NULL value.
     * @param stValue string to be converted.
     * @return Double object.
     */
    public static Double getDouble(String stValue) {
        return stValue == null || "".equals(stValue) ? null : new Double(stValue);
    }

    /**
     * Convert stValue to Long.<br>
     * Empty string is converted to NULL value.
     * @param stValue string to be converted.
     * @return Long object.
     */
    public static Long getLong(String stValue) {
        return stValue == null || "".equals(stValue) ? null : new Long(stValue);
    }

    /**
     * Convert stValue to integer.<br>
     * Empty or NULL string is converted to 0.
     * @param stValue string to be converted.
     * @return int primitive data type.
     */
    public static int getInt(String stValue) {
        return stValue == null || "".equals(stValue) ? 0 : Integer.parseInt(stValue);
    }

    /**
     * Format Double to String.<br>
     * NULL dbValue will be converted to empty string.
     * @param dbValue Double to be formatted (eg: 1000000).
     * @return string in predefined format (eg: 1,000,000).
     */
    public static String format(Double dbValue) {
        return dbValue == null ? "" : df.format(dbValue);
    }

    /**
     * format Double to String using customized format.<br>
     * NULL dbValue will be converted to empty string.
     * @param dbValue Double to be formatted (eg: 1000000).
     * @param stFormat Number format to be applied (eg: "###.###,00").
     * @return string in spesified format (eg: 1.000.000,00).
     */

    public static String format(Integer dbValue, String stFormat) {
        df.applyPattern(stFormat);
        String stReturn = dbValue == null ? "" : df.format(dbValue);
        df.applyPattern(Constant.stNumberFormat);

        return stReturn;
    }

    public static String format(Double dbValue, String stFormat) {
        df.applyPattern(stFormat);
        String stReturn = dbValue == null ? "" : df.format(dbValue);
        df.applyPattern(Constant.stNumberFormat);

        return stReturn;
    }

    /**
     *
     * @param dbValue
     * @return
     */
    public static String formatCurrency(Double dbValue) {
        df.applyPattern("###,##0.00");
        String stResult = dbValue == null ? "" : df.format(dbValue);
        df.applyPattern(Constant.stNumberFormat);
        return stResult;
    }

    /**
     *
     * @param dbValue
     * @return
     */
    public static String formatVolume(Double dbValue) {
        df.applyPattern("###,##0.000");
        String stResult = dbValue == null ? "" : df.format(dbValue);
        df.applyPattern(Constant.stNumberFormat);
        return stResult;
    }

    /**
     *
     * @param stValue
     * @return
     * @throws java.text.ParseException
     */
    public static Double formatToDouble(String stValue) throws ParseException {
        return stValue == null || "".equals(stValue) ? null : new Double(df.parse(stValue).doubleValue());
    }

    public static Long formatToLong(String stValue) throws ParseException {
        return stValue == null || "".equals(stValue) ? null : new Long(df.parse(stValue).longValue());
    }

    public static Integer formatToInteger(String stValue) throws ParseException {
        return stValue == null || "".equals(stValue)? null : new Integer(df.parse(stValue).intValue());
    }

    /**
     * Format Long to String.<br>
     * NULL dbValue will be converted to empty string.
     * @param lgValue Double to be formatted (eg: 1000000).
     * @return string in predefined format (eg: 1,000,000).
     */
    public static String format(Long lgValue) {
        return lgValue == null ? "" : df.format(lgValue);
    }

    /**
     * format Long to String using customized format.<br>
     * NULL dbValue will be converted to empty string.
     * @param lgValue Double to be formatted (eg: 1000000).
     * @param stFormat Number format to be applied (eg: "###.###,00").
     * @return string in spesified format (eg: 1.000.000,00).
     */
    public static String format(Long lgValue, String stFormat) {
        df.applyPattern(stFormat);
        String stReturn = lgValue == null ? "" : df.format(lgValue);
        df.applyPattern(Constant.stNumberFormat);

        return stReturn;
    }

    public static String lZero(String stValue, int intStringLength) {
        int intLen = stValue.length();
        if (intLen > intStringLength) return stValue;

        String stResult = new String(stValue);
        for (; intLen < intStringLength; intLen++) {
            stResult = "0" + stResult;
        }

        return stResult;
    }

    public static String rZero(String stValue, int intStringLength) {
        int intLen = stValue.length();
        if (intLen > intStringLength) return stValue;

        String stResult = new String(stValue);
        for (; intLen < intStringLength; intLen++) {
            stResult = stResult + "0";
        }

        return stResult;
    }

    public static String rTrimZero(String stValue) {
        String stResult = new String(stValue);
        int intLen = stValue.length();
        for (int i = 0; i < intLen; i++) {
            if (stValue.charAt(i) == '0') {
                stResult.substring(1, stResult.length());
            } else {
                break;
            }
        }
        return stResult;
    }

    public static String lTrimZero(String stValue) {
        String stResult = new String(stValue);
        int intLen = stValue.length();
        for (; --intLen >= 0;) {
            if (stValue.charAt(intLen) == '0') {
                stResult.substring(0, stResult.length() - 1);
            } else {
                break;
            }
        }
        return stResult;
    }

}
