package com.chen.beth.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.chen.beth.db.dao.BlockDao;
import com.chen.beth.db.dao.SearchHistoryDao;
import com.chen.beth.db.dao.TransactionDao;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.SearchHistory;
import com.chen.beth.models.TransactionBean;

@Database(entities = {BlockSummaryBean.class, SearchHistory.class, TransactionBean.class},version = 1)
public abstract class DBData extends RoomDatabase {
    public abstract BlockDao getBlockDao();
    public abstract SearchHistoryDao getSearchHistoryDao();
    public abstract TransactionDao getTransactionDao();
}