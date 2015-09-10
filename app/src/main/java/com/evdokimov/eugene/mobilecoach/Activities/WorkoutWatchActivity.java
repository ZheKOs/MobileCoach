package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Utils.SharingContentManager;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;

import java.sql.SQLException;

public class WorkoutWatchActivity extends AppCompatActivity {

    Context context;

    Workout workout;
    String workoutName;

    TextView name,instruction;

    ImageButton btnBack,btnMore,btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_watch);

        context = this;

        FloatingActionButton fabEditWorkout = (FloatingActionButton) findViewById(R.id.btn_edit_workout);
        fabEditWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WorkoutEditActivity.class);
                startActivity(intent);
            }
        });

        workoutName = getIntent().getStringExtra("workoutName");
        getWorkout();

        View.OnClickListener actionBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_back_workout_watch:
                        finish();
                        break;
                    case R.id.btn_share_workout_watch:
                        share();
                        break;
                    case R.id.btn_more_workout_watch:
                        showPopup();
                        break;
                }
            }
        };

        btnBack = (ImageButton) findViewById(R.id.btn_back_workout_watch);
        btnBack.setOnClickListener(actionBtnListener);

        btnShare = (ImageButton) findViewById(R.id.btn_share_workout_watch);
        btnShare.setOnClickListener(actionBtnListener);

        btnMore = (ImageButton) findViewById(R.id.btn_more_workout_watch);
        btnMore.setOnClickListener(actionBtnListener);


        name = (TextView) findViewById(R.id.tv_watch_workout_name);
        name.setText(workoutName);

        instruction = (TextView) findViewById(R.id.tv_watch_workout_instruction);
        instruction.setText(workout.getInstruction());


    }

    private void share(){
        SharingContentManager scm = new SharingContentManager(this,0,workoutName);
        scm.prepareAndShareContent();
    }

    private void showPopup(){
        Context wrapper = new ContextThemeWrapper(this, R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, btnMore);
        popup.getMenuInflater()
                .inflate(R.menu.menu_watch_train_plan, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

//                switch (item.getItemId()) {
//                    case R.id.edit_plan_mt:
//                        Intent intent = new Intent(this, EditTrainingPlanActivity.class);
//                        intent.putExtra("planName", curPlan);
//                        startActivity(intent);
//                        break;
//
//                    case R.id.delete_plan_mt:
//                        final Dialog deleteDialog = new Dialog(this);
//                        deleteDialog
//                                .title("Удалить план")
//                                .positiveActionTextColor(Color.parseColor("#F44336"))
//                                .positiveAction("УДАЛИТЬ")
//                                .positiveActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        deletePlan(position);
//                                        deleteDialog.dismiss();
//                                    }
//                                })
//                                .negativeActionTextColor(Color.parseColor("#727272"))
//                                .negativeAction("ОТМЕНА")
//                                .negativeActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        deleteDialog.dismiss();
//                                    }
//                                });
//                        deleteDialog.show();
//                        break;
//                }
                return true;
            }
        });
        popup.show();
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
