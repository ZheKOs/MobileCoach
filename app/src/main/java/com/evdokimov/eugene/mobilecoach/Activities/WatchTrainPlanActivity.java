package com.evdokimov.eugene.mobilecoach.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class WatchTrainPlanActivity extends AppCompatActivity {

    private String planName;
    private ArrayList<WorkoutPlan> workoutPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_train_plan);

        planName = getIntent().getStringExtra("planName");

        getWorkoutPlan(planName);

        TextView tvPlanName = (TextView) findViewById(R.id.tv_plan_name_watch);
        tvPlanName.setText(planName);

        ListView lv = (ListView) findViewById(R.id.lv_watch_plan);           //TODO NullPointerException!!!
        WorkoutsAdapter adapter = new WorkoutsAdapter(this,workoutPlan,false,false);
        lv.setAdapter(adapter);

    }

    private void getWorkoutPlan(String name){

        try {
            workoutPlan = new ArrayList<>(HelperFactory.getDbHelper()
                    .getWorkoutPlanDAO().getWorkoutPlanByName(name));
        }catch (SQLException e){
            Log.e("TAG_ERROR", "can't get WorkoutPlan");
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch_train_plan, menu);
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
