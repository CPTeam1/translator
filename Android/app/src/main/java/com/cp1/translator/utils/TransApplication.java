package com.cp1.translator.utils;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.cp1.translator.R;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Lang;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = TransApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TransApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TransApplication.context = this;

        ParseObject.registerSubclass(Entry.class);
        ParseObject.registerSubclass(Lang.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);

        ActiveAndroid.initialize(this);

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Quicksand-Bold.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

		// set applicationId and server based on the values in the Heroku settings.
		// any network interceptors must be added with the Configuration Builder given this syntax
        Parse.enableLocalDatastore(this);
		Parse.initialize(new Parse.Configuration.Builder(this)
				.applicationId("Cp1TranslatorId")
				.clientKey(null)
                .enableLocalDataStore()
				.addNetworkInterceptor(new ParseLogInterceptor())
				.server("https://cp1-translator.herokuapp.com/parse/").build());

		ParseInstallation.getCurrentInstallation().saveInBackground();
	}

	public static RestClient getRestClient() {
		return (RestClient) RestClient.getInstance(RestClient.class, TransApplication.context);
	}
}