package com.chen.beth.blockchainfragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemAllBlockChainBinding;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.mainfragment.ItemLatestBlockDataBinding;
import com.chen.beth.mainfragment.MainFragment;
import com.chen.beth.ui.RVItemClickListener;

public class BlockChainViewHolder extends RecyclerView.ViewHolder {
    public RvItemAllBlockChainBinding binding;
    public BlockChainViewHolder(@NonNull RvItemAllBlockChainBinding binding, RVItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
    }

    public void bind(ItemLatestBlockDataBinding viewModel, BlockChainFragment fragment){
        binding.setData(viewModel);
        binding.setHolder(fragment);
        binding.executePendingBindings();
    }
}
