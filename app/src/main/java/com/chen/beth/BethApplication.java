package com.chen.beth;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import androidx.room.Room;

import com.chen.beth.db.BlockData;
import com.facebook.stetho.Stetho;
import com.tencent.mmkv.MMKV;

public class BethApplication extends Application {
    private static Context context;
    private static int mainThread;
    private static Handler handler;
    private static BlockData blockData;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        MMKV.initialize(this);
        context = getApplicationContext();
        mainThread = android.os.Process.myTid();
        handler = new Handler();
        blockData = Room.databaseBuilder(this, BlockData.class,"blocks.db").build();
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

    public static BlockData getBlockData(){
        return blockData;
    }
}
