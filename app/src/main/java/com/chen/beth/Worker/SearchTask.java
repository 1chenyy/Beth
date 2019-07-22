package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.OneBlockSummaryBean;
import com.chen.beth.models.SearchHistory;
import com.chen.beth.models.TransactionBean;
import com.chen.beth.models.TransactionDetailBean;
import com.chen.beth.net.RetrofitManager;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SearchTask extends IntentService {
    private static final String ACTION_SEARCH_TRANSACTION = "com.chen.beth.Worker.action.search_transaction";
    private static final String ACTION_SEARCH_BLOCK = "com.chen.beth.Worker.action.search_block";
    private static final String ACTION_QUERY_ALL_HISTORY = "com.chen.beth.Worker.action.query_all_history";
    private static final String ACTION_STORE_TRANSACTION = "com.chen.beth.Worker.action.store_transaction";

    private static final String EXTRA_TX_HASH = "com.chen.beth.Worker.extra.param_tx_hash";
    private static final String EXTRA_BLOCK_NUMBER = "com.chen.beth.Worker.extra.param_block_number";
    private static final String EXTRA_BLOCK_NUMBER_USER = "com.chen.beth.Worker.extra.param_user";
    private static final String EXTRA_TRANSACTION = "com.chen.beth.Worker.extra.param_transaction";

    public SearchTask() {
        super("SearchTask");
    }

    public static void startSearchTransaction(Context context, String hash) {
        Intent intent = new Intent(context, SearchTask.class);
        intent.setAction(ACTION_SEARCH_TRANSACTION);
        intent.putExtra(EXTRA_TX_HASH, hash);
        context.startService(intent);
    }
    private static Disposable searchTransactionDisposable;

    public static void stopSearchTransaction(){
        if (searchTransactionDisposable!=null){
            LogUtil.d(SearchTask.class,searchTransactionDisposable.isDisposed()+"");
            //searchTransactionDisposable.dispose();
            LogUtil.d(SearchTask.class,searchTransactionDisposable.isDisposed()+"");
        }

    }

    public static void startSearchBlockByNumber(Context context, int num,boolean user){
        Intent intent = new Intent(context, SearchTask.class);
        intent.setAction(ACTION_SEARCH_BLOCK);
        intent.putExtra(EXTRA_BLOCK_NUMBER, num);
        intent.putExtra(EXTRA_BLOCK_NUMBER_USER, user);
        context.startService(intent);
    }

    public static void startQueryAllHistory(Context context){
        Intent intent = new Intent(context, SearchTask.class);
        intent.setAction(ACTION_QUERY_ALL_HISTORY);
        context.startService(intent);
    }

    public static void startStoreTransaction(Context context,TransactionBean bean){
        Intent intent = new Intent(context, SearchTask.class);
        intent.setAction(ACTION_STORE_TRANSACTION);
        intent.putExtra(EXTRA_TRANSACTION,bean);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
                case ACTION_SEARCH_TRANSACTION:
                    String hash = intent.getStringExtra(EXTRA_TX_HASH);
                    if (!TextUtils.isEmpty(hash)){
                        saveHistory(Const.TYPE_TX,hash);
                        handleSearchTransactionByHash(hash);
                    }else{
                        handleFailedSearchTransactionByHash(new Throwable("hash is empty"));
                    }
                    break;
                case ACTION_SEARCH_BLOCK:
                    int num = intent.getIntExtra(EXTRA_BLOCK_NUMBER,-1);
                    boolean user = intent.getBooleanExtra(EXTRA_BLOCK_NUMBER_USER,false);
                    if (num>=0){
                        if (user){
                            saveHistory(Const.TYPE_BLOCK,num+"");
                        }
                        handleSearchBlockByNumber(num);
                    }else{
                        handleFailedSearchBlockByNumber(new Throwable("num is empty"));
                    }
                    break;
                case ACTION_QUERY_ALL_HISTORY:
                    LogUtil.d(this.getClass(),"开始查询所有历史记录");
                    EventBus.getDefault().post(BethApplication.getDBData().getSearchHistoryDao().getAll());
                    break;
                case ACTION_STORE_TRANSACTION:
                    handleStoreTransaction(intent);
                    break;
            }
        }
    }

    private void handleStoreTransaction(Intent intent){
        LogUtil.d(this.getClass(),"开始缓存交易");
        try {
            TransactionBean transactionBean = (TransactionBean) intent.getSerializableExtra(EXTRA_TRANSACTION);
            if (transactionBean!=null){
                BethApplication.getDBData().getTransactionDao().insertHistories(transactionBean);
            }
        }catch (Exception e){
            LogUtil.d(this.getClass(),e.getMessage());
        }
    }

    private void handleSearchBlockByNumber(int num){
        BlockSummaryBean[] results = BethApplication.getDBData().getBlockDao().getBlockSummaryByNumber(num);
        if (results.length>0){
            OneBlockSummaryBean event = new OneBlockSummaryBean();
            event.status = Const.RESULT_SUCCESS;
            event.result = results[0];
            EventBus.getDefault().post(event);
        }else{
            RetrofitManager.getBethAPIServices().getBlockByNumber(num)
                    .subscribe(b->handleSucceedSearchBlockByNumber(b),
                            t->handleFailedSearchBlockByNumber(t));
        }
    }

    public void handleSucceedSearchBlockByNumber(OneBlockSummaryBean bean){
        if (bean.result!=null){
            bean.status = Const.RESULT_SUCCESS;
            BethApplication.getDBData().getBlockDao().insertBlock(bean.result);
        }else{
            bean.status = Const.RESULT_NO_DATA;
        }
        EventBus.getDefault().post(bean);
    }

    public void handleFailedSearchBlockByNumber(Throwable throwable){
        OneBlockSummaryBean event = new OneBlockSummaryBean();
        event.status = Const.RESULT_NO_NET;
        EventBus.getDefault().post(event);
    }



    private void handleSearchTransactionByHash(String hash){
        LogUtil.d(this.getClass(),"开始查询交易："+hash);
        TransactionBean[] beans = BethApplication.getDBData().getTransactionDao().getTransactionByHash(hash);
        if (beans.length!=0){
            LogUtil.d(this.getClass(),"查询到缓存交易");
            TransactionDetailBean event = new TransactionDetailBean();
            event.result = beans[0];
            EventBus.getDefault().post(event);
        }else{
            searchTransactionDisposable = RetrofitManager.getEtherscanProxyAPIServices()
                    .getTransactionByHash(Const.ETHERSCAN_PROXY_MODULE,
                            Const.ETHERSCAN_PROXY_ACTION_GETTXS,
                            hash)
                    .subscribeOn(Schedulers.io())
                    .subscribe(b->handleSucceedSearchTransactionByHash(b),
                            t->handleFailedSearchTransactionByHash(t));
        }

    }

    private void text(String t){
        LogUtil.d(this.getClass(),t);
    }

    private void handleSucceedSearchTransactionByHash(TransactionDetailBean bean){
        LogUtil.d(this.getClass(),"查询成功");
        if (bean.result==null){
            bean.result = new TransactionBean();
            bean.result.hash="";
        }
        EventBus.getDefault().post(bean);
    }
    private void handleFailedSearchTransactionByHash(Throwable throwable){
        LogUtil.d(this.getClass(),"SearchTransactionByHash: " + throwable.getMessage());
        TransactionDetailBean event = new TransactionDetailBean();
        event.result = null;
        EventBus.getDefault().post(event);
    }

    private void saveHistory(int type,String content){
        SearchHistory[] historys = BethApplication.getDBData().getSearchHistoryDao().querySameHistory(type,content);
        if (historys.length==0){
            SearchHistory history = new SearchHistory();
            history.content = content;
            history.type = type;
            BethApplication.getDBData().getSearchHistoryDao().insertHistories(history);
        }
    }
}
