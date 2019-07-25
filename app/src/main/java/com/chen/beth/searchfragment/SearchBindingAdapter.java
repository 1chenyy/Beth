package com.chen.beth.searchfragment;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;

import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.mktcapfragment.MktcapFragmentBindAdapter;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.MinerMark;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

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
                tv.setTextColor(Color.parseColor("#009933"));
                tv.setText(R.string.history_account);
                break;
            case Const.TYPE_BLOCK:
                tv.setTextColor(Color.parseColor("#0099CC"));
                tv.setText(R.string.history_block);
                break;
        }
    }

    @BindingAdapter("showHistoryContent")
    public static void showHistoryContent(TextView tv,String conent){
        tv.setText(BaseUtil.omitMinerString(conent,8));
    }

    @BindingAdapter("isShowMark")
    public static void isShowMark(View view,String hash){
        if (TextUtils.isEmpty(hash)){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("setBalance")
    public static void setBalance(TextView tv,String str){
        if (BaseUtil.getString(R.string.main_top_loading).equals(str)
                || BaseUtil.getString(R.string.main_bt_no_date_refresh).equals(str)){
            tv.setTypeface(null);
        }else{
            tv.setTypeface(BaseUtil.ACCOUNT_BALANCE_TYPEFACE);
        }
        tv.setText(str);
        if (BaseUtil.getString(R.string.main_bt_no_date_refresh).equals(str)){
            tv.setTextColor(BethApplication.getContext().getColor(R.color.colorPrimary));
            tv.setEnabled(true);
        }else{
            tv.setTextColor(Color.BLACK);
            tv.setEnabled(false);
        }
    }



    public static void showMarkDialog(Context context,int id, String hash) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_miner_mark_layout,null);
        TextInputLayout textInputLayout = view.findViewById(R.id.edit_layout);
        textInputLayout.setCounterMaxLength(20);
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setHint(BaseUtil.getString(R.string.miner_mark_hint));
        TextInputEditText editText = view.findViewById(R.id.et_miner);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>textInputLayout.getCounterMaxLength()){
                    textInputLayout.setError(BaseUtil.getString(R.string.edit_warn));
                }
            }
        });
        builder.setTitle(R.string.dialog_title_miner_mark);
        builder.setView(view);
        builder.setPositiveButton(BaseUtil.getString(R.string.bt_mark), (d, w)->{
            String s = editText.getText().toString().trim();
            if (TextUtils.isEmpty(s)){
                BaseUtil.showToast(BaseUtil.getString(R.string.info_no_input));
            }else{
                MMKV.defaultMMKV().encode(hash,s);
                EventBus.getDefault().post(new MinerMark(id,hash));
            }
        });
        builder.setNegativeButton(BaseUtil.getString(R.string.bt_remove),(d,w)->{
            MMKV.defaultMMKV().removeValueForKey(hash);
            EventBus.getDefault().post(new MinerMark(id,hash));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(MMKV.defaultMMKV().contains(hash));
    }
}
