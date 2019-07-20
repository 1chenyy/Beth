package com.chen.beth.blockdetailsfragment;

import android.text.SpannableString;

import androidx.databinding.ObservableField;

public class BlockDetailsDataBinding {
    public ObservableField<String> number = new ObservableField<>();
    public ObservableField<String> time = new ObservableField<>();
    public ObservableField<String> txs = new ObservableField<>();
    public ObservableField<String> miner = new ObservableField<>();

    public BlockDetailsDataBinding(String num,String time,String txs,String miner){
        this.number.set(num);
        this.time.set(time);
        this.txs.set(txs);
        this.miner.set(miner);
    }
}
