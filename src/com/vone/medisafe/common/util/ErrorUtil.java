package com.vone.medisafe.common.util;

import java.util.ArrayList;

import com.vone.medisafe.common.services.MessagesService;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: Jun 13, 2005
 * Time: 12:28:18 PM
 * Desc : This class function is to store errors and be retrieved on the client side to display the errors
 */
public class ErrorUtil {

    ArrayList errList = new ArrayList();
    ArrayList errListDetail = new ArrayList();

    public ErrorUtil(String stError){
        doAdd(stError);
    }

    public void add(String stError){
        doAdd(stError);
    }

    public String getMsg(){
        StringBuffer str = new StringBuffer();
        for (int i=0 ;i<errList.size(); i++){
            str.append(errList.get(i));
        }

        return str.toString();
    }

    private void doAdd(String stError){    	

        if (StringUtils.hasValue(stError))return;
        
        errList.add(stError);
    }

    public ArrayList getErrList() {
        return errList;
    }

}
