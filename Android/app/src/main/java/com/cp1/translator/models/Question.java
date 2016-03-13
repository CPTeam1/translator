package com.cp1.translator.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.parceler.Parcel;

import static com.cp1.translator.utils.Constants.APP_TAG;

@Parcel(analyze={Question.class})
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

    @Column(name = "fromLanguage")
    String fromLang;

    @Column(name = "toLanguage")
    String toLang;

    @Column(name = "asked_user")
    String askedByUser;

    @Column(name = "answers")
    String answers;

    // added by Hyunji; to be used by QuestionsAdapter to display the icon of the question type
    @Column(name = "type")
    String type = Types.TEXT; // by default

    public String getFromLang() {
        return fromLang;
    }

    public void setFromLang(String fromLang) {
        this.fromLang = fromLang;
    }

    public String getToLang() {
        return toLang;
    }

    public void setToLang(String toLang) {
        this.toLang = toLang;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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