package com.cp1.translator.models;

import java.util.ArrayList;

public class User {
    private String uID;
    private String profilePic;
    private ArrayList<String> friends;
    private ArrayList<String> primaryLang;
    private ArrayList<String> secondaryLang;

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getPrimaryLang() {
        return primaryLang;
    }

    public void setPrimaryLang(ArrayList<String> primaryLang) {
        this.primaryLang = primaryLang;
    }

    public ArrayList<String> getSecondaryLang() {
        return secondaryLang;
    }

    public void setSecondaryLang(ArrayList<String> secondaryLang) {
        this.secondaryLang = secondaryLang;
    }
}
