package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.MktcapDetailBean;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class QueryMktcapTask extends IntentService {


    public QueryMktcapTask() {
        super("QueryMktcapTask");
    }


    public static void startQueryMktcap() {
        Intent intent = new Intent(BethApplication.getContext(), QueryMktcapTask.class);
        BethApplication.getContext().startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            handleQueryEthMktcap();
        }
    }

    private void handleQueryEthMktcap() {
        LogUtil.d(this.getClass(), "开始查询以太币分布情况");
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
            if (rows.size() == 0)
                throw new IllegalArgumentException("rows.size()==0");
            for (Element row : rows) {
                Elements content = row.select("div.col-md-5");
                if (content.size() != 0) {
                    result.add(Double.parseDouble(handleEthMktcapString(content.get(0).text())));
                }
            }
            if (result.size() != 4)
                throw new IllegalArgumentException("result.size()!=4");
            bean.result = result;
            bean.status = Const.RESULT_SUCCESS;
        } catch (IOException e) {
            LogUtil.d(this.getClass(), "网络错误");
            bean.status = Const.RESULT_NO_NET;
        } catch (Exception e) {
            LogUtil.d(this.getClass(), "解析出错" + e.getMessage());
            bean.status = Const.RESULT_NO_DATA;
        }
        EventBus.getDefault().post(bean);
    }

    private String handleEthMktcapString(String original) {
        return original.replace(",", "").split(" ")[0];
    }


}
