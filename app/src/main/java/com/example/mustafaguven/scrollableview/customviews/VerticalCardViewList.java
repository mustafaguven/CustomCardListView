package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mustafaguven.scrollableview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MustafaGuven on 10.3.2015.
 */
public class VerticalCardViewList extends ScrollView {

    private static final int CARD_WIDTH_SPACE = 10; //dp
    private static final int FLPLAIN_MIN_HEIGHT = 200; //dp
    private static final int CARD_HEIGHT = 150; //dp

    private FrameLayout flPlain;
    private final int MARGIN = 150; //dp


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
        flPlain = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, convertDpToPixel(20), 0, 0);
        flPlain.setLayoutParams(params);
        //flPlain.setBackgroundColor(Color.RED);

        this.setOnTouchListener(touchListener);
        fillItems(views);


        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                flPlain.setMinimumHeight(convertDpToPixel(FLPLAIN_MIN_HEIGHT));
            }
        });
        this.addView(flPlain);

    }

    private void fillItems(List<View> views) {
        for (int i = views.size()-1; i >= 0; i--) {
            TextView v = (TextView)views.get(i);
            v.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.rounded_message_card_background_shape));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = getCardWidth() - (i * convertDpToPixel(CARD_WIDTH_SPACE));
            params.height = convertDpToPixel(CARD_HEIGHT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, getFixedMargin(i, views.size()), 0, 0);
            v.setLayoutParams(params);
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

    public int getCardWidth() {
        return 1200;
    }

    public int getFixedMargin(int index, int itemCount) {
        index++;
        int a =  (MARGIN * (itemCount - index) / itemCount);
        Log.e("", String.format("%s %s", a, index));
        return a;
    }
}
