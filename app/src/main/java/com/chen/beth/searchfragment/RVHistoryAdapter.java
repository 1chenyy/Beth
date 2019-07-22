package com.chen.beth.searchfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemHistoryBinding;
import com.chen.beth.models.SearchHistory;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RVHistoryAdapter extends RecyclerView.Adapter<RVHistoryViewHolder> {
    private List<SearchHistory> list;
    private RVItemClickListener listener;

    public RVHistoryAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RVHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RVHistoryViewHolder(RvItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,false
        ),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHistoryViewHolder holder, int position) {
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

    public void setListener(RVItemClickListener listener) {
        this.listener = listener;
    }

    public SearchHistory getHistory(int pos){
        return list.get(pos);
    }
}
