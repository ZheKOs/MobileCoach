package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.rey.material.widget.ImageButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class WatchTrainPlanActivity extends AppCompatActivity {

    private final int REQUEST_EDIT_PLAN = 700;

    private Context context;

    private String planName;
    private ArrayList<WorkoutPlan> workoutPlan;

    private TextView tvPlanName;

    private WorkoutsAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_train_plan);

        context = this;

        planName = getIntent().getStringExtra("planName");

        getWorkoutPlan(planName);

        tvPlanName = (TextView) findViewById(R.id.tv_plan_name_watch);
        tvPlanName.setText(planName);

        lv = (ListView) findViewById(R.id.lv_watch_plan);
        adapter = new WorkoutsAdapter(this,workoutPlan,false,false);
        lv.setAdapter(adapter);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_back_wtp:
                        finish();
                        break;
                    case R.id.btn_edit_plan_wtp:
                        Intent intent = new Intent(context, EditTrainingPlanActivity.class);
                        intent.putExtra("planName", planName);
                        intent.putExtra("editingCommonPlan",true);
                        startActivityForResult(intent, REQUEST_EDIT_PLAN);
                        break;
                }
            }
        };
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back_wtp);
        btnBack.setOnClickListener(listener);
        ImageButton btnEdit = (ImageButton) findViewById(R.id.btn_edit_plan_wtp);
        btnEdit.setOnClickListener(listener);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_PLAN){
            switch (resultCode){
                case EditTrainingPlanActivity.RESULT_SAVE:
                    setResult(EditTrainingPlanActivity.RESULT_SAVE);
                    String pName = data.getStringExtra("planNameBack");
                    tvPlanName.setText(pName);

                    //also dirty way TODO in proper way
                    getWorkoutPlan(pName);
                    adapter = new WorkoutsAdapter(this,workoutPlan,false,false);
                    lv.setAdapter(adapter);
                    break;
                case EditTrainingPlanActivity.RESULT_DELETE:
                    setResult(EditTrainingPlanActivity.RESULT_DELETE);
                    finish();
                    break;
            }
        }

    }
}
