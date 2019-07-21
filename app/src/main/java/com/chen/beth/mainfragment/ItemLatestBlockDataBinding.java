package com.chen.beth.mainfragment;

import androidx.databinding.ObservableField;

public class ItemLatestBlockDataBinding {
    public String blockNum ;
    public String blockMiner ;
    public String blockTxsAndDate ;
    public String blockReward ;
    public int number ;

    public ItemLatestBlockDataBinding(String num, String miner, String txsAndDate, String reward,int number){
        blockNum = num;
        blockMiner = miner;
        blockTxsAndDate = txsAndDate;
        blockReward = reward;
        this.number = number;
    }
}
