package com.chen.beth.ui;

import android.content.Context;
import android.widget.TextView;

import com.chen.beth.R;
import com.chen.beth.Utils.Const;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.Calendar;

public class TransactionAndPriceMarker extends MarkerView {
    public static final String TYPE_TRANSACTION = "transaction";
    public static final String TYPE_PRICE = "price";

    private TextView tvDate,tvTxs;
    private String type;
    public TransactionAndPriceMarker(Context context, int layoutResource,String type) {
        super(context, layoutResource);
        tvDate = findViewById(R.id.tv_date);
        tvTxs = findViewById(R.id.tv_txs);
        this.type = type;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvTxs.setText("$ "+e.getY());
        Calendar calendar = Calendar.getInstance();
        if (TYPE_TRANSACTION.equals(type)){
            calendar.add(Calendar.DAY_OF_MONTH,-(14-((int)e.getX())+1));
            tvDate.setText(Const.SDF_DAY.format(calendar.getTime()));
        }else if(TYPE_PRICE.equals(type)){
            calendar.add(Calendar.DAY_OF_MONTH,-(16-((int)e.getX())-1));
            tvDate.setText(Const.SDF_DAY.format(calendar.getTime())+" 8:00");
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
