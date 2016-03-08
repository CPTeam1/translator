package com.cp1.translator.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishanpande on 3/4/16.
 */
@ParseClassName("Post")
public class Post extends ParseObject{
    public static final String QUESTION_KEY = "question";
    public static final String ANSWERS_KEY = "answers";

    public Entry getQuestion() {
        return (Entry) getParseObject(QUESTION_KEY);
    }

    public void setQuestion(Entry entry) {
        entry.put(Entry.IS_QUESTION_KEY, true); // Just in case, the flag was not set by user.
        put(QUESTION_KEY, entry);
    }

    public List<Entry> getAnswers() {
        return getList(ANSWERS_KEY);
    }

    public void addAnswer(Entry answer) {
        answer.remove(Entry.IS_QUESTION_KEY); // Being paranoid.
        List<Entry> answers = getAnswers();
        if (answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(answer);
        put(ANSWERS_KEY, answers);
    }

    public void removeAnswer(Entry answer) {
        List<Entry> answers = getAnswers();
        if (answers != null) {
            answers.remove(answer);
            put(ANSWERS_KEY, answers);
        }
    }

    @Override
    public boolean equals(Object o) {
        Post otherPost = (Post) o;
        return getObjectId().equals(otherPost.getObjectId());
    }
}
