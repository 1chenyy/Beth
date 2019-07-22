package com.chen.beth;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import androidx.room.Room;

import com.chen.beth.db.DBData;
import com.facebook.stetho.Stetho;
import com.tencent.mmkv.MMKV;

public class BethApplication extends Application {
    private static Context context;
    private static int mainThread;
    private static Handler handler;
    private static DBData DBData;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        MMKV.initialize(this);
        context = getApplicationContext();
        mainThread = android.os.Process.myTid();
        handler = new Handler();
        DBData = Room.databaseBuilder(this, DBData.class,"blocks.db").build();
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

    public static DBData getDBData(){
        return DBData;
    }
}
