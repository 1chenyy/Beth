package com.chen.beth.mktcapfragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chen.beth.models.LoadingState;

import java.util.ArrayList;
import java.util.List;

public class MktcapViewModel extends ViewModel {
    public MutableLiveData<String> gensis = new MutableLiveData<>();
    public MutableLiveData<String> block = new MutableLiveData<>();
    public MutableLiveData<String> uncle = new MutableLiveData<>();
    public MutableLiveData<String> total = new MutableLiveData<>();
    public MutableLiveData<List<Double>> charData = new MutableLiveData<>();
    public MutableLiveData<LoadingState> loadingState = new MutableLiveData<>();
    public MktcapViewModel(){
        List<Double> list = new ArrayList<>();
        for (int i= 0;i<3;i++)
            list.add(0.0);
        charData.setValue(list);
        loadingState.setValue(LoadingState.LODING);
    }
}
