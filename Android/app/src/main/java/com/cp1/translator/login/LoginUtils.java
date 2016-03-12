package com.cp1.translator.login;

import android.app.Activity;
import android.content.Intent;

import com.parse.ParseUser;

/**
 * Created by eelango on 3/12/16.
 */
public class LoginUtils {
    public static void checkIfLoggedIn(Activity currActivity) {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(currActivity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            currActivity.startActivity(intent);
            currActivity.finish();
        }
    }

    public static void logout(Activity currActivity) {
        ParseUser.logOut();
        LoginUtils.checkIfLoggedIn(currActivity);
    }
}
