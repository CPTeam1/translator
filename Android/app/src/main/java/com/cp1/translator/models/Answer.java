package com.cp1.translator.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ishanpande on 3/4/16.
 */
@Table(name = "answers")
public class Answer extends Model{
    @Column(name = "answerID")
    private String answerID;

    @Column(name = "ans_by_user")
    private User ansByUser;

    @Column(name = "ans_text")
    private String answerText;

    @Column(name = "ans_image")
    private String answerImgURI;

    @Column(name = "ans_audio")
    private String answerAudioURI;

    @Column(name = "ans_video")
    private String answerVideoURI;

    public Answer() {
        super();
    }
}
