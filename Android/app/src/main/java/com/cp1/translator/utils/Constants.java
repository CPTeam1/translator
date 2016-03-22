package com.cp1.translator.utils;

import android.text.format.DateUtils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ishanpande on 3/3/16.
 */
public class Constants {
    public static final String APP_TAG = "socialtranslator";
    public static final String photoFileName = "photo";
    public static final String SEPARATOR = "_";
    public static final String PIC_EXT = ".jpg";
    public static final String QNO = "questionNo";
    public static final String AUDIO_EXT = ".3gp";
    public static final String IMAGE = "image";
    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String TEXT = "text";
    public static final String ENTRY_KEY = "entry_key";
    public static final String POST_KEY = "post_key";
    public static final String IS_ANSWER_KEY = "is_answer_key";
    public static final String HIDE_FAB_KEY = "hide_fab_key";

    public static final String ANS_HINT = "Please type in your answer..";
    public static final String QS_HINT = "What's your question?";

    /*********************** req codes *********************/
    public static final int ASK_QS_REQ_CODE = 500;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public static final int VIDEO_CAPTURE = 101;
    /*********************** end of req codes*/



    public static String getRelativeTimeAgo(Date date) {
        Format formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        String rawJsonDate = formatter.format(date);
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}