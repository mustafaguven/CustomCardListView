package com.example.mustafaguven.scrollableview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafaguven.scrollableview.customviews.CustomCardViewList;
import com.example.mustafaguven.scrollableview.customviews.RoundedCard;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        for (int i = 0; i < 150; i++) {

            RoundedCard r = new RoundedCard(this);
            /*TextView a = new TextView(this);
            a.setBackgroundColor(Color.RED);
            a.setTextSize(50);
            a.setGravity(Gravity.CENTER);
            a.setText(String.valueOf(i));
            views.add(a);*/
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
