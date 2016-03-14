package com.cp1.translator.models;

import com.cp1.translator.utils.ModelListener;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ishanpande on 3/4/16.
 */
@Parcel(analyze={Entry.class})
@ParseClassName("Entry")
public class Entry extends ParseObject {
    public interface EntriesListener extends ModelListener {
        void onEntries(List<Entry> questions);
    }

    public Entry(){}

    public static final String TEXT_KEY         = "text";
    public static final String IMAGE_URL_KEY    = "image";
    public static final String AUDIO_URL_KEY    = "audio";
    public static final String VIDEO_URL_KEY    = "video";
    public static final String IS_QUESTION_KEY  = "isQuestion";
    public static final String USER_KEY         = "user";
    public static final String FROM_LANG_KEY    = "fromLang";
    public static final String TO_LANG_KEY      = "toLang";
    public static final String QS_TYPE_KEY      = "type";


    public User getUser() {
        return (User) getParseObject(USER_KEY);
    }

    public void setUser(User user){
        put(USER_KEY,user);
    }

    public String getText() {
        return getString(TEXT_KEY);
    }

    public void setText(String text) {
        put(TEXT_KEY, text);
    }

    public void setFromLang(String fromLang){
        put(FROM_LANG_KEY,fromLang);
    }

    public void setToLang(String toLang){
        put(TO_LANG_KEY,toLang);
    }

    public String getFromLang(){
        return getString(FROM_LANG_KEY);
    }

    public String getToLang(){
        return getString(TO_LANG_KEY);
    }

    public ParseFile getImageUrl() {
        return getParseFile(IMAGE_URL_KEY);
    }

    public void setImageUrl(ParseFile url) {
        put(IMAGE_URL_KEY, url);
    }

    public ParseFile getAudioUrl() {
        return getParseFile(AUDIO_URL_KEY);
    }

    public void setAudioUrl(ParseFile url) {
        put(AUDIO_URL_KEY, url);
    }

    public ParseFile getVideoUrl() {
        return getParseFile(VIDEO_URL_KEY);
    }

    public void setVideoUrl(ParseFile url) {
        put(VIDEO_URL_KEY, url);
    }

    public boolean isQuestion() {
        return getBoolean(IS_QUESTION_KEY);
    }

    public void setAsQuestion() {
        put(IS_QUESTION_KEY, true);
    }

    public String getType() {
        return getString(QS_TYPE_KEY);
    }

    public void setType(String type) {
        put(QS_TYPE_KEY,type);
    }

    @Override
    public boolean equals(Object o) {
        Entry otherEntry = (Entry) o;
        return getObjectId().equals(otherEntry.getObjectId());
    }
}
