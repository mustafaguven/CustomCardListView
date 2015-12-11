package com.example.mustafaguven.scrollableview.customviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mustafaguven.scrollableview.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by mustafaguven on 08.12.2015.
 */
public class RecyclerCardViewListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<T> views;

    public RecyclerCardViewListAdapter(Context context, List<T> views){
        this.mContext = context;
        this.views = views;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout rlView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.rounded_card, null);
        return new RoundedCardHolder(rlView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return views.size();
    }


    public void addNewCard(T r) {
        views.add(r);
        notifyDataSetChanged();
    }

    class RoundedCardHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.lblUserName)
        TextView lblUserName;

        @Bind(R.id.lblHome)
        TextView lblHome;

        public RoundedCardHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
