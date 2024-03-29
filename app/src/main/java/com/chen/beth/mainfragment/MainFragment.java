package com.chen.beth.mainfragment;


import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.Worker.QueryBlocksTask;
import com.chen.beth.Worker.QueryHistoryTransactions;
import com.chen.beth.blockdetailsfragment.BlockDetails;
import com.chen.beth.databinding.FragmentMainBinding;
import com.chen.beth.models.MainFragmentBlockBundleBean;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.MinerMark;
import com.chen.beth.models.PriceAndMktcapBean;
import com.chen.beth.ui.RVItemClickListener;
import com.chen.beth.ui.TopViewFactory;
import com.chen.beth.ui.TransactionAndPriceMarker;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class MainFragment extends BaseFragment implements RVItemClickListener {
    private FragmentMainBinding binding;
    private MainTopViewModel viewModel;
    private ArrayList<BlockSummaryBean> temp;
    private LatestBlockAdapter adapter;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater,container,false);
        viewModel = ViewModelProviders.
                of(this,new MainTopViewModel.Factory(BethApplication.getContext())).get(MainTopViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(viewModel);
        binding.setHandler(this);
        binding.mainTop.tvLatestContent.setFactory(new TopViewFactory(getContext()));
        binding.mainTop.tvPriceContent.setFactory(new TopViewFactory(getContext()));
        binding.mainTop.tvDifficultyContent.setFactory(new TopViewFactory(getContext()));
        binding.mainTop.tvMktcapContent.setFactory(new TopViewFactory(getContext()));
        configChart();
        configRecycleView();
        LogUtil.d(this.getClass(),"onCreateView");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d(this.getClass(),"onViewCreated");
        QueryBlocksTask.startActionQueryLatest15Blocks(BethApplication.getContext());
        queryHistoryTransactions();
    }

    private void queryHistoryTransactions() {
        MMKV mmkv = MMKV.defaultMMKV();
        String now = BaseUtil.getTodayString();
        String vaule = mmkv.decodeString(Const.KEY_HISTORY_TRANSACTION_VALUE,"");
        if (now.equals(mmkv.decodeString(Const.KEY_HISTORY_TRANSACTION_DATE,""))
            && !TextUtils.isEmpty(vaule)){
            LogUtil.d(this.getClass(),"从缓存加载交易历史");
            List<Integer> number = BaseUtil.StringToIntList(vaule);
            viewModel.txHistory.setValue(number);
        }else{
            LogUtil.d(this.getClass(),"从网络加载交易历史");
            QueryHistoryTransactions.startQuery();
        }
    }

    private void configRecycleView() {
        RecyclerView rv = binding.rvLatestBlocks;
        rv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new ScaleInTopAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        temp = new ArrayList<>();
        adapter = new LatestBlockAdapter(this);
        adapter.setListener(this);
        rv.setAdapter(adapter);

    }

    private void configChart() {
        LineChart chart = binding.chart;
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        YAxis y = chart.getAxisLeft();
        y.setTextColor(Color.BLACK);
        y.setDrawGridLines(false);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setAxisLineColor(Color.TRANSPARENT);

        XAxis x = chart.getXAxis();
        x.setTextColor(Color.BLACK);
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setAxisLineColor(Color.TRANSPARENT);
        x.setGranularity(1f);

        TransactionAndPriceMarker marker = new TransactionAndPriceMarker(getContext(),
                R.layout.chart_marker_layout,
                TransactionAndPriceMarker.TYPE_TRANSACTION);
        marker.setChartView(chart);
        chart.setMarker(marker);
        chart.animateX(1000);
        //chart.invalidate();
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onPriceEvent(PriceAndMktcapBean bean){
        String price = "",mktcap = "";
        switch (bean.status){
            case Const.RESULT_SUCCESS:
                price = "$ "+bean.result.price;
                mktcap = "$ "+bean.result.mktcap;
                break;
            case Const.RESULT_NO_DATA:
                price = getString(R.string.main_top_no_data);
                mktcap = getString(R.string.main_top_no_data);
                break;
            case Const.RESULT_NO_NET:
                price = getString(R.string.main_top_no_network);
                mktcap = getString(R.string.main_top_no_network);
                break;
        }
        viewModel.ethPrice.setValue(price);
        viewModel.ethMarketCap.setValue(mktcap);

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onLatestBlockNumberEvent(LatestBlockBean bean){
        String latest = "",difficult = "";
        switch (bean.status){
            case Const.RESULT_SUCCESS:
                if (adapter.getCurrent()!=0 && bean.result.number>adapter.getCurrent()){
                    if (bean.result.number - adapter.getCurrent() > 50){
                        QueryBlocksTask.startActionQueryLatestBlocks(BethApplication.getContext(),bean.result.number-50);
                    }else{
                        QueryBlocksTask.startActionQueryLatestBlocks(BethApplication.getContext(),adapter.getCurrent());
                    }
                }

                latest = bean.result.number+"";
                difficult = bean.result.total_difficult;
                break;
            case Const.RESULT_NO_DATA:
                latest = getString(R.string.main_top_no_data);
                difficult = getString(R.string.main_top_no_data);
                break;
            case Const.RESULT_NO_NET:
                latest = getString(R.string.main_top_no_network);
                difficult = getString(R.string.main_top_no_network);
                break;
        }
        viewModel.ethLatestBlock.setValue(latest);
        viewModel.ethDifficulty.setValue(difficult);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLatest15BlocksEvent(MainFragmentBlockBundleBean event){
        switch (event.status){
            case Const.RESULT_SUCCESS:
                temp.clear();
                if (adapter.getCurrent() == 0){
                    viewModel.loadingState.setValue(LoadingState.LOADING_SUCCEED);
                    for (BlockSummaryBean bean : event.result.blocks){
                        temp.add(bean);
                    }
                    adapter.setInitList(temp);
                    adapter.setCurrent(event.result.blocks.get(0).number);
                }else{
                    int tempHight = 0;
                    for (BlockSummaryBean bean : event.result.blocks){
                        if (bean.number > adapter.getCurrent()){
                            if (tempHight == 0)
                                tempHight = bean.number;
                            temp.add(bean);
                        }else
                            break;

                    }
                    if (tempHight!=0){
                        adapter.addItems(temp);
                        adapter.setCurrent(tempHight);
                    }

                }
                break;
            case Const.RESULT_NO_DATA:
            case Const.RESULT_NO_NET:
                if (adapter.getCurrent() == 0){
                    viewModel.loadingState.setValue(LoadingState.LOADING_FAILED);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTransactionHistoryEvent(HistoryTransactionBean bean){
        switch (bean.status){
            case Const.RESULT_SUCCESS:
                viewModel.txHistory.setValue(bean.result.number);
                MMKV mmkv = MMKV.defaultMMKV();
                mmkv.encode(Const.KEY_HISTORY_TRANSACTION_DATE, BaseUtil.getTodayString());
                mmkv.encode(Const.KEY_HISTORY_TRANSACTION_VALUE,BaseUtil.IntListToString(bean.result.number));
                break;
            default:
                LogUtil.d(this.getClass(),"暂无数据"+bean.status);
                break;
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMinerMarkEvent(MinerMark event){
        adapter.notifyDataSetChanged();
    }

    public void onClickTopView(View view){
        FragmentNavigator.Extras extras;
        switch (view.getId()){
            case R.id.rl_price:
                extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(view.findViewById(R.id.iv_price),getString(R.string.shared_element_price)).build();
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_priceFragment,
                        null,null,extras);
                break;
            case R.id.rl_mktcap:
                extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(view.findViewById(R.id.iv_mktcap),getString(R.string.shared_element_mktcap)).build();
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_mktcapFragment,
                        null,null,extras);
                break;
            case R.id.rl_latest:
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_blockChainFragment);
                break;
        }
    }

    public void onClickRefresh(View view){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(800);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(rotateAnimation);
        view.setEnabled(false);
    }

    public void onClickMainBT(View view,LoadingState state){
        if (state == LoadingState.LOADING_FAILED){
            viewModel.loadingState.setValue(LoadingState.LODING);
            QueryBlocksTask.startActionQueryLatest15Blocks(BethApplication.getContext());
        }else if (state == LoadingState.LOADING_SUCCEED){
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_blockChainFragment);
        }
    }



    @Override
    public void onItemClick(int position) {
        LogUtil.d(this.getClass(),position+"");
        BlockDetails.showBlockDetails(getContext(),adapter.getList().get(position));
    }
}
