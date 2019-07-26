package com.chen.beth.favoritefragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.databinding.RvItemFavoriteBinding;
import com.chen.beth.models.FavoriteBean;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private RVItemClickListener listener;
    public List<FavoriteBean> list;
    public FavoriteAdapter(){
        this.list = new ArrayList<>();
    }
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(RvItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        ),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteBean bean = list.get(position);
        holder.bind(new FavoriteDataBinding(bean.type,bean.content));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnclickListener(RVItemClickListener listener){
        this.listener = listener;
    }

    public void addData(List<FavoriteBean> beans){
        list.addAll(beans);
        notifyItemRangeInserted(list.size()-beans.size(),beans.size());
    }

    public void initData(List<FavoriteBean> beans){
        list.clear();
        list.addAll(beans);
        notifyDataSetChanged();
    }
    public FavoriteBean getBean(int pos){
        return list.get(pos);
    }

    public void removeItem(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    public void removeAll(){
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0,size);
    }
}
