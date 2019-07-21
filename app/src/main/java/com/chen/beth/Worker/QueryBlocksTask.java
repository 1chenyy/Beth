package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.MainFragmentBlockBundleBean;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class QueryBlocksTask extends IntentService {
    private static final String ACTION_QUERY_LATEST_15_BLOCKS = "com.chen.beth.Worker.action.queryLatest15Blocks";
    private static final String ACTION_QUERY_LATEST_BLOCKS = "com.chen.beth.Worker.action.queryLatestBlocks";

    private static final String EXTRA_PARAM_CURRENT = "com.chen.beth.Worker.extra.param.current";

    public QueryBlocksTask() {
        super("QueryBlocksTask");
    }

    public static void startActionQueryLatest15Blocks(Context context) {
        Intent intent = new Intent(context, QueryBlocksTask.class);
        intent.setAction(ACTION_QUERY_LATEST_15_BLOCKS);
        context.startService(intent);
    }
    public static void startActionQueryLatestBlocks(Context context,int current) {
        Intent intent = new Intent(context, QueryBlocksTask.class);
        intent.putExtra(EXTRA_PARAM_CURRENT,current);
        intent.setAction(ACTION_QUERY_LATEST_BLOCKS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
                case ACTION_QUERY_LATEST_15_BLOCKS:
                    LogUtil.d(this.getClass(),"开始查询最近的15个区块");
                    handleQueryLatest15Blocks();
                    break;
                case ACTION_QUERY_LATEST_BLOCKS:
                    int num = intent.getIntExtra(EXTRA_PARAM_CURRENT,-1);
                    if (num != -1){
                        LogUtil.d(this.getClass(),"开始查询从第"+num+"个区块开始查询最新区块");
                        handleQueryLatestBlocks(intent.getIntExtra(EXTRA_PARAM_CURRENT,num));
                    }
                    break;
            }
        }
    }

    private void saveData(List<BlockSummaryBean> list){
        BethApplication.getBlockData().getBlockDao().insertBlocks(list);
    }

    private void handleQueryLatest15Blocks(){
        RetrofitManager.getBethAPIServices().getLatest15Blocks()
                .map(b->b = BaseUtil.filterBlocks(b))
                .subscribe(b->handleSucceed( b,0),
                        t->handleFailed(t));
    }



    private void handleQueryLatestBlocks(int current){
        RetrofitManager.getBethAPIServices().getLatestBlocks(current)
                .map(b->b = BaseUtil.filterBlocks(b))
                .subscribe(b->handleSucceed(b,current),
                        t->handleFailed(t));
    }

    private void handleSucceed(MainFragmentBlockBundleBean bean, int type){
        MainFragmentBlockBundleBean event = new MainFragmentBlockBundleBean();
        if (bean.status == Const.RESULT_SUCCESS){
            if (bean.result!=null){
                LogUtil.d(this.getClass(),"查询成功");
                event = bean;
                if (type == 0){
                    event.result.blocks = BaseUtil.validatorFromHighToLow(event.result.blocks);
                }else {
                    event.result.blocks = BaseUtil.validatorFromLowToHigh(event.result.blocks,type);
                }
                if (event.result.blocks.size()!=0){
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



    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        MainFragmentBlockBundleBean event = new MainFragmentBlockBundleBean();
        event.status = Const.RESULT_NO_NET;
        EventBus.getDefault().post(event);
    }


}
