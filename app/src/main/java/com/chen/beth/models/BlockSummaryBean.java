package com.chen.beth.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "block_summary")
public class BlockSummaryBean {
    @PrimaryKey()
    public int number;
    public String hash;
    public int time;
    public int txs;
    public String miner;
    public String reward;
    public String unclereward;
    public String difficult;
    public String totaldifficulty;
    public int size;
    public int gasused;
    public int gaslimit;
    public String extra;
}
