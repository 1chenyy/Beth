package com.chen.beth.pricefragment;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import androidx.databinding.BindingAdapter;

import com.chen.beth.R;
import com.chen.beth.Utils.Const;
import com.chen.beth.models.LoadingState;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PriceFragmentBindAdapter {
    @BindingAdapter("drawHistoryPriceChart")
    public static void drawHistoryPriceChart(LineChart chart, List<Float> list){
        int total = list.size();
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0;i<total;i++){
            values.add(new Entry(i,list.get(i)));
        }

        LineDataSet set = new LineDataSet(values,"txs");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setDrawCircles(false);
        set.setCircleRadius(4f);
        set.setColor(Color.BLACK);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawVerticalHighlightIndicator(false);
        XAxis x = chart.getXAxis();
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH,-(total-1-((int) value)));
                return Const.CHART_CHART_DATE.format(calendar.getTime());
            }
        });

        LineData data = new LineData(set);
        data.setDrawValues(false);
        chart.setData(data);
    }

    @BindingAdapter("isShowRefreshBT")
    public static void isShowRefreshBT(Button bt, LoadingState state){
        switch (state){
            case LODING:
            case LOADING_SUCCEED:
                bt.setVisibility(View.GONE);
                break;
            case LOADING_FAILED:
                bt.setVisibility(View.VISIBLE);
                bt.setText(R.string.main_bt_no_date_refresh);
                break;

        }
    }
}
