package com.chen.beth.viewModel;

import androidx.databinding.ObservableField;

public class ItemLatestBlockDataBinding {
    public ObservableField<String> blockNum = new ObservableField<>();
    public ObservableField<String> blockMiner = new ObservableField<>();
    public ObservableField<String> blockTxsAndDate = new ObservableField<>();
    public ObservableField<String> blockReward = new ObservableField<>();

    public ItemLatestBlockDataBinding(String num, String miner, String txsAndDate, String reward){
        blockNum.set(num);
        blockMiner.set(miner);
        blockTxsAndDate.set(txsAndDate);
        blockReward.set(reward);
    }
}
