package com.chen.beth.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogUtil {
    private static final String TAG = "Beth";
    private static boolean showLog = true;
    private static boolean WriteLog = false;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void e(Class c,String msg){
        if (showLog) {
            String str = String.format("%s(%s) : %s", SDF.format(Calendar.getInstance().getTime()), c.getSimpleName(), msg);
            Log.e(TAG, str);
        }

    }

    public static void d(Class c,String msg){
        if (showLog) {
            String str = String.format("%s(%s) : %s", SDF.format(Calendar.getInstance().getTime()),c.getSimpleName(), msg);
            Log.d(TAG, str);
        }

    }


}
