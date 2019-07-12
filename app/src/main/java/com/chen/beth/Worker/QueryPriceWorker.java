package com.chen.beth.Worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.PriceAndMktcapBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import retrofit2.Response;

public class QueryPriceWorker extends Worker {

    public QueryPriceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (!isStopped()){
            RetrofitManager.getAPIServices().getPriceAndMketcap().
                    subscribe(p->handleSucceed(p),
                            t->handleFailed(t));
        }
        return Result.success();
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
