package com.evdokimov.eugene.mobilecoach.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;

import java.sql.SQLException;
import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity{

    String planName;

    ArrayList<WorkoutPlan> workoutPlan;
    TextView curWorkout, prevWorkout, nextWorkout;
    ImageView ivPrev, ivNext;
    Button btnInfo, btnIterator;

    Dialog dialog;

    int position, count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        planName = getIntent().getStringExtra("planname");
        getWorkoutPlan();
        position = 0;

        curWorkout = (TextView) findViewById(R.id.tv_cur_workout_name);

        ivPrev = (ImageView) findViewById(R.id.iv_prev_workout);
        prevWorkout = (TextView) findViewById(R.id.tv_prev_workout);
        ivNext = (ImageView) findViewById(R.id.iv_next_workout);
        nextWorkout = (TextView) findViewById(R.id.tv_next_workout);

        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_info_workout:
                        initDialog();
                        dialog.show();
                        break;
                    case R.id.btn_iterator:
                        if (count>0)
                            btnIterator.setText(String.valueOf(--count));
                        break;
                }
            }
        };

        btnInfo = (Button) findViewById(R.id.btn_info_workout);
        btnInfo.setOnClickListener(btnListener);
        btnIterator = (Button) findViewById(R.id.btn_iterator);
        btnIterator.setText(String.valueOf(workoutPlan.get(position).getCount()));
        btnIterator.setOnClickListener(btnListener);

        refreshPrevNextAndCurLinks();

    }

    private void initDialog(){
        dialog = new Dialog(this);
        dialog.title(workoutPlan.get(position).getName());
        View contentView = View.inflate(this, R.layout.dialog_text_content, null);
        ((TextView) contentView.findViewById(R.id.tv_dialog_text_content))
                .setText(workoutPlan.get(position).getWorkout().getInstruction());
        dialog.contentView(contentView);
        dialog.positiveAction("OK");
        dialog.positiveActionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
    It use planName to get WorkoutPlan
     */
    private void getWorkoutPlan(){
        try {
            workoutPlan = new ArrayList<>(
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName(planName));
        }catch (SQLException e){
            Log.e("TAG_ERROR", "can't get all workouts");
            throw new RuntimeException(e);
        }
    }


    private void refreshCount(){
        count = workoutPlan.get(position).getCount();
        btnIterator.setText(String.valueOf(count));
    }
    /**
     It use position to set both links
     */
    private void refreshPrevNextAndCurLinks(){
        if (position >= 0 && position <= workoutPlan.size()){

            curWorkout.setText(workoutPlan.get(position).getWorkout().getName());

            ivPrev.setVisibility(View.VISIBLE);
            prevWorkout.setVisibility(View.VISIBLE);

            if (position-1 >= 0) {
                prevWorkout.setText(workoutPlan.get(position-1).getWorkout().getName());
            }else{
                ivPrev.setVisibility(View.INVISIBLE);
                prevWorkout.setVisibility(View.INVISIBLE);
            }

            ivNext.setVisibility(View.VISIBLE);
            nextWorkout.setVisibility(View.VISIBLE);

            if (workoutPlan.size() > position+1){
                nextWorkout.setText(workoutPlan.get(position+1).getWorkout().getName());
            }else{
                ivNext.setVisibility(View.INVISIBLE);
                nextWorkout.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void switchWorkoutListener(View v) {

        if (position >= 0) {
            switch (v.getId()) {
                case R.id.iv_prev_workout:
                case R.id.tv_prev_workout:
                    if (position>0){
                        position--;
                    }
                    break;
                case R.id.iv_next_workout:
                case R.id.tv_next_workout:
                    if (position<workoutPlan.size()){
                        position++;
                    }
                    break;
            }
            refreshPrevNextAndCurLinks();
            refreshCount();
        }

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
