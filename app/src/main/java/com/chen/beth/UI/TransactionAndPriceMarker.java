package com.chen.beth.UI;

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
    private TextView tvDate,tvTxs,tvPrice;
    public TransactionAndPriceMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        tvDate = findViewById(R.id.tv_date);
        tvTxs = findViewById(R.id.tv_txs);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvTxs.setText(e.getY()+"");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,-(14-((int)e.getX())+1));
        tvDate.setText(Const.SDF_DAY.format(calendar.getTime()));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
