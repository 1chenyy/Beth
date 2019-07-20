package com.chen.beth.blockdetailsfragment;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.databinding.DialogBlockDetailsBinding;
import com.chen.beth.models.BlockSummaryBean;

public class BlockDetails {

    public static void showBlockDetails(Context context, BlockSummaryBean bean){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogBlockDetailsBinding binding = DialogBlockDetailsBinding.inflate(LayoutInflater.from(context));
        configData(binding,bean);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton("ok",null);
        builder.create().show();
    }

    private static void configData(DialogBlockDetailsBinding binding, BlockSummaryBean bean) {
        binding.tvNumber.setTypeface(BaseUtil.BLOCK_NUMBET_TYPEFACE);
        BlockDetailsDataBinding data = new BlockDetailsDataBinding(
                bean.number+"",
                BaseUtil.timestampToString(bean.time),
                bean.txs+"",
                bean.miner);
        binding.tvMinerContent.setSelected(true);

        binding.setData(data);
    }
}
