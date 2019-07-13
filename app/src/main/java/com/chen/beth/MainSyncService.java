package com.chen.beth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.UI.NotificationHelper;
import com.chen.beth.Worker.QueryLatestBlockTask;
import com.chen.beth.Worker.QueryPriceTask;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.chen.beth.UI.NotificationHelper.FOREGROUND_SERVICE_ID;

public class MainSyncService extends Service {
    private NotificationHelper notificationHelper ;
    private ScheduledExecutorService periodicPriceQueryService;
    private ScheduledExecutorService periodicLatestBlockQueryService;
    private Timer periodicLatestBlockTimer;
    private boolean isPriceQueryRunning = false;

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
        periodicLatestBlockQueryService = Executors.newScheduledThreadPool(2);
        QueryLatestBlockTask queryLatestBlockTask = new QueryLatestBlockTask();
        periodicLatestBlockQueryService.scheduleAtFixedRate(queryLatestBlockTask,0,5,TimeUnit.SECONDS);
    }

    private void stopQueryLatestBlock(){
        periodicLatestBlockQueryService.shutdownNow();
    }

    private void startQueryPrice() {
        LogUtil.d(this.getClass(),"开始周期性查询价格和市值");
        periodicPriceQueryService = Executors.newScheduledThreadPool(2);
        QueryPriceTask queryPriceTask = new QueryPriceTask();
        periodicPriceQueryService.scheduleAtFixedRate(queryPriceTask,0,15, TimeUnit.MINUTES);
        isPriceQueryRunning = true;
    }

    private void stopQueryPrice(){
        LogUtil.d(this.getClass(),"停止周期性查询价格和市值");
        periodicPriceQueryService.shutdownNow();
        isPriceQueryRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void configForegroundService(){
        notificationHelper = new NotificationHelper();
        startForeground(FOREGROUND_SERVICE_ID,
                notificationHelper.getMainSyncNotification(getString(R.string.main_top_loading),getString(R.string.main_top_loading)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onPriceEvent(PriceAndMktcapBean bean){
        String price = "";
        switch (bean.status){
            case Const.RESULT_NORMAL:
                price = "$ "+bean.result.price;
                break;
            case Const.RESULT_NO_DATA:
                price = getString(R.string.main_top_no_data);
                break;
            case Const.RESULT_NO_NET:
                price = getString(R.string.main_top_no_network);
                break;
        }
        notificationHelper.updateMainSyncNotification("",price);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onLatestBlockEvent(LatestBlockBean bean){
        String latest = "";
        switch (bean.status){
            case Const.RESULT_NORMAL:
                latest = ""+bean.result.number;
                break;
            case Const.RESULT_NO_DATA:
                latest = getString(R.string.main_top_no_data);
                break;
            case Const.RESULT_NO_NET:
                latest = getString(R.string.main_top_no_network);
                break;
        }
        notificationHelper.updateMainSyncNotification(latest,"");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopQueryPrice();
        stopQueryLatestBlock();
        EventBus.getDefault().unregister(this);
    }
}
