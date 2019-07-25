package com.chen.beth.searchfragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemTransactionsBinding;
import com.chen.beth.ui.RVItemClickListener;

public class BlockTransactionsViewHolder extends RecyclerView.ViewHolder {
    public RvItemTransactionsBinding binding;
    public BlockTransactionsViewHolder(@NonNull RvItemTransactionsBinding binding, RVItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(v->listener.onItemClick(v,getAdapterPosition()));

    }

    public void bind(TransactionSummaryDataBinding data){
        binding.setData(data);
        binding.executePendingBindings();
    }
}
