package com.cp1.translator.push;

import com.cp1.translator.models.Entry;
import com.parse.ParseCloud;
import com.parse.ParseUser;

import java.util.HashMap;

/**
 * Created by eelango on 3/13/16.
 */
public class EntryPusher {
    public static void pushTestToEveryone() {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("customData", "My message");
        ParseCloud.callFunctionInBackground("pushChannelTest", payload);
    }

    public static void pushEntryToFriends(String entry) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("username", ParseUser.getCurrentUser().getUsername());
        payload.put("entryid", entry);
        ParseCloud.callFunctionInBackground("pushEntryToFriends", payload);
    }
}
