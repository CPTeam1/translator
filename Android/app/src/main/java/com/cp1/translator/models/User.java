package com.cp1.translator.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String USERNAME_KEY = "username";
    public static final String PROFILE_PIC_KEY = "profilePic";
    public static final String FRIENDS_KEY = "friends";
    public static final String SKILLS_KEY = "skills";
    public static final String NICKNAME_KEY = "nickname";

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
        put(FRIENDS_KEY, friends);
    }

    public void removeFriend(User user) {
        List<User> friends = getFriends();
        if (friends != null) {
            friends.remove(user);
            put(FRIENDS_KEY, friends);
        }
    }

    @Override
    public boolean equals(Object o) {
        User otherUser = (User) o;
        return getObjectId().equals(otherUser.getObjectId());
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
        put(SKILLS_KEY, skill);
    }

    public void removeSkill(Skill skill) {
        List<Skill> skills = getSkills();
        if (skills != null) {
            skills.remove(skill);
            put(SKILLS_KEY, skills);
        }
    }

    public String getNickname() {
        return getString(NICKNAME_KEY);
    }

    public void setNickname(String nickname) {
        put(NICKNAME_KEY, nickname);
    }
}
