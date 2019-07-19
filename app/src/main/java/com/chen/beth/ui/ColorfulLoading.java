package com.chen.beth.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static com.chen.beth.Utils.BaseUtil.dpToPx;

public class ColorfulLoading extends View {

    public static final int[] COLORS = { Color.parseColor("#FE4365"), Color.parseColor("#DE7D2C")
            , Color.parseColor("#458994"), Color.parseColor("#23EBBA")
            , Color.parseColor("#3EBCCA"), Color.parseColor("#773460")};
    private static final int SHADOW_COLOR = Color.parseColor("#1a000000");
    private static final int DEFAULT_WIDTH = 4;
    private static final int DEFAULT_SHADOW_POSITION = 2;
    private static final int DEFAULT_SPEED_OF_DEGREE = 7;
    private static final int MAX_DEGREE = 360;
    private static final int MAX = 160;
    private static final int MIN = 10;

    private Context mContext;
    private float arc,speedOfArc;
    private RectF loadingRect,shadowRect;
    private int width,shadowWidth,speedOfDegree;
    private int topDegree = 10,bottomDegree = 190;
    private Paint mPaint,mShadowPaint;
    private boolean circleFlag = true, colorFlag = false;
    private int flag = 0;

    public ColorfulLoading(Context context) {
        this(context,null);
    }

    public ColorfulLoading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorfulLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ColorfulLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        this.mContext = context;
        init();
    }

    private void init(){
        width = dpToPx( DEFAULT_WIDTH);
        shadowWidth = dpToPx(DEFAULT_SHADOW_POSITION);
        speedOfDegree = DEFAULT_SPEED_OF_DEGREE;
        speedOfArc = speedOfDegree / 4;

        mPaint = new Paint();
        mPaint.setColor(COLORS[0]);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(width);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mShadowPaint = new Paint();
        mShadowPaint.setColor(SHADOW_COLOR);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setStrokeWidth(width);
        mShadowPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        arc = MIN;
        loadingRect = new RectF(2 * width, 2 * width, w - 2 * width, h - 2 * width);
        shadowRect = new RectF(2 * width + shadowWidth, 2 * width + shadowWidth, w - 2 * width + shadowWidth, h - 2 * width + shadowWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(shadowRect, topDegree, arc, false, mShadowPaint);
        canvas.drawArc(shadowRect, bottomDegree, arc, false, mShadowPaint);

        canvas.drawArc(loadingRect, topDegree, arc, false, mPaint);
        canvas.drawArc(loadingRect, bottomDegree, arc, false, mPaint);

        topDegree += speedOfDegree;
        bottomDegree += speedOfDegree;

        if (topDegree > MAX_DEGREE) {
            topDegree = topDegree - MAX_DEGREE;
        }
        if (bottomDegree > MAX_DEGREE) {
            bottomDegree = bottomDegree - MAX_DEGREE;
        }

        if (circleFlag) {
            if (colorFlag) {
                colorFlag = !colorFlag;
                flag++;
                mPaint.setColor(COLORS[flag % COLORS.length]);
            }
            if (arc < MAX) {
                arc += speedOfArc;
                invalidate();
            }
        } else {
            if (!colorFlag) {
                colorFlag = !colorFlag;
            }
            if (arc > MIN) {
                arc -= 2 * speedOfArc;
                invalidate();
            }
        }
        if (arc >= MAX || arc <= MIN) {
            circleFlag = !circleFlag;
            invalidate();
        }
    }

}
