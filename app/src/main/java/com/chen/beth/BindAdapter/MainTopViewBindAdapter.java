package com.chen.beth.BindAdapter;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class MainTopViewBindAdapter {

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

    @BindingAdapter("isShow")
    public static void showRefresh(ImageButton view, boolean isShow){
        view.clearAnimation();
        view.setEnabled(true);
        view.setVisibility(isShow? View.VISIBLE:View.GONE);
    }

}
