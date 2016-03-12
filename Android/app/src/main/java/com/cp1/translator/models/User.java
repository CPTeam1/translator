package com.cp1.translator.models;

import android.util.Log;

import com.cp1.translator.utils.Constants;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.cp1.translator.utils.Constants.*;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String USERNAME_KEY     = "username";
    public static final String PROFILE_PIC_KEY  = "profilePic";
    public static final String FRIENDS_KEY      = "friends";
    public static final String SKILLS_KEY       = "skills";
    public static final String ENTRIES_KEY      = "entries";
    public static final String NICKNAME_KEY     = "nickname";



    public String getProfilePic() {
        return getString(PROFILE_PIC_KEY);
    }

    public void setProfilePic(String profilePic) {
        put(PROFILE_PIC_KEY, profilePic);
    }

    public List<User> getFriends() {
        List<User> friends = getList(FRIENDS_KEY);
        if (friends == null)
            friends = new ArrayList<>();
        return friends;
    }

    public List<Entry> getEntries(){
        return getList(ENTRIES_KEY);
    }


    public void addEntry(Entry entry) {
        List<Entry> currEntries = getEntries();
        if (currEntries == null) {
            Log.d(APP_TAG, "curr Entries are null, creating new entry list");
            currEntries = new ArrayList<>();
        }
        currEntries.add(entry);
        put(ENTRIES_KEY, currEntries);
    }

    public void removeEntry(Entry entry) {
        List<Entry> entries = getEntries();
        if (entries != null) {
            Log.d(APP_TAG,"removing entry "+entry);
            entries.remove(entry);
            put(FRIENDS_KEY, entries);
        }
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
