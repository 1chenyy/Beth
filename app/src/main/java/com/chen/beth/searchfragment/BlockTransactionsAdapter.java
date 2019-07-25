package com.chen.beth.searchfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.databinding.RvItemTransactionsBinding;
import com.chen.beth.models.TransactionSummaryBean;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BlockTransactionsAdapter extends RecyclerView.Adapter<BlockTransactionsViewHolder> {
    private List<TransactionSummaryBean> list;
    private RVItemClickListener listener;
    private boolean fromBeth = true;
    public BlockTransactionsAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public BlockTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BlockTransactionsViewHolder(
                RvItemTransactionsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,false),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockTransactionsViewHolder holder, int position) {
        holder.bind(generateData(list.get(position)));
    }

    public void setListener(RVItemClickListener listener){
        this.listener = listener;
    }

    private TransactionSummaryDataBinding generateData(TransactionSummaryBean bean) {
        TransactionSummaryDataBinding data = new TransactionSummaryDataBinding();
        data.from = BaseUtil.getString(R.string.block_result_from)+" : "+BaseUtil.omitMinerString(bean.from,6);
        data.to =  BaseUtil.getString(R.string.block_result_to)+" : "+BaseUtil.omitMinerString(bean.to,6);
        String v;
        if (fromBeth){
            v = Double.parseDouble(bean.value)==0?"0 Eth":bean.value+" Eth";
        }else{
            v = AccountResultFragment.formatBalance(bean.value);
        }

        data.value =  BaseUtil.getString(R.string.tx_value)+" : "+v;
        return data;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void initData(List<TransactionSummaryBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(TransactionSummaryBean[] beans){
        int oldsize = list.size();
        for (TransactionSummaryBean bean:beans){
            list.add(bean);
        }
        notifyItemRangeInserted(oldsize,beans.length);
    }

    public void addData(List<TransactionSummaryBean> list){
        this.list.addAll(list);
        notifyItemRangeInserted(this.list.size()-list.size(),this.list.size());
    }

    public TransactionSummaryBean getBean(int pos){
        return list.get(pos);
    }

    public void setFromEthscan(){
        fromBeth = false;
    }
}
