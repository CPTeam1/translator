package com.cp1.translator.utils;

import com.parse.ParseException;

/**
 * Created by eelango on 3/12/16.
 */
public interface SaveListener {
    void saved();
    void onError(ParseException e);
}
