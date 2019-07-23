package com.chen.beth.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction_summary")
public class TransactionSummaryBean {

    /**
     * hash : 0xd390563c7c08d5aa7ed860d147a8ec9aca407f9c7f366d41fbbf140439a12304
     * from : 0x698103f9e5e97fd3f069cad52762363e9fd80a4b
     * to : 0x21ab6c9fac80c59d401b37cb43f81ea9dde7fe34
     * fee : 0.0022000
     * value : 0.0000000
     */
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int number;
    public String hash;
    public String from;
    public String to;
    public String fee;
    public String value;
}
