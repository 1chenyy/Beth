package com.chen.beth.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.Adapter.ViewHolder.LatestBlockViewHolder;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.viewModel.ItemLatestBlockDataBinding;

import java.util.ArrayList;

public class LatestBlockAdapter extends RecyclerView.Adapter<LatestBlockViewHolder> {
    private ArrayList<ItemLatestBlockDataBinding> list;
    private int current = 0;
    public LatestBlockAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public LatestBlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemBlockChainBinding binding = RvItemBlockChainBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new LatestBlockViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestBlockViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public void setInitList(ArrayList<ItemLatestBlockDataBinding> items){
        list.addAll(0,items);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<ItemLatestBlockDataBinding> items){
        list.addAll(0,items);
        notifyItemRangeInserted(0,items.size());
    }

    public int getCurrent(){
        return current;
    }

    public void setCurrent(int current){
        this.current = current;
    }
}
