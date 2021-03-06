package com.example.mustafaguven.scrollableview.customviews;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.mustafaguven.scrollableview.customviews.widgets.CenterizedViewPager;

import java.util.List;

/**
 * Created by mustafaguven on 08.12.2015.
 */
public class CenterizedViewPagerAdapter<T extends View> extends PagerAdapter {

    Context mContext;
    List<T> views;

    public CenterizedViewPagerAdapter(Context context, List<T> views){
        this.mContext = context;
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public float getPageWidth(int position) {
        return 0.8f;
    }


    public void addView(T view, CenterizedViewPager pager) {
        views.add(view);
        notifyDataSetChanged();
        pager.setCurrentItem(getCount(), true);
    }

    public void removeView(int index, CenterizedViewPager pager){
        views.remove(index);

        notifyDataSetChanged();
        int position = getCount();
        if(index < getCount() -1){
            position = index;
        }
        pager.setCurrentItem(position, true);
    }

    public void removeView(T view, CenterizedViewPager pager){
        int index = views.indexOf(view);
        views.remove(view);
        notifyDataSetChanged();
        int position = getCount();
        if(index < getCount() -1){
            position = index;
        }
        pager.setCurrentItem(position, true);
    }



}
