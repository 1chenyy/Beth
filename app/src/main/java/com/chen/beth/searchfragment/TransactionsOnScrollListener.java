package com.chen.beth.searchfragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.TransactionSummaryBean;
import com.chen.beth.models.TransactionSummaryBundleBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransactionsOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean isSlidingUp = false,disable = false;
    private int prePos = -1;
    private int itemCount,lastItemPosition, offest,number;
    public TransactionsOnScrollListener(){
        this.offest = 0;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LogUtil.d(this.getClass(),""+disable);
        if (disable){
            return;
        }
        isSlidingUp = dy>0;
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        itemCount = manager.getItemCount();
        lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
        if (isSlidingUp && lastItemPosition!=prePos){

            prePos = lastItemPosition;
            if (lastItemPosition == itemCount-5){
                LogUtil.d(this.getClass(),"开始从"+ offest +"预加载");
                Observable.just(offest)
                        .subscribeOn(Schedulers.io())
                        .map(i->BethApplication.getDBData()
                                .getTransactionSummaryDAO()
                                .getTransactionByNumber(number, offest))
                        .subscribe(b->handleSucceed(b));
            }
        }
    }

    private void handleSucceed(TransactionSummaryBean[] beans){
        if (beans.length!=0){
            EventBus.getDefault().post(beans);
        }
    }

    public void setOffset(int offest){
        this.offest = offest;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void isDisable(boolean b){
        disable = b;
    }
}
