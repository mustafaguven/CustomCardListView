package com.example.mustafaguven.scrollableview.customviews;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mustafaguven.scrollableview.R;

import java.util.ArrayList;

/**
 * Created by MustafaGuven on 24.2.2015.
 */
public class CustomCardViewList extends HorizontalScrollView {

    private final int THRESHOLD = 3;
    private final int DURATION = 250;
    private final int MIN_ALPHA = 50;
    private final int MAX_ALPHA = 255;
    private int mActiveItemIndex = 0;
    ImageView preView, currentView, nextView;
    private LinearLayout lnCardPlain;


    public interface OnEndScrollListener {
        public void onEndScroll();
    }

    private boolean mIsFling;
    private OnEndScrollListener mOnEndScrollListener;

    public CustomCardViewList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setOnTouchListener(touchListener);
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.custom_card_view_list, this);
        lnCardPlain = (LinearLayout) findViewById(R.id.lnCardPlain);

        currentView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex);
        nextView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex+1);

        scrollIt();
    }


    public CustomCardViewList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCardViewList(Context context) {
        super(context);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCardViewList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void playCurrentViewAnimator(View view, float quotient, int alpha){
        ObjectAnimator animScrollX=ObjectAnimator.ofInt(this, "scrollX", (int)view.getX());
        animScrollX.setDuration(DURATION);
        ObjectAnimator animScaleX =ObjectAnimator.ofFloat(view, "scaleX", quotient);
        animScaleX.setDuration(DURATION);
        ObjectAnimator animScaleY=ObjectAnimator.ofFloat(view, "scaleY", quotient);
        animScaleY.setDuration(DURATION);
        ObjectAnimator animAlpha=ObjectAnimator.ofInt(view, "imageAlpha", alpha);
        animAlpha.setDuration(DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animScrollX).with(animScaleX).with(animScaleY).with(animAlpha);
        animatorSet.start();
    }

    private void playNextViewAnimator(View view, float quotient, int alpha){
        ObjectAnimator animScaleX =ObjectAnimator.ofFloat(view, "scaleX", quotient);
        animScaleX.setDuration(DURATION);
        ObjectAnimator animScaleY=ObjectAnimator.ofFloat(view, "scaleY", quotient);
        animScaleY.setDuration(DURATION);
        ObjectAnimator animAlpha=ObjectAnimator.ofInt(view, "imageAlpha", alpha);
        animAlpha.setDuration(DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animScaleX).with(animScaleY).with(animAlpha);
        animatorSet.start();
    }

    private void scrollIt() {
        /*currentView.setImageAlpha(255);*/
        playCurrentViewAnimator(currentView, 1f, MAX_ALPHA);
        if(nextView!=null) {
            playNextViewAnimator(nextView, 0.75f, MIN_ALPHA);
        }
        Log.e("scrollIt", String.format("%s %s %s %s", "finished", currentView!=null, nextView!=null, mActiveItemIndex));
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        findActiveItem();
        //Log.e("scrollIt", String.format("%s %s %s %s", l, oldl, Math.abs(l - oldl), mIsFling));
        if (mIsFling) {
            //Log.e("scrollIt", String.format("%s %s %s", l, oldl, Math.abs(l - oldl)));

            if (Math.abs(l - oldl) < THRESHOLD) {
                scrollIt();

                if (mOnEndScrollListener != null) {
                    mOnEndScrollListener.onEndScroll();
                }
                mIsFling = false;
            } else {
                //Log.e("scrollIt", "scrolling");
            }
        }

        float alpha = l / 360f * 255f;
       // Log.e("",String.format("%s %s", l, alpha));
        if(alpha>50f && alpha < 255f) {

             nextView.setImageAlpha((int) alpha);
            //nextView.setScaleX(nextView.getScaleX() * 2 * alpha/255f);
            //nextView.setScaleY(nextView.getScaleY() * 2 * alpha/255f);
            //Log.e("scaleX",String.format("%s", nextView.getScaleY() * 2 * alpha/255f));
        }

    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX);
        Log.e("fling", "true");
        mIsFling = true;
    }

    private void findActiveItem() {
        double scrollX = getScrollX();
        //double horizontalViewVisibleWidth = v.getMeasuredWidth();
        double horizontalViewVisibleWidth = 1720;

        // mActiveItemIndex = (int)Math.round (scrollX/horizontalViewVisibleWidth);
        mActiveItemIndex = (int) Math.round(scrollX / 360);

        ImageView preView = null;
        if(mActiveItemIndex>0) {
            preView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex-1);
        }
        nextView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex+1);

        //double scrollX = getScrollX();
        //double horizontalViewVisibleWidth = v.getMeasuredWidth();
        double ceil = scrollX/horizontalViewVisibleWidth;
        //Log.e("ceil", String.format("scrollx: %s -- ceil: %s -- mActiveItemIndex: %s",
                //String.valueOf(scrollX), String.valueOf(ceil + "-" + Math.round(ceil)), String.valueOf(mActiveItemIndex)));
        //int scrollTo = mActiveItemIndex * horizontalViewVisibleWidth;

        currentView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex);
        if(currentView==null)
            currentView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex-1);

    }


    OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                findActiveItem();
                scrollIt();
                return true;
            }
            else{
                return false;
            }
        }
    };




    public OnEndScrollListener getOnEndScrollListener() {
        return mOnEndScrollListener;
    }

    public void setOnEndScrollListener(OnEndScrollListener mOnEndScrollListener) {
        this.mOnEndScrollListener = mOnEndScrollListener;
    }

















    public void setScrollView(ArrayList items){

        //LinearLayout internalWrapper = new LinearLayout(getContext());
        //internalWrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //internalWrapper.setOrientation(LinearLayout.HORIZONTAL);
        //addView(internalWrapper);
       /* this.mItems = items;
        for(int i = 0; i< items.size();i++){
            LinearLayout featureLayout = (LinearLayout) View.inflate(this.getContext(), R.layout.homefeature, null);

            internalWrapper.addView(featureLayout);
        }*/
    }
}


