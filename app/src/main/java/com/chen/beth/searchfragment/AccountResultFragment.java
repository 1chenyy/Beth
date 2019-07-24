package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Worker.SearchTask;
import com.chen.beth.databinding.FragmentAccountResultBinding;
import com.chen.beth.models.AccountBalanceBean;
import com.chen.beth.models.AccountTransactionsBean;
import com.chen.beth.models.LoadingState;
import com.chen.beth.net.RetrofitManager;
import com.chen.beth.ui.ItemOffsetDecoration;
import com.chen.beth.ui.RVItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountResultFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        RVItemClickListener ,IPreLoad{
    private FragmentAccountResultBinding binding;
    private AccountResultViewModel viewModel;
    private String arg;
    private boolean isUser;
    private RecyclerView rv;
    private int currentMode = -1;
    private RVTransactionsAdapter transactionAdapter;
    private int page = 1;

    public AccountResultFragment() {

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
        binding = FragmentAccountResultBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(AccountResultViewModel.class);
        binding.setData(viewModel);
        binding.setHandler(this);
        configSpinner();
        configRecycler();
        return binding.getRoot();
    }

    private void configRecycler() {
        rv = binding.rv;
        rv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new ScaleInTopAnimator());
        rv.addItemDecoration(new ItemOffsetDecoration());
        AccountOnScrollListener listener  =new AccountOnScrollListener();
        listener.setOnPreLoad(this);
        rv.addOnScrollListener(listener);
        transactionAdapter = new RVTransactionsAdapter();
        transactionAdapter.setFromEthscan();
        transactionAdapter.setListener(this);
    }

    private void configSpinner() {
        String[] items = new String[]{BaseUtil.getString(R.string.account_transaction),BaseUtil.getString(R.string.account_block)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, items);
        AppCompatSpinner spinner = binding.spinner;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchTask.startQueryAccountBalance(BethApplication.getContext(),arg,isUser);
        viewModel.balance.setValue(BaseUtil.getString(R.string.main_top_loading));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountBalanceEvent(AccountBalanceBean event) {
        if (event!=null){
            if (!TextUtils.isEmpty(event.status)){
                viewModel.balance.setValue(formatBalance(event.result));
            }else{
                viewModel.balance.setValue(BaseUtil.getString(R.string.main_bt_no_date_refresh));
            }
        }else{
            viewModel.balance.setValue(BaseUtil.getString(R.string.main_bt_no_date_refresh));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SearchTask.stopQueryAccountBalance();
    }

    public static String formatBalance(String balance){
        String str = "0";
        try {
            Double d = Double.valueOf(balance);
            d = d/1e18;
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(8);
            nf.setGroupingUsed(false);
            str = nf.format(d)+" Eth";
        }catch (Exception e){
            str = BaseUtil.getString(R.string.main_bt_no_date_refresh);
        }
        return str;
    }

    public void onBalanceClick(View v){
        SearchTask.startQueryAccountBalance(BethApplication.getContext(),arg,isUser);
        viewModel.balance.setValue(BaseUtil.getString(R.string.main_top_loading));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentMode = position;
        if (position == 0){
            if(transactionAdapter.getItemCount()!=0){
                double size = transactionAdapter.getItemCount();
                double offset = Const.ETHERSCAN_ACCOUNT_ARG_OFFSET;
                page = (int) Math.ceil(size/offset) + 1;
                rv.setAdapter(transactionAdapter);
            }else{
                page = 1;
                viewModel.state.setValue(LoadingState.LODING);
                rv.setAdapter(transactionAdapter);
                SearchTask.startQueryAccountTransactions(BethApplication.getContext(),arg,page);
            }
        }else{
            page = 1;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountTransactionEvent(AccountTransactionsBean event){
        if (!TextUtils.isEmpty(event.status)){

            if (transactionAdapter.getItemCount() == 0){
                viewModel.state.setValue(LoadingState.LOADING_SUCCEED);
                if (event.result.size()==0){
                    viewModel.hasContent.setValue(false);
                }else{
                    page++;
                    transactionAdapter.initData(event.result);
                }
            }else{
                page++;
                transactionAdapter.addData(event.result);
            }
        }else{
            if (transactionAdapter.getItemCount() == 0){
                viewModel.state.setValue(LoadingState.LOADING_FAILED);
            }
        }
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onItemClick(View view, int pos) {
        if (currentMode == 0){
            View shared = view.findViewById(R.id.iv);
            shared.setTransitionName(BaseUtil.getString(R.string.shared_element_search_tx));
            FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                    .addSharedElement(shared,shared.getTransitionName())
                    .build();
            Bundle bundle = new Bundle();
            bundle.putBoolean(Const.ARG_USER,false);
            bundle.putString(Const.ARG_ARG,transactionAdapter.getBean(pos).hash);
            Navigation.findNavController(view).navigate(R.id.action_accountResultFragment_to_transactionResultFragment,
                    bundle,null,extras);
        }

    }

    @Override
    public void onPreLoad() {
        if (currentMode == 0){
            SearchTask.startQueryAccountTransactions(BethApplication.getContext(),arg,page);
        }
    }
}
