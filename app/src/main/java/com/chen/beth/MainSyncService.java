package com.chen.beth;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.UI.NotificationHelper;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.Worker.QueryLatestBlockTask;
import com.chen.beth.Worker.QueryPriceTask;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.chen.beth.UI.NotificationHelper.FOREGROUND_SERVICE_ID;

public class MainSyncService extends Service {
    private NotificationHelper notificationHelper ;
    private ScheduledExecutorService executorService;
    private Timer periodicLatestBlockTimer;

    public MainSyncService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().register(this);
        LogUtil.d(this.getClass(),"MainSyncService Start !");
        configForegroundService();
        executorService = Executors.newScheduledThreadPool(3);
        startQueryPrice();
        startQueryLatestBlock();
        //startQueryTransactionHistory();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startQueryTransactionHistory() {
        String now = Const.SDF.format(new Date());
        if (now.equals(PreferenceUtil.getString(Const.KEY_HISTORY_DATE,""))){

        }else{

        }
    }

    private void startQueryLatestBlock() {
        QueryLatestBlockTask queryLatestBlockTask = new QueryLatestBlockTask();
        executorService.scheduleAtFixedRate(queryLatestBlockTask,0,5,TimeUnit.SECONDS);
    }



    private void startQueryPrice() {
        LogUtil.d(this.getClass(),"开始周期性查询价格和市值");
        QueryPriceTask queryPriceTask = new QueryPriceTask();
        executorService.scheduleAtFixedRate(queryPriceTask,0,15, TimeUnit.MINUTES);
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
        executorService.shutdownNow();
        EventBus.getDefault().unregister(this);
    }
}
