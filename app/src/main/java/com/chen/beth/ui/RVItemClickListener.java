package com.chen.beth.ui;

import android.view.View;

import com.chen.beth.models.BlockSummaryBean;

public interface RVItemClickListener {
    void onItemClick(int pos);
    default void onItemClick(View view,int pos){

    };

    default void onDeleteClick(int pos){

    }
}
