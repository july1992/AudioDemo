package com.vily.audiodemo2;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by chenxf on 17-7-14.
 */

public class CommonApp extends Application {
    private static final String TAG = "CommonApp";
    private static Application mApplication = null;

    public static Context getContext() {
        return mApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        if (mApplication == null) {
            Log.i(TAG, "onCreate: Application has not been set, extends from Application now");
            mApplication = this;
            super.onCreate();
        }
    }
}
