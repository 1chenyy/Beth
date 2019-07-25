package com.chen.beth.favoritefragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chen.beth.models.LoadingState;

public class FavoriteFragmentViewModel extends ViewModel {
    public MutableLiveData<Boolean> hasFavorite = new MutableLiveData<>();
    public MutableLiveData<LoadingState> state = new MutableLiveData<>();
    public FavoriteFragmentViewModel(){
        hasFavorite.setValue(false);
        state.setValue(LoadingState.LODING);
    }
}
