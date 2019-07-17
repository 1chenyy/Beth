package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.HistoryPriceBean;
import com.chen.beth.models.MktcapDetailBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
    private static final String ACTION_QUERY_ETH_MKTCAP = "com.chen.beth.Worker.action.query_eth_mktcap";

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

    public static void queryEthMktcap(Context context){
        Intent intent = new Intent(context,ShortTask.class);
        intent.setAction(ACTION_QUERY_ETH_MKTCAP);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            switch (intent.getAction()){
                case ACTION_QUERY_HISTORY_PRICE:
                    handleQueryHistoryPrice();
                    break;
                case ACTION_QUERY_ETH_MKTCAP:
                    handleQueryEthMktcap();
                    break;
            }
        }
    }

    private void handleQueryEthMktcap(){
        LogUtil.d(this.getClass(),"开始查询以太币分布情况");
        MktcapDetailBean bean = new MktcapDetailBean();
        ArrayList<Double> result = new ArrayList<>();
        try {
            Document document = Jsoup.connect("https://etherscan.io/stat/supply").get();
            Elements cards = document.select("div.card.mb-3");
            if (cards.size() == 0)
                throw new IllegalArgumentException("cards.size() == 0");
            Elements cardBodys = cards.get(0).select("div.card-body");
            if (cardBodys.size() == 0)
                throw new IllegalArgumentException("ccardBodys.size() == 0");
            Elements rows = cardBodys.get(0).select("div.row");
            if (rows.size()==0)
                throw new IllegalArgumentException("rows.size()==0");
            for (Element row : rows){
                Elements content = row.select("div.col-md-5");
                if (content.size()!=0) {
                    result.add(Double.parseDouble(handleEthMktcapString(content.get(0).text())));
                }
            }
            if (result.size()!=4)
                throw new IllegalArgumentException("result.size()!=4");
            bean.result = result;
            bean.status = Const.RESULT_NORMAL;
        } catch (IOException e) {
            LogUtil.d(this.getClass(),"网络错误");
            bean.status = Const.RESULT_NO_NET;
        } catch (Exception e){
            LogUtil.d(this.getClass(),"解析出错"+e.getMessage());
            bean.status = Const.RESULT_NO_DATA;
        }
        EventBus.getDefault().post(bean);
    }

    private String handleEthMktcapString(String original){
        return original.replace(",","").split(" ")[0];
    }

    private void handleQueryHistoryPrice(){
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
