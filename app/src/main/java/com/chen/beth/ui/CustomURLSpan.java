package com.chen.beth.ui;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;

import androidx.annotation.NonNull;

public class CustomURLSpan extends URLSpan {
    public CustomURLSpan(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(Color.parseColor("#3498DB"));
        ds.setUnderlineText(false);
    }
}
