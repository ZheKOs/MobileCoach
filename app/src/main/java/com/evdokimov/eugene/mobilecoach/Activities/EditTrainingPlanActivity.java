package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.mobeta.android.dslv.DragSortListView;
import com.rey.material.widget.EditText;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTrainingPlanActivity extends AppCompatActivity {

    public static final int RESULT_SAVE = 3;
    public static final int RESULT_DELETE = 4;


    private int mode; //define situation: edit or create

    private WorkoutsAdapter workoutsAdapter;
    private ArrayList<WorkoutPlan> workouts;
    String planName;

    String tmpName;

    private FloatingActionButton btnAdd;

    //private RelativeLayout instruction; //layout is instruction for empty list

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    WorkoutPlan item = workoutsAdapter.getItem(from);

                    workoutsAdapter.notifyDataSetChanged();
                    workoutsAdapter.remove(item);
                    workoutsAdapter.insert(item, to);

                    onDropForWorkoutPlan(from,to);

                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    onRemoveForWorkoutPlan(which);
                    workoutsAdapter.remove(workoutsAdapter.getItem(which));
                    //if (workoutsAdapter.getCount()<1)
                        //instruction.setVisibility(View.VISIBLE);
                }
            };

    private DragSortListView.DragScrollProfile ssProfile =
            new DragSortListView.DragScrollProfile() {
                @Override
                public float getSpeed(float w, long t) {
                    if (w > 0.8f) {
                        // Traverse all views in a millisecond
                        return ((float) workoutsAdapter.getCount()) / 0.001f;
                    } else {
                        return 10.0f * w;
                    }
                }
            };
    private void onDropForWorkoutPlan(int from, int to){
        if (from!=to) {
            try {

//            WorkoutPlan fromWP = HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName("MainPlan").get(from);
//            WorkoutPlan toWP = HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName("MainPlan").get(to);
//
//            int tmp = fromWP.getIdPlan();
//
//            fromWP.setIdPlan(toWP.getIdPlan());
//            HelperFactory.getDbHelper().getWorkoutPlanDAO().update(fromWP);
//
//            toWP.setIdPlan(tmp);
//            HelperFactory.getDbHelper().getWorkoutPlanDAO().update(toWP);

                int i = 0;
                for (WorkoutPlan workoutPlan : workoutsAdapter.getWorkoutPlan())
                {
                    workoutPlan.setOrder(i);
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().update(workoutPlan);
                    i++;
                }
//                WorkoutPlan fromWP = HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName(planName).get(from);
//                HelperFactory.getDbHelper().getWorkoutPlanDAO().updateId(fromWP, to);

            } catch (SQLException e) {
                Log.e("TAG_ERROR", "can't swipe ids of workoutPlan");
                throw new RuntimeException(e);
            }
        }
    }
    private void onRemoveForWorkoutPlan(int which){
        try {
            HelperFactory.getDbHelper().getWorkoutPlanDAO().delete(workoutsAdapter.getItem(which));
        }catch (SQLException e){
            Log.e("TAG_ERROR", "can't delete workout plan");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tp);

        planName = getIntent().getStringExtra("planName");
        tmpName = planName;
        getWorkoutPlan(planName);

        //Now let's grab ArrayList which should be "packed" and send using Parcel
                //new ArrayList<>(getIntent().<WorkoutPlan>getParcelableArrayListExtra("workoutPlan")); //yeap, this is how it "unpacks" in real code. See Workout.class

        //instruction = (RelativeLayout) findViewById(R.id.instruction); //TODO Instruction what to do if list is empty

        DragSortListView lv = (DragSortListView) findViewById(R.id.dslv_edit_tp);

        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setDragScrollProfile(ssProfile);

        workoutsAdapter = new WorkoutsAdapter(this,workouts,true);
        lv.setAdapter(workoutsAdapter);

//        if (workoutsAdapter.getCount()>0)
//            instruction.setVisibility(View.GONE);

        View.OnClickListener listener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btn_back_etp:
                        setResult(RESULT_CANCELED);
                        finish();
                        break;
//                    case R.id.btn_save_plan_etp:
//
//                        resultIntent = new Intent();
//                        resultIntent.putExtra("workouts_back",workoutsAdapter.getWorkoutPlan());
//                        setResult(RESULT_SAVE, resultIntent);
//                        finish();
//                        break;
                    case R.id.btn_delete_plan_etp:
                        setResult(RESULT_DELETE);
                        break;
                }
            }
        };

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back_etp);
        btnBack.setOnClickListener(listener);
//        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save_plan_etp);
//        btnSave.setOnClickListener(listener);

        EditText planNameEditText = (EditText) findViewById(R.id.et_edit_name_tp);
        planNameEditText.setText(planName);
        planNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                tmpName = s.toString();
            }
        });

        btnAdd = (FloatingActionButton) findViewById(R.id.btn_edit_tp_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Workout plank = HelperFactory.getDbHelper().getWorkoutDAO().getWorkoutByName("Планка");
                    WorkoutPlan wPlan = new WorkoutPlan();
                    wPlan.setName(planName);
                    wPlan.setOrder(workoutsAdapter.getWorkoutPlan().size() + 1);
                    wPlan.setWorkout(plank);
                    wPlan.setCount(0);

                    HelperFactory.getDbHelper().getWorkoutPlanDAO().create(wPlan);
                    workouts.add(wPlan);
                    workoutsAdapter.setWorkoutPlan(workouts);
                } catch (SQLException e){
                    Log.e("TAG_ERROR", "can't add test workout");
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void getWorkoutPlan(String name){

        try {
            workouts = new ArrayList<>(HelperFactory.getDbHelper()
                    .getWorkoutPlanDAO().getWorkoutPlanByName(name));
        }catch (SQLException e){
            Log.e("TAG_ERROR", "can't get WorkoutPlan");
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onPause() {
        if (!tmpName.equals(planName)) {
            for (WorkoutPlan workoutPlan : workoutsAdapter.getWorkoutPlan()) {
                workoutPlan.setName(tmpName);
                try {
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().update(workoutPlan);
                    SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                    String pickedPlan = sharedPref.getString("pickedplan", "");
                    if (planName.equals(pickedPlan)) {

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("pickedplan", tmpName);
                        editor.commit();
                    }
                } catch (SQLException e) {
                    Log.e("TAG_ERROR", "can't update name of WorkoutPlan");
                    throw new RuntimeException(e);
                }
            }
        }
        super.onPause();
    }

}
