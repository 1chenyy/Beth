package com.chen.beth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.chen.beth.ui.NotificationHelper;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.Worker.QueryLatestBlockNumberTask;
import com.chen.beth.Worker.QueryPriceTask;
import com.chen.beth.Worker.QueryTransactionHistoryTask;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.chen.beth.ui.NotificationHelper.FOREGROUND_SERVICE_ID;

public class MainSyncService extends Service {
    private NotificationHelper notificationHelper ;
    private ScheduledExecutorService scheduledService;
    private ExecutorService executorService;
    private Timer periodicLatestBlockTimer;

    public MainSyncService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationHelper = new NotificationHelper();
        EventBus.getDefault().register(this);
        LogUtil.d(this.getClass(),"MainSyncService Start !");
        configForegroundService();
        scheduledService = Executors.newScheduledThreadPool(3);
        executorService = Executors.newCachedThreadPool();
        startQueryPrice();
        startQueryLatestBlock();
        startQueryTransactionHistory();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startQueryTransactionHistory() {
        String now = BaseUtil.getTodayString();
        QueryTransactionHistoryTask queryTransactionHistoryTask = new QueryTransactionHistoryTask();
        if (now.equals(PreferenceUtil.getString(Const.KEY_HISTORY_DATE,"")) &&
                !TextUtils.isEmpty(PreferenceUtil.getString(Const.KEY_HISTORY_VALUE,""))){
            List<Integer> number = BaseUtil.StringToList(PreferenceUtil.getString(Const.KEY_HISTORY_VALUE,""));
            HistoryTransactionBean event = new HistoryTransactionBean();
            event.status = 1;
            event.result = new HistoryTransactionBean.ResultBean();
            event.result.number = number;
            BaseUtil.RemoveAndSendStickEvent(event);
        }else{
            executorService.execute(queryTransactionHistoryTask);
        }
        scheduledService.scheduleAtFixedRate(queryTransactionHistoryTask,BaseUtil.getRemainTime()+1,24*60*60,TimeUnit.SECONDS);
    }

    private void startQueryLatestBlock() {
        QueryLatestBlockNumberTask queryLatestBlockTask = new QueryLatestBlockNumberTask();
        scheduledService.scheduleAtFixedRate(queryLatestBlockTask,0,5,TimeUnit.SECONDS);
    }



    private void startQueryPrice() {
        LogUtil.d(this.getClass(),"开始周期性查询价格和市值");
        QueryPriceTask queryPriceTask = new QueryPriceTask();
        scheduledService.scheduleAtFixedRate(queryPriceTask,0,15, TimeUnit.MINUTES);
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
        scheduledService.shutdownNow();
        executorService.shutdownNow();
        EventBus.getDefault().unregister(this);
    }
}
