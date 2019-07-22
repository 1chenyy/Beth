package com.chen.beth.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.chen.beth.models.SearchHistory;

import java.util.List;

@Dao
public interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistories(List<SearchHistory> histories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistories(SearchHistory... histories);

    @Query("select * from search_history")
    SearchHistory[] getAll();

    @Query("select * from search_history limit 10 offset :offset")
    SearchHistory[] getOnePage(int offset);

    @Query("delete from search_history")
    void deleteAll();

    @Query("select count(*) from search_history")
    int getCount();

    @Query("select * from search_history where type=:type and content=:content")
    SearchHistory[] querySameHistory(int type,String content);
}
