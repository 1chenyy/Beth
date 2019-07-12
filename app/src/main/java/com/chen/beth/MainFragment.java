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
        binding.setMainTopViewModel(viewModel);
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
        if (bean.status == 0){
            viewModel.ethPrice.setValue(getString(R.string.main_top_no_data));
            viewModel.ethMarketCap.setValue(getString(R.string.main_top_no_data));
        }else{
            LogUtil.d(this.getClass(),"接收到最新价格与市值消息，准备更新："+bean.result.price+"  "+bean.result.mktcap);
            viewModel.ethPrice.setValue("$ "+bean.result.price);
            viewModel.ethMarketCap.setValue("$ "+bean.result.mktcap);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onLatestBlockEvent(LatestBlockBean bean){
        if (bean.status == 0){
            viewModel.ethLatestBlock.setValue(getString(R.string.main_top_no_data));
            viewModel.ethDifficulty.setValue(getString(R.string.main_top_no_data));
        }else{
            viewModel.ethLatestBlock.setValue(bean.result.number+"");
            viewModel.ethDifficulty.setValue(bean.result.total_difficult);
        }
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

    public void onClickTopView(View view,String s){
        switch (s){
            case TAG_PRICE:
                break;
            case TAG_MKTCAP:
                break;
            case TAG_LATEST:
                break;
            case TAG_DIFFICULTY:
                break;
        }
        BaseUtil.showToast(s);
    }


}
