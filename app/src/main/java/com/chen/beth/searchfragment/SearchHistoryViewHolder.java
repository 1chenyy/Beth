package com.chen.beth.searchfragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemHistoryBinding;
import com.chen.beth.ui.RVItemClickListener;

public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
    public RvItemHistoryBinding binding;
    public SearchHistoryViewHolder(@NonNull RvItemHistoryBinding binding, RVItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
        binding.ibDelete.setOnClickListener(v->listener.onDeleteClick(getAdapterPosition()));
    }

    public void bind(SearchHistoryDataBinding dataBinding){
        binding.setData(dataBinding);
        binding.executePendingBindings();
    }
}
