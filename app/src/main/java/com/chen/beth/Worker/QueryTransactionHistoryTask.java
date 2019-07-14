package com.chen.beth.Worker;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.net.RetrofitManager;

import java.util.List;

public class QueryTransactionHistoryTask implements Runnable{
    @Override
    public void run() {
        LogUtil.d(this.getClass(),"开始查询历史交易");
        if (BaseUtil.isConnected()){
            RetrofitManager.getAPIServices().getHistoryTransaction()
                    .subscribe(b->handleSucceed(b),
                            t->handleFailed(t));
        }else{
            HistoryTransactionBean event = new HistoryTransactionBean();
            event.status = -1;
            BaseUtil.RemoveAndSendStickEvent(event);
        }
    }

    private void handleSucceed(HistoryTransactionBean bean){
        HistoryTransactionBean event = new HistoryTransactionBean();
        if (bean.status == 1 ){
            if (bean.result!=null){
                event = bean;
                saveData(event.result.number);
            }else{
                LogUtil.e(this.getClass(),"本次请求没有数据");
                event.status = 0;
            }
        }else{
            LogUtil.e(this.getClass(),bean.error);
            event.status = 0;
        }
        BaseUtil.RemoveAndSendStickEvent(event);
    }

    private void saveData(List<Integer> number) {
        BaseUtil.runOnUiThread(()->{
            PreferenceUtil.putString(Const.KEY_HISTORY_DATE,BaseUtil.getTodayString());
            PreferenceUtil.putString(Const.KEY_HISTORY_VALUE,BaseUtil.ListToString(number));
        });
    }


    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        HistoryTransactionBean event = new HistoryTransactionBean();
        event.status = 0;
        BaseUtil.RemoveAndSendStickEvent(event);
    }
}
