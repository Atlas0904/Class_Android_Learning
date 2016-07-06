package com.as.atlas.teaorder;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by atlas on 2016/6/23.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Order.class);  // ParseObejct 需要 init
        ParseObject.registerSubclass(Drink.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
//                .applicationId("76ee57f8e5f8bd628cc9586e93d428d5")
//                .server("http://parseserver-ps662-env.us-east-1.elasticbeanstalk.com/parse/")
                //.clientKey("S8O5WCV4pTM9dsSIQQR4vwPXUHUUDYAMnx0nJ6HC")   // if choose non-public
                .applicationId("kwiZHAaPNqyMGA9Eh26zWW8EfXC1om5mHH4y0fJ4")
                .clientKey("Cj0BOcGRYU33eEOApOEz3FDus1OnwRJ2ec8Q8GDD")
                .server("https://parseapi.back4app.com/")
                .enableLocalDataStore()
                .build()

        );

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        ParseFacebookUtils.initialize(this);

    }
}
