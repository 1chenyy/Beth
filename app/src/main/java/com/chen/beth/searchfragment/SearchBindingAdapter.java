package com.chen.beth.searchfragment;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.mktcapfragment.MktcapFragmentBindAdapter;
import com.chen.beth.models.LoadingState;

public class SearchBindingAdapter {
    @BindingAdapter("isShowSearchBT")
    public static void isShowSearchBT(Button bt, LoadingState state){
        switch (state){
            case LODING:
            case LOADING_SUCCEED:
                bt.setVisibility(View.GONE);
                break;
            case LOADING_FAILED:
                bt.setVisibility(View.VISIBLE);
                bt.setText(R.string.main_bt_no_date_refresh);
                break;
            case LOADING_NO_DATA:
                bt.setVisibility(View.VISIBLE);
                bt.setEnabled(false);
                bt.setText(R.string.bt_search_no_data);
                break;

        }
    }

    @BindingAdapter("showHistoryType")
    public static void showHistoryType(TextView tv,int type){
        switch (type){
            case Const.TYPE_TX:
                tv.setTextColor(Color.parseColor("#9999FF"));
                tv.setText(R.string.history_tx);
                break;
            case Const.TYPE_ACCOUNT:
                tv.setTextColor(Color.parseColor("#66CCFF"));
                tv.setText(R.string.history_account);
                break;
            case Const.TYPE_BLOCK:
                tv.setTextColor(Color.parseColor("#0099CC"));
                tv.setText(R.string.history_block);
                break;
        }
    }

    @BindingAdapter("showHistoryContent")
    public static void showHistoryContent(TextView tv,SearchHistoryDataBinding data){
        switch (data.type){
            case Const.TYPE_TX:
                tv.setText(BaseUtil.omitHashString(data.content,8));
                break;
            case Const.TYPE_ACCOUNT:
                tv.setText(data.content);
                break;
            case Const.TYPE_BLOCK:
                tv.setText(BaseUtil.omitMinerString(data.content,8));
                break;
        }
    }
}
