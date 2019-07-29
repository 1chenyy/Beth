package com.chen.beth.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Dimension;

public class TopViewFactory implements ViewSwitcher.ViewFactory {
    private Context context;
    public TopViewFactory(Context context){
        this.context = context;
    }
    @Override
    public View makeView() {
        TextView tv = new TextView(context);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(Dimension.SP,16);
        return tv;
    }
}
