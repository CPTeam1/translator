package com.cp1.translator.models;

import android.os.Parcelable;

import com.cp1.translator.utils.ModelListener;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ishanpande on 3/4/16.
 */
@ParseClassName("Entry")
public class Entry extends ParseObject implements Parcelable {

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
    public static final String CREATED_AT_KEY   = "createdAt";

    private String text;
    private String imgURL;
    private String videoURL;
    private String audioURL;
    private String userID;
    private String fromLang;
    private String toLang;
    private String type;
    private String isQuestion = "false";



    @Override
    public int describeContents() {
        return 0;
    }

    private Entry(android.os.Parcel in){
        this.text = in.readString();
        this.imgURL = in.readString();
        this.videoURL = in.readString();
        this.audioURL = in.readString();
        this.fromLang = in.readString();
        this.toLang = in.readString();
        this.type = in.readString();
        this.isQuestion = in.readString();
        this.userID = in.readString();
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.imgURL);
        dest.writeString(this.videoURL);
        dest.writeString(this.audioURL);
        dest.writeString(this.fromLang);
        dest.writeString(this.toLang);
        dest.writeString(this.type);
        dest.writeString(this.isQuestion);
        dest.writeString(this.userID);
    }


    public String getImgURL() {
        return imgURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public String getIsQuestion() {
        return isQuestion;
    }

    public String getQuestionText() {
        return this.text;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<Entry> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Entry> CREATOR
            = new Parcelable.Creator<Entry>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Entry createFromParcel(android.os.Parcel in) {
            return new Entry(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public User getUser() {
        return (User) getParseObject(USER_KEY);
    }

    public void setUser(User user){
        this.userID = user.getUsername();
        put(USER_KEY,user);
    }

    public String getText() {
        return getString(TEXT_KEY);
    }

    public void setText(String text) {
        this.text = text;
        put(TEXT_KEY, text);
    }

    public void setFromLang(String fromLang){
        this.fromLang = fromLang;
        put(FROM_LANG_KEY,fromLang);
    }

    public void setToLang(String toLang){
        this.toLang = toLang;
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
        this.imgURL = url.toString();
        put(IMAGE_URL_KEY, url);
    }

    public ParseFile getAudioUrl() {
        return getParseFile(AUDIO_URL_KEY);
    }

    public void setAudioUrl(ParseFile url) {

        this.audioURL = url.toString();
        put(AUDIO_URL_KEY, url);
    }

    public ParseFile getVideoUrl() {
        return getParseFile(VIDEO_URL_KEY);
    }

    public void setVideoUrl(ParseFile url) {
        this.videoURL = url.toString();
        put(VIDEO_URL_KEY, url);
    }

    public boolean isQuestion() {
        return getBoolean(IS_QUESTION_KEY);
    }

    public void setAsQuestion() {
        this.isQuestion = "true";
        put(IS_QUESTION_KEY, true);
    }

    public String getType() {
        return getString(QS_TYPE_KEY);
    }

    public String getTypeLocally(){
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
        put(QS_TYPE_KEY,type);
    }

    @Override
    public boolean equals(Object o) {
        Entry otherEntry = (Entry) o;
        return getObjectId().equals(otherEntry.getObjectId());
    }
}
