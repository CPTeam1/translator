package com.cp1.translator.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cp1.translator.R;
import com.cp1.translator.activities.PostActivity;
import com.cp1.translator.models.Entry;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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
                // Iterate the parse keys if needed
//                Iterator<String> itr = json.keys();
//                while (itr.hasNext()) {
//                    String key = (String) itr.next();
//                    String value = json.getString(key);
//                    Log.d(TAG, "..." + key + " => " + value);
//                    // Extract custom push data
//                    if (key.equals("username")) {
//                        // create a local notification
//                        createNotification(context, value);
//                    }
//                    else if (key.equals("launch")) {
//                        // Handle push notification by invoking activity directly
//                        launchSomeActivity(context, value);
//                    } else if (key.equals("broadcast")) {
//                        // OR trigger a broadcast to activity
//                        triggerBroadcastToActivity(context, value);
//                    }
//            }
        }catch(JSONException ex){
            Log.d(TAG, "JSON failed!");
        }
    }

}

public static final int NOTIFICATION_ID = 45;

    // Create a local dashboard notification to tell user about the event
    // See: http://guides.codepath.com/android/Notifications
    private void createNotification(Context context, JSONObject json) {
        try {
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

    // Handle push notification by sending a local broadcast
    // to which the activity subscribes to
    // See: http://guides.codepath.com/android/Starting-Background-Services#communicating-with-a-broadcastreceiver
//    private void triggerBroadcastToActivity(Context context, String datavalue) {
//        Intent intent = new Intent(intentAction);
//        intent.putExtra("data", datavalue);
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//    }
}
