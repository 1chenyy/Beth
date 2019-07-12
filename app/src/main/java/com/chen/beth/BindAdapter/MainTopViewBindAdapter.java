package com.chen.beth.BindAdapter;

import android.graphics.drawable.Drawable;

import androidx.databinding.BindingAdapter;

import com.chen.beth.UI.MainTopView;

public class MainTopViewBindAdapter {
    @BindingAdapter("imageSrc")
    public static void setImage(MainTopView view, Drawable src){
        view.setImage(src);
    }

    @BindingAdapter("title")
    public static void setTitle(MainTopView view, String title){
        view.setTitle(title);
    }

    @BindingAdapter("content")
    public static void setContent(MainTopView view, String content){
        view.setContent(content);
    }

    @BindingAdapter("line")
    public static void showLine(MainTopView view, boolean isShow){
        view.showLine(isShow);
    }

    @BindingAdapter("refresh")
    public static void showRefresh(MainTopView view, boolean isShow){
        view.showRefresh(isShow);
    }

    @BindingAdapter("stringTag")
    public static void setStringTag(MainTopView view, String tag){
        view.setStringTag(tag);
    }
}
