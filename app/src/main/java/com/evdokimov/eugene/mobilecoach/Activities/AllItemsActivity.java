package com.evdokimov.eugene.mobilecoach.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.evdokimov.eugene.mobilecoach.Adapters.AllItemsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;

public class AllItemsActivity extends AppCompatActivity {

    private boolean isWorkoutMode;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter aiAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Workout> workouts;
    ArrayList<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        isWorkoutMode = getIntent().getBooleanExtra("isWorkoutMode",true);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back_workout_ai);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_ai);
        recyclerView.setHasFixedSize(true);

        if (isWorkoutMode){
            mLayoutManager = new LinearLayoutManager(getBaseContext());
        }else {
            mLayoutManager = new GridLayoutManager(getBaseContext(), 2);
        }
        recyclerView.setLayoutManager(mLayoutManager);

        aiAdapter = new AllItemsAdapter(this, isWorkoutMode);
        recyclerView.setAdapter(aiAdapter);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_workouts, menu);
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
