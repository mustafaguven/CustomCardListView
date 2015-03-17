package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.mustafaguven.scrollableview.R;

import java.util.List;

/**
 * Created by MustafaGuven on 10.3.2015.
 */
public class VerticalCardViewList extends FrameLayout {

    private static final int CARD_DEPTH_SPACE = 10; //dp
    private static final int FLPLAIN_MIN_HEIGHT = 200; //dp
    private static final int CARD_HEIGHT = 200; //dp
    private static final int THRESHOLD_FOR_VISIBLE_RECORD = 6;
    private final int DURATION = 250;
    private static final int TOTAL_PADDING = 36; //dp

    private FrameLayout flPlain;
    private int mMargin;
    private List<View> mViews;
    private View mDraggingItem;
    private boolean mIsViewOutOfBounds = false; //created for bounds checking while dragging to understand whether mDraggingItem is out of bounds or not.



    private OnEmptyListener mOnEmpty;


    public interface OnEmptyListener{
        public void onEmpty();
    }


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
        mViews = views;
        flPlain = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        //flPlain.setPadding(0, convertDpToPixel(25), 0, convertDpToPixel(25));
        flPlain.setLayoutParams(params);

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
        flPlain.removeAllViews();
        for (int i = getUsableSize()-1; i >= 0; i--) {
            View v = mViews.get(i);
            v.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.rounded_message_card_background_shape));
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.width = getViewWidth() - (i * convertDpToPixel(CARD_DEPTH_SPACE));
            params.height = convertDpToPixel(CARD_HEIGHT);
            params.gravity = Gravity.CENTER;
            params.setMargins(0, getCardMargin(i), 0, 0);
            v.setLayoutParams(params);
            v.setClickable(true);
            if(i==0) {
                v.setOnTouchListener(touchListener);
                v.setOnDragListener(dragListener);
            }
            flPlain.addView(v);
        }

        if(getUsableSize()==0 && mOnEmpty!=null){
            mOnEmpty.onEmpty();
        }
    }

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mIsViewOutOfBounds = false;
                mDraggingItem = v;
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);

                return true;
            } else {
                return false;
            }
        }
    };

    OnDragListener dragListener = new OnDragListener() {
        @Override
        public boolean onDrag(final View v, DragEvent event) {
            if(event.getAction()==DragEvent.ACTION_DRAG_STARTED){
                v.setVisibility(View.INVISIBLE);
            } else if (event.getAction() == DragEvent.ACTION_DRAG_LOCATION){
                mIsViewOutOfBounds = false;
            }
            else if (event.getAction() == DragEvent.ACTION_DRAG_EXITED){
                mIsViewOutOfBounds = true;
            }
            else if(event.getAction() == DragEvent.ACTION_DRAG_ENDED){
                mDraggingItem = (View) event.getLocalState();
                mDraggingItem.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mIsViewOutOfBounds) {
                            mViews.remove(mDraggingItem);
                            fillItems();
                        } else {
                            Log.e("OutofBound", "view is out of bound  ");
                            mDraggingItem.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            return true;
        }
    };

    private int getTotalPadding(){
        return convertDpToPixel(TOTAL_PADDING);
    }

    private int getViewWidth(){
        return getMeasuredWidth() - getTotalPadding();
    }

    //important: it holds only the item size which is going to be shown to the user, NOT the whole item count.
    private int getUsableSize(){
        return mViews.size() <= THRESHOLD_FOR_VISIBLE_RECORD ? mViews.size() : THRESHOLD_FOR_VISIBLE_RECORD;
    }

    private int getCardMargin(int index) {
       index++;
       return (mMargin * (getUsableSize() - index) / getUsableSize());
    }

    public void setOnEmpty(OnEmptyListener onEmpty) {
        this.mOnEmpty = onEmpty;
    }
}
