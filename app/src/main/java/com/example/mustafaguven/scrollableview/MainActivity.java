package com.example.mustafaguven.scrollableview;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mustafaguven.scrollableview.customviews.CenterizedViewPagerAdapter;
import com.example.mustafaguven.scrollableview.customviews.RecyclerCardViewList;
import com.example.mustafaguven.scrollableview.customviews.RecyclerCardViewListAdapter;
import com.example.mustafaguven.scrollableview.customviews.RoundedCard;
import com.example.mustafaguven.scrollableview.customviews.RoundedMessageCard;
import com.example.mustafaguven.scrollableview.customviews.VerticalCardViewList;
import com.example.mustafaguven.scrollableview.customviews.widgets.CenterizedViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rlMain)
    RelativeLayout rlMain;

    @Bind(R.id.addnew)
    Button addnew;

 /*   @Bind(R.id.lblDuration)
    DurationTextView lblDuration;*/

    @Bind(R.id.pager)
    CenterizedViewPager pager;

    RecyclerCardViewList recyclerView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        samplePager();
        sampleForVerticalCardViewList();

        //lblDuration.setDuration(3662);
        //sampleForRecyclerCardViewList();
/*        sampleForCustomCardViewList();
       */
    }

    @OnClick(R.id.addnew)
    void addNew() {
        RoundedCard r = new RoundedCard(this);
        r.findViewById(R.id.rnCard).setBackgroundColor(getRandomColor());
        CenterizedViewPagerAdapter adapter = (CenterizedViewPagerAdapter) pager.getAdapter();
        adapter.addView(r, pager);

    }

    @OnClick(R.id.remove)
    void remove(){
        CenterizedViewPagerAdapter adapter = (CenterizedViewPagerAdapter) pager.getAdapter();

        adapter.removeView(3, pager);
    }

    private void samplePager() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //RoundedMessageCard r = new RoundedMessageCard(this);
            //r.setTextName(String.format("%s nolu kart", i + 1));
            RoundedCard r = new RoundedCard(this);
            r.findViewById(R.id.rnCard).setBackgroundColor(getRandomColor());
            r.setTag(i);
            views.add(r);
        }
        CenterizedViewPagerAdapter adapter = new CenterizedViewPagerAdapter(this, views);
        //pager.setOffscreenPageLimit(100);
        pager.setAdapter(adapter);
        pager.enableCenterLockOfChilds();

    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

/*    private int convertPxToDp(int px) {
        return Math.round((float)px / (Resources.getSystem().getDisplayMetrics().xdpi / 160.0F));
    }*/

    public int convertPixelsToDp(float px){
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int)dp;
    }

    private void sampleForRecyclerCardViewList() {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            /*TextView t = new TextView(this);
            t.setText(String.valueOf(i));
            t.setTextSize(30);
            t.setGravity(Gravity.CENTER);
            views.add(t);*/
            RoundedMessageCard r = new RoundedMessageCard(this);
            r.setTextName(String.format("%s nolu kart", i + 1));
            views.add(r);
        }


        RecyclerCardViewListAdapter<View> adapter = new RecyclerCardViewListAdapter<>(this, views);
        recyclerView = new RecyclerCardViewList(this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.e("a", String.format("%s %s", dx, dy));
            }
        });

        rlMain.addView(recyclerView);
    }



    private void sampleForVerticalCardViewList() {
        final VerticalCardViewList v = (VerticalCardViewList) findViewById(R.id.myVerticalCardViewList);
        v.setOnEmpty(new VerticalCardViewList.OnEmptyListener() {
            @Override
            public void onEmpty() {
                //v.setVisibility(View.GONE);
            }
        });

        List<View> views = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
/*            *//**//*TextView t = new TextView(this);
            t.setText(String.valueOf(i));
            t.setTextSize(30);
            t.setGravity(Gravity.CENTER);
            views.add(t);*//**//**/
            RoundedMessageCard r = new RoundedMessageCard(this);
            r.setTextName(String.format("%s nolu kart", i + 1));
            views.add(r);
        }
        v.setItems(views);
    }
/*
    private void sampleForCustomCardViewList() {
        CustomCardViewList c = (CustomCardViewList) findViewById(R.id.myCustomCardViewList);
        c.setOnSelectedItemListener(new CustomCardViewList.OnSelectedItemListener() {
            @Override
            public void onSelectedItem(View v, int position) {
                //Toast.makeText(MainActivity.this, String.format("Scrolled... Position: %s, View: %s", position, v), Toast.LENGTH_SHORT).show();
                Log.e("CustomCardViewList", String.format("Scrolled... Position: %s, View: %s", position, v));
            }
        });

        c.setOnClickedItemListener(new CustomCardViewList.OnClickedItemListener() {
            @Override
            public void onClickedItem(View v, int position) {
                //Toast.makeText(MainActivity.this, String.format("Clicked... Position: %s, View: %s", position, v), Toast.LENGTH_SHORT).show();
                Log.e("CustomCardViewList", String.format("Clicked... Position: %s, View: %s", position, v));
            }
        });
        c.setItems(getViews());
    }*/

    ArrayList<View> getViews() {
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RoundedCard r = new RoundedCard(this);
            views.add(r);
        }
        return views;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
