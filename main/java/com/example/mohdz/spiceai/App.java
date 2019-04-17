package com.example.mohdz.spiceai;

import android.app.Application;
import android.content.Context;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by zeeshan on 2017-10-28.
 */

public class App extends Application {

    private static final String TAG = "APP";
    private static App appInstance;
    private static Context context;

    public static Context getAppContext() {
        return App.context;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        appInstance = this;
        App.context = getApplicationContext();
        TypefaceProvider.registerDefaultIconSets();
    }

    public static App getAppInstance(){
        return appInstance;
    }
}
