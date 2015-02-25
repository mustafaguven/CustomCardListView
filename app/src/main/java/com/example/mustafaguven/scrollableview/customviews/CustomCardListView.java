package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mustafaguven.scrollableview.R;

import java.util.ArrayList;

/**
 * Created by MustafaGuven on 24.2.2015.
 */

public class CustomCardListView extends RelativeLayout {

    private InnerCustomHorizontalScrollView horizontalView;

    public CustomCardListView(Context context) {
        super(context);
        init(context);
    }

    public CustomCardListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomCardListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCardListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.custom_horizontal_scrollable_view, this);
        this.horizontalView = (InnerCustomHorizontalScrollView) findViewById(R.id.hvScrollable);
        this.horizontalView.setBackgroundColor(Color.CYAN);


        ArrayList<String> abc = new ArrayList<>();
        abc.add("asdkjasd");
        abc.add("asdkjasd");
        abc.add("asdkjasd");
        abc.add("asdkjasd");
        this.horizontalView.setScrollView(abc);
    }


}
