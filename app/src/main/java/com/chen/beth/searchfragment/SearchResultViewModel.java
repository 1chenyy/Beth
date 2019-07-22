package com.chen.beth.searchfragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.models.HashEditable;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.TransactionBean;
import com.chen.beth.models.TransactionDetailBean;

public class SearchResultViewModel extends ViewModel  {
    public MutableLiveData<LoadingState> state = new MutableLiveData<>();
    public MutableLiveData<TransactionBean> raw = new MutableLiveData<TransactionBean>();
    public MutableLiveData<String> hash = new MutableLiveData();
    public MutableLiveData<String> block = new MutableLiveData();
    public MutableLiveData<String> time = new MutableLiveData();
    public MutableLiveData<String> from = new MutableLiveData();
    public MutableLiveData<String> to = new MutableLiveData();
    public MutableLiveData<String> value = new MutableLiveData();
    public MutableLiveData<String> gas = new MutableLiveData();
    public MutableLiveData<String> gasPrice = new MutableLiveData();
    public MutableLiveData<String> fee = new MutableLiveData();
    public MutableLiveData<String> nonce = new MutableLiveData();
    public MutableLiveData<String> input = new MutableLiveData();
    public MutableLiveData<String> r = new MutableLiveData();
    public MutableLiveData<String> s = new MutableLiveData();
    public MutableLiveData<String> v = new MutableLiveData();

    public SearchResultViewModel(){
        state.setValue(LoadingState.LODING);
    }


}
