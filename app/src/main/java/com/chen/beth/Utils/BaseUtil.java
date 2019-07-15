package com.chen.beth.Utils;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.chen.beth.BethApplication;
import com.chen.beth.R;

import org.greenrobot.eventbus.EventBus;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BaseUtil {
    private static Toast toast = null;
    public static final DisplayMetrics METRICS = BethApplication.getContext().getResources().getDisplayMetrics();
    public static final float SCREEN_DENSITY = METRICS.density;
    public static final float SCREEN_WIDTH = METRICS.widthPixels;
    public static final float SCREEN_HEIGHT = METRICS.heightPixels;
    public static final ConnectivityManager CONNECTIVITY_MANAGER = ((ConnectivityManager) BethApplication.getContext().getSystemService(
            Context.CONNECTIVITY_SERVICE));


    public static boolean isRunOnUiThread(){
        return BethApplication.getMainThread() == android.os.Process.myTid();
    }

    public static void runOnUiThread(Runnable runnable){
        if (isRunOnUiThread())
            runnable.run();
        else
            BethApplication.getHandler().post(runnable);
    }

    public static void showToast(int msg){
        showToast(getString(msg));
    }

    public static void showToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null)
                    toast = Toast.makeText(BethApplication.getContext(),msg,Toast.LENGTH_SHORT);
                else
                    toast.setText(msg);
                toast.show();
            }
        });
    }

    public static String getString(int res){
        return BethApplication.getContext().getString(res);
    }

    public static Drawable getDrawable(int res){
        return  BethApplication.getContext().getDrawable(res);
    }

    public static int dpToPx(float value){
        return (int) (value*SCREEN_DENSITY+0.5f);
    }

    public static int pxToDp(float value){
        return (int) (value/SCREEN_DENSITY + 0.5f);
    }

    public static NetworkInfo.State getCurrentNetworkState() {
        NetworkInfo networkInfo = CONNECTIVITY_MANAGER.getActiveNetworkInfo();
        return networkInfo != null ? networkInfo.getState() : null;
    }

    public static boolean isConnected(){
        return getCurrentNetworkState() == NetworkInfo.State.CONNECTED;
    }

    public static void closeIO(Closeable closeable){
        if (closeable!=null)
            try {
                closeable.close();
            }catch (Exception e){

            }
    }

    public static void createNotificationChannel(String id,String name,String description){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(id,name, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(description);
            channel.setSound(null,null);
            BethApplication.getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    public static boolean isServiceRunning(String name){
        if (TextUtils.isEmpty(name))
            return false;
        ActivityManager manager = (ActivityManager) BethApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> serviceInfos = (ArrayList<ActivityManager.RunningServiceInfo>) manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : serviceInfos){
            if (info.service.getClassName().equals(name))
                return true;
        }
        return false;
    }

    public static void RemoveAndSendStickEvent(Object event){
        EventBus.getDefault().removeStickyEvent(event);
        EventBus.getDefault().postSticky(event);
    }

    public static int getRemainTime(){
        Calendar now  =Calendar.getInstance();
        return (23-now.get(Calendar.HOUR_OF_DAY))*3600 + (59-now.get(Calendar.MINUTE))*60 + (59-now.get(Calendar.SECOND));
    }

    public static String ListToString(List<Integer> list){
        StringBuilder sb = new StringBuilder();
        for (Integer i : list){
            sb.append(i);
            sb.append("-");
        }
        return sb.toString();
    }

    public static List<Integer> StringToList(String s){
        String[] strs = s.split("-");
        List<Integer> list = new ArrayList<>();
        for(String str:strs){
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    public static String getTodayString(){
        return Const.SDF_DAY.format(new Date());
    }

    public static String timestampToString(int time){
        return Const.SDF_DETAIL.format(new Date(time*1000L));
    }

    public static String omitMinerString(String miner){
        //0x5a0b54d5dc17e0aadc383d2db43b0a0d3e029c4c
        StringBuilder sb = new StringBuilder();
        sb.append(miner.substring(0,6));
        sb.append("...");
        sb.append(miner.substring(38));
        return sb.toString();
    }

    public static int recoveryNumFromBlockInfo(String info){
        String result = info.replace(getString(R.string.block_info_numer),"");
        return Integer.parseInt(result);
    }
}