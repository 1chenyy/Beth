package com.chen.beth.blockchainfragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemAllBlockChainBinding;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.mainfragment.ItemLatestBlockDataBinding;
import com.chen.beth.mainfragment.MainFragment;

public class BlockChainViewHolder extends RecyclerView.ViewHolder {
    private RvItemAllBlockChainBinding binding;
    public BlockChainViewHolder(@NonNull RvItemAllBlockChainBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(ItemLatestBlockDataBinding viewModel, BlockChainFragment fragment){
        binding.setData(viewModel);
        binding.setHolder(fragment);
        binding.executePendingBindings();
    }
}
