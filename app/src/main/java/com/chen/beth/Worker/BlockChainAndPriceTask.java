package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.db.dao.BlockDao;
import com.chen.beth.models.BlockChainFragmentBlockBundleBean;
import com.chen.beth.models.MainFragmentBlockBundleBean;
import com.chen.beth.models.BlockSummaryBean;
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
import java.util.List;

import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BlockChainAndPriceTask extends IntentService {
    private static final String ACTION_QUERY_HISTORY_PRICE = "com.chen.beth.Worker.action.query_history_price";
    private static final String ACTION_QUERY_ETH_MKTCAP = "com.chen.beth.Worker.action.query_eth_mktcap";
    private static final String ACTION_QUERY_BLOCKS_FROM_DB = "com.chen.beth.Worker.action.query_blocks_from_db";
    private static final String ACTION_QUERY_LATEST_BLOCKS_FROM_NET = "com.chen.beth.Worker.action.query_latest_block_from_net";

    private static final String EXTRA_PARAM_START = "com.chen.beth.Worker.extra.start";

    public BlockChainAndPriceTask() {
        super("BlockChainAndPriceTask");
    }


    public static void queryHistoryPrice(Context context) {
        Intent intent = new Intent(context, BlockChainAndPriceTask.class);
        intent.setAction(ACTION_QUERY_HISTORY_PRICE);
        context.startService(intent);
    }

    public static void queryEthMktcap(Context context) {
        Intent intent = new Intent(context, BlockChainAndPriceTask.class);
        intent.setAction(ACTION_QUERY_ETH_MKTCAP);
        context.startService(intent);
    }

    public static void queryBlocksFromDB(Context context, int start) {
        Intent intent = new Intent(context, BlockChainAndPriceTask.class);
        intent.putExtra(EXTRA_PARAM_START, start);
        intent.setAction(ACTION_QUERY_BLOCKS_FROM_DB);
        context.startService(intent);
    }

    public static void queryLatestBlocksFromNet(Context context, int start) {
        Intent intent = new Intent(context, BlockChainAndPriceTask.class);
        intent.putExtra(EXTRA_PARAM_START, start);
        intent.setAction(ACTION_QUERY_LATEST_BLOCKS_FROM_NET);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        int start = -1;
        if (intent != null) {
            switch (intent.getAction()) {
                case ACTION_QUERY_HISTORY_PRICE:
                    handleQueryHistoryPrice();
                    break;
                case ACTION_QUERY_ETH_MKTCAP:
                    handleQueryEthMktcap();
                    break;
                case ACTION_QUERY_BLOCKS_FROM_DB:
                    start = intent.getIntExtra(EXTRA_PARAM_START, -1);
                    if (start != -1) {
                        handleQueryBlocksFromDB(start);
                    }
                    break;
                case ACTION_QUERY_LATEST_BLOCKS_FROM_NET:
                    start = intent.getIntExtra(EXTRA_PARAM_START, -1);
                    if (start != -1) {
                        handleQueryLatesBlocksFromNet(start);
                    }
                    break;
            }
        }
    }

    private void handleQueryLatesBlocksFromNet(int start) {
        LogUtil.d(this.getClass(),"从"+start+"开始查询最新区块");
        RetrofitManager.getBethAPIServices().getLatestBlocks(start)
                .map(b -> b = BaseUtil.filterBlocks(b))
                .subscribe(b->handleQueryLatestBlockSucceed(b,start),
                        t->handleQueryLatestBlockFailed(t));
    }


    public void handleQueryLatestBlockSucceed(MainFragmentBlockBundleBean bean,int start){

        BlockChainFragmentBlockBundleBean event = new BlockChainFragmentBlockBundleBean();
        event.result = new BlockChainFragmentBlockBundleBean.ResultBean();
        if (bean.status == Const.RESULT_SUCCESS){
            if (bean.result!=null){
                event.result.blocks = BaseUtil.validatorFromLowToHigh(bean.result.blocks,start);
                if (event.result.blocks.size()!=0){
                    event.status = Const.RESULT_SUCCESS;
                    LogUtil.d(this.getClass(),"查询成功");
                    saveData(event.result.blocks);
                }else{
                    event.status = Const.RESULT_NO_DATA;
                }
            }else{
                LogUtil.e(this.getClass(),"本次请求没有数据");
                event.status = Const.RESULT_NO_DATA;
            }
        }else{
            LogUtil.e(this.getClass(),bean.error);
            event.status = Const.RESULT_NO_DATA;
        }
        EventBus.getDefault().post(event);
    }
    public void handleQueryLatestBlockFailed(Throwable throwable){
        BlockChainFragmentBlockBundleBean event = new BlockChainFragmentBlockBundleBean();
        event.status = Const.RESULT_NO_NET;
        EventBus.getDefault().post(event);
    }

    private void handleQueryBlocksFromDB(int start) {
        List<BlockSummaryBean> list = new ArrayList<>();
        BlockChainFragmentBlockBundleBean event = new BlockChainFragmentBlockBundleBean();
        event.result = new BlockChainFragmentBlockBundleBean.ResultBean();
        int netStart = 0;
        if (start == 0) {
            LogUtil.d(this.getClass(), "开始从数据库取最近15个区块");
            BlockSummaryBean[] result = BethApplication.getBlockData().getBlockDao().queryLatest15Blocks();
            for (BlockSummaryBean bean : result) {
                list.add(bean);
            }
            list = BaseUtil.validatorFromHighToLow(list);
            if (list.size() != 15) {
                netStart = list.get(list.size() - 1).number;
                LogUtil.d(this.getClass(), "数据库中区块不足15个，从" + netStart + "开始从网络查询");
            }
        } else {
            BlockDao blockDao = BethApplication.getBlockData().getBlockDao();
            LogUtil.d(this.getClass(), "从" + start + "开始从数据库取10个区块");
            for (int i = 0; i < 10; i++) {
                BlockSummaryBean[] temp = blockDao.getBlockSummaryByNumber(start);
                start--;
                if (temp.length != 0)
                    list.add(temp[0]);
                else
                    break;
            }
            if (list.size() != 10) {
                netStart = start;
                LogUtil.d(this.getClass(), "数据库中区块不足10个，从" + netStart + "开始从网络查询");
            }
        }
        if (netStart != 0) {
            List<BlockSummaryBean> finalList = list;
            try {
                LogUtil.d(this.getClass(), "从" + netStart + "开始从网络取10个区块");
                Response<MainFragmentBlockBundleBean> response = RetrofitManager.getBethAPIServices().getOnePageBlocks(netStart).execute();
                if (response.isSuccessful() && response.body() != null && response.body().result != null) {
                    LogUtil.d(this.getClass(), "从网络取区块成功" + response.body().status);
                    MainFragmentBlockBundleBean respBody = BaseUtil.filterBlocks(response.body());
                    if (respBody.result.blocks.size() != 0) {
                        saveData(respBody.result.blocks);
                        list.addAll(BaseUtil.validatorFromHighToLow(respBody.result.blocks));
                    }
                }
            } catch (IOException e) {
                LogUtil.d(this.getClass(), "查询失败");
                LogUtil.d(this.getClass(), e.getMessage());
                event.status = Const.RESULT_NO_NET;
            }
        }
        if (list.size() != 0) {
            event.status = Const.RESULT_SUCCESS;
            event.result.blocks = list;
        } else {
            if (event.status != Const.RESULT_NO_NET) {
                event.status = Const.RESULT_NO_DATA;
            }
        }
        EventBus.getDefault().post(event);
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

    private void handleQueryHistoryPrice() {
        LogUtil.d(this.getClass(), "开始查询历史价格");
        RetrofitManager.getBethAPIServices().getHistoryPrice()
                .subscribe(b -> handleSucceed(b),
                        t -> handleHistoryPriceFailed(t));
    }

    private void handleSucceed(HistoryPriceBean bean) {
        HistoryPriceBean event = new HistoryPriceBean();
        if (bean.status == 1) {
            if (bean.result != null) {
                event = bean;
            } else {
                LogUtil.e(this.getClass(), "本次请求没有数据");
                event.status = 0;
            }
        } else {
            LogUtil.e(this.getClass(), bean.error);
            event.status = 0;
        }
        BaseUtil.RemoveAndSendStickEvent(event);
    }


    private void handleHistoryPriceFailed(Throwable throwable) {
        LogUtil.e(this.getClass(), throwable.getMessage());
        HistoryPriceBean event = new HistoryPriceBean();
        event.status = 0;
        BaseUtil.RemoveAndSendStickEvent(event);
    }


    private void saveData(List<BlockSummaryBean> list) {
        BethApplication.getBlockData().getBlockDao().insertBlocks(list);
    }


}
