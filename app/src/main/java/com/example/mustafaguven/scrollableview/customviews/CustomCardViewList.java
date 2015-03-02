package com.example.mustafaguven.scrollableview.customviews;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
    private final float MIN_ALPHA = 50f;
    private final float MAX_ALPHA = 255f;
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

        this.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < 16) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        scrollIt();
                    }
                });
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

    private void playCurrentViewAnimator(float quotient, int alpha){
        int factor = (this.getMeasuredWidth() - currentView.getWidth()) / 2;
        Resources r = getResources();
        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        int gotoScroll = mActiveItemIndex * (currentView.getWidth()+padding - factor);

        //Log.e("", String.format("%s %s", factor, currentView.getScrollX()));
        ObjectAnimator animScrollX=ObjectAnimator.ofInt(this, "scrollX", gotoScroll);
        animScrollX.setDuration(DURATION);
        ObjectAnimator animScaleX =ObjectAnimator.ofFloat(currentView, "scaleX", quotient);
        animScaleX.setDuration(DURATION);
        ObjectAnimator animScaleY=ObjectAnimator.ofFloat(currentView, "scaleY", quotient);
        animScaleY.setDuration(DURATION);
        ObjectAnimator animAlpha=ObjectAnimator.ofInt(currentView, "imageAlpha", alpha);
        animAlpha.setDuration(DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animScrollX)
                .with(animScaleX).with(animScaleY)
                .with(animAlpha);
        animatorSet.start();
    }

    private void playNextViewAnimator(View v, float quotient, int alpha){

        ObjectAnimator animScaleX =ObjectAnimator.ofFloat(v, "scaleX", quotient);
        animScaleX.setDuration(DURATION);
        ObjectAnimator animScaleY=ObjectAnimator.ofFloat(v, "scaleY", quotient);
        animScaleY.setDuration(DURATION);
        ObjectAnimator animAlpha=ObjectAnimator.ofInt(v, "imageAlpha", alpha);
        animAlpha.setDuration(DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animAlpha)
        .with(animScaleY).with(animScaleX)
        ;
        animatorSet.start();
    }

    private void scrollIt() {
        playCurrentViewAnimator(1f, (int)MAX_ALPHA);
        if(preView!=null) {
            playNextViewAnimator(preView, 0.75f, (int)MIN_ALPHA);
        }
        if(nextView!=null) {
            playNextViewAnimator(nextView, 0.75f, (int)MIN_ALPHA);
        }
        //Log.e("scrollIt", String.format("%s %s %s %s", "finished", currentView!=null, nextView!=null, mActiveItemIndex));
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

        float alpha = l / 360f * MAX_ALPHA;
        if(alpha>MIN_ALPHA && alpha < MAX_ALPHA) {
            nextView.setImageAlpha((int) alpha);
        }

    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX);
        mIsFling = true;
    }

    private void findActiveItem() {

        //mActiveItemIndex = (int) Math.round(scrollX / getMeasuredWidth());
        mActiveItemIndex = Math.round(getScrollX() / 360);

        Log.e("", String.format("%s %s", getScrollX(), mActiveItemIndex));


        currentView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex);
        if(currentView==null)
            currentView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex - 1);

        preView = null;
        if(mActiveItemIndex>0) {
            preView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex-1);
        }
        nextView = (ImageView) lnCardPlain.getChildAt(mActiveItemIndex+1);
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


