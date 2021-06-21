package com.apowersoft.wxmultireceiver;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * author : Terry.Tao
 * date   : 2020/1/16
 * desc   :
 */
public class MirrorGrideView extends FrameLayout {
    private int w;
    private int h;

    public MirrorGrideView(@NonNull Context context) {
        super(context);
    }

    public MirrorGrideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MirrorGrideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (w == 0 || h == 0) {
            w = getMeasuredWidth();
            h = getMeasuredHeight();
//        }
        int childCount = getChildCount();
        //2行，拆分多少列会有多余
        int i1 = childCount / 2;
        int i2 = childCount % 2;
        //多少列
        int cloum = i1;
        if (i2 > 0) {
            cloum = i1 + 1;
        }

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int itemWidth = 0;
            int itemHeight = 0;
            if (childCount <= 2) {
                itemWidth = (w - getPaddingRight() - getPaddingLeft()) / childCount;
                itemHeight = h - getPaddingTop() - getPaddingBottom();
            } else {
                //两行
                itemHeight = (h - getPaddingTop() - getPaddingBottom()) / 2;
                if ((i + 1) <= cloum) {
                    //第一行
                    itemWidth = (w - getPaddingRight() - getPaddingLeft()) / cloum;
                } else {
                    //第二行
                    itemWidth = (w - getPaddingRight() - getPaddingLeft()) / (childCount - cloum);
                }
            }
            childView.getLayoutParams().width = itemWidth;
            childView.getLayoutParams().height = itemHeight;
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        if (w == 0 || h == 0) {
            w = getMeasuredWidth();
            h = getMeasuredHeight();
//        }
        int childCount = getChildCount();
        //2行，拆分多少列会有多余
        int i1 = childCount / 2;
        int i2 = childCount % 2;
        //多少列
        int cloum = i1;
        if (i2 > 0) {
            cloum = i1 + 1;
        }

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int l = 0;
            int t = 0;
            int r = 0;
            int b = 0;

            int itemWidth = 0;
            int itemHeight = 0;
            if (childCount <= 2) {
                itemWidth = (w - getPaddingRight() - getPaddingLeft()) / childCount;
                itemHeight = h - getPaddingTop() - getPaddingBottom();
                //只有一行
                l = itemWidth * i + getPaddingLeft();
                t = getPaddingTop();
                r = l + itemWidth * (i + 1);
                b = t + itemHeight;

            } else {
                //两行
                itemHeight = (h - getPaddingTop() - getPaddingBottom()) / 2;
                if ((i + 1) <= cloum) {
                    //第一行
                    itemWidth = (w - getPaddingRight() - getPaddingLeft()) / cloum;

                    l = itemWidth * i + getPaddingLeft();
                    t = getPaddingTop();
                    r = l + itemWidth * (i + 1);
                    b = t + itemHeight;

                } else {
                    //第二行
                    itemWidth = (w - getPaddingRight() - getPaddingLeft()) / (childCount - cloum);
                    l = itemWidth * (i - cloum) + getPaddingLeft();
                    t = itemHeight + getPaddingTop();
                    r = l + itemWidth * (i - cloum + 1);
                    b = t + itemHeight;
                }
            }
            //子view进行布局
            childView.layout(l, t, r, b);
        }
    }

}
