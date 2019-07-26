package com.chen.beth.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.chen.beth.models.SearchHistory;
import com.chen.beth.models.TransactionBean;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistories(TransactionBean... transactionBeans);

    @Query("select * from transactions where hash=:hash")
    TransactionBean[] getTransactionByHash(String hash);

    @Query("delete from transactions")
    void deleteAll();
}
