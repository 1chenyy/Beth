package com.chen.beth;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.chen.beth.Utils.BaseUtil;

import java.util.ArrayList;

public class MainTopViewModel extends ViewModel {
    public MutableLiveData<String> ethPrice  = new MutableLiveData<>();
    public MutableLiveData<String> ethMarketCap  = new MutableLiveData<>();
    public MutableLiveData<String> ethLatestBlock = new MutableLiveData<>();
    public MutableLiveData<String> ethDifficulty = new MutableLiveData<>();
    public MutableLiveData<String[]> titleString = new MutableLiveData<>();
    public MutableLiveData<Drawable[]> titleSrc = new MutableLiveData<>();

    public MainTopViewModel(Context context){
        String loading = context.getString(R.string.main_top_loading);
        ethPrice.setValue(loading);
        ethMarketCap.setValue(loading);
        ethLatestBlock.setValue(loading);
        ethDifficulty.setValue(loading);
        titleString.setValue(new String[]{
                BaseUtil.getString(R.string.main_top_eth_price_title),
                BaseUtil.getString(R.string.main_top_eth_market_title),
                BaseUtil.getString(R.string.main_top_latest_block_title),
                BaseUtil.getString(R.string.main_top_difficulty_title)});
        titleSrc.setValue(new Drawable[]{
                BaseUtil.getDrawable(R.drawable.ic_ethereum_price),
                BaseUtil.getDrawable(R.drawable.ic_ethereum_market_value),
                BaseUtil.getDrawable(R.drawable.ic_eth_latest),
                BaseUtil.getDrawable(R.drawable.ic_eth_difficulty),
        });
    }

    public static class Factory implements ViewModelProvider.Factory{
        private Context context;

        public Factory(Context context){
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainTopViewModel(this.context);
        }
    }
}
