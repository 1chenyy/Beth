package com.chen.beth.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chen.beth.MainFragment;
import com.chen.beth.R;
import com.chen.beth.utils.BaseUtil;

public class RefreshReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(BaseUtil.getString(R.string.extra_type));
        switch (type){
            case MainFragment.TAG_PRICE:
                break;
            case MainFragment.TAG_MKTCAP:
                break;
            case MainFragment.TAG_LATEST:
                break;
            case MainFragment.TAG_DIFFICULTY:
                break;
        }
        BaseUtil.showToast(type);
    }
}
