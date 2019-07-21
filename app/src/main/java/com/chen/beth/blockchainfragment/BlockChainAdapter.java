package com.chen.beth.blockchainfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.databinding.ItemFooterLayoutBinding;
import com.chen.beth.databinding.RvItemAllBlockChainBinding;
import com.chen.beth.databinding.RvItemBlockChainBinding;
import com.chen.beth.mainfragment.LatestBlockViewHolder;
import com.chen.beth.models.BlockSummaryBean;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BlockChainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BlockChainFragment fragment;
    private ArrayList<BlockSummaryBean> list;
    private BlockChainFooterDatabinding footerDatabinding;
    private static final int TYPE_BLOCK = 1;
    private static final int TYPE_FOOTER = TYPE_BLOCK+1;
    private RVItemClickListener listener;

    public BlockChainAdapter(BlockChainFragment fragment) {
        list = new ArrayList<>();
        this.fragment = fragment;
        footerDatabinding = new BlockChainFooterDatabinding();
    }

    public void setListener(RVItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==TYPE_BLOCK){
            RvItemAllBlockChainBinding binding = RvItemAllBlockChainBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return new BlockChainViewHolder(binding,listener);
        }else{
            ItemFooterLayoutBinding binding = ItemFooterLayoutBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return new BlockChainFooterViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BlockChainViewHolder){
            ((BlockChainViewHolder)holder).bind(BaseUtil.generatorItemDataBinding(list.get(position)),fragment);
        }else if(holder instanceof BlockChainFooterViewHolder){
            ((BlockChainFooterViewHolder)holder).bind(footerDatabinding);
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1==getItemCount()){
            return TYPE_FOOTER;
        }else{
            return  TYPE_BLOCK;
        }
    }
    public void addFrontItems(List<BlockSummaryBean> items){
        list.addAll(0,items);
        notifyItemRangeInserted(0,items.size());
    }


    public void addTailItems(List<BlockSummaryBean> items){
        list.addAll(items);
        notifyDataSetChanged();
    }

    public BlockSummaryBean getBlockByPosition(int position){
        return list.get(position);
    }

    public void setLoadingState(boolean state){
        footerDatabinding.loadingState.set(state);
    }
}
