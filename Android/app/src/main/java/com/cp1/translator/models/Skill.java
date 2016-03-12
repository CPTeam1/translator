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
    public static final String LANG_CODE_KEY = "langcode";

    private boolean isSavedRemotely = false;

    public Lang loadLangFromRemote(String langCode) {
        Lang lang = null;

        ParseQuery<Lang> langParseQuery = ParseQuery.getQuery("Lang");
        try {
            List<Lang> queryResults = langParseQuery.whereEqualTo("name", langCode).find();
            lang = queryResults.get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lang;
    }

    public Lang getLang() {
        return (Lang) getParseObject(LANG_KEY);
    }

    public void setLang(Lang lang) {
        put(LANG_KEY, lang);
    }

    public int getLevel() {
        return getInt(LEVEL_KEY);
    }

    public void setLevel(int level) {
        put(LEVEL_KEY, level);
    }

    public String getLangCode() {
        return getString(LANG_CODE_KEY);
    }

    public void setLangCode(String langCode) {
        put(LANG_CODE_KEY, langCode);
    }

    public boolean isSavedRemotely() {
        return isSavedRemotely;
    }

    public void doneSave() {
        isSavedRemotely = true;
    }

    @Override
    public boolean equals(Object o) {
        Skill otherSkill = (Skill) o;
        return getObjectId().equals(otherSkill.getObjectId());
    }
}
