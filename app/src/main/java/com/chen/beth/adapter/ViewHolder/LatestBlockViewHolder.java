package com.chen.beth.adapter.ViewHolder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.MainFragment;
import com.chen.beth.adapter.LatestBlockAdapter;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.viewModel.ItemLatestBlockDataBinding;

public class LatestBlockViewHolder extends RecyclerView.ViewHolder {
    private RvItemBlockChainBinding binding;
    public LatestBlockViewHolder(@NonNull RvItemBlockChainBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(ItemLatestBlockDataBinding viewModel, MainFragment fragment){
        binding.setData(viewModel);
        binding.setHolder(fragment);
        binding.executePendingBindings();
    }
}
