package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by eelango on 3/5/16.
 */

@ParseClassName("Skill")
public class Skill extends ParseObject {
    public static final String LANG_KEY = "lang";
    public static final String LEVEL_KEY = "level";

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

    @Override
    public boolean equals(Object o) {
        Skill otherSkill = (Skill) o;
        return getObjectId().equals(otherSkill.getObjectId());
    }
}
