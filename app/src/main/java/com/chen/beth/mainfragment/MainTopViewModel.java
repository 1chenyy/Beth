package com.chen.beth.mainfragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.chen.beth.R;
import com.chen.beth.models.LoadingState;

import java.util.ArrayList;
import java.util.List;

public class MainTopViewModel extends ViewModel {
    public MutableLiveData<String> ethPrice  = new MutableLiveData<>();
    public MutableLiveData<String> ethMarketCap  = new MutableLiveData<>();
    public MutableLiveData<String> ethLatestBlock = new MutableLiveData<>();
    public MutableLiveData<String> ethDifficulty = new MutableLiveData<>();
    public MutableLiveData<List<Integer>> txHistory = new MutableLiveData<>();
    public MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();

    public MainTopViewModel(Context context){
        String loading = context.getString(R.string.main_top_loading);
        ethPrice.setValue(loading);
        ethMarketCap.setValue(loading);
        ethLatestBlock.setValue(loading);
        ethDifficulty.setValue(loading);
        ArrayList<Integer> historyList = new ArrayList<>();
        for (int i = 0;i<15;i++)
            historyList.add(0);
        txHistory.setValue(historyList);
        loadingState.setValue(LoadingState.LODING);
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
