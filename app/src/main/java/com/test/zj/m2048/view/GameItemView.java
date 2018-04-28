package com.test.zj.m2048.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by admin on 2018/4/8.
 */

public class GameItemView extends FrameLayout {
    private TextView mTextView;
    private Point targetPosition;
    private int value=0;
    private int targetvalue=0;
    private boolean merge=false;
    private float radius = 10;

    public GameItemView(Context context) {
        super(context);
        init(context);
    }

    public GameItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        mTextView = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mTextView.setLayoutParams(layoutParams);
        mTextView.setTextSize(32);
        this.addView(mTextView);
        mTextView.setTextColor(0xFF999999);
        mTextView.setBackgroundColor(0x00000000);
        setBackgroundColor(getColor(0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int color = getColor(value);
        Paint paint = new Paint();
        paint.setColor(color);
        int h = getWidth();
        canvas.drawRoundRect(0,0,getWidth(),getHeight(),radius,radius,paint);
        super.onDraw(canvas);
    }

    //region 属性访问


    public Point getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(int x, int y) {
        if(this.targetPosition == null)
            this.targetPosition = new Point(-1,-1);
        this.targetPosition.set(x,y);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if(this.value == value)
            return;

        setContent(value);

        invalidate();
    }

    public int getTargetvalue() {
        return targetvalue;
    }

    public void setTargetvalue(int targetvalue) {
        this.targetvalue = targetvalue;
    }

    public boolean isMerge() {
        return merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    //endregion

    private void setContent(int value){
        this.value = value;
        if(value==0){
            mTextView.setText("");
        }else
        {
            mTextView.setText(value+"");
        }
    }

    public int getColor(int num){

        switch (num) {
            case 0:
                return 0x00bbbbbb;
            case 2:
                return 0xfffff8e1;
            case 4:
                return 0xffffecb3;
            case 8:
                return 0xffffe082;
            case 16:
                return 0xffffd54f;
            case 32:
                return 0xffffca28;
            case 64:
                return 0xffffc107;
            case 128:
                return 0xffffb300;
            case 256:
                return 0xffffa000;
            case 512:
                return 0xffff8f00;
            case 1024:
                return 0xffff6f00;
            case 2048:
                return 0xffe65100;
            case 4096:
                return 0xff8d6e63;
            default:
                return 0x33b0bec5;
        }
    }
}
