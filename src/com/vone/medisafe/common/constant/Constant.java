package com.vone.medisafe.common.constant;

/**
 * Created by JiM
 * User: HP compaq nx5000
 * Date: May 27, 2005
 * Time: 12:49:16 AM
 */
public class Constant {

    public final static int MAX_INACTIVE_INTERVAL = 10;
    /**
     * Data format constant.
     */
    public final static String stDateFormat = "dd/MM/yyyy";

    /**
     * Number format constant.
     */
    public final static String stNumberFormat = "###,###.##############";

    /**
     *Currency format constant.
     */
    public final static String stCurrencyFormat = "###,###.00";

    /**
     * Maximum fraction digits constant.
     */
    public final static int intMaxFractionDigits = 2;
    
    /**
     * Navigation Variable
     */
    public final static String stFormAction = "stFormAction";

    /**
     * First page navigation constant.
     */
    public final static String FIRST_ACTION = "FIRST";

    public final static String DATE_CHANGE = "D_CHANGE";

    public final static String DATE_CREATE = "D_CREATE";

    /**
     * Previous page navigation constant.
     */
    public final static String PREV_ACTION = "PREV";

    /**
     * Next page navigation constant.
     */
    public final static String NEXT_ACTION = "NEXT";

    /**
     * Last page navigation constant.
     */
    public final static String LAST_ACTION = "LAST";

    /**
     * Menu navigation constant.
     */
    public final static String MENU_NAVIGATION = "MENU";

    /**
     * Save add constant.
     */
    public final static String SAVE_ADD_ACTION = "SAVE_ADD";

    /**
     * Save modify constant.
     */
    public final static String SAVE_MODIFY_ACTION = "SAVE_MODIFY";

    /**
     * Delete constant.
     */
    public final static String DELETE_ACTION = "DELETE";

    /**
     * Modify constant.
     */
    public final static String MODIFY_ACTION = "MODIFY";

    /**
     * Abort constant.
     */
    public final static String ABORT_ACTION = "ABORT";

    /**
     * Close constant.
     */
    public final static String CLOSE_ACTION = "CLOSED";

    /**
     * Duplicate constant.
     */
    public final static String DUPLICATE_ACTION = "DUPLICATE";

    /**
     *Add Constant.
     */
    public final static String ADD_ACTION = "ADD";

    /**
     * User info constant.
     */
    public final static String USER_INFO = "USER_INFO";

    /**
     * View constant.
     */
    public final static String VIEW_ACTION = "VIEW";

    // Screen Mode Constant....

    public final static String ADD_MODE = "ADD";

    public final static String NEW_MODE = "NEW";

    public final static String VIEW_MODE = "VIEW";

    public final static String MODIFY_MODE = "MODIFY";

    public final static String ADD_LAST_PAGE = "ADD_LAST_PAGE";

    public final static String RETRIEVAL_MODE = "RETRIEVAL";

    public final static String LOGOUT = "LOGOUT";

    /**
     * Print Action.
     */
    public final static String PRINT = "PRINT";

    public final static int QUERY_FAILED = 0;

    public final static int QUERY_SUCCESS = 1;

    public final static String READ_WRITE = "RW";

    public final static String READ = "RD";

    /**
     * Separator constant.
     */
    public final static String KEY_SEPARATOR = "#";
    
    public static String APP_CONTEXT = "/medisafe/";

    public static String APP_CONTEXT_NAME = "medisafe";

    /**
     * Error Handling
     */
    public final static String ERROR_MSG = "ERR";
    public final static String ERROR_UTIL = "ERR_UTIL";
    public final static String EXCEPTION = "VONEAppException";

}
