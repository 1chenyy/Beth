package com.chen.beth.ui;

import android.view.View;

import com.chen.beth.models.BlockSummaryBean;

public interface RVItemClickListener {
    public void onItemClick(int pos);
    default public void onItemClick(View view,int pos){

    };
}
