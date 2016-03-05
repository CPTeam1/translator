package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String PROFILE_PIC_KEY = "profilePic";
    public static final String FRIENDS_KEY = "friends";
    public static final String SKILLS_KEY = "skills";
    public static final String QUESTIONS_KEY = "questions";
    public static final String ANSWERS_KEY = "answers";


    public String getProfilePic() {
        return getString(PROFILE_PIC_KEY);
    }

    public void setProfilePic(String profilePic) {
        put(PROFILE_PIC_KEY, profilePic);
    }

    public ParseRelation<User> getFriends() {
        return getRelation(FRIENDS_KEY);
    }

    public void addFriend(User user) {
        getFriends().add(user);
        saveInBackground();
    }

    public void removeFriend(User user) {
        getFriends().remove(user);
        saveInBackground();
    }

    public ParseRelation<Skill> getSkills() {
        return getRelation(SKILLS_KEY);
    }

    public void addSkill(Skill skill) {
        getSkills().add(skill);
        saveInBackground();
    }

    public void removeSkill(Skill skill) {
        getSkills().remove(skill);
        saveInBackground();
    }

    public ParseRelation<Entry> getQuestions() {
        return getRelation(QUESTIONS_KEY);
    }

    public void addQuestion(Entry question) {
        getQuestions().add(question);
        saveInBackground();
    }

    public void removeQuestion(Entry question) {
        getQuestions().remove(question);
        saveInBackground();
    }

    public ParseRelation<Entry> getAnswers() {
        return getRelation(ANSWERS_KEY);
    }

    public void addAnswer(Entry answer) {
        getAnswers().add(answer);
        saveInBackground();
    }

    public void removeAnswer(Entry answer) {
        getAnswers().remove(answer);
        saveInBackground();
    }
}
