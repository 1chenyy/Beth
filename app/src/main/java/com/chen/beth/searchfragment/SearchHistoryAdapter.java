package com.chen.beth.searchfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.BethApplication;
import com.chen.beth.databinding.RvItemHistoryBinding;
import com.chen.beth.models.SearchHistory;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {
    private List<SearchHistory> list;
    private RVItemClickListener listener;

    public SearchHistoryAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchHistoryViewHolder(RvItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,false
        ),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        SearchHistory history = list.get(position);
        holder.bind(new SearchHistoryDataBinding(history.type,history.content));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void initList(SearchHistory[] histories){
        for (SearchHistory history:histories){
            list.add(history);
        }
        notifyDataSetChanged();
    }

    public void addItems(SearchHistory[] histories){
        for (SearchHistory history:histories){
            list.add(history);
        }
        notifyItemRangeInserted(list.size()-histories.length,list.size());
    }

    public void setListener(RVItemClickListener listener) {
        this.listener = listener;
    }

    public SearchHistory getHistory(int pos){
        return list.get(pos);
    }

    public void deleteItem(int pos){
        SearchHistory history = list.remove(pos);
        notifyItemRemoved(pos);
        Observable.just(history)
                .subscribeOn(Schedulers.io())
                .map(h-> BethApplication.getDBData().getSearchHistoryDao().deleteItem(h))
                .subscribe();
    }

    public void deleteAll(){
        list.clear();
        notifyDataSetChanged();
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .map(i->BethApplication.getDBData().getSearchHistoryDao().deleteAll())
                .subscribe();
    }
}
