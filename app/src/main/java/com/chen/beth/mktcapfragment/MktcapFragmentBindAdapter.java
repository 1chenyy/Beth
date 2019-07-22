package com.chen.beth.mktcapfragment;

import android.graphics.Color;

import androidx.databinding.BindingAdapter;

import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class MktcapFragmentBindAdapter {
    public static final int[] LABELS = new int[]{R.string.label_genesis,R.string.label_block,R.string.label_uncle};
    public static final String[] COLORS = new String[]{"#CC3399","#99CC00","#0099CC"};
    @BindingAdapter("drawMktcapChart")
    public static void drawMktcapChart(PieChart chart, List<Double> list){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0;i<list.size()-1;i++){
            entries.add(new PieEntry(list.get(i).floatValue(), BaseUtil.getString(LABELS[i])));
        }

        PieDataSet dataSet = new PieDataSet(entries,"mktcap");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setDrawValues(false);
        dataSet.setSelectionShift(10f);
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor(COLORS[0]));
        colors.add(Color.parseColor(COLORS[1]));
        colors.add(Color.parseColor(COLORS[2]));
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();
    }
}
