package com.chen.beth.UI;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chen.beth.BethApplication;
import com.chen.beth.R;
import com.chen.beth.Utils.BaseUtil;

public class MainTopView extends RelativeLayout {
    private ValueAnimator contentAnimator = ValueAnimator.ofFloat(1,0,1).setDuration(800);
    private ImageView iv;
    private TextView tvTitle,tvContent;
    private View line;
    private ImageButton ibRefresh;
    private String stringTag;
    public MainTopView(Context context){
        this(context,null);
    }

    public MainTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.main_top_view,this,true);
        iv = view.findViewById(R.id.iv);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        line = findViewById(R.id.line);
        ibRefresh = findViewById(R.id.ib_refresh);
        ibRefresh.setOnClickListener(v->handleRefresh(v));
    }

    private void handleRefresh(View view){
        Intent refreshIntent = new Intent(BaseUtil.getString(R.string.action_mtv_refresh));
        refreshIntent.setPackage(BethApplication.getContext().getPackageName());
        refreshIntent.putExtra(BaseUtil.getString(R.string.extra_type),stringTag);
        LocalBroadcastManager.getInstance(BethApplication.getContext()).sendBroadcast(refreshIntent);

        RotateAnimation animation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(800);
        animation.setRepeatCount(Animation.INFINITE);
        ibRefresh.setEnabled(false);
        ibRefresh.startAnimation(animation);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setImage(Drawable src){
        iv.setImageDrawable(src);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setContent(String content){
        if (content.equals(tvContent.getText()))
            return;
        contentAnimator.addUpdateListener(a->contentAnim(a,content));
        contentAnimator.start();
    }

    public void contentAnim(ValueAnimator animator,String content){
        float value = (float) animator.getAnimatedValue();
        if (value < 0.1){
            tvContent.setText(content);
        }
        tvContent.setAlpha(value);
    }

    public void showLine(boolean isShow){
        line.setVisibility(isShow? View.VISIBLE:View.GONE);
    }

    public void showRefresh(boolean isShow){
        ibRefresh.clearAnimation();
        ibRefresh.setEnabled(true);
        ibRefresh.setVisibility(isShow? View.VISIBLE:View.GONE);
    }

    public void setStringTag(String stringTag){
        this.stringTag = stringTag;
    }
}
