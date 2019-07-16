package com.chen.beth.pricefragment;


import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.Worker.ShortTask;
import com.chen.beth.databinding.FragmentPriceBinding;
import com.chen.beth.models.HistoryPriceBean;
import com.chen.beth.models.LoadingState;
import com.chen.beth.ui.TransactionAndPriceMarker;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PriceFragment extends BaseFragment {
    private FragmentPriceBinding binding;
    private PriceFragmentViewModel viewModel;

    public PriceFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPriceBinding.inflate(inflater,container,false);
        viewModel = ViewModelProviders.of(this).get(PriceFragmentViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(viewModel);
        binding.setHandler(this);
        configChart();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryHistoryPrice();
    }

    private void queryHistoryPrice() {
        if (BaseUtil.getTodayString().equals(PreferenceUtil.getString(Const.KEY_HISTORY_PRICE_DATE,""))){
            List<Float> list = BaseUtil.StringToFloatList(PreferenceUtil.getString(Const.KEY_HISTORY_PRICE_VALUE,""));
            viewModel.loadingState.setValue(LoadingState.LOADING_SUCCEED);
            viewModel.chartData.setValue(list);
        }else{
            ShortTask.queryHistoryPrice(BethApplication.getContext());
        }
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
                TransactionAndPriceMarker.TYPE_PRICE);
        marker.setChartView(chart);
        chart.setMarker(marker);
        chart.animateX(1000);
        chart.invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPriceHistoryEvent(HistoryPriceBean event){
        switch (event.status){
            case Const.RESULT_NORMAL:
                List<Float> list = StringToFloatList(event.result.price);
                viewModel.loadingState.setValue(LoadingState.LOADING_SUCCEED);
                viewModel.chartData.setValue(list);
                PreferenceUtil.putString(Const.KEY_HISTORY_PRICE_DATE, BaseUtil.getTodayString());
                PreferenceUtil.putString(Const.KEY_HISTORY_PRICE_VALUE,BaseUtil.FloatListToString(list));
                break;
            case Const.RESULT_NO_DATA:
            case Const.RESULT_NO_NET:
                viewModel.loadingState.setValue(LoadingState.LOADING_FAILED);
                break;
        }
    }

    private List<Float> StringToFloatList(List<String> list){
        List<Float> result = new ArrayList<>();
        for (String s:list){
            result.add(Float.parseFloat(s));
        }
        return result;
    }

    public void onRefreshClick(View v){
        viewModel.loadingState.setValue(LoadingState.LODING);
        queryHistoryPrice();
    }
}
