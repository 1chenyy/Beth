package com.chen.beth;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.facebook.stetho.Stetho;

public class BethApplication extends Application {
    private static Context context;
    private static int mainThread;
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        context = getApplicationContext();
        mainThread = android.os.Process.myTid();
        handler = new Handler();
    }

    public static Context getContext() {
        return context;
    }

    public static int getMainThread() {
        return mainThread;
    }

    public static Handler getHandler() {
        return handler;
    }


}
