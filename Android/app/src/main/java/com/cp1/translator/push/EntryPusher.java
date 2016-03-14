package com.cp1.translator.push;

import com.parse.ParseCloud;

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
}
