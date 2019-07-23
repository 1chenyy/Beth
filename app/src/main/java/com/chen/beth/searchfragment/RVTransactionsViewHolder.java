package com.chen.beth.searchfragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemTransactionsBinding;

public class RVTransactionsViewHolder extends RecyclerView.ViewHolder {
    public RvItemTransactionsBinding binding;
    public RVTransactionsViewHolder(@NonNull RvItemTransactionsBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(TransactionSummaryDataBinding data){
        binding.setData(data);
        binding.executePendingBindings();
    }
}
