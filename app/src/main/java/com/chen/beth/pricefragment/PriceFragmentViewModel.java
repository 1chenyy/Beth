package com.chen.beth.pricefragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chen.beth.models.LoadingState;

import java.util.ArrayList;
import java.util.List;

public class PriceFragmentViewModel extends ViewModel {
    public MutableLiveData<List<Float>> chartData = new MutableLiveData<>();
    public MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();

    public PriceFragmentViewModel(){
        List<Float> list = new ArrayList<>();
        for (int i = 0;i<15;i++)
            list.add(0f);
        chartData.setValue(list);
        loadingState.setValue(LoadingState.LODING);
    }
}
