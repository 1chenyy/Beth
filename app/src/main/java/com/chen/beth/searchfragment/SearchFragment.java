package com.chen.beth.searchfragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chen.beth.BaseFragment;
import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Worker.SearchTask;
import com.chen.beth.databinding.FragmentSearchBinding;
import com.chen.beth.models.SearchHistory;
import com.chen.beth.ui.IPreLoad;
import com.chen.beth.ui.ItemOffsetDecoration;
import com.chen.beth.ui.OnScrollListener;
import com.chen.beth.ui.RVItemClickListener;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment implements RVItemClickListener, IPreLoad {
    private FragmentSearchBinding binding;
    private RecyclerView rv;
    private SearchHistoryAdapter adapter;
    private SearchFragmentDataBinding dataBinding;
    private OnScrollListener listener;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        binding.setHandle(this);
        dataBinding = new SearchFragmentDataBinding();
        dataBinding.hasHistory.set(false);
        binding.setData(dataBinding);
        configRecycleView();
        return binding.getRoot();
    }

    private void configRecycleView() {
        rv = binding.rvHistory;
        rv.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new ItemOffsetDecoration());
        adapter = new SearchHistoryAdapter();
        adapter.setListener(this);
        rv.setAdapter(adapter);
        listener = new OnScrollListener(5);
        listener.setOnPreLoad(this);
        rv.addOnScrollListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvHistory.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alpha_in_anim));
        binding.tvHistory.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.alpha_in_anim));
        SearchTask.startQueryAllHistory(BethApplication.getContext(),0);
    }

    public void onCardClick(View view){
        showEditDialog(view.getId(),view);
    }

    public void showEditDialog(int type,View nav){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_miner_mark_layout,null);
        TextInputLayout textInputLayout = view.findViewById(R.id.edit_layout);
        EditText editText = view.findViewById(R.id.et_miner);
        textInputLayout.setCounterEnabled(true);
        switch (type){
            case R.id.card_tx:
                builder.setTitle(R.string.search_transaction);
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_tx));
                break;
            case R.id.card_account:
                builder.setTitle(R.string.search_account);
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_account));
                break;
            case R.id.card_block:
                builder.setTitle(R.string.search_block);
                textInputLayout.setHint(BaseUtil.getString(R.string.hint_block));
                break;
        }

        builder.setView(view);
        builder.setPositiveButton(R.string.menu_search,null);
        builder.setNegativeButton(R.string.bt_return,null);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button searchButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        searchButton.setOnClickListener(v->{
            String str = editText.getText().toString().trim().toLowerCase();
            str = str.replace(" ","");
            String longHex = "\\b0x[0-9a-fA-F]+\\b";
            String shortHex = "\\b[0-9a-fA-F]+\\b";
            switch (type){
                case R.id.card_tx:
                    if (!str.startsWith("0x")){
                        str = "0x"+str;
                    }
                    if (str.length()!=66){
                        editText.setError(BaseUtil.getString(R.string.err_tx_length));
                        break;
                    }else if(!str.matches(longHex)){
                        editText.setError(BaseUtil.getString(R.string.error_hash));
                        break;
                    }
                    dialog.dismiss();
                    jump(Const.TYPE_TX,R.id.iv_tx,nav,str);
                    break;
                case R.id.card_account:
                    if (!str.startsWith("0x")){
                        str = "0x"+str;
                    }
                    if (str.length()!=42){
                        editText.setError(BaseUtil.getString(R.string.error_account_length));
                        break;
                    }else if(!str.matches(longHex)){
                        editText.setError(BaseUtil.getString(R.string.error_hash));
                        break;
                    }
                    jump(Const.TYPE_ACCOUNT,R.id.iv_account,nav,str);
                    dialog.dismiss();

                    break;
                case R.id.card_block:
                    if (!TextUtils.isDigitsOnly(str) || TextUtils.isEmpty(str) || Integer.parseInt(str)<0){
                        editText.setError(BaseUtil.getString(R.string.error_block));
                        break;
                    }
                    dialog.dismiss();
                    jump(Const.TYPE_BLOCK,R.id.iv_block,nav,str);
                    break;
            }

        });
    }

    public void jump(int type,int id,View view,String arg){
        Bundle bundle = new Bundle();
        ImageView sharedView = view.findViewById(id);
        bundle.putString(Const.ARG_ARG,arg);
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(sharedView,sharedView.getTransitionName()).build();
        switch (type){
            case Const.TYPE_TX:
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_transactionResultFragment,
                        bundle,null,extras);
                break;
            case Const.TYPE_ACCOUNT:
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_accountResultFragment,
                        bundle,null,extras);
                    break;
            case Const.TYPE_BLOCK:
                Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_blockResulFragment,
                        bundle,null,extras);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHistoryEvent(SearchHistory[] event){
        listener.setEnable(true);
        if (event.length!=0){
            if (adapter.getItemCount()==0){
                adapter.initList(event);
                dataBinding.hasHistory.set(true);
            }else{
                adapter.addItems(event);
            }
        }else{
            if (adapter.getItemCount() == 0){
                dataBinding.hasHistory.set(false);
            }
        }
    }

    @Override
    public void onItemClick(int pos) {
        SearchHistory history = adapter.getHistory(pos);

        switch(history.type){
            case Const.TYPE_TX:
                jump(Const.TYPE_TX,R.id.iv_tx,binding.cardTx,history.content);
                break;
            case Const.TYPE_ACCOUNT:
                jump(Const.TYPE_ACCOUNT,R.id.iv_account,binding.cardAccount,history.content);
                break;
            case Const.TYPE_BLOCK:
                jump(Const.TYPE_BLOCK,R.id.iv_block,binding.cardBlock,history.content);
                break;
        }
    }

    @Override
    public void onDeleteClick(int pos) {
        adapter.deleteItem(pos);
        dataBinding.hasHistory.set(adapter.getItemCount()!=0);
    }

    public void deleteAllHistory(View v){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_all_title)
                .setMessage(R.string.delete_adll_title_content)
                .setPositiveButton(R.string.delete_all_ok,
                        (d,w)->{adapter.deleteAll();dataBinding.hasHistory.set(false);})
                .setNegativeButton(R.string.delete_all_cancel,null)
                .create()
                .show();
    }

    @Override
    public void onPreLoad() {
        SearchTask.startQueryAllHistory(BethApplication.getContext(),adapter.getItemCount());
    }
}
