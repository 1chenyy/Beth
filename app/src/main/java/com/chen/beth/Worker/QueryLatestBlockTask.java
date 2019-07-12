package com.chen.beth.Worker;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;

import java.util.TimerTask;

public class QueryLatestBlockTask extends TimerTask {
    @Override
    public void run() {
        if (BaseUtil.isConnected()){
            RetrofitManager.getAPIServices().getLatestBlock()
                    .subscribe(l->handleSucceed(l),t->handleFailed(t));
        }else{

        }

    }

    private void handleSucceed(LatestBlockBean latestBlockBean){
        LatestBlockBean event = new LatestBlockBean();
        if (latestBlockBean.status == 1 ){
            if (latestBlockBean.result!=null){
                LogUtil.d(this.getClass(),"查询到最新价格与市值");
                event = latestBlockBean;
            }else{
                LogUtil.e(this.getClass(),"本次请求没有数据");
                event.status = 0;
            }
        }else{
            LogUtil.e(this.getClass(),latestBlockBean.error);
            event.status = 0;
        }
        BaseUtil.RemoveAndSendStickEvent(event);
    }

    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        LatestBlockBean event = new LatestBlockBean();
        event.status = 0;
        BaseUtil.RemoveAndSendStickEvent(event);
    }
}
