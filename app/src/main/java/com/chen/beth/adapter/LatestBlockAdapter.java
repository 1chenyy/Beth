package com.chen.beth.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.adapter.ViewHolder.LatestBlockViewHolder;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.viewModel.ItemLatestBlockViewModel;

import java.util.ArrayList;

public class LatestBlockAdapter extends RecyclerView.Adapter<LatestBlockViewHolder> {
    private ArrayList<ItemLatestBlockViewModel> list;
    public LatestBlockAdapter(ArrayList<ItemLatestBlockViewModel> list){
        this.list = list;
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

}
