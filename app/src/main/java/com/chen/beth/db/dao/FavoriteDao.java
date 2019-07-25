package com.chen.beth.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.chen.beth.models.FavoriteBean;
import com.chen.beth.models.SearchHistory;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<long[]> addFavorite(FavoriteBean... histories);

    @Query("select * from favorite where type=:type and content=:content ")
    Maybe<List<FavoriteBean>> isFavorite(int type,String content);

    @Query("delete from favorite")
    int deleteAll();

    @Delete
    int removeFavorite(FavoriteBean history);

    @Query("delete from favorite where type=:type and content=:content")
    Maybe<Integer> removeFavorite(int type,String content);

    @Query("select * from favorite order by id desc limit 30 offset :offset")
    Maybe<List<FavoriteBean>> getFavorite(int offset);

    @Query("select * from favorite where type = 0 order by id desc limit 30 offset :offset")
    Maybe<List<FavoriteBean>> getTxsFavorite(int offset);

    @Query("select * from favorite where type = 1 order by id desc limit 30 offset :offset")
    Maybe<List<FavoriteBean>> getAccountFavorite(int offset);

    @Query("select * from favorite where type = 2 order by id desc limit 30 offset :offset")
    Maybe<List<FavoriteBean>> getBlockFavorite(int offset);
}
