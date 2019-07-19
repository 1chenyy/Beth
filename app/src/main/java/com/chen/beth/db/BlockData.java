package com.chen.beth.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.chen.beth.db.dao.BlockDao;
import com.chen.beth.models.BlockSummaryBean;

@Database(entities = {BlockSummaryBean.class},version = 1)
public abstract class BlockData extends RoomDatabase {
    public abstract BlockDao getBlockDao();
}
