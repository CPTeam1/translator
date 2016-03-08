package com.cp1.translator.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.cp1.translator.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import static com.cp1.translator.utils.Constants.*;

@Table(name = "Questions")
public class Question extends Model {
    // Define database columns and associated fields
    @Column(name = "question")
    String question;

    @Column(name = "image")
    String imageURI;

    @Column(name = "audio")
    String audioURI;

    @Column(name = "video")
    String videoURI;

    @Column(name = "answered")
    Boolean answered;


    @Column(name = "asked_user")
    String askedByUser;


    public String getAskedByUser() {
        return askedByUser;
    }

    public void setAskedByUser(String askedByUser) {
        this.askedByUser = askedByUser;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getAudioURI() {
        return audioURI;
    }

    public void setAudioURI(String audioURI) {
        this.audioURI = audioURI;
    }

    public String getVideoURI() {
        return videoURI;
    }

    public void setVideoURI(String videoURI) {
        this.videoURI = videoURI;
    }

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    // Make sure to always define this constructor with no arguments
    public Question() {
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public Question(String qs, String user){
        super();

        try {
            this.question = qs;
            this.answered = false;
            this.askedByUser = user;

        } catch (Exception e) {
            Log.e(APP_TAG,"Failed in creating Tweet Object" + e.getMessage());
        }
    }


    public static Question toQuestion (String qs, String askedByUser) {
        Question ques = new Question(qs,askedByUser);
        ques.save();
        return ques;
    }
}