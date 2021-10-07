package com.vone.medisafe.common.util;

/**
 * Project : GHOST
 * Prepared by : JiM
 * Copyright : PT VisiONE System
 * <p/>
 * User: HP compaq nx5000
 * Date: Jul 2, 2005
 * Time: 1:32:03 PM
 * author : James Pang
 */
public class SequenceGenerator {

    private SequenceGenerator(){};

        /**
     * this method will create a string of sequence Alphabet by reference of count
     * eg. if count = 0 then this method would return A
     * eg. if count = 27 then this method would return AA
     * eg. if count = 28 then this method would return AB
     * eg. if count = 18277 then this method would return ZZZ
     * using Character class, and forDigit() method. 'a' starts from digit of 10
     * @param count
     * @return
     */
    public static String getSequenceString(int count){
        String stRes = "";

        int intDiv = count / 26;
        int intMod = count % 26;
        int intDiv2 = intDiv;

        while (intDiv2 > 26){
            stRes += getSequenceString(--intDiv2);
            intDiv2 = intDiv2 / 26;
        }

        if ((intDiv > 0) && (intDiv < 27))
            stRes += Character.forDigit(intDiv+9,Character.MAX_RADIX);

        stRes += Character.forDigit(intMod+10,Character.MAX_RADIX);

        return stRes.toUpperCase();
    }

}
