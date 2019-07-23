package com.chen.beth.searchfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.databinding.RvItemTransactionsBinding;
import com.chen.beth.models.TransactionSummaryBean;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RVTransactionsAdapter extends RecyclerView.Adapter<RVTransactionsViewHolder> {
    private List<TransactionSummaryBean> list;
    private RVItemClickListener listener;
    public RVTransactionsAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RVTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RVTransactionsViewHolder(
                RvItemTransactionsBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,false),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RVTransactionsViewHolder holder, int position) {
        holder.bind(generateData(list.get(position)));
    }

    public void setListener(RVItemClickListener listener){
        this.listener = listener;
    }

    private TransactionSummaryDataBinding generateData(TransactionSummaryBean bean) {
        TransactionSummaryDataBinding data = new TransactionSummaryDataBinding();
        data.from = BaseUtil.getString(R.string.tx_from)+" "+BaseUtil.omitMinerString(bean.from,10);
        data.to =  BaseUtil.getString(R.string.tx_to)+" "+BaseUtil.omitMinerString(bean.to,10);
        data.value =  BaseUtil.getString(R.string.tx_value)+" "+bean.value;
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

    public TransactionSummaryBean getBean(int pos){
        return list.get(pos);
    }
}
