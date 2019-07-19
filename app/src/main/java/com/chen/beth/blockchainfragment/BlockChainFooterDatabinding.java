package com.chen.beth.blockchainfragment;

import androidx.databinding.ObservableField;

import com.chen.beth.models.LoadingState;

public class BlockChainFooterDatabinding {
    public ObservableField<Boolean> loadingState = new ObservableField<>();
    public BlockChainFooterDatabinding(){
        loadingState.set(true);
    }
}
