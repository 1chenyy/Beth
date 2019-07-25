package com.chen.beth.blockdetailsfragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.databinding.DialogBlockDetailsBinding;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.MinerMark;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;

public class BlockDetails {
    private static BlockDetailsDataBinding data;
    public static void showBlockDetails(Context context, BlockSummaryBean bean){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogBlockDetailsBinding binding = DialogBlockDetailsBinding.inflate(LayoutInflater.from(context));
        configData(binding,bean,context);
        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        builder.setPositiveButton(BaseUtil.getString(R.string.bt_return),null);
        builder.create().show();
    }

    private static void configData(DialogBlockDetailsBinding binding, BlockSummaryBean bean, Context context) {
        binding.tvNumber.setTypeface(BaseUtil.BLOCK_NUMBET_TYPEFACE);
        binding.tvMinerContent.setOnClickListener(v->showHashSnackBar(v,bean.miner,context));
        binding.tvHashContent.setOnClickListener(v->showHashSnackBar(v,bean.hash,context));
        binding.ibMinerEdit.setOnClickListener(v->showMinerMarkDialog(context,bean.miner));
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String gasPercent = numberFormat.format((double)bean.gasused/(double)bean.gaslimit*100)+"%";
        data = new BlockDetailsDataBinding(
                bean.number+"",
                BaseUtil.timestampToString(bean.time),
                bean.txs+"",
                BaseUtil.omitMinerString(bean.miner,6),
                bean.reward+" Eth",
                Double.parseDouble(bean.unclereward)==0?"0 Eth":bean.unclereward+" Eth",
                bean.difficult,
                bean.totaldifficulty,
                bean.size + " "+BaseUtil.getString(R.string.info_byte),
                bean.gasused+" ("+gasPercent+")",
                bean.gaslimit+"",
                bean.extra,
                BaseUtil.omitHashString(bean.hash,8));
        binding.tvExtraContent.setSelected(true);
        binding.setData(data);
    }

    public static void showHashSnackBar(View v,String hash,Context context){
        Snackbar snackbar = Snackbar.make(v,hash,Snackbar.LENGTH_SHORT)
                .setAction(BaseUtil.getString(R.string.info_copy),
                        view->copy(context,hash));
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextSize(10);
        snackbar.show();

    }

    public static void copy(Context context,String s){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("hash",s));
        BaseUtil.showToast(BaseUtil.getString(R.string.info_copy_toast));
    }

    private static void showMinerMarkDialog(Context context,String miner){
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
                MMKV.defaultMMKV().encode(miner,s);
                //PreferenceUtil.putString(miner,s,PreferenceUtil.PREFERENCE_MINER_MARK);
                data.miner.set(s);
                EventBus.getDefault().post(new MinerMark());
            }
        });
        builder.setNegativeButton(BaseUtil.getString(R.string.bt_remove),(d,w)->{
            MMKV.defaultMMKV().removeValueForKey(miner);
            EventBus.getDefault().post(new MinerMark());
            data.miner.set(BaseUtil.omitMinerString(miner,6));
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(MMKV.defaultMMKV().contains(miner));

    }


}
