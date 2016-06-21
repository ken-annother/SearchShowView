package com.ugoutech.searchshowview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


/**
 * @描述: 允许设置最小宽度的Linearlayout
 * @项目名: SearchShowView
 * @包名: com.ugoutech.searchshowview
 * @类名:
 * @作者: soongkun
 * @创建时间: 2016/4/29 13:16
 */

public class ExLinearlayout extends LinearLayout {


    private int mTotalWidth;
    private int mTotalHeigth;

    //搜索框的保留宽度
    private int editTextRetainWidth;


    public ExLinearlayout(Context context) {
        super(context);
    }


    public ExLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ExLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mTotalWidth = MeasureSpec.getSize(widthMeasureSpec);
        mTotalHeigth = MeasureSpec.getSize(heightMeasureSpec);
        editTextRetainWidth = (int) (mTotalWidth * 0.2);

        if(getChildCount() != 2){
            return;

        }else{
            View childAt0 = getChildAt(0);
            View childAt1 = getChildAt(1);

            int childAt0Width = childAt0.getMeasuredWidth();

            if(mTotalWidth - childAt0Width < editTextRetainWidth){      //保留宽度将过小
                childAt0.measure(
                        MeasureSpec.makeMeasureSpec(mTotalWidth - editTextRetainWidth,MeasureSpec.EXACTLY),
                        0
                );

                childAt1.measure(
                        MeasureSpec.makeMeasureSpec(editTextRetainWidth,MeasureSpec.EXACTLY),
                        0
                );
            }
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(getClass().getSimpleName(),"孩子有" + getChildCount());

        if(getChildCount() != 2){
            super.onLayout(changed,l,t,r,b);
            return;

        }else{
            View childAt0 = getChildAt(0);
            View childAt1 = getChildAt(1);

            int childAt0Width = childAt0.getMeasuredWidth();

            if(mTotalWidth - childAt0Width < editTextRetainWidth){      //保留宽度将过小

                childAt0.layout(l,t,l + mTotalWidth - editTextRetainWidth,b);
                childAt1.layout(r-editTextRetainWidth,t,r,b);
            }else{
                super.onLayout(changed,l,t,r,b);
            }
        }

    }
}
