package com.chen.beth.searchfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Worker.SearchTask;
import com.chen.beth.blockdetailsfragment.BlockDetails;
import com.chen.beth.databinding.FragmentBlockResultBinding;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.models.LoadingState;
import com.chen.beth.models.MinerMark;
import com.chen.beth.models.OneBlockSummaryBean;
import com.chen.beth.models.TransactionSummaryBundleBean;
import com.chen.beth.ui.ItemOffsetDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlockResultFragment extends BaseFragment {
    private FragmentBlockResultBinding binding;
    private BlockResultViewModel viewModel;
    private String arg;
    private RecyclerView rv;
    private RVTransactionsAdapter adapter;

    public BlockResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arg = getArguments().getString(Const.ARG_ARG,"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBlockResultBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(BlockResultViewModel.class);
        binding.setData(viewModel);
        binding.setHandler(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configRecyclerView();
        SearchTask.startSearchBlockByNumber(BethApplication.getContext(),Integer.parseInt(arg),true);

    }

    private void configRecyclerView() {
        rv = binding.rvTxs;
        rv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new ScaleInTopAnimator());
        rv.addItemDecoration(new ItemOffsetDecoration());
        adapter = new RVTransactionsAdapter();
        rv.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchBlockByNumber(OneBlockSummaryBean event){
        switch (event.status){
            case Const.RESULT_SUCCESS:
                viewModel.state.setValue(LoadingState.LOADING_SUCCEED);
                updateUI(event.result);
                break;
            case Const.RESULT_NO_DATA:
                viewModel.state.setValue(LoadingState.LOADING_NO_DATA);
                break;
            case Const.RESULT_NO_NET:
                viewModel.state.setValue(LoadingState.LOADING_FAILED);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQueryBlockTransaction(TransactionSummaryBundleBean event){
        switch (event.status){
            case Const.RESULT_SUCCESS:
                viewModel.stateTxs.setValue(LoadingState.LOADING_SUCCEED);
                adapter.initData(event.result.txs);
                break;
            case Const.RESULT_NO_DATA:
                viewModel.stateTxs.setValue(LoadingState.LOADING_NO_DATA);
                break;
            case Const.RESULT_NO_NET:
                viewModel.stateTxs.setValue(LoadingState.LOADING_FAILED);
                break;
        }
    }


    private void updateUI(BlockSummaryBean bean) {
        viewModel.raw.setValue(bean);
        viewModel.number.setValue(bean.number+"");
        viewModel.time.setValue(BaseUtil.timestampToString(bean.time));
        viewModel.txs.setValue(bean.txs+"");
        if (bean.txs>0){
            SearchTask.startQueryBlockTransaction(BethApplication.getContext(),Integer.parseInt(arg));
        }else{
            viewModel.stateTxs.setValue(LoadingState.LOADING_NO_DATA);
        }
        viewModel.miner.setValue(BaseUtil.omitMinerString(bean.miner,6));
        viewModel.reward.setValue(bean.reward+" Eth");
        viewModel.uncleReward.setValue(Double.parseDouble(bean.unclereward)==0?"0 Eth":bean.unclereward+" Eth");
        viewModel.difficult.setValue(bean.difficult);
        viewModel.totalDifficult.setValue(bean.totaldifficulty);
        viewModel.size.setValue(bean.size + " "+BaseUtil.getString(R.string.info_byte));
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String gasPercent = numberFormat.format((double)bean.gasused/(double)bean.gaslimit*100)+"%";
        viewModel.gasUsed.setValue(bean.gasused+" ("+gasPercent+")");
        viewModel.gasLimit.setValue(bean.gaslimit+"");
        viewModel.extra.setValue(bean.extra);
        binding.tvExtraContent.setSelected(true);
        viewModel.hash.setValue(BaseUtil.omitHashString(bean.hash,8));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SearchTask.stopSearchBlockByNumber();
        SearchTask.stopQueryBlockTransaction();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarkEvent(MinerMark mark){
        switch (mark.id){
            case R.id.ib_miner_edit:
                viewModel.miner.setValue(BaseUtil.omitMinerString(mark.hash,6));
                break;
        }
    }

    public void onRefreshClick(View view){
        viewModel.state.setValue(LoadingState.LODING);
        SearchTask.startSearchBlockByNumber(BethApplication.getContext(),Integer.parseInt(arg),true);
    }

    public void onTxRefreshClick(View view){
        viewModel.stateTxs.setValue(LoadingState.LODING);
        SearchTask.startQueryBlockTransaction(BethApplication.getContext(),Integer.parseInt(arg));
    }

    public void onTextClick(View v,String info){
        BlockDetails.showHashSnackBar(binding.getRoot(),info,getContext());
    }
    public void onMarkClick(View v,String hash){
        SearchBindingAdapter.showMarkDialog(getContext(),v.getId(),hash);
    }

}
