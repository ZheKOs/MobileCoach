package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import com.mobeta.android.dslv.DragSortListView;

import com.rey.material.app.Dialog;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;

public class EditTrainingPlanActivity extends AppCompatActivity {

    public static final int RESULT_CREATED = 2;
    public static final int RESULT_SAVE = 3;
    public static final int RESULT_DELETE = 4;

    private final String EMPTY_PICKED_PLAN = "_empty_";


    //private int mode; //define situation: edit or create

    private Context context;

    private boolean addingMode = false;
    private boolean editingCommonPlan = false;

    ArrayList<Workout> workoutArrayList;

    private WorkoutsAdapter workoutsAdapter;
    private ArrayList<WorkoutPlan> workouts;
    String planName;

    private ArrayList<WorkoutPlan> newWorkouts;
    private ArrayList<WorkoutPlan> deletedWorkouts;
    String tmpName;

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
//            try {
                int i = 0;
                for (WorkoutPlan workoutPlan : workoutsAdapter.getWorkoutPlan())
                {
                    workoutPlan.setOrder(i++);
//                    HelperFactory.getDbHelper().getWorkoutPlanDAO().update(workoutPlan);
//                    i++;
                }
//            } catch (SQLException e) {
//                Log.e("TAG_ERROR", "can't swipe ids of workoutPlan");
//                throw new RuntimeException(e);
//            }
        }
    }
    private void onRemoveForWorkoutPlan(int which){
        if(!addingMode){
            deletedWorkouts.add(workouts.get(which));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tp);

        context = this;

        addingMode = getIntent().getBooleanExtra("addingMode", false);
        editingCommonPlan = getIntent().getBooleanExtra("editingCommonPlan", false);

        if (addingMode){

            planName = "Новый план";
            tmpName = planName;
            workouts =  new ArrayList<>();

        }else {

            planName = getIntent().getStringExtra("planName");
            tmpName = planName;
            getWorkoutPlan(planName);
            newWorkouts = new ArrayList<>();
            deletedWorkouts = new ArrayList<>();
        }
        //instruction = (RelativeLayout) findViewById(R.id.instruction); //TODO Instruction what to do if list is empty

        DragSortListView lv = (DragSortListView) findViewById(R.id.dslv_edit_tp);

        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setDragScrollProfile(ssProfile);

        workoutsAdapter = new WorkoutsAdapter(this,workouts,true);
        lv.setAdapter(workoutsAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    workoutArrayList = new ArrayList<>(
                            HelperFactory.getDbHelper().getWorkoutDAO().getAllWorkouts());

                    CharSequence[] items = new CharSequence[workoutArrayList.size()];
                    int i = 0;
                    for (Workout workout : workoutArrayList){
                        items[i++] = workout.getName();
                    }

                    final SimpleDialog dialog = new SimpleDialog(context);
                    dialog.title("Выберите упражнение");
                    dialog.positiveAction("Выбрать");
                    dialog.negativeAction("Отменить");
                    dialog.items(items, -1);

                    dialog.show();

                    dialog.positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //try {
                                WorkoutPlan workoutPlan;
                                workoutPlan = workoutsAdapter.getWorkoutPlan().get(position);
                                int i = dialog.getSelectedIndex();
                                if (i >= 0) {
                                    //DBHelper dbHelper = HelperFactory.getDbHelper();
                                    Workout workout = workoutArrayList.get(dialog.getSelectedIndex());
                                    workoutPlan.setWorkout(workout);
                                    workoutsAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
//                            } catch (SQLException e) {
//                                throw new RuntimeException(e);
//                            }
                        }
                    });
                    dialog.negativeActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                } catch (SQLException e){
                    Log.e("TAG_ERROR","can't get all workouts");
                    throw new RuntimeException(e);
                }
            }
        });

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
                    case R.id.btn_save_plan_etp:
                        savePlan();

                        finish();
                        break;
                    case R.id.btn_delete_plan_etp:
                        deletePlan();
                        setResult(RESULT_DELETE);
                        finish();
                        break;
                }
            }
        };

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back_etp);
        btnBack.setOnClickListener(listener);
        ImageButton btnDeletePlan = (ImageButton) findViewById(R.id.btn_delete_plan_etp);
        btnDeletePlan.setOnClickListener(listener);
        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save_plan_etp);
        btnSave.setOnClickListener(listener);

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

        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.btn_edit_tp_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                final ArrayList<Workout> workoutArrayList;
                try {
                    workoutArrayList = new ArrayList<>(
                            HelperFactory.getDbHelper().getWorkoutDAO().getAllWorkouts());
                }catch (SQLException e){
                    Log.e("TAG_ERROR","Can't get all workouts");
                    throw new RuntimeException(e);
                }

                String[]items  = new String[workoutArrayList.size()];
                int i = 0;
                for (Workout workout : workoutArrayList) { items[i++] = workout.getName(); }
                //Workout plank = HelperFactory.getDbHelper().getWorkoutDAO().getWorkoutByName("Планка");
                View contentView = View.inflate(context, R.layout.dialog_workout_plan_add_new_item, null);

                final Spinner spinner = (Spinner) contentView.findViewById(R.id.dialog_add_workout_plan_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,R.layout.row_spn, items);
                adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
                spinner.setAdapter(adapter);

                final EditText etCount = (EditText) contentView.findViewById(R.id.dialog_add_workout_plan_et);
                etCount.setText("1");

                dialog.setContentView(contentView);

                dialog.positiveAction("Добавить");
                dialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etCount.getText().length() > 0
                                && !Integer.valueOf(etCount.getText().toString()).equals(0)) {
                            int count = Integer.valueOf(etCount.getText().toString());
                            //try {
                            int item = spinner.getSelectedItemPosition();
                            Workout workout = workoutArrayList.get(item);
                            WorkoutPlan wPlan = new WorkoutPlan();
                            wPlan.setName(planName);
                            wPlan.setOrder(workoutsAdapter.getWorkoutPlan().size() + 1);
                            wPlan.setWorkout(workout);
                            wPlan.setCount(count);

                            if (!addingMode) {
                                //HelperFactory.getDbHelper().getWorkoutPlanDAO().create(wPlan);
                                newWorkouts.add(wPlan);
                            }

                            workouts.add(wPlan);
                            workoutsAdapter.notifyDataSetChanged();
                            dialog.dismiss();
//                            } catch (SQLException e) {
//                                Log.e("TAG_ERROR", "can't add test workout");
//                                throw new RuntimeException(e);
//                            }
                        }
                    }
                });
                dialog.negativeAction("Отменить");
                dialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

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

//    @Override
//    protected void onPause() {
//        if (!tmpName.equals(planName)) {
//            if (addingMode){
//                try {
//                    HelperFactory.getDbHelper().getWorkoutPlanDAO().create(workouts);
//                }catch (SQLException e){
//                    Log.e("TAG_ERROR","can't create new plan");
//                    throw new RuntimeException(e);
//                }
//            }else {
//                for (WorkoutPlan workoutPlan : workoutsAdapter.getWorkoutPlan()) {
//                    workoutPlan.setName(tmpName);
//                    try {
//                        HelperFactory.getDbHelper().getWorkoutPlanDAO().update(workoutPlan);
//                        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
//                        String pickedPlan = sharedPref.getString("pickedplan", EMPTY_PICKED_PLAN);
//                        if (planName.equals(pickedPlan)) {
//
//                            SharedPreferences.Editor editor = sharedPref.edit();
//                            editor.putString("pickedplan", tmpName);
//                            editor.commit();
//                        }
//                    } catch (SQLException e) {
//                        Log.e("TAG_ERROR", "can't update name of WorkoutPlan");
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
//        super.onPause();
//    }

    private void savePlan(){
        if (addingMode){
            setResult(RESULT_CREATED);
            if (!tmpName.equals(planName)) {
                for (WorkoutPlan workoutPlan : workoutsAdapter.getWorkoutPlan()) {
                    workoutPlan.setName(tmpName);
                }
            }
            try {
                HelperFactory.getDbHelper().getWorkoutPlanDAO().create(workouts);
            }catch (SQLException e){
                Log.e("TAG_ERROR","can't create new plan");
                throw new RuntimeException(e);
            }
        }else {

            if(editingCommonPlan){
                Intent dataBack = new Intent();
                dataBack.putExtra("planNameBack", tmpName);
                setResult(RESULT_SAVE, dataBack);
            } else { setResult(RESULT_SAVE); }

            if (!tmpName.equals(planName)) {
                for (WorkoutPlan workoutPlan : workoutsAdapter.getWorkoutPlan()) {
                    workoutPlan.setName(tmpName);
                }

                SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                String pickedPlan = sharedPref.getString("pickedplan", EMPTY_PICKED_PLAN);
                if (planName.equals(pickedPlan)) {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("pickedplan", tmpName);
                    editor.apply();
                }
            }
            try {
                for (WorkoutPlan workoutPlan : workouts) {
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().update(workoutPlan);
                }
                if (!newWorkouts.isEmpty()){
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().create(newWorkouts);
                }
                if (!deletedWorkouts.isEmpty()){
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().delete(deletedWorkouts);
                }
            } catch (SQLException e) {
                Log.e("TAG_ERROR", "can't update WorkoutPlan");
                throw new RuntimeException(e);
            }

        }
    }
    private void deletePlan(){
        tmpName = EMPTY_PICKED_PLAN;
        try {
            HelperFactory.getDbHelper().getWorkoutPlanDAO().delete(workouts);
            if (workouts.size() > 0) {
                workouts.clear();
            }
            workoutsAdapter.notifyDataSetInvalidated();
        } catch (SQLException e) {
            Log.e("TAG_ERROR", "can't delete picked plan");
            throw new RuntimeException(e);
        }

        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        String pickedPlan = sharedPref.getString("pickedplan", EMPTY_PICKED_PLAN);
        if (planName.equals(pickedPlan)) {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pickedplan", tmpName);
            editor.apply();
        }

    }

}
