package com.chen.beth.searchfragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chen.beth.models.LoadingState;

public class AccountResultViewModel extends ViewModel {
    public MutableLiveData<String> balance = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasContent = new MutableLiveData<>();
    public MutableLiveData<LoadingState> state = new MutableLiveData<>();

    public AccountResultViewModel(){
        state.setValue(LoadingState.LODING);
        hasContent.setValue(true);
    }
}
