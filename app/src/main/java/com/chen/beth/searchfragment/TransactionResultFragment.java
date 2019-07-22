package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.ConvertUtil;
import com.chen.beth.Utils.PreferenceUtil;
import com.chen.beth.Worker.SearchTask;
import com.chen.beth.blockdetailsfragment.BlockDetails;
import com.chen.beth.databinding.FragmentTransactionResultBinding;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.MinerMark;
import com.chen.beth.models.OneBlockSummaryBean;
import com.chen.beth.models.TransactionDetailBean;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Entity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionResultFragment extends BaseFragment {
    private FragmentTransactionResultBinding binding;
    private SearchResultViewModel viewModel;
    private String arg;

    public TransactionResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        arg = bundle.getString(Const.ARG_ARG,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionResultBinding.inflate(inflater,container,false);
        viewModel = ViewModelProviders.of(this).get(SearchResultViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setData(viewModel);
        binding.setHandler(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchTask.startSearchTransaction(BethApplication.getContext(),arg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTransactionByHash(TransactionDetailBean event){
        if (event.result!=null){
            if (TextUtils.isEmpty(event.result.hash)){
                viewModel.state.setValue(LoadingState.LOADING_FAILED);
            }else{
                viewModel.raw.setValue(event.result);
                viewModel.state.setValue(LoadingState.LOADING_SUCCEED);
                viewModel.hash.setValue(BaseUtil.omitHashString(event.result.hash,8));
                viewModel.block.setValue(ConvertUtil.HexStringToLong(event.result.blockNumber)+"");
                if (TextUtils.isEmpty(event.result.time)){
                    SearchTask.startSearchBlockByNumber(BethApplication.getContext(), (int) ConvertUtil.HexStringToLong(event.result.blockNumber),false);
                    viewModel.time.setValue(BaseUtil.getString(R.string.main_top_loading));
                }else{
                    viewModel.time.setValue(event.result.time);
                }
                viewModel.from.setValue(BaseUtil.omitMinerString(event.result.from,8));
                viewModel.to.setValue(BaseUtil.omitMinerString(event.result.to,8));
                viewModel.value.setValue(ConvertUtil.HexStringToWei(event.result.value)+" Wei");
                viewModel.gas.setValue(ConvertUtil.HexStringToLong(event.result.gas)+"");
                viewModel.gasPrice.setValue(ConvertUtil.HexStringToWei(event.result.gasPrice)+" Wei");
                viewModel.nonce.setValue(ConvertUtil.HexStringToLong(event.result.nonce)+"");
                viewModel.input.setValue(BaseUtil.omitHashString(event.result.input,8));
                viewModel.r.setValue(BaseUtil.omitHashString(event.result.r,8));
                viewModel.s.setValue(BaseUtil.omitHashString(event.result.s,8));
                viewModel.v.setValue(BaseUtil.omitHashString(event.result.v,8));
            }
        }else{
            viewModel.state.setValue(LoadingState.LOADING_FAILED);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchBlockByNumber(OneBlockSummaryBean event){
        if (event.status==Const.RESULT_SUCCESS){
            String time = BaseUtil.timestampToString(event.result.time);
            viewModel.raw.getValue().time = time;
            viewModel.time.setValue(time);
            SearchTask.startStoreTransaction(BethApplication.getContext(),viewModel.raw.getValue());
        }else{
            viewModel.time.setValue(BaseUtil.getString(R.string.main_top_no_data));
        }
    }

    public void onTextClick(View v,String info){
        BlockDetails.showHashSnackBar(binding.getRoot(),info,getContext());
    }

    public void onRefreshClick(View view){
        viewModel.state.setValue(LoadingState.LODING);
        SearchTask.startSearchTransaction(BethApplication.getContext(),arg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarkEvent(MinerMark mark){
        switch (mark.id){
            case R.id.tv_from_content:
                viewModel.from.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
            case R.id.tv_to_content:
                viewModel.to.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
            case R.id.tv_hash_content:
                viewModel.hash.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
        }
    }

    public void onMarkClick(View v,String hash){
        showMarkDialog(v.getId(),hash);
    }

    private void showMarkDialog(int id,String hash) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_miner_mark_layout,null);
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
                EventBus.getDefault().post(new MinerMark());
            }
        });
        builder.setNegativeButton(BaseUtil.getString(R.string.bt_remove),(d,w)->{
            MMKV.defaultMMKV().removeValueForKey(hash);
            EventBus.getDefault().post(new MinerMark());
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(MMKV.defaultMMKV().contains(hash));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SearchTask.stopSearchTransaction();
    }
}