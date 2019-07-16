package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.models.HistoryPriceBean;
import com.chen.beth.net.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ShortTask extends IntentService {
    private static final String ACTION_STORE_BLOCKS = "com.chen.beth.Worker.action.store_blocks";
    private static final String ACTION_QUERY_HISTORY_PRICE = "com.chen.beth.Worker.action.query_history_price";

    private static final String EXTRA_PARAM_BLOCKS = "com.chen.beth.Worker.extra.blocks";

    public ShortTask() {
        super("ShortTask");
    }


    public static void startActionStoreBlocks(Context context, String param1) {
        Intent intent = new Intent(context, ShortTask.class);
        intent.setAction(ACTION_STORE_BLOCKS);
        intent.putExtra(EXTRA_PARAM_BLOCKS, param1);
        context.startService(intent);
    }

    public static void queryHistoryPrice(Context context){
        Intent intent = new Intent(context,ShortTask.class);
        intent.setAction(ACTION_QUERY_HISTORY_PRICE);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            switch (intent.getAction()){
                case ACTION_QUERY_HISTORY_PRICE:
                    handlerQueryHistoryPrice();
                    break;
            }
        }
    }

    private void handlerQueryHistoryPrice(){
        LogUtil.d(this.getClass(),"开始查询历史价格");
        RetrofitManager.getAPIServices().getHistoryPrice()
                .subscribe(b->handleSucceed(b),
                        t->handleFailed(t));
    }

    private void handleSucceed(HistoryPriceBean bean){
        HistoryPriceBean event = new HistoryPriceBean();
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
        HistoryPriceBean event = new HistoryPriceBean();
        event.status = 0;
        BaseUtil.RemoveAndSendStickEvent(event);
    }

}
