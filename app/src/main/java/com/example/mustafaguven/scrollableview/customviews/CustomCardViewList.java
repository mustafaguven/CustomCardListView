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
    private int mPadding, mMargin;
    Button previousCard, currentCard, nextCard;
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
        mPadding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
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
                Button view = (Button) lnCardPlain.getChildAt(i);
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
                view.setAlpha((int) MIN_ALPHA);
            }
        }

        int gotoScroll = 0;
        if(mActiveItemIndex>0)
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
            playCurrentViewAnimator(1f, (int) MAX_ALPHA);
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
      //  mActiveItemIndex = getScrollX() / (getMeasuredWidth()/2);

        double x = Math.ceil((getScrollX() - (currentCard.getWidth()/2))/currentCard.getWidth());

        double viewWidth = currentCard.getWidth()/2;
        double sonuc = (getScrollX() - viewWidth) / (2*viewWidth);
        mActiveItemIndex = (int)Math.ceil(sonuc);
        Log.e("", String.format("%s %s %s %s", getScrollX(), sonuc, currentCard.getWidth(), mActiveItemIndex));


        //while the first item is showing
        if(mActiveItemIndex == 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(currentCard.getLayoutParams());
            //params.width = getMeasuredWidth() - (mMargin*8/getDensity());
            //params.setMargins(0, mMargin, 0, mMargin);
            params.setMargins((getMeasuredWidth() - params.width) / 2, mMargin, 0, mMargin);
            params.gravity = Gravity.CENTER;
            currentCard.setLayoutParams(params);
        }

        // while the latest item is showing
        if(mActiveItemIndex >= lnCardPlain.getChildCount()){
            mActiveItemIndex = lnCardPlain.getChildCount()-1;
            setViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0,0,(getMeasuredWidth() - currentCard.getWidth()) / 2, 0);
            params.gravity = Gravity.CENTER;
            currentCard.setLayoutParams(params);
        }
    }

    void setWidthForCard(View... views){
        for (View v : views) {
            if(v!=null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(currentCard.getLayoutParams());
                params.width = getMeasuredWidth() - (mMargin*8/getDensity());
                params.setMargins(0, mMargin, 0, mMargin);
                params.gravity = Gravity.CENTER;
                v.setLayoutParams(params);
            }
        }

    }

    int getDensity(){
        return (int) getResources().getDisplayMetrics().density < 2 ? (int) getResources().getDisplayMetrics().density : 2;
    }


    void setViews(){
        currentCard = (Button) lnCardPlain.getChildAt(mActiveItemIndex);
        previousCard = null;
        if(mActiveItemIndex>0) {
            previousCard = (Button) lnCardPlain.getChildAt(mActiveItemIndex-1);
        }
        nextCard = (Button) lnCardPlain.getChildAt(mActiveItemIndex+1);

        setWidthForCard(currentCard, previousCard, nextCard);
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


