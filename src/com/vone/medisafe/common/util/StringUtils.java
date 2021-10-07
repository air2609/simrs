package com.vone.medisafe.common.util;

/**
 * Created by IntelliJ IDEA.
 * User: Isyak
 * Date: Jun 24, 2005
 * Time: 11:37:15 PM
 */

public class StringUtils
{

    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    /**
     * Check if the string is not empty and not null.
     *
     * @param s String value.
     * @return True if not empty and not null; Else false.
     */
    public static boolean hasValue(String s)
    {
        return hasValue(s, false);
    }

    /**
     * Check if the string is not empty and not null.
     * When ignoring space, the string will be trimmed before checking.
     *
     * @param s           String value.
     * @param ignoreSpace Flag to ignore space.
     * @return True if not empty and not null; Else false.
     */
    public static boolean hasValue(String s, boolean ignoreSpace)
    {
        if (ignoreSpace)
        {
            return s != null && !s.trim().equals("");
        }
        else
        {
            return s != null && !s.equals("");
        }
    }

    /**
     * give String additional digit(s)
     * @param stInput
     * @param digit
     * @param stReplacement
     * @param orientation
     * @return
     * by James
     */
    public static String addDigit(String stInput, int digit, String stReplacement, String orientation){
        if ((stInput.length() >= digit) || (stReplacement.length() > 1))
            return stInput;

        if (LEFT.equalsIgnoreCase(orientation))
            while (stInput.length() < digit){
                stInput = stReplacement + stInput;
            }
        else
            while (stInput.length() < digit){
                stInput = stInput + stReplacement;
            }

        return stInput;
    }

    /**
     * to remove any existing blank space
     * @param stInput
     * @return
     * by James
     */
    public static String trimAll(String stInput){
        String stRes = "";
        if(stInput == null) return null;

        for (int i=0; i<stInput.length(); i++)
            if (!" ".equals(stInput.substring(i,i+1)))
                stRes += stInput.charAt(i);

        return stRes;
    }

    /**
     * to remove any existing "0" character at rightest part of string
     * used for Chart of Accounts
     * @param stInput
     * @return
     * by James
     */
    public static String removeZeroRight(String stInput){
        String stRes = "";

        for (int i=stInput.length(); i>0; i--){
            if (!"0".equals(stInput.substring(i,i-1)))
                stRes += stInput.charAt(i);
        }

        return stRes;
    }

    /**
     * to convert int to String with delimiters
     * eg. 1000 -> 1,000
     * @param input
     * @return
     * by James
     */
    public static String getStringRep(int input, String replacement){

        String stInput = new Integer(input).toString();
        String stResult = "";
        int count = 0;

        for (int i=stInput.length()-1; i>=0; i--){
            if (count++ >=3){
                stResult = replacement + stResult;
                count = 0;
            }
            stResult = stInput.charAt(i) + stResult;
        }

        return stResult;
    }
}
