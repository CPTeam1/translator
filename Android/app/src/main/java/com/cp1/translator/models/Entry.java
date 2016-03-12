package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by ishanpande on 3/4/16.
 */
@ParseClassName("Entry")
public class Entry extends ParseObject {
    public static final String TEXT_KEY         = "text";
    public static final String IMAGE_URL_KEY    = "image";
    public static final String AUDIO_URL_KEY    = "audio";
    public static final String VIDEO_URL_KEY    = "video";
    public static final String UPVOTE_KEY       = "upvote";
    public static final String DOWNVOTE_KEY     = "downvote";
    public static final String IS_QUESTION_KEY  = "isQuestion";
    public static final String USER_KEY         = "user";

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

    public int getUpvote() {
        return getInt(UPVOTE_KEY);
    }

    public void upvote() {
        increment(UPVOTE_KEY);
    }

    public int getDownvote() {
        return getInt(DOWNVOTE_KEY);
    }

    public void downvote() {
        increment(DOWNVOTE_KEY);
    }

    public boolean isQuestion() {
        return getBoolean(IS_QUESTION_KEY);
    }

    public void setAsQuestion() {
        put(IS_QUESTION_KEY, true);
    }

    @Override
    public boolean equals(Object o) {
        Entry otherEntry = (Entry) o;
        return getObjectId().equals(otherEntry.getObjectId());
    }
}
