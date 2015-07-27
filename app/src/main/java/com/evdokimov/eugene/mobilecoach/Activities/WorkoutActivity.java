package com.evdokimov.eugene.mobilecoach.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.evdokimov.eugene.mobilecoach.R;
import com.rey.material.widget.Button;

public class WorkoutActivity extends AppCompatActivity {

    boolean isRed;
    Button btnTimer;
    CardView cTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        isRed = false;
        cTimer = (CardView) findViewById(R.id.cardview_timer_workout);
        btnTimer = (Button) findViewById(R.id.btn_timer_workout);
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRed) {
                    btnTimer.setBackgroundColor(Color.parseColor("#F44336"));
                    btnTimer.applyStyle(R.style.ButtonTimerRed);
                    isRed = true;
                }else{
                    btnTimer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    btnTimer.applyStyle(R.style.ButtonTimerGreen);
                    isRed = false;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout, menu);
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
