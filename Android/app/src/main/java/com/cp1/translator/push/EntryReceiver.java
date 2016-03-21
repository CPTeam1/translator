package com.cp1.translator.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cp1.translator.R;
import com.cp1.translator.activities.PostActivity;
import com.cp1.translator.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EntryReceiver extends BroadcastReceiver {
    private static final String TAG = "EntryReceiver";
    public static final String intentAction = "com.parse.push.intent.RECEIVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.d(TAG, "Receiver intent null");
        } else {
            // Parse push message and handle accordingly
            processPush(context, intent);
        }
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "got action " + action);
        if (action.equals(intentAction)) {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                createNotification(context, json);
            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }

    }

    public static final int NOTIFICATION_ID = 45;

    // Create a local dashboard notification to tell user about the event
    // See: http://guides.codepath.com/android/Notifications
    private void createNotification(final Context context, JSONObject json) {
        try {
            if (json.has("entryid")) {

                String username = json.getString("username");
                String entryid = json.getString("entryid");

                // add a pending intent to launch the PostActivity
                Intent intent = new Intent(context, PostActivity.class);
                // Next, let's turn this into a PendingIntent using
                //   public static PendingIntent getActivity(Context context, int requestCode,
                //       Intent intent, int flags)
                int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
                int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
                PendingIntent postIntent = PendingIntent.getActivity(context, requestID, intent, flags);


                Notification notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(username + " asked a question.")
                        .setContentIntent(postIntent)
                        .setAutoCancel(true)
                        .setContentText(entryid).build();


                NotificationManager mNotificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            } else if (json.has("new_buddy_id")) {
                String new_buddy_id = json.getString("new_buddy_id");

                ParseQuery<User> uquery = new ParseQuery<>(User.class);
                uquery.whereEqualTo("objectId", new_buddy_id);
                uquery.findInBackground(new FindCallback<User>() {
                    @Override
                    public void done(List<User> users, ParseException e) {
                        if (e == null) {
                            User friend = users.get(0);
                            Notification notification = new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle(friend.getNickname())
                                    .setContentText("Added you as a Buddy!")
                                    .build();

                            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(NOTIFICATION_ID, notification);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Log.e("ELANLOG", json.toString());
            }

        } catch (JSONException exp) {
            exp.printStackTrace();
        }

    }

    // Handle push notification by invoking activity directly
    // See: http://guides.codepath.com/android/Using-Intents-to-Create-Flows
//    private void launchSomeActivity(Context context, String datavalue) {
//        Intent pupInt = new Intent(context, ShowPopUp.class);
//        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        pupInt.putExtra("data", datavalue);
//        context.getApplicationContext().startActivity(pupInt);
//    }
}
