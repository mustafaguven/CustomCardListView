package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.mustafaguven.scrollableview.R;

/**
 * Created by MustafaGuven on 5.3.2015.
 */
public class RoundedCard extends RelativeLayout {
    public RoundedCard(Context context) {
        super(context);
        init();
    }

    public RoundedCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundedCard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.rounded_card, this);

    }

}
