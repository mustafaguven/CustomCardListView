package com.example.mustafaguven.scrollableview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mustafaguven.scrollableview.customviews.CustomCardViewList;
import com.example.mustafaguven.scrollableview.customviews.RoundedCard;
import com.example.mustafaguven.scrollableview.customviews.RoundedMessageCard;
import com.example.mustafaguven.scrollableview.customviews.VerticalCardViewList;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleForCustomCardViewList();
        sampleForVerticalCardViewList();
    }

    private void sampleForVerticalCardViewList() {
        final VerticalCardViewList v = (VerticalCardViewList) findViewById(R.id.myVerticalCardViewList);
        v.setOnEmpty(new VerticalCardViewList.OnEmptyListener() {
            @Override
            public void onEmpty() {
                v.setVisibility(View.GONE);
            }
        });

        List<View> views = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            /*TextView t = new TextView(this);
            t.setText(String.valueOf(i));
            t.setTextSize(30);
            t.setGravity(Gravity.CENTER);
            views.add(t);*/
            RoundedMessageCard r = new RoundedMessageCard(this);
            r.setTextName(String.format("%s nolu kart", i+1));
            views.add(r);
        }
        v.setItems(views);
    }

    private void sampleForCustomCardViewList(){
        CustomCardViewList c = (CustomCardViewList)findViewById(R.id.myCustomCardViewList);
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
    }

    ArrayList<View> getViews(){
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
}
