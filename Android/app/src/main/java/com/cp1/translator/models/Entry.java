package com.cp1.translator.models;

import com.cp1.translator.utils.ModelListener;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by ishanpande on 3/4/16.
 */
@ParseClassName("Entry")
public class Entry extends ParseObject {
    public interface EntriesListener extends ModelListener {
        void onEntries(List<Entry> questions);
    }

    public static final String TEXT_KEY         = "text";
    public static final String IMAGE_URL_KEY    = "image";
    public static final String AUDIO_URL_KEY    = "audio";
    public static final String VIDEO_URL_KEY    = "video";
    public static final String IS_QUESTION_KEY  = "isQuestion";
    public static final String USER_KEY         = "user";

    // added by Hyunji; to be used by QuestionsAdapter to display the icon of the type
    String type = Types.TEXT; // by default

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
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        Entry otherEntry = (Entry) o;
        return getObjectId().equals(otherEntry.getObjectId());
    }
}
