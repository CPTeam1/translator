package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ishanpande on 3/4/16.
 */
@ParseClassName("Entry")
public class Entry extends ParseObject {
    public static final String POST_KEY = "post";
    public static final String USER_KEY = "user";
    public static final String TEXT_KEY = "text";
    public static final String IMAGE_URL_KEY = "image";
    public static final String AUDIO_URL_KEY = "audio";
    public static final String VIDEO_URL_KEY = "video";
    public static final String UPVOTE_KEY = "upvote";
    public static final String DOWNVOTE_KEY = "downvote";
    public static final String IS_QUESTION_KEY = "isQuestion";

    public Post getPost() {
        return (Post) getParseObject(POST_KEY);
    }

    public void setPost(Post post) {
        put(POST_KEY, post);
    }

    public User getUser() {
        return (User) getParseObject(USER_KEY);
    }

    public void setUser(User user) {
        put(USER_KEY, user);
    }

    public String getText() {
        return getString(TEXT_KEY);
    }

    public void setText(String text) {
        put(TEXT_KEY, text);
    }

    public String getImageUrl() {
        return getString(IMAGE_URL_KEY);
    }

    public void setImageUrl(String url) {
        put(IMAGE_URL_KEY, url);
    }

    public String getAudioUrl() {
        return getString(AUDIO_URL_KEY);
    }

    public void setAudioUrl(String url) {
        put(AUDIO_URL_KEY, url);
    }

    public String getVideoUrl() {
        return getString(VIDEO_URL_KEY);
    }

    public void setVideoUrl(String url) {
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

    public void setQuestion() {
        put(IS_QUESTION_KEY, true);
    }
}
