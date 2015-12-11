package com.example.mustafaguven.scrollableview.customviews.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.mustafaguven.scrollableview.R;


/**
 * Created by mustafaguven on 09.12.2015.
 */
public class DurationTextView extends TextView {

    private int mShowType = 0;
    Paint paint = new Paint();

    public DurationTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.durationTextView);
            if(typedArray.hasValue(R.styleable.durationTextView_showtype)){
                mShowType = typedArray.getInt(R.styleable.durationTextView_showtype, 0);
            }
            if(typedArray.hasValue(R.styleable.durationTextView_durationtext)){
                setDuration(typedArray.getInt(R.styleable.durationTextView_durationtext, 0));
            }

            //setTextColor(typedArray.getColor(R.styleable.durationTextView_show_type, Color.TRANSPARENT));
        } finally {
            if(typedArray!=null) {
                typedArray.recycle();
            }
        }
    }

    public void setDuration(int duration){
        int durationInMinutes = duration / 60;
        int minute = durationInMinutes % 60;
        int hour = durationInMinutes / 60;
        String hourText = hour > 0 ? String.format(" %s hour%s", hour, hour > 1 ? "s" : "") : "";
        String minuteText = minute > 0 ? String.format(" %s minute%s", minute, minute > 1 ? "s" : "") : "";
        String secondText = "";
        if(duration % 60 > 0){
            secondText = String.format(" %s second%s", duration % 60, duration % 60 > 1 ? "s" : "");
        }

        String totalText = hourText;
        switch (mShowType){
            case 0:
                totalText += minuteText + secondText;
                break;
            case 1:
                totalText += minuteText;
                break;
            case 2:
                totalText = String.valueOf(durationInMinutes) + " minutes";
                break;
        }
        setText(totalText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec / 2, heightMeasureSpec / 2);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF33B5E5);
        paint.setStrokeWidth(4);
        paint.setShadowLayer(4, 2, 2, 0x80000000);
        paint.setAntiAlias(true);
        canvas.drawLine(0, 0, getWidth(), getHeight(), paint);
        canvas.drawLine(0, getHeight(), getWidth(), 0, paint);
    }
}
