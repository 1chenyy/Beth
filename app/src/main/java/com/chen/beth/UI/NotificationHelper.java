package com.chen.beth.UI;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.chen.beth.BethApplication;
import com.chen.beth.MainActivity;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;

public class NotificationHelper {
    public static final int FOREGROUND_SERVICE_ID = 2;
    public static final String KEY_LATEST = "latest";
    public static final String KEY_PRICE = "price";
    private NotificationManager manager;
    private ArrayMap<String,NotificationCompat.Builder> builders = new ArrayMap<>();
    private ArrayMap<String,Object> olds = new ArrayMap<>();

    public NotificationHelper(){
        init();
    }

    private void init() {
        if (manager == null)
            manager = (NotificationManager) BethApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private NotificationCompat.Builder getBuilder(String channelID){
        if (builders.get(channelID)!=null)
            return builders.get(channelID);
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(BethApplication.getContext(),channelID);
        builders.put(channelID,builder);
        return builder;
    }

    public Notification getMainSyncNotification(String latest,String price){
        PendingIntent mainIntent = PendingIntent.getActivity(BethApplication.getContext(),
                0,
                new Intent(BethApplication.getContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent settingIntent = PendingIntent.getBroadcast(BethApplication.getContext(),
                1,
                new Intent("aa"),
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = getBuilder(BaseUtil.getString(R.string.id_channel_mainsync));
        RemoteViews notificationLayout = new RemoteViews(BethApplication.getContext().getPackageName(),R.layout.notification_main_sync);

        if (olds.containsKey(KEY_LATEST)){
            if (TextUtils.isEmpty(latest) ){
                latest = (String) olds.get(KEY_LATEST);
            }else{
                olds.put(KEY_LATEST,latest);
            }
        }else{
            olds.put(KEY_LATEST,latest);
        }
        notificationLayout.setTextViewText(R.id.tv_latest,latest);

        if (olds.containsKey(KEY_PRICE)){
            if (TextUtils.isEmpty(price)){
                price = (String) olds.get(KEY_PRICE);
            }else{
                olds.put(KEY_PRICE,price);
            }
        }else{
            olds.put(KEY_PRICE,price);
        }
        notificationLayout.setTextViewText(R.id.tv_price,price);

        notificationLayout.setOnClickPendingIntent(R.id.iv_settings,settingIntent);
        builder.setSmallIcon(R.drawable.ic_menu_blocks)
                .setContent(notificationLayout)
                .setContentIntent(mainIntent);
        return builder.build();
    }

    public void updateMainSyncNotification(String latest,String price){
        manager.notify(FOREGROUND_SERVICE_ID,getMainSyncNotification(latest,price));
    }
}
