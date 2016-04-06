package com.tq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class WrapHeightGridView extends GridView {
    public WrapHeightGridView(Context context) {
        this(context,null);
    }

    public WrapHeightGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WrapHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
