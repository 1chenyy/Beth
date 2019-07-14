package com.chen.beth.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemLatestBlockViewModel extends ViewModel {
    public MutableLiveData<String> blockNum = new MutableLiveData<>();
    public MutableLiveData<String> blockMiner = new MutableLiveData<>();
    public MutableLiveData<String> blockTxsAndDate = new MutableLiveData<>();
}
