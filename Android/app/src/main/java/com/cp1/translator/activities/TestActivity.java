package com.cp1.translator.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.login.LoginUtils;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.Constants;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.cp1.translator.utils.Constants.*;

public class TestActivity extends AppCompatActivity {
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginUtils.checkIfLoggedIn(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tv = (TextView) findViewById(R.id.tvTestQs);
        imageView = (ImageView) findViewById(R.id.ivTestImage);

        User currUser = (User) User.getCurrentUser();
        // Query for all Entries for this user
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Entry");
        query.whereEqualTo("user", currUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    Log.e(APP_TAG, "Error in retrieving questions by current user");
                } else {
                    Log.d(APP_TAG, "entries are here!");

                    // TODO: remove! this is just a test
                    // Should never loop through all entries like this
                    for (int i = 0; i < objects.size(); i++) {
                        // Cast to Entry, since query is of type entry
                        final Entry entry = (Entry) objects.get(i);
                        Log.d(APP_TAG, "entry: " + entry.getText());
                        if (entry.getImageUrl() != null) {

                            ParseFile imgFile = entry.getImageUrl();
                            imgFile.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e) {
                                    if (e != null) {
                                        Log.e(APP_TAG, "Error in fetching the image");
                                    } else {
                                        Log.d(APP_TAG, "Image retrieved from Parse successfully");
                                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        imageView.setImageBitmap(bmp);
                                        tv.setText(entry.getText());
                                    }// end else
                                }// end done
                            }); // end data callback
                        }// end if imgURL
                    }// end for
                }// end else

            }
        });
    }
}

