package com.example.mustafaguven.scrollableview.customviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


/**
 * Created by mustafaguven on 08.12.2015.
 */
public class RecyclerCardViewList extends RecyclerView {

    public RecyclerCardViewList(Context context) {
        super(context);
        this.addOnScrollListener(scrollListener);
    }

    OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.e("asdasd", String.format("%s ", dx));
        }
    };

/*    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.e("asdasd", String.format("%s ", velocityX + velocityY));
        return super.fling(velocityX, velocityY);
    }*/
}
