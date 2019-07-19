package com.chen.beth.blockchainfragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.BethApplication;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Worker.BlockChainAndPriceTask;

public class BlockChainOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean isSlidingUp = false;
    private int prePos = -1;
    private int itemCount,lastItemPosition,current;
    public BlockChainOnScrollListener(int current){
        this.current = current-1;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        isSlidingUp = dy>0;
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        itemCount = manager.getItemCount();
        lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
        if (isSlidingUp && lastItemPosition!=prePos){
            prePos = lastItemPosition;
            if (lastItemPosition == itemCount-6){
                LogUtil.d(this.getClass(),"开始从"+current+"预加载");
                BlockChainAndPriceTask.queryBlocksFromDB(BethApplication.getContext(),current);
            }
        }
    }

    public void setCurrent(int current){
        this.current = current-1;
    }
}
