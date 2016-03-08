package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by eelango on 3/5/16.
 */

@ParseClassName("Lang")
public class Lang extends ParseObject {
    public static final String NAME_KEY = "name";

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    @Override
    public boolean equals(Object o) {
        Lang otherLang = (Lang) o;
        return getObjectId().equals(otherLang.getObjectId());
    }
}
