package com.chen.beth.Worker;



import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.PriceAndMktcapBean;
import com.chen.beth.net.RetrofitManager;

public class QueryPriceTask implements Runnable {
    @Override
    public void run() {
        LogUtil.d(this.getClass(),"开始获取价格与市值");
        if (BaseUtil.isConnected()){
            RetrofitManager.getAPIServices().getPriceAndMketcap().
                    subscribe(p->handleSucceed(p),
                            t->handleFailed(t));
        }else{
            PriceAndMktcapBean event = new PriceAndMktcapBean();
            event.status = -1;
            BaseUtil.RemoveAndSendStickEvent(event);
        }
    }

    private void handleSucceed(PriceAndMktcapBean priceAndMktcapBean){
        PriceAndMktcapBean event = new PriceAndMktcapBean();
        if (priceAndMktcapBean.status == 1 ){
            if (priceAndMktcapBean.result!=null){
                LogUtil.d(this.getClass(),"查询到最新价格与市值");
                event = priceAndMktcapBean;
            }else{
                LogUtil.e(this.getClass(),"本次请求没有数据");
                event.status = 0;
            }
        }else{
            LogUtil.e(this.getClass(),priceAndMktcapBean.error);
            event.status = 0;
        }
        BaseUtil.RemoveAndSendStickEvent(event);
    }

    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        PriceAndMktcapBean event = new PriceAndMktcapBean();
        event.status = 0;
        BaseUtil.RemoveAndSendStickEvent(event);
    }


}
