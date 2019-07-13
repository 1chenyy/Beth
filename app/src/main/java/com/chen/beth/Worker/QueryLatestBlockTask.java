package com.chen.beth.Worker;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.NetState;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;

public class QueryLatestBlockTask implements Runnable {
    private static NetState state = NetState.NONE;
    @Override
    public void run() {
        if (state!=getState()){
            NetState oldState = state;
            state = getState();
            if (oldState!=NetState.NONE){
                new QueryPriceTask().run();
            }
        }
        if (BaseUtil.isConnected()){
            RetrofitManager.getAPIServices().getLatestBlock()
                    .subscribe(l->handleSucceed(l),t->handleFailed(t));
        }else{
            LatestBlockBean event = new LatestBlockBean();
            event.status = -1;
            BaseUtil.RemoveAndSendStickEvent(event);
        }

    }

    private NetState getState(){
        return BaseUtil.isConnected()?NetState.CONNECTED:NetState.DISCONNECTED;
    }

    private void handleSucceed(LatestBlockBean latestBlockBean){
        LatestBlockBean event = new LatestBlockBean();
        if (latestBlockBean.status == 1 ){
            if (latestBlockBean.result!=null){
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
