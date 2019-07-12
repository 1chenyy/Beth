package com.chen.beth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class MainTopViewModel extends ViewModel {
    public MutableLiveData<String> ethPrice  = new MutableLiveData<>();
    public MutableLiveData<String> ethMarketCap  = new MutableLiveData<>();
    public MutableLiveData<String> ethLatestBlock = new MutableLiveData<>();
    public MutableLiveData<String> ethDifficulty = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowPriceRefresh = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowMarketCapRefresh = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowLatestBlockRefresh = new MutableLiveData<>();
    public MutableLiveData<Boolean> isShowDifficultyRefresh = new MutableLiveData<>();

    public MainTopViewModel(Context context){
        String loading = context.getString(R.string.main_top_loading);
        ethPrice.setValue(loading);
        ethMarketCap.setValue(loading);
        ethLatestBlock.setValue(loading);
        ethDifficulty.setValue(loading);
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
