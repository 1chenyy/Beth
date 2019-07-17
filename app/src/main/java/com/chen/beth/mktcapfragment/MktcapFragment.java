package com.chen.beth.mktcapfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.Worker.ShortTask;
import com.chen.beth.databinding.FragmentMktcapBinding;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.MktcapDetailBean;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MktcapFragment extends BaseFragment {

    private MktcapViewModel mViewModel;
    private FragmentMktcapBinding binding;
    private MktcapViewModel viewModel;

    public static MktcapFragment newInstance() {
        return new MktcapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMktcapBinding.inflate(inflater,container,false);
        viewModel = ViewModelProviders.of(this).get(MktcapViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(viewModel);
        binding.setHandler(this);
        configChart();
        return binding.getRoot();
    }

    private void configChart() {
        PieChart chart = binding.chart;
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(false);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(false);
        chart.setDrawEntryLabels(false);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateXY(800,800);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Calendar.getInstance().getTimeInMillis() > 60*60*1000 + PreferenceUtil.getLong(Const.KEY_MKTCAP_DATE,0)){
            ShortTask.queryEthMktcap(BethApplication.getContext());
        }else if(!TextUtils.isEmpty(PreferenceUtil.getString(Const.KEY_MKTCAP_VALUE,""))){
            List<Double> list = BaseUtil.StringToDoubleList(PreferenceUtil.getString(Const.KEY_MKTCAP_VALUE,""));
            setupViewModel(list);
        }else{
            ShortTask.queryEthMktcap(BethApplication.getContext());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMktcapDetailEvent(MktcapDetailBean event){
        switch (event.status){
            case Const.RESULT_NORMAL:
                ArrayList<Double> result = event.result;
                setupViewModel(result);
                PreferenceUtil.getEdit().putLong(Const.KEY_MKTCAP_DATE, Calendar.getInstance().getTimeInMillis())
                        .putString(Const.KEY_MKTCAP_VALUE,BaseUtil.DoubleListToString(result))
                        .commit();
                break;
            case Const.RESULT_NO_NET:
            case Const.RESULT_NO_DATA:
                viewModel.loadingState.setValue(LoadingState.LOADING_FAILED);
                break;
        }
    }

    private void setupViewModel(List<Double> result) {
        viewModel.loadingState.setValue(LoadingState.LOADING_SUCCEED);
        viewModel.charData.setValue(result);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);

        viewModel.gensis.setValue(String.format("%s : %s %s",
                BaseUtil.getString(R.string.label_genesis),
                nf.format(result.get(0)),
                BaseUtil.getString(R.string.ether)));
        viewModel.block.setValue(String.format("%s : %s %s",
                BaseUtil.getString(R.string.label_block),
                nf.format(result.get(1)),
                BaseUtil.getString(R.string.ether)));
        viewModel.uncle.setValue(String.format("%s : %s %s",
                BaseUtil.getString(R.string.label_uncle),
                nf.format(result.get(2)),
                BaseUtil.getString(R.string.ether)));
        viewModel.total.setValue(String.format("%s : %s %s",
                BaseUtil.getString(R.string.label_total),
                nf.format(result.get(3)),
                BaseUtil.getString(R.string.ether)));
    }

    public void onRefreshClick(View view){
        viewModel.loadingState.setValue(LoadingState.LODING);
        ShortTask.queryEthMktcap(BethApplication.getContext());
    }
}
