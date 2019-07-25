package com.chen.beth.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class FavoriteBean {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int type;
    public String content;

    public FavoriteBean(int type, String content) {
        this.type = type;
        this.content = content;
    }
}
