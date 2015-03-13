package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mustafaguven.scrollableview.R;

import java.util.List;

/**
 * Created by MustafaGuven on 10.3.2015.
 */
public class VerticalCardViewList extends ScrollView {

    private static final int CARD_WIDTH_SPACE = 10; //dp
    private static final int FLPLAIN_MIN_HEIGHT = 200; //dp
    private static final int CARD_HEIGHT = 150; //dp
    private static final int THRESHOLD_FOR_VISIBLE_RECORD = 6;

    private FrameLayout flPlain;
    private int mMargin;
    private List<View> mViews;


    public VerticalCardViewList(Context context) {
        super(context);
    }

    public VerticalCardViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalCardViewList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalCardViewList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int convertDpToPixel(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setItems(final List<View> views) {
        //LayoutInflater.from(getContext()).inflate(R.layout.vertical_test, this);
        mMargin = convertDpToPixel(20);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        this.mViews = views;
        flPlain = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        flPlain.setLayoutParams(params);
        flPlain.setBackgroundColor(Color.RED);




        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                fillItems();
                flPlain.setMinimumHeight(convertDpToPixel(FLPLAIN_MIN_HEIGHT));
            }
        });
        this.addView(flPlain);

    }

    private void fillItems() {
        for (int i = getUsableSize()-1; i >= 0; i--) {
            TextView v = (TextView)mViews.get(i);
            v.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.rounded_message_card_background_shape));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = getViewWidth() - (i * convertDpToPixel(CARD_WIDTH_SPACE));
            params.height = convertDpToPixel(CARD_HEIGHT);
            params.gravity = Gravity.CENTER;
            params.setMargins(0, getCardMargin(i), 0, 0);
            v.setLayoutParams(params);
            v.setClickable(true);
            v.setOnTouchListener(touchListener);
            flPlain.addView(v);
        }
    }

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    Log.e("touchListener", String.format("%s %s %s", event.getX(), event.getY(), v));
                    return true;
                default:
                    return false;
            }
        }
    };


    private int getTotalPadding(){
        return (mMargin * 8 / getDensity());
    }

    private int getViewWidth(){
        return getMeasuredWidth() - getTotalPadding();
    }

    private int getDensity(){
        return (int) getResources().getDisplayMetrics().density < 2 ? (int) getResources().getDisplayMetrics().density : 2;
    }

    private int getUsableSize(){
        return mViews.size() <= THRESHOLD_FOR_VISIBLE_RECORD ? mViews.size() : THRESHOLD_FOR_VISIBLE_RECORD;
    }



    private int getCardMargin(int index) {
       index++;
       return (mMargin * (getUsableSize() - index) / getUsableSize());

    }
}
