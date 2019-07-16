package com.chen.beth.BindAdapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.beth.R;
import com.chen.beth.Utils.Const;
import com.chen.beth.models.LoadingState;
import com.chen.beth.ui.ColorfulLoading;
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

public class MainFragmentBindAdapter {

    @BindingAdapter("content")
    public static void setContent(TextView view, String content){
        if (content.equals(view.getText()))
            return;
        ValueAnimator contentAnimator = ValueAnimator.ofFloat(1,0,1).setDuration(800);
        contentAnimator.addUpdateListener(a->{
            float value = (float) a.getAnimatedValue();
            if (value < 0.5 && !content.equals(view.getText())){
                view.setText(content);
            }
            view.setAlpha(value);
        });
        contentAnimator.start();

    }

    @BindingAdapter("isShowRefresh")
    public static void showRefresh(ImageButton view, boolean isShow){
        view.clearAnimation();
        view.setEnabled(true);
        view.setVisibility(isShow? View.VISIBLE:View.GONE);
    }

    @BindingAdapter("isShowLoading")
    public static void showLoading(ColorfulLoading loading, LoadingState state){
        if (state == LoadingState.LODING){
            loading.setVisibility(View.VISIBLE);
        }else{
            loading.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("isShowMainBT")
    public static void showLoading(Button bt,LoadingState state){
        switch (state){
            case LODING:
                bt.setVisibility(View.GONE);
                break;
            case LOADING_FAILED:
                bt.setVisibility(View.VISIBLE);
                bt.setText(R.string.main_bt_no_date_refresh);
                break;
            case LOADING_SUCCEED:
                bt.setVisibility(View.VISIBLE);
                bt.setText(R.string.main_bt_more);
                break;
        }
    }

    @BindingAdapter("isShowRecyclerView")
    public static void showRecyclerView(RecyclerView rv, LoadingState state){
        switch (state){
            case LODING:
            case LOADING_FAILED:
                rv.setVisibility(View.GONE);
                break;
            case LOADING_SUCCEED:
                rv.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(rv,"alpha",0f,1f).setDuration(1000).start();
                break;
        }
    }

    @BindingAdapter("drawChart")
    public static void drawChart(LineChart chart, List<Integer> num){
        String date[] = new String[15];
        ArrayList<Entry> values = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0;i<num.size();i++){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            date[14-i] = Const.CHART_CHART_DATE.format(calendar.getTime());
            values.add(new Entry(i,num.get(14-i)));
        }

        LineDataSet set = new LineDataSet(values,"txs");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setCircleRadius(4f);
        set.setColor(Color.parseColor("#1C8FF0"));
        set.setFillColor(Color.parseColor("#1C8FF0"));
        set.setDrawHorizontalHighlightIndicator(false);
        set.setDrawVerticalHighlightIndicator(false);
        XAxis x = chart.getXAxis();
        x.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return date[(int) value];
            }
        });
        YAxis y = chart.getAxisLeft();
        y.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return ((int)value/1000)+"K";
            }
        });
        x.setAxisMaximum(14);
        LineData data = new LineData(set);
        data.setDrawValues(false);
        chart.setData(data);
    }

}
