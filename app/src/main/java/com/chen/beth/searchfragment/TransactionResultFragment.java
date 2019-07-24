package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.ConvertUtil;
import com.chen.beth.Worker.SearchTask;
import com.chen.beth.blockdetailsfragment.BlockDetails;
import com.chen.beth.databinding.FragmentTransactionResultBinding;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.MinerMark;
import com.chen.beth.models.OneBlockSummaryBean;
import com.chen.beth.models.TransactionDetailBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionResultFragment extends BaseFragment {
    private FragmentTransactionResultBinding binding;
    private TransactionResultViewModel viewModel;
    private String arg;
    private boolean isUser;

    public TransactionResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        arg = bundle.getString(Const.ARG_ARG,"");
        isUser = bundle.getBoolean(Const.ARG_USER,true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransactionResultBinding.inflate(inflater,container,false);
        viewModel = ViewModelProviders.of(this).get(TransactionResultViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setData(viewModel);
        binding.setHandler(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println(isUser);
        SearchTask.startSearchTransaction(BethApplication.getContext(),arg,isUser);
        postponeEnterTransition();
        binding.iv.setTransitionName(BaseUtil.getString(R.string.shared_element_search_tx));
        startPostponedEnterTransition();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTransactionByHash(TransactionDetailBean event){
        if (event.result!=null){
            if (TextUtils.isEmpty(event.result.hash)){
                viewModel.state.setValue(LoadingState.LOADING_FAILED);
            }else{
                viewModel.raw.setValue(event.result);
                viewModel.state.setValue(LoadingState.LOADING_SUCCEED);
                viewModel.hash.setValue(BaseUtil.omitMinerString(event.result.hash,8));
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
        if (v.getId()==R.id.tv_input_content){
            showInputData(info);
            return;
        }
        BlockDetails.showHashSnackBar(binding.getRoot(),info,getContext());
    }

    public void onRefreshClick(View view){
        viewModel.state.setValue(LoadingState.LODING);
        SearchTask.startSearchTransaction(BethApplication.getContext(),arg, isUser);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarkEvent(MinerMark mark){
        switch (mark.id){
            case R.id.ib_from_miner:
                viewModel.from.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
            case R.id.ib_to_miner:
                viewModel.to.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
            case R.id.ib_hash:
                viewModel.hash.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
        }
    }

    public void onMarkClick(View v,String hash){
        SearchBindingAdapter.showMarkDialog(getContext(),v.getId(),hash);
    }



    private void showInputData(String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.tx_input);
        builder.setMessage(data);
        builder.setPositiveButton(R.string.bt_return,null);
        builder.setNegativeButton(R.string.info_copy,(d,w)->BlockDetails.copy(getContext(),data));
        builder.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SearchTask.stopSearchTransaction();
    }
}
