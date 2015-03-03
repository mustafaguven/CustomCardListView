package com.example.mustafaguven.scrollableview.customviews;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
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
    private float MIN_SCALE = 0.75f;
    private float MAX_SCALE = 1f;
    private int mMargin;
    private LinearLayout lnCardPlain;
    private boolean mIsFling;

    View previousCard, currentCard, nextCard;

    /*
    ******* Listeners ************************************************
    */
    private OnEndScrollListener mOnEndScrollListener;
    private OnSelectedItemListener mOnSelectedItemListener;

    public interface OnSelectedItemListener{
        public void onSelectedItem(View v, int position);
    }

    public interface OnEndScrollListener {
        public void onEndScroll();
    }
    // ***************************************************************




    public CustomCardViewList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    private void init() {
        mMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setOnTouchListener(touchListener);
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.custom_card_view_list, this);
        lnCardPlain = (LinearLayout) findViewById(R.id.lnCardPlain);

        this.getViewTreeObserver().
                addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        findActiveItem();
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
        for (int i = 0; i < lnCardPlain.getChildCount(); i++) {
            if(i!=mActiveItemIndex) {
                View view = lnCardPlain.getChildAt(i);
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
                view.setAlpha(MIN_ALPHA);
            }
        }

        int gotoScroll = 0;
        if(mActiveItemIndex > 0)
            gotoScroll = (int)currentCard.getX() - ((getMeasuredWidth() - currentCard.getWidth()) / 2);

        ObjectAnimator animScrollX=ObjectAnimator.ofInt(this, "scrollX", gotoScroll);
        animScrollX.setDuration(DURATION);
        ObjectAnimator animScaleX =ObjectAnimator.ofFloat(currentCard, "scaleX", quotient);
        animScaleX.setDuration(DURATION);
        ObjectAnimator animScaleY=ObjectAnimator.ofFloat(currentCard, "scaleY", quotient);
        animScaleY.setDuration(DURATION);
        ObjectAnimator animAlpha=ObjectAnimator.ofInt(currentCard, "imageAlpha", alpha);
        animAlpha.setDuration(DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animScrollX).with(animScaleX).with(animScaleY).with(animAlpha);
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
        animatorSet.play(animAlpha).with(animScaleY).with(animScaleX);
        animatorSet.start();
    }

    private void scrollIt() {
        Log.e("", "girdi");
        if(currentCard !=null) {
            if(mOnSelectedItemListener!=null){
                mOnSelectedItemListener.onSelectedItem(currentCard, mActiveItemIndex);
            }
            playCurrentViewAnimator(MAX_SCALE, (int) MAX_ALPHA);
            if (previousCard != null) {
                playNextViewAnimator(previousCard, MIN_SCALE, (int) MIN_ALPHA);
            }
            if (nextCard != null) {
                playNextViewAnimator(nextCard, MIN_SCALE, (int) MIN_ALPHA);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        findActiveItem();

        if (mIsFling) {
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
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX);
        mIsFling = true;
    }

    private void findActiveItem() {
        setViews();
        double viewWidth = currentCard.getWidth()/2;
        double sonuc = (getScrollX() - viewWidth) / (2*viewWidth);
        mActiveItemIndex = (int)Math.ceil(sonuc);
        Log.e("", String.format("%s %s %s %s", getScrollX(), sonuc, viewWidth, mActiveItemIndex));
    }

    int getViewWidth(){
        return getMeasuredWidth() - (mMargin * 8 / getDensity());
    }

    int getDensity(){
        return (int) getResources().getDisplayMetrics().density < 2 ? (int) getResources().getDisplayMetrics().density : 2;
    }

    void setViews(){
        currentCard = lnCardPlain.getChildAt(mActiveItemIndex);
        if(currentCard != null) {


            previousCard = null;
            if (mActiveItemIndex > 0) {
                previousCard = lnCardPlain.getChildAt(mActiveItemIndex - 1);
            }
            nextCard = lnCardPlain.getChildAt(mActiveItemIndex + 1);

            setWidthForCard(currentCard, previousCard, nextCard);
            setMargins();
        }
    }

    void setWidthForCard(View... views){
        for (View v : views) {
            if(v!=null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v.getLayoutParams());
                params.width = getViewWidth();
                params.setMargins(0, mMargin, 0, mMargin);
                params.gravity = Gravity.CENTER;
                v.setLayoutParams(params);
            }
        }
    }

    private void setMargins() {
        //while the first item is showing
        if (lnCardPlain != null && lnCardPlain.getChildCount() > 0) {
            View firstItem = lnCardPlain.getChildAt(0);
            if (firstItem != null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(firstItem.getLayoutParams());
                params.setMargins((getMeasuredWidth() - params.width) / 2, mMargin, 0, mMargin);
                params.gravity = Gravity.CENTER;
                firstItem.setLayoutParams(params);
            }

            // while the latest item is showing
            if (lnCardPlain.getChildCount() > 1) {
                View lastItem = lnCardPlain.getChildAt(lnCardPlain.getChildCount() - 1);
                if (lastItem != null) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(lastItem.getLayoutParams());
                    //below line should be put otherwise last item can not be selectable by the operator
                    params.width = getViewWidth() + 1;
                    params.setMargins(0, mMargin, (getMeasuredWidth() - params.width) / 2, mMargin);
                    params.gravity = Gravity.CENTER;
                    lastItem.setLayoutParams(params);
                }
            }
        }
    }

    OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case  MotionEvent.ACTION_UP:
                case  MotionEvent.ACTION_CANCEL:
                    findActiveItem();
                    scrollIt();
                    return true;
                default:
                    return false;
            }
        }
    };

    public OnEndScrollListener getOnEndScrollListener() {
        return mOnEndScrollListener;
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        this.mOnEndScrollListener = onEndScrollListener;
    }

    public OnSelectedItemListener getOnSelectedItemListener() {
        return mOnSelectedItemListener;
    }

    public void setOnSelectedItemListener(OnSelectedItemListener onSelectedItemListener) {
        this.mOnSelectedItemListener = onSelectedItemListener;
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


