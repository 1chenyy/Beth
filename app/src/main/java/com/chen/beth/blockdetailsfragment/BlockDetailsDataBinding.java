package com.chen.beth.blockdetailsfragment;

import android.text.SpannableString;

import androidx.databinding.ObservableField;

public class BlockDetailsDataBinding {
    public String number ;
    public String time ;
    public String txs ;
    public ObservableField<String> miner = new ObservableField<>();
    public String reward ;
    public String uncleReward ;
    public String difficult ;
    public String totalDifficult ;
    public String size ;
    public String gasUsed ;
    public String gasLimit ;
    public String extra ;
    public String hash;

    public BlockDetailsDataBinding(String num,String time,String txs,String miner,
                                   String reward,String uncleReward,String difficult,
                                   String totalDifficult,String size,String gasUsed,
                                   String gasLimit,String extra,String hash){
        this.number = num;
        this.time = time;
        this.txs = txs;
        this.miner.set(miner);
        this.reward = reward;
        this.uncleReward = uncleReward;
        this.difficult = difficult;
        this.totalDifficult = totalDifficult;
        this.size = size;
        this.gasUsed = gasUsed;
        this.gasLimit = gasLimit;
        this.extra = extra;
        this.hash = hash;
    }
}
