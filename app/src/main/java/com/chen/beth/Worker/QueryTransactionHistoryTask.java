package com.chen.beth.Worker;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.net.RetrofitManager;

public class QueryTransactionHistoryTask implements Runnable{
    @Override
    public void run() {
        LogUtil.d(this.getClass(),"开始查询历史交易");
        if (BaseUtil.isConnected()){
            RetrofitManager.getBethAPIServices().getHistoryTransaction()
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

    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        HistoryTransactionBean event = new HistoryTransactionBean();
        event.status = 0;
        BaseUtil.RemoveAndSendStickEvent(event);
    }
}
