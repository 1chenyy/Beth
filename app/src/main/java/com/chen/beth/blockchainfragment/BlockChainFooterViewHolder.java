package com.chen.beth.blockchainfragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.ItemFooterLayoutBinding;

public class BlockChainFooterViewHolder extends RecyclerView.ViewHolder {
    private ItemFooterLayoutBinding binding;
    public BlockChainFooterViewHolder(@NonNull ItemFooterLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(BlockChainFooterDatabinding databinding){
        binding.setViewholder(databinding);
    }
}
