package com.chen.beth.blockdetailsfragment;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.room.Database;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.databinding.DialogBlockDetailsBinding;
import com.chen.beth.models.BlockSummaryBean;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;

public class BlockDetails {
    private static BlockDetailsDataBinding data;
    public static void showBlockDetails(Context context, BlockSummaryBean bean){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogBlockDetailsBinding binding = DialogBlockDetailsBinding.inflate(LayoutInflater.from(context));
        configData(binding,bean,context);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton("ok",null);
        builder.create().show();
    }

    private static void configData(DialogBlockDetailsBinding binding, BlockSummaryBean bean, Context context) {
        binding.tvNumber.setTypeface(BaseUtil.BLOCK_NUMBET_TYPEFACE);
        binding.tvMinerContent.setOnClickListener(v->showMinerSnackBar(v,bean.miner,context));
        data = new BlockDetailsDataBinding(
                bean.number+"",
                BaseUtil.timestampToString(bean.time),
                bean.txs+"",
                BaseUtil.omitMinerString(bean.miner,8));

        binding.setData(data);
    }

    private static void showMinerSnackBar(View v,String miner,Context context){
        Snackbar.make(v,miner,Snackbar.LENGTH_SHORT)
                .setAction(BaseUtil.getString(R.string.miner_mark),
                        view->showMinerMarkDialog(context,miner)).show();
    }

    private static void showMinerMarkDialog(Context context,String miner){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_miner_mark_layout,null);
        EditText editText = view.findViewById(R.id.et_miner);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", (d,w)->{
            String s = editText.getText().toString();
            if (TextUtils.isEmpty(s)){
                BaseUtil.showToast("没有任何输入，不做改变");
            }else{
                PreferenceUtil.putString(miner,s,PreferenceUtil.PREFERENCE_MINER_MARK);
                data.miner.set(s);
                EventBus.getDefault().post("");
            }
        });
        builder.create().show();
    }
}
