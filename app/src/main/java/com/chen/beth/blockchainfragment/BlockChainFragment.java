package com.chen.beth.blockchainfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Worker.BlockChainAndPriceTask;
import com.chen.beth.blockdetailsfragment.BlockDetails;
import com.chen.beth.databinding.FragmentBlockChainBinding;
import com.chen.beth.models.BlockChainFragmentBlockBundleBean;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.MinerMark;
import com.chen.beth.ui.ColorfulLoading;
import com.chen.beth.ui.IPreLoad;
import com.chen.beth.ui.ItemOffsetDecoration;
import com.chen.beth.ui.OnScrollListener;
import com.chen.beth.ui.RVItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class BlockChainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RVItemClickListener, IPreLoad {


    private FragmentBlockChainBinding binding;
    private ArrayList<BlockSummaryBean> temp;
    private BlockChainAdapter adapter;
    private OnScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rv;
    private int high = 0,low = 0;

    public static BlockChainFragment newInstance() {
        return new BlockChainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBlockChainBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configRecycleView();
        configSwipeRefresh();
        BlockChainAndPriceTask.queryBlocksFromDB(BethApplication.getContext(),0);
    }

    private void configSwipeRefresh() {
        swipeRefreshLayout = binding.swipeFresh;
        swipeRefreshLayout.setColorSchemeColors(ColorfulLoading.COLORS);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void configRecycleView() {
        rv = binding.rvBlockChain;
        rv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new ScaleInTopAnimator());
        scrollListener = new OnScrollListener(5);
        scrollListener.setOnPreLoad(this);
        rv.addOnScrollListener(scrollListener);
        rv.addItemDecoration(new ItemOffsetDecoration());
        temp = new ArrayList<>();
        adapter = new BlockChainAdapter(this);
        adapter.setListener(this);
        rv.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBlocksEvent(BlockChainFragmentBlockBundleBean event){
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
        scrollListener.setEnable(true);
        if (event.status == Const.RESULT_SUCCESS){
            if (event.result.blocks.size()>0){
                int max = event.result.blocks.get(0).number;
                int min = event.result.blocks.get(event.result.blocks.size()-1).number;
                if (max > high && high!=0){
                    LogUtil.d(this.getClass(),"更新最高区块高度"+min);
                    high = max;
                    adapter.addFrontItems(event.result.blocks);
                    rv.scrollToPosition(0);
                }else{
                    adapter.addTailItems(event.result.blocks);
                    LogUtil.d(this.getClass(),"更新最低区块高度"+min);
                    low = min;
                }

                if (high == 0){
                    high = max;
                    LogUtil.d(this.getClass(),"更新最高区块高度"+high);
                    low = min;
                }
            }else{
                adapter.setLoadingState(false);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMinerMarkEvent(MinerMark event){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        BlockChainAndPriceTask.queryLatestBlocksFromNet(BethApplication.getContext(),high);
    }


    @Override
    public void onItemClick(int position) {
        BlockDetails.showBlockDetails(getContext(),adapter.getBlockByPosition(position));
    }

    @Override
    public void onPreLoad() {
        BlockChainAndPriceTask.queryBlocksFromDB(BethApplication.getContext(),low-1);
    }
}
