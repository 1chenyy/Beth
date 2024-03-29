package com.chen.beth.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.chen.beth.BethApplication;

public class PreferenceUtil {
    public static String PREFERENCE_NAME = "BethData";
    public static String PREFERENCE_MINER_MARK = "Miner";
    public static final String IS_FIRST_RUN = "isFirstRun";

    public static boolean hasKey(String key){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                .contains(key);
    }

    public static boolean putBoolean(String key,boolean value){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(String key,boolean defValue){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
                .getBoolean(key,defValue);
    }

    public static boolean putInt(String key, int value){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit().putInt(key,value).commit();
    }

    public static int getInt(String key,int defValue){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
                .getInt(key,defValue);
    }

    public static boolean putString(String key,String value){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit().putString(key,value).commit();
    }

    public static String getString(String key,String defValue){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
                .getString(key,defValue);
    }

    public static boolean putString(String key,String value,String name){
        return BethApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit().putString(key,value).commit();
    }

    public static String getString(String key,String defValue,String name){
        return BethApplication.getContext().getSharedPreferences(name,Context.MODE_PRIVATE)
                .getString(key,defValue);
    }

    public static long getLong(String key,long defValue){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
                .getLong(key,defValue);
    }

    public static SharedPreferences.Editor getEdit(){
        return BethApplication.getContext().getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
                .edit();
    }

    public static boolean isFirst(){
        return getBoolean(IS_FIRST_RUN,true);
    }
}
