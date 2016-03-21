package com.cp1.translator.utils;

import com.parse.ParseException;

/**
 * Created by erioness1125(Hyunji Kim) on 3/21/2016.
 */
public class ParseErrorConverter {
    
    public static String getErrMsg(int parseErrCode) {
        String errMsg = "";
        switch (parseErrCode) {
            case ParseException.CONNECTION_FAILED:
                errMsg = "Connection to the server failed";
                break;

            case ParseException.EMAIL_NOT_FOUND:
            case ParseException.INVALID_EMAIL_ADDRESS:
                errMsg = "Invalid username and/or password";
                break;

            case ParseException.EMAIL_MISSING:
            case ParseException.PASSWORD_MISSING:
            case ParseException.USERNAME_MISSING:
                errMsg = "Username and/or password missing";
                break;

            case ParseException.TIMEOUT:
                errMsg = "Request timed out";
                break;

            default:
                errMsg = "Server error";
                break;
        }
        
        return errMsg;
    }
}
