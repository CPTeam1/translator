package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by eelango on 3/5/16.
 */

@ParseClassName("Skill")
public class Skill extends ParseObject {
    public static final String LANG_KEY = "lang";
    public static final String LEVEL_KEY = "level";

    public Lang getLang(String createdBy) {
        List<Lang> langList = null;

        ParseQuery<Lang> langParseQuery = ParseQuery.getQuery("Lang");
        try {
            List<Lang> queryResults = langParseQuery.whereEqualTo("createdBy", createdBy).find();
            langList.addAll(queryResults);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return langList.get(0);
    }

    public void setLang(Lang lang, String createdBy) {
        put("createdBy", createdBy);
        put(LANG_KEY, lang);
    }

    public int getLevel() {
        return getInt(LEVEL_KEY);
    }

    public void setLevel(int level) {
        put(LEVEL_KEY, level);
    }

    @Override
    public boolean equals(Object o) {
        Skill otherSkill = (Skill) o;
        return getObjectId().equals(otherSkill.getObjectId());
    }
}
