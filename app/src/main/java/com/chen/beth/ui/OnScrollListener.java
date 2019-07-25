package com.chen.beth.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.TransactionSummaryBean;

import org.greenrobot.eventbus.EventBus;

public class OnScrollListener extends RecyclerView.OnScrollListener {
    private boolean isSlidingUp = false, enable = false;
    private int prePos = -1;
    private int itemCount,lastItemPosition, preloadPos;
    public OnScrollListener(int preloadPos){
        this.preloadPos = preloadPos;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!enable){
            return;
        }
        isSlidingUp = dy>0;
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        itemCount = manager.getItemCount();
        lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
        if (isSlidingUp && lastItemPosition!=prePos){
            prePos = lastItemPosition;
            if (lastItemPosition == itemCount-preloadPos){
                LogUtil.d(this.getClass(),"开始预加载");
                enable = false;
                preLoad.onPreLoad();
            }
        }
    }

    private void handleSucceed(TransactionSummaryBean[] beans){
        if (beans.length!=0){
            EventBus.getDefault().post(beans);
        }
    }

    private IPreLoad preLoad;
    public void setOnPreLoad(IPreLoad preLoad){
        this.preLoad = preLoad;
    }

    public void setEnable(boolean b){
        enable = b;
    }

}
