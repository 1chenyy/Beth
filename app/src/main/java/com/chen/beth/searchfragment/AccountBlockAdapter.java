package com.chen.beth.searchfragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.databinding.RvItemAccountBlocksBinding;
import com.chen.beth.models.AccountBlocksBean;
import com.chen.beth.ui.RVItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AccountBlockAdapter extends RecyclerView.Adapter<AccountBlocksViewHolder> {

    private List<AccountBlocksBean.ResultBean> list;
    private RVItemClickListener listener;

    public AccountBlockAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public AccountBlocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountBlocksViewHolder(RvItemAccountBlocksBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        ), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountBlocksViewHolder holder, int position) {
        holder.onBind(generateData(list.get(position)));
    }

    private AccountBlocksDataBinding generateData(AccountBlocksBean.ResultBean bean) {
        AccountBlocksDataBinding data = new AccountBlocksDataBinding();
        data.number = bean.blockNumber;
        data.time = BaseUtil.timestampToString(Integer.parseInt(bean.timeStamp));
        return data;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListener(RVItemClickListener listener) {
        this.listener = listener;
    }

    public void initData(List<AccountBlocksBean.ResultBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(List<AccountBlocksBean.ResultBean> list) {
        this.list.addAll(list);
        notifyItemRangeInserted(this.list.size() - list.size(), this.list.size());
    }

    public AccountBlocksBean.ResultBean getBean(int pos) {
        return list.get(pos);
    }
}
