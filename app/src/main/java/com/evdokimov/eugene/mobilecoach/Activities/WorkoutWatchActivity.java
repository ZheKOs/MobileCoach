package com.evdokimov.eugene.mobilecoach.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.sql.SQLException;

public class WorkoutWatchActivity extends AppCompatActivity {

    Workout workout;
    String workoutName;

    TextView name,instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_watch);

        workoutName = getIntent().getStringExtra("workoutName");
        getWorkout();


        name = (TextView) findViewById(R.id.tv_watch_workout_name);
        name.setText(workoutName);

        instruction = (TextView) findViewById(R.id.tv_watch_workout_instruction);
        instruction.setText(workout.getInstruction());


    }

    private void getWorkout(){
        try {
            workout = HelperFactory.getDbHelper().getWorkoutDAO().getWorkoutByName(workoutName);
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't get workout");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_watch, menu);
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
