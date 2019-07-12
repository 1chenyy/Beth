package com.chen.beth;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.chen.beth.Utils.LogUtil;
import com.chen.beth.UI.NotificationHelper;
import com.chen.beth.Worker.QueryLatestBlockTask;
import com.chen.beth.Worker.QueryPriceWorker;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static com.chen.beth.UI.NotificationHelper.FOREGROUND_SERVICE_ID;

public class MainSyncService extends Service {
    private NotificationHelper notificationHelper = new NotificationHelper();
    private Timer timer ;
    private NotificationManager notificationManager = (NotificationManager) BethApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
    private static final String QUERY_PRICE_TAG = "PeriodQueryPrice";

    public MainSyncService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().register(this);
        LogUtil.d(this.getClass(),"MainSyncService Start !");
        configForegroundService();
        startQueryPrice();
        startQueryLatestBlock();
        return super.onStartCommand(intent, flags, startId);
    }



    private void startQueryLatestBlock() {
        timer = new Timer();
        QueryLatestBlockTask task = new QueryLatestBlockTask();
        timer.schedule(task,0,5000);
    }

    private void startQueryPrice() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(QueryPriceWorker.class,
                        16,
                        TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .addTag(QUERY_PRICE_TAG)
                        .build();
        WorkManager.getInstance()
                .enqueue(periodicWorkRequest);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void configForegroundService(){
        startForeground(FOREGROUND_SERVICE_ID,
                notificationHelper.getMainSyncNotification(getString(R.string.main_top_loading),getString(R.string.main_top_loading)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onPriceEvent(PriceAndMktcapBean bean){
        if (bean.status == 0){
            notificationHelper.updateMainSyncNotification("",getString(R.string.main_top_no_data));
        }else{
            notificationHelper.updateMainSyncNotification("","$"+bean.result.price);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onLatestBlockEvent(LatestBlockBean bean){
        if (bean.status == 0){
            notificationHelper.updateMainSyncNotification(getString(R.string.main_top_no_data),"");
        }else{
            notificationHelper.updateMainSyncNotification(""+bean.result.number,"");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        WorkManager.getInstance().cancelAllWorkByTag(QUERY_PRICE_TAG);
        if (timer!=null)
            timer.cancel();
        EventBus.getDefault().unregister(this);
    }
}
