package com.chen.beth;


import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.broadcast.RefreshReceiver;
import com.chen.beth.databinding.FragmentMainBinding;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private FragmentMainBinding binding;
    private MainTopViewModel viewModel;
    private RefreshReceiver refreshReceiver;

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

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChart();
    }

    private void setChart() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0,900));
        entries.add(new Entry(1,800));
        entries.add(new Entry(2,1123));
        entries.add(new Entry(3,650));
        entries.add(new Entry(4,950));
        entries.add(new Entry(5,1320));
        entries.add(new Entry(6,1000));
        entries.add(new Entry(7,856));
        entries.add(new Entry(8,745));
        entries.add(new Entry(9,513));
        entries.add(new Entry(10,635));
        entries.add(new Entry(11,985));
        entries.add(new Entry(12,1341));
        entries.add(new Entry(13,1110));
        entries.add(new Entry(14,1023));
        entries.add(new Entry(15,1560));
        LineDataSet dataSet = new LineDataSet(entries,"label");
        dataSet.setColor(Color.BLACK);
        LineData data = new LineData(dataSet);
        binding.chart.setData(data);
        binding.chart.invalidate();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
