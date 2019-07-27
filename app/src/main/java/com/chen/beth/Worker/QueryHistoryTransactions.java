package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;


public class QueryHistoryTransactions extends IntentService {

    public QueryHistoryTransactions() {
        super("QueryHistoryTransactions");
    }

    public static void startQuery() {
        Intent intent = new Intent(BethApplication.getContext(), QueryHistoryTransactions.class);
        BethApplication.getContext().startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        RetrofitManager.getBethAPIServices().getHistoryTransaction()
                .subscribe(b->handleSucceed(b),
                        t->handleFailed(t));
    }

    private void handleSucceed(HistoryTransactionBean bean){
        LogUtil.d(this.getClass(),"加载成功,status="+bean.status);
        HistoryTransactionBean event = new HistoryTransactionBean();
        if (bean.status == Const.RESULT_SUCCESS ){
            if (bean.result!=null){
                event = bean;
            }else{
                event.status = 0;
            }
        }else{
            LogUtil.e(this.getClass(),bean.error);
            event.status = 0;
        }
        EventBus.getDefault().post(event);
    }

    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        HistoryTransactionBean event = new HistoryTransactionBean();
        event.status = 0;
        EventBus.getDefault().post(event);
    }
}
