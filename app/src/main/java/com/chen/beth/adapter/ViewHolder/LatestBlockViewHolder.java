package com.chen.beth.adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.viewModel.ItemLatestBlockViewModel;

public class LatestBlockViewHolder extends RecyclerView.ViewHolder {
    private RvItemBlockChainBinding binding;
    public LatestBlockViewHolder(@NonNull RvItemBlockChainBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(ItemLatestBlockViewModel viewModel){
        binding.setViewmodel(viewModel);
        binding.executePendingBindings();
    }
}
