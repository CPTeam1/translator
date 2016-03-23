package com.cp1.translator.models;

import com.cp1.translator.utils.ModelListener;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by eelango on 3/5/16.
 */

@Parcel(analyze={Lang.class})
@ParseClassName("Lang")
public class Lang extends ParseObject {
    public interface LangsListener extends ModelListener {
        void onLangs(List<Lang> langs);
    }

    public static final String NAME_KEY = "name";
    public static final String CODE_KEY = "code";

    public static Lang getOrCreate(String langName) {
        ParseQuery<Lang> query = ParseQuery.getQuery(Lang.class);
        query.whereEqualTo(NAME_KEY, langName);
        Lang lang = null;
        try {
            List<Lang> langs = query.find();
            if (langs.size() == 0) {
                lang = new Lang();
                lang.setName(langName);
                lang.save();
            } else {
                lang = langs.get(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lang;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public String getCode() {
        return getString(CODE_KEY);
    }

    public void setCode(String code) {
        put(CODE_KEY, code);
    }

    @Override
    public boolean equals(Object o) {
        ParseObject otherObject = (ParseObject) o;
        if (otherObject.getClassName() != "Lang")
            return false;
        Lang otherLang = (Lang) o;
        return getObjectId().equals(otherLang.getObjectId());
    }
}
