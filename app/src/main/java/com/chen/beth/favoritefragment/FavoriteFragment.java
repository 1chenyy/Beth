package com.chen.beth.favoritefragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.databinding.FragmentFavoriteBinding;
import com.chen.beth.models.FavoriteBean;
import com.chen.beth.models.LoadingState;
import com.chen.beth.ui.IPreLoad;
import com.chen.beth.ui.ItemOffsetDecoration;
import com.chen.beth.ui.OnScrollListener;
import com.chen.beth.ui.RVItemClickListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements AdapterView.OnItemSelectedListener, RVItemClickListener, IPreLoad {
    private FragmentFavoriteBinding binding;
    private FavoriteFragmentViewModel data;
    private RecyclerView rv;
    private FavoriteAdapter adapter;
    private OnScrollListener listener;
    private int current;
    private boolean isReload = true;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater,container,false);
        binding.setLifecycleOwner(this);
        data = ViewModelProviders.of(this).get(FavoriteFragmentViewModel.class);
        binding.setData(data);
        binding.setHandler(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configSpinner();
        configRecyclerView();
    }

    private void configRecyclerView() {
        rv = binding.rv;
        rv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new ItemOffsetDecoration());
        adapter = new FavoriteAdapter();
        rv.setAdapter(adapter);
        listener = new OnScrollListener(5);
        listener.setOnPreLoad(this);
        adapter.setOnclickListener(this);
        rv.addOnScrollListener(listener);
    }

    private void configSpinner() {
        String[] items = new String[]{BaseUtil.getString(R.string.favorite_all),
                BaseUtil.getString(R.string.favorite_tx),BaseUtil.getString(R.string.favorite_account),
                BaseUtil.getString(R.string.favorite_blocks)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, items);
        AppCompatSpinner spinner = binding.spinner;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        current = position;
        isReload = true;
        switch (position){
            case 0:
                loadAll(0);
                break;
            case 1:
                loadTxs(0);
                break;
            case 2:
                loadAccount(0);
                break;
            case 3:
                loadBlock(0);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private Disposable loadAllDisposable;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadAllDisposable!=null && !loadAllDisposable.isDisposed())
            loadAllDisposable.dispose();
    }

    private void loadAll(int offset){
        LogUtil.d(this.getClass(),"开始加载收藏记录，offset"+offset);
        loadAllDisposable = BethApplication.getDBData().getFavoriteDao()
                .getFavorite(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l->handleSucceed(l),t->handleFailed(t));
    }
    private void handleSucceed(List<FavoriteBean> list){
        if (adapter.getItemCount()==0 || isReload){
            data.hasFavorite.setValue(list.size() != 0);
            adapter.initData(list);
            isReload = false;
        }else{
            adapter.addData(list);
        }

    }
    private void handleFailed(Throwable throwable){
        if (adapter.getItemCount()==0){
            data.hasFavorite.setValue(adapter.getItemCount()!=0);
        }
    }

    @Override
    public void onItemClick(int pos) {
        int dest = 0;
        FavoriteBean bean  =adapter.getBean(pos);
        switch (bean.type){
            case 0:
                dest = R.id.action_favoriteFragment_to_transactionResultFragment;
                break;
            case 1:
                dest = R.id.action_favoriteFragment_to_accountResultFragment;
                break;
            case 2:
                dest = R.id.action_favoriteFragment_to_blockResulFragment;
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Const.ARG_ARG,bean.content);
        bundle.putBoolean(Const.ARG_USER,false);
        Navigation.findNavController(binding.rv).navigate(dest,bundle,null,null);
    }

    @Override
    public void onItemClick(View view, int pos) {
        switch (current){
            case 0:
                loadAll(adapter.getItemCount());
                break;
            case 1:
                loadTxs(adapter.getItemCount());
                break;
            case 2:
                loadAccount(adapter.getItemCount());
                break;
            case 3:
                loadBlock(adapter.getItemCount());
                break;
        }
    }

    private void loadBlock(int offset) {
        LogUtil.d(this.getClass(),"开始区块加载收藏记录，offset"+offset);
        loadAllDisposable = BethApplication.getDBData().getFavoriteDao()
                .getBlockFavorite(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l->handleSucceed(l),t->handleFailed(t));
    }

    private void loadAccount(int offset) {
        LogUtil.d(this.getClass(),"开始账户加载收藏记录，offset"+offset);
        loadAllDisposable = BethApplication.getDBData().getFavoriteDao()
                .getAccountFavorite(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l->handleSucceed(l),t->handleFailed(t));
    }

    private void loadTxs(int offset) {
        LogUtil.d(this.getClass(),"开始交易加载收藏记录，offset"+offset);
        loadAllDisposable = BethApplication.getDBData().getFavoriteDao()
                .getTxsFavorite(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l->handleSucceed(l),t->handleFailed(t));
    }

    private Disposable disposable;
    @Override
    public void onDeleteClick(int pos) {
        FavoriteBean bean  = adapter.getBean(pos);
        adapter.removeItem(pos);
        data.hasFavorite.setValue(adapter.getItemCount()!=0);
        disposable = BethApplication.getDBData().getFavoriteDao()
                .removeFavorite(bean.type,bean.content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
        if (loadAllDisposable!=null && !loadAllDisposable.isDisposed()){
            loadAllDisposable.dispose();
        }
    }

    @Override
    public void onPreLoad() {
        switch (current){
            case 0:
                loadAll(adapter.getItemCount());
                break;
            case 1:
                loadTxs(adapter.getItemCount());
                break;
            case 2:
                loadAccount(adapter.getItemCount());
                break;
            case 3:
                loadBlock(adapter.getItemCount());
                break;
        }
    }

    public void onDeleteAll(View v){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_all_favorite_title)
                .setMessage(R.string.delete_all_favorite)
                .setPositiveButton(R.string.delete_all_ok,
                        (d,w)->{deleteAll();})
                .setNegativeButton(R.string.delete_all_cancel,null)
                .create()
                .show();

    }
    private void deleteAll(){
        switch (current){
            case 0:
                disposable = BethApplication.getDBData().getFavoriteDao().deleteAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                break;
            case 1:
                disposable = BethApplication.getDBData().getFavoriteDao().deleteType(Const.TYPE_TX)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                break;
            case 2:
                disposable = BethApplication.getDBData().getFavoriteDao().deleteType(Const.TYPE_ACCOUNT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                break;
            case 3:
                disposable = BethApplication.getDBData().getFavoriteDao().deleteType(Const.TYPE_BLOCK)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
                break;
        }
        adapter.removeAll();
        data.hasFavorite.setValue(false);
    }
}
