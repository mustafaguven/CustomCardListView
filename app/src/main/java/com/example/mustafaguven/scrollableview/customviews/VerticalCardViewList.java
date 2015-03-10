package com.example.mustafaguven.scrollableview.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.example.mustafaguven.scrollableview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MustafaGuven on 10.3.2015.
 */
public class VerticalCardViewList extends ScrollView {

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

    public void setItems() {
        LayoutInflater.from(getContext()).inflate(R.layout.vertical_test, this);

    }

    public List<View> getItems() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Button b = new Button(getContext());
            views.add(b);
        }
        return views;
    }
}
