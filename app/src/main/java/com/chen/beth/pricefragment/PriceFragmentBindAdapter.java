package com.chen.beth.pricefragment;

import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.chen.beth.Utils.Const;
import com.chen.beth.Utils.LogUtil;
import com.chen.beth.models.LoadingState;
import com.chen.beth.ui.CustomURLSpan;
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
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return "$ "+value;
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

    @BindingAdapter({"setDataSourceName","setDataSourceWebsite"})
    public static void setDataSource(TextView tv,String name,String website){
        String prefix = BaseUtil.getString(R.string.data_source);
        String data = prefix+name;
        SpannableString spannableString = new SpannableString(prefix+name);
        spannableString.setSpan(new CustomURLSpan(website), prefix.length(),data.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
