package com.evdokimov.eugene.mobilecoach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.util.ArrayList;
import java.util.Arrays;

public class WatchTrainPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_train_plan);

        final Workout[] workoutsArray = {
                new Workout("Приседания", "", null),
                new Workout("Отжимания", "", null),
                new Workout("Подтягивание", "", null),
                new Workout("Приседания", "", null),
                new Workout("Планка", "", null),
                new Workout("Дельфин", "", null),
                new Workout("Пресс", "", null)
        };
        ArrayList<Workout> workouts = new ArrayList<Workout>(Arrays.asList(workoutsArray));

//        ListView lv = (ListView) findViewById(R.id.lv_watch_plan);           //TODO NullPointerException!!!
//        WorkoutsAdapter adapter = new WorkoutsAdapter(this,workouts,false);
//        adapter.setMainWatch(false);
//        lv.setAdapter(adapter);

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
