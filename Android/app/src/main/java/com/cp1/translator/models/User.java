package com.cp1.translator.models;

import com.cp1.translator.utils.SaveListener;
import com.parse.ParseClassName;
import com.cp1.translator.utils.ModelListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcel;

import java.util.List;

@Parcel(analyze={User.class})
@ParseClassName("_User")
public class User extends ParseUser {
    public interface UsersListener extends ModelListener {
        void onUsers(List<User> users);
    }

    public interface FriendshipListener {
        void changedFriendship(User user, boolean isFriend);
    }

    public static final String USERNAME_KEY = "username";
    public static final String NICKNAME_KEY = "nickname";
    public static final String PROFILE_PIC_KEY = "profilePic";
    public static final String FRIENDS_KEY = "friends";
    public static final String LANGS_KEY = "langs";


    public String getProfilePic() {
        return getString(PROFILE_PIC_KEY);
    }

    public void setProfilePic(String profilePic) {
        put(PROFILE_PIC_KEY, profilePic);
    }

    public ParseRelation<User> getFriendsRelation() {
        return getRelation(FRIENDS_KEY);
    }

    public void getFriends(final UsersListener listener) {
        ParseRelation<User> friends = getRelation(FRIENDS_KEY);
        ParseQuery<User> query = friends.getQuery();
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null)
                    listener.onUsers(objects);
                else
                    listener.onError(e);
            }
        });
    }

    public void addFriend(User user, final SaveListener listener) {
        ParseRelation<User> friendsRelation = getRelation(FRIENDS_KEY);
        friendsRelation.add(user);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.saved();
                } else {
                    listener.onError(e);
                }
            }
        });
    }

    public void removeFriend(User user, final SaveListener listener) {
        ParseRelation<User> friendsRelation = getRelation(FRIENDS_KEY);
        friendsRelation.remove(user);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.saved();
                } else {
                    listener.onError(e);
                }
            }
        });
    }

    public void getQuestions(final Entry.EntriesListener listener) {
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
        query.whereEqualTo(Entry.USER_KEY, this);
        query.whereEqualTo(Entry.IS_QUESTION_KEY, true);
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> objects, ParseException e) {
                if (e == null) {
                    listener.onEntries(objects);
                } else {
                    listener.onError(e);
                }
            }
        });
    }

    public void getAnswers(final Entry.EntriesListener listener) {
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
        query.whereEqualTo(Entry.USER_KEY, this);
        query.whereEqualTo(Entry.IS_QUESTION_KEY, false);
        query.findInBackground(new FindCallback<Entry>() {
            @Override
            public void done(List<Entry> objects, ParseException e) {
                if (e == null) {
                    listener.onEntries(objects);
                } else {
                    listener.onError(e);
                }
            }
        });
    }

    public void getLangs(final Lang.LangsListener listener) {
        ParseRelation<Lang> langs = getRelation(LANGS_KEY);
        ParseQuery<Lang> query = langs.getQuery();
        query.findInBackground(new FindCallback<Lang>() {
            @Override
            public void done(List<Lang> objects, ParseException e) {
                if (e == null)
                    listener.onLangs(objects);
                else
                    listener.onError(e);
            }
        });
    }

    public void addLang(Lang lang) {
        ParseRelation<Lang> langs = getRelation(LANGS_KEY);
        langs.add(lang);
    }

    public void removeLang(Lang lang) {
        ParseRelation<Lang> langs = getRelation(LANGS_KEY);
        langs.remove(lang);
    }

    @Override
    public boolean equals(Object o) {
        User otherUser = (User) o;
        return getObjectId().equals(otherUser.getObjectId());
    }

    public String getNickname() {
        return getString(NICKNAME_KEY);
    }

    public void setNickname(String nickname) {
        put(NICKNAME_KEY, nickname);
    }
}
