package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String PROFILE_PIC_KEY = "profilePic";
    public static final String FRIENDS_KEY = "friends";
    public static final String SKILLS_KEY = "skills";

    public String getProfilePic() {
        return getString(PROFILE_PIC_KEY);
    }

    public void setProfilePic(String profilePic) {
        put(PROFILE_PIC_KEY, profilePic);
    }

    public List<User> getFriends() {
        return getList(FRIENDS_KEY);
    }

    public void addFriend(User user) {
        List<User> friends = getFriends();
        if (friends == null) {
            friends = new ArrayList<>();
        }
        friends.add(user);
    }

    public void removeFriend(User user) {
        List<User> friends = getFriends();
        if (friends != null) {
            friends.remove(user);
            put(FRIENDS_KEY, friends);
        }
    }

    public List<Skill> getSkills() {
        return getList(SKILLS_KEY);
    }

    public void addSkill(Skill skill) {
        List<Skill> skills = getSkills();
        if (skills == null) {
            skills = new ArrayList<>();
        }
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        List<Skill> skills = getSkills();
        if (skills != null) {
            skills.remove(skill);
            put(SKILLS_KEY, skills);
        }
    }
}
