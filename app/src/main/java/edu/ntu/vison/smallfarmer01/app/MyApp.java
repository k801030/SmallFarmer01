package edu.ntu.vison.smallfarmer01.app;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Vison on 2015/9/25.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
