package com.chen.beth.mainfragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;

public class LatestBlockAdapter extends RecyclerView.Adapter<LatestBlockViewHolder> {
    private static final int MAX_ITEMS = 50;
    private ArrayList<BlockSummaryBean> list;
    private int current = 0;
    private MainFragment fragment;
    private RVItemClickListener listener;

    public LatestBlockAdapter(MainFragment fragment){
        list = new ArrayList<>();
        this.fragment = fragment;
    }

    public void setListener(RVItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public LatestBlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvItemBlockChainBinding binding = RvItemBlockChainBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new LatestBlockViewHolder(binding,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestBlockViewHolder holder, int position) {
        holder.bind(BaseUtil.generatorItemDataBinding(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public void setInitList(ArrayList<BlockSummaryBean> items){
        list.addAll(0,items);
        notifyDataSetChanged();
    }

    public void addItems(ArrayList<BlockSummaryBean> items){
        list.addAll(0,items);
        notifyItemRangeInserted(0,items.size());
        int oldSize = 0;
        if (list.size()>MAX_ITEMS){
            oldSize = list.size();
            list.removeAll(list.subList(MAX_ITEMS,oldSize));
            notifyItemRangeRemoved(MAX_ITEMS,oldSize-MAX_ITEMS);
        }

    }

    public ArrayList<BlockSummaryBean> getList(){
        return list;
    }

    public int getCurrent(){
        return current;
    }

    public void setCurrent(int current){
        this.current = current;
    }

}
