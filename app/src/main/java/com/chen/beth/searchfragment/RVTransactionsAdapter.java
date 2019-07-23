package com.chen.beth.searchfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.R;
import com.chen.beth.databinding.RvItemTransactionsBinding;
import com.chen.beth.models.TransactionSummaryBean;

import java.util.ArrayList;
import java.util.List;

public class RVTransactionsAdapter extends RecyclerView.Adapter<RVTransactionsViewHolder> {
    private List<TransactionSummaryBean> list;
    public RVTransactionsAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public RVTransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RVTransactionsViewHolder(
                RvItemTransactionsBinding.inflate(LayoutInflater.from(parent.getContext()))
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RVTransactionsViewHolder holder, int position) {
        holder.bind(generateData(list.get(position)));
    }

    private TransactionSummaryDataBinding generateData(TransactionSummaryBean bean) {
        TransactionSummaryDataBinding data = new TransactionSummaryDataBinding();
        data.from =  bean.from;
        data.to =  bean.to;
        data.value =  bean.value;
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
}
