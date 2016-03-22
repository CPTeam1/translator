package com.cp1.translator.models;


import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ishanpande on 3/4/16.
 */
@Parcel(analyze={Post.class})
@ParseClassName("Post")
public class Post extends ParseObject{
    public static final String QUESTION_KEY = "question";
    public static final String ANSWERS_KEY = "answers";
    public static final String FROM_LANG_KEY = "fromLang";
    public static final String TO_LANG_KEY = "toLang";
    public static final String CREATED_AT = "createdAt";

    String fromLang;
    String toLang;

    public Entry getQuestion() {
        return (Entry) getParseObject(QUESTION_KEY);
    }

    public void setQuestion(Entry entry) {
        entry.put(Entry.IS_QUESTION_KEY, true); // Just in case, the flag was not set by user.
        put(QUESTION_KEY, entry);
    }

    public void getAnswers(final Entry.EntriesListener listener) {
        ParseRelation<Entry> answers = getRelation(ANSWERS_KEY);
        ParseQuery<Entry> query = answers.getQuery();
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> entriesList, ParseException e) {
                if (e != null)
                    listener.onError(e);
                else
                    listener.onEntries(entriesList);
            }
        });
    }

    public void addAnswer(Entry answer) {
        ParseRelation<Entry> answers = getRelation(ANSWERS_KEY);
        answers.add(answer);
    }

    public void removeAnswer(Entry answer) {
        ParseRelation<Entry> answers = getRelation(ANSWERS_KEY);
        answers.remove(answer);
    }

    public void setFromLang(String fromLang) {
        this.fromLang = fromLang;
        put(FROM_LANG_KEY, fromLang);
    }

    public void setToLang(String toLang) {
        this.toLang = toLang;
        put(TO_LANG_KEY, toLang);
    }

    public String getFromLang() {
        return getString(FROM_LANG_KEY);
    }

    public String getToLang(){
        return getString(TO_LANG_KEY);
    }


    @Override
    public boolean equals(Object o) {
        ParseObject otherObject = (ParseObject) o;
        if (otherObject.getClassName() != "Post")
            return false;
        Post otherPost = (Post) o;
        return getObjectId().equals(otherPost.getObjectId());
    }
}
