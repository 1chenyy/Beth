package com.chen.beth;


import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.beth.adapter.LatestBlockAdapter;
import com.chen.beth.ui.TransactionAndPriceMarker;
import com.chen.beth.utils.BaseUtil;
import com.chen.beth.utils.Const;
import com.chen.beth.utils.LogUtil;
import com.chen.beth.broadcast.RefreshReceiver;
import com.chen.beth.databinding.FragmentMainBinding;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;
import com.chen.beth.viewModel.ItemLatestBlockViewModel;
import com.chen.beth.viewModel.MainTopViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MainTopViewModel viewModel;
    private RefreshReceiver refreshReceiver;
    private ArrayList<ItemLatestBlockViewModel> list;
    private LatestBlockAdapter adapter;

    public static final String TAG_PRICE = "price";
    public static final String TAG_MKTCAP = "mktcap";
    public static final String TAG_LATEST = "latest";
    public static final String TAG_DIFFICULTY = "difficulty";

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
        configChart();
        configRecycleView();
        return binding.getRoot();
    }

    private void configRecycleView() {
        RecyclerView rv = binding.rvLatestBlocks;
        rv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        list = new ArrayList<>();
        adapter = new LatestBlockAdapter(list);
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

        TransactionAndPriceMarker marker = new TransactionAndPriceMarker(getContext(),R.layout.chart_marker_layout);
        marker.setChartView(chart);
        chart.setMarker(marker);
        chart.animateX(1000);
        chart.invalidate();
    }


    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onPriceEvent(PriceAndMktcapBean bean){
        String price = "",mktcap = "";
        switch (bean.status){
            case Const.RESULT_NORMAL:
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
    public void onLatestBlockEvent(LatestBlockBean bean){
        String latest = "",difficult = "";
        switch (bean.status){
            case Const.RESULT_NORMAL:
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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onTransactionHistoryEvent(HistoryTransactionBean bean){
        LogUtil.d(this.getClass(),BaseUtil.ListToString(bean.result.number));
        viewModel.txHistory.setValue(bean.result.number);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        refreshReceiver = new RefreshReceiver();
        LocalBroadcastManager.getInstance(BethApplication.getContext()).
                registerReceiver(refreshReceiver,new IntentFilter(BaseUtil.getString(R.string.action_mtv_refresh)));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(BethApplication.getContext()).
                unregisterReceiver(refreshReceiver);

    }

    public void onClickTopView(View view){

        BaseUtil.showToast("aaa");
    }

    public void onClickRefresh(View view){

        BaseUtil.showToast("bbb");
    }
}
