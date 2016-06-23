package com.as.atlas.teaorder;

import android.app.Application;

import com.parse.Parse;
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
                .applicationId("76ee57f8e5f8bd628cc9586e93d428d5")
                .server("http://parseserver-ps662-env.us-east-1.elasticbeanstalk.com/parse/")
                //.clientKey("S8O5WCV4pTM9dsSIQQR4vwPXUHUUDYAMnx0nJ6HC")   // if choose non-public
                .enableLocalDataStore()
                .build()

        );

    }
}
