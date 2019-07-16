package com.chen.beth.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.mainfragment.MainFragment;
import com.chen.beth.adapter.ViewHolder.LatestBlockViewHolder;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.mainfragment.ItemLatestBlockDataBinding;

import java.util.ArrayList;

public class LatestBlockAdapter extends RecyclerView.Adapter<LatestBlockViewHolder> {
    private static final int MAX_ITEMS = 50;
    private ArrayList<ItemLatestBlockDataBinding> list;
    private int current = 0;
    private MainFragment fragment;
    public LatestBlockAdapter(MainFragment fragment){
        list = new ArrayList<>();
        this.fragment = fragment;
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
        holder.bind(list.get(position),fragment);
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
        int oldSize = 0;
        if (list.size()>MAX_ITEMS){
            oldSize = list.size();
            list.removeAll(list.subList(MAX_ITEMS,oldSize));
            notifyItemRangeRemoved(MAX_ITEMS,oldSize-MAX_ITEMS);
        }

    }

    public ArrayList<ItemLatestBlockDataBinding> getList(){
        return list;
    }

    public int getCurrent(){
        return current;
    }

    public void setCurrent(int current){
        this.current = current;
    }

}
