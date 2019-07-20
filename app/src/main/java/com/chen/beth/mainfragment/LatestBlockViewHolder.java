package com.chen.beth.mainfragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.mainfragment.MainFragment;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.mainfragment.ItemLatestBlockDataBinding;
import com.chen.beth.ui.RVItemClickListener;

public class LatestBlockViewHolder extends RecyclerView.ViewHolder {
    public RvItemBlockChainBinding binding;
    public LatestBlockViewHolder(@NonNull RvItemBlockChainBinding binding, RVItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
    }

    public void bind(ItemLatestBlockDataBinding viewModel){
        binding.setData(viewModel);
        binding.executePendingBindings();
    }
}
