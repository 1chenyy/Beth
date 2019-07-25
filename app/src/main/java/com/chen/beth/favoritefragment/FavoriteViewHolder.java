package com.chen.beth.favoritefragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemFavoriteBinding;
import com.chen.beth.ui.RVItemClickListener;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public RvItemFavoriteBinding binding;
    public FavoriteViewHolder(@NonNull RvItemFavoriteBinding binding, RVItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.getRoot().setOnClickListener(v->listener.onItemClick(getAdapterPosition()));
        binding.ibDelete.setOnClickListener(v->listener.onDeleteClick(getAdapterPosition()));
    }
    public void bind(FavoriteDataBinding dataBinding){
        binding.setData(dataBinding);
        binding.executePendingBindings();
    }
}
