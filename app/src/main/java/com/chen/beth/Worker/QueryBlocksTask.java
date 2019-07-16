package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.util.LruCache;

import androidx.room.Room;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.db.BlockData;
import com.chen.beth.models.BlockBundleBean;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class QueryBlocksTask extends IntentService {
    private static final String ACTION_QUERY_LATEST_15_BLOCKS = "com.chen.beth.Worker.action.queryLatest15Blocks";
    private static final String ACTION_QUERY_LATEST_BLOCKS = "com.chen.beth.Worker.action.queryLatestBlocks";

    private static final String EXTRA_PARAM_CURRENT = "com.chen.beth.Worker.extra.param.current";
    private static final String EXTRA_PARAM2 = "com.chen.beth.Worker.extra.PARAM2";

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

    private void handleQueryLatest15Blocks(){
        RetrofitManager.getAPIServices().getLatest15Blocks()
                .subscribe(b->handleSucceed(b),
                        t->handleFailed(t));
    }

    private void handleQueryLatestBlocks(int current){
        RetrofitManager.getAPIServices().getLatestBlocks(current)
                .subscribe(b->handleSucceed(b),
                        t->handleFailed(t));
    }

    private void handleSucceed(BlockBundleBean bean){
        BlockBundleBean event = new BlockBundleBean();
        if (bean.status == 1 ){
            if (bean.result!=null){
                LogUtil.d(this.getClass(),"查询成功");
                event = bean;
            }else{
                LogUtil.e(this.getClass(),"本次请求没有数据");
                event.status = 0;
            }
        }else{
            LogUtil.e(this.getClass(),bean.error);
            event.status = 0;
        }
        EventBus.getDefault().post(event);
    }



    private void handleFailed(Throwable throwable){
        LogUtil.e(this.getClass(),throwable.getMessage());
        BlockBundleBean event = new BlockBundleBean();
        event.status = 0;
        EventBus.getDefault().post(event);
    }
}
