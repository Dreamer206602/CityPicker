package com.tq.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.tq.R;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class SideLetterBar extends View {
    private static final String[] b =
            {"定位", "热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private int choose = -1;
    private Paint paint = new Paint();
    private boolean showBg = false;
    private OnLetterChangedListener onLetterChangeListener;
    private TextView overlay;

    public SideLetterBar(Context context) {
        super(context);
    }

    public SideLetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideLetterBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置悬浮的textView
     *
     * @param overlay
     */
    public void setOverlay(TextView overlay) {
        this.overlay = overlay;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBg) {
            canvas.drawColor(Color.TRANSPARENT);
        }
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setTextSize(getResources().getDimension(R.dimen.side_letter_bar_letter_size));
            paint.setColor(getResources().getColor(R.color.gray));
            paint.setAntiAlias(true);
            if (i == choose) {
                    paint.setColor(getResources().getColor(R.color.gray_deep));
            }
            float xPos=width/2-paint.measureText(b[i])/2;
            float yPos=singleHeight*i+singleHeight;
            canvas.drawText(b[i],xPos,yPos,paint);
            paint.reset();

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action=event.getAction();
        final  float y=event.getY();
        final int oldChoose=choose;
        final OnLetterChangedListener listener=onLetterChangeListener;
        final int c= (int) (y/getHeight()*b.length);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                showBg=true;
                if(oldChoose!=c&&listener!=null){
                    if(c>0&&c<b.length){
                        listener.onLetterChanged(b[c]);
                        choose=c;
                        invalidate();
                        if(overlay!=null){
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(b[c]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(oldChoose!=c&&listener!=null){
                    if(c>=0&& c<b.length){
                        listener.onLetterChanged(b[c]);
                        choose=c;
                        invalidate();
                        if(overlay!=null){
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(b[c]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBg=false;
                choose=-1;
                invalidate();
                if(overlay!=null){
                    overlay.setVisibility(GONE);
                }
                break;
        }
        return true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnLetterChangeListener(OnLetterChangedListener onLetterChangeListener) {
        this.onLetterChangeListener = onLetterChangeListener;
    }

    public interface OnLetterChangedListener{
        void onLetterChanged(String letter);
    }
}
