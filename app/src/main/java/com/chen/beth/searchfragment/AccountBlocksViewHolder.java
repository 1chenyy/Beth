package com.chen.beth.searchfragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemAccountBlocksBinding;
import com.chen.beth.ui.RVItemClickListener;

public class AccountBlocksViewHolder extends RecyclerView.ViewHolder {
    private RvItemAccountBlocksBinding binding;
    public AccountBlocksViewHolder(@NonNull RvItemAccountBlocksBinding binding, RVItemClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.getRoot().setOnClickListener(v->listener.onItemClick(v,getAdapterPosition()));
    }

    public void onBind(AccountBlocksDataBinding dataBinding){
        binding.setData(dataBinding);
        binding.executePendingBindings();
    }
}
