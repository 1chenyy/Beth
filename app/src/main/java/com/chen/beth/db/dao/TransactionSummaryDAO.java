package com.chen.beth.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.chen.beth.models.TransactionBean;
import com.chen.beth.models.TransactionSummaryBean;

import java.util.List;

@Dao
public interface TransactionSummaryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransactions(List<TransactionSummaryBean> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransaction(TransactionSummaryBean... list);

    @Query("select * from transaction_summary where number = :number")
    TransactionSummaryBean[] getTransactionByNumber(int number);

    @Query("delete from transaction_summary")
    void deleteAll();
}
