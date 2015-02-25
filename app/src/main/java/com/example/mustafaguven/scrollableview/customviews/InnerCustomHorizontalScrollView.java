package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mustafaguven.scrollableview.R;

import java.util.ArrayList;

/**
 * Created by MustafaGuven on 24.2.2015.
 */
public class InnerCustomHorizontalScrollView extends HorizontalScrollView {

    private int mActiveItemIndex = 0;
    ImageView preView, currentView, nextView;

    public InnerCustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InnerCustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerCustomHorizontalScrollView(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InnerCustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollView(ArrayList items){
        final LinearLayout internalWrapper = (LinearLayout) findViewById(R.id.lnCardPlain);
        currentView = (ImageView) internalWrapper.getChildAt(mActiveItemIndex);
        nextView = (ImageView) internalWrapper.getChildAt(mActiveItemIndex+1);
        scrollToNewActiveItem();
        //LinearLayout internalWrapper = new LinearLayout(getContext());
        //internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        //addView(internalWrapper);
       /* this.mItems = items;
        for(int i = 0; i< items.size();i++){
            LinearLayout featureLayout = (LinearLayout) View.inflate(this.getContext(), R.layout.homefeature, null);

            internalWrapper.addView(featureLayout);
        }*/
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                    double scrollX = getScrollX();
                    double horizontalViewVisibleWidth = v.getMeasuredWidth();

                   // mActiveItemIndex = (int)Math.round (scrollX/horizontalViewVisibleWidth);
                    mActiveItemIndex = (int) Math.round(scrollX / 360);

                    ImageView preView = null;
                    if(mActiveItemIndex>0) {
                        preView = (ImageView) internalWrapper.getChildAt(mActiveItemIndex-1);
                    }
                    nextView = (ImageView) internalWrapper.getChildAt(mActiveItemIndex+1);

                    //double scrollX = getScrollX();
                    //double horizontalViewVisibleWidth = v.getMeasuredWidth();
                    double ceil = scrollX/horizontalViewVisibleWidth;
                    Log.e("ceil", String.format("scrollx: %s -- ceil: %s -- mActiveItemIndex: %s",
                            String.valueOf(scrollX), String.valueOf(ceil + "-" + Math.round(ceil)), String.valueOf(mActiveItemIndex)));
                    //int scrollTo = mActiveItemIndex * horizontalViewVisibleWidth;

                    currentView = (ImageView) internalWrapper.getChildAt(mActiveItemIndex);
                    if(currentView==null)
                        currentView = (ImageView) internalWrapper.getChildAt(mActiveItemIndex-1);

                    scrollToNewActiveItem();
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    private void scrollToNewActiveItem() {

        if(nextView!=null){
            nextView.setImageAlpha(50);
            // nextView.setScaleX(nextView.getScaleX() / 2);
            // nextView.setScaleY(nextView.getScaleY() / 2);
        }

        smoothScrollTo((int)currentView.getX(), 0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        float alpha = l / 360f * 255f;
       // Log.e("",String.format("%s %s", l, alpha));
        if(alpha>50f && alpha < 255f) {

            nextView.setImageAlpha((int) alpha);
            //nextView.setScaleX(nextView.getScaleX() * 2 * alpha/255f);
            //nextView.setScaleY(nextView.getScaleY() * 2 * alpha/255f);
            Log.e("scaleX",String.format("%s", nextView.getScaleY() * 2 * alpha/255f));
        }

    }
}


