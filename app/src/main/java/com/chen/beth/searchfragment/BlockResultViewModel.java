package com.chen.beth.searchfragment;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.LoadingState;

public class BlockResultViewModel extends ViewModel {
    public MutableLiveData<LoadingState> state = new MutableLiveData<>();
    public MutableLiveData<LoadingState> stateTxs = new MutableLiveData<>();
    public MutableLiveData<BlockSummaryBean> raw = new MutableLiveData<>();
    public MutableLiveData<String>  number = new MutableLiveData<>();
    public MutableLiveData<String>  time = new MutableLiveData<>();
    public MutableLiveData<String>  txs = new MutableLiveData<>();
    public MutableLiveData<String>  miner = new MutableLiveData<>();
    public MutableLiveData<String>  reward = new MutableLiveData<>();
    public MutableLiveData<String>  uncleReward = new MutableLiveData<>();
    public MutableLiveData<String>  difficult = new MutableLiveData<>();
    public MutableLiveData<String>  totalDifficult = new MutableLiveData<>();
    public MutableLiveData<String>  size = new MutableLiveData<>();
    public MutableLiveData<String>  gasUsed = new MutableLiveData<>();
    public MutableLiveData<String>  gasLimit = new MutableLiveData<>();
    public MutableLiveData<String>  extra = new MutableLiveData<>();
    public MutableLiveData<String>  hash = new MutableLiveData<>();

    public BlockResultViewModel() {
        state.setValue(LoadingState.LODING);
        stateTxs.setValue(LoadingState.LODING);
    }
}
