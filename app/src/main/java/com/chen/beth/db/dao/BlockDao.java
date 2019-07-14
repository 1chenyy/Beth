package com.chen.beth.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.chen.beth.models.BlockSummaryBean;

import java.util.List;

@Dao
public interface BlockDao {
    @Insert
    public void insertBlocks(List<BlockSummaryBean> blocks);

    @Insert
    public void insertBlock(BlockSummaryBean... blocks);

    @Query("select * from block_summary where :num = number")
    public BlockSummaryBean[] getBlockSummaryByNumber(int num);
}
