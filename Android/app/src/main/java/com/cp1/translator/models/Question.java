package com.cp1.translator.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;

/**
 * Created by ishanpande on 3/4/16.
 */
@Table(name = "questions")
public class Question {
    @Column(name = "question")
    private String question;

    @Column(name = "image")
    private String imageURI;

    @Column(name = "audio")
    private String audioURI;

    @Column(name = "video")
    private String videoURI;

    @Column(name = "answered")
    private Boolean answered;

    @Column(name = "answers")
    private ArrayList<String> answerIDs;

    @Column(name = "asked_user")
    private User askedByUser;

    public Question() {
        super();
    }


}
