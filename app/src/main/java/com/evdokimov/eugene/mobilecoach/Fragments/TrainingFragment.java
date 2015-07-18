package com.evdokimov.eugene.mobilecoach.Fragments;

import com.evdokimov.eugene.mobilecoach.Activities.EditTrainingPlanActivity;
import com.evdokimov.eugene.mobilecoach.Adapters.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evdokimov.eugene.mobilecoach.Activities.MainActivity;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Activities.WorkoutActivity;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.SnackBar;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TrainingFragment extends Fragment{

    final int REQUEST_EDIT_WORKOUT_PLAN_ACTIVITY = 1;

    ListView lv_plan_workouts;
    WorkoutsAdapter workoutsAdapter;

    ArrayList<WorkoutPlan> workoutPlan;

    String[] plans;

    private FloatingActionButton mainFAB, secondFAB, thirdFAB;

    private ScaleAnimation scaleAnimationIn = new ScaleAnimation(0,1,0,1,28.5f,28.5f);
    private ScaleAnimation scaleAnimationOut = new ScaleAnimation(1,0,1,0,28.5f,28.5f);

    public static TrainingFragment newInstance(){
        TrainingFragment fragment = new TrainingFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    private SnackBar snackBar;

    TextView tvPlanNamePicked;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainActivity = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.fragment_training,container,false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        String workoutPlanName = sharedPref.getString("pickedplan", "MainPlan");
        getWorkoutPlan(workoutPlanName);

        setAnimation();

        getAllPlans();

        View mTop = inflater.inflate(R.layout.row_main_plan_t_main,null);
        tvPlanNamePicked = (TextView) mTop.findViewById(R.id.textViewPlanNamePicked);
        tvPlanNamePicked.setText(workoutPlanName);
        ImageButton editPlan = (ImageButton) mTop.findViewById(R.id.main_btn_edit_plan);
        editPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.main_btn_edit_plan:
                        Intent intent = new Intent(getActivity(), EditTrainingPlanActivity.class);
                        intent.putExtra("planName", workoutsAdapter.getWorkoutPlan().get(0).getName());
                        startActivityForResult(intent, REQUEST_EDIT_WORKOUT_PLAN_ACTIVITY);
                        break;
                }
            }
        });
        lv_plan_workouts = (ListView) mTop.findViewById(R.id.lv_main_workouts);
        workoutsAdapter = new WorkoutsAdapter(getActivity(), workoutPlan, false);
        lv_plan_workouts.setAdapter(workoutsAdapter);
        lv_plan_workouts.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        ListView lv_plans = (ListView) v.findViewById(R.id.lv_main_plans);
        lv_plans.addHeaderView(mTop);
        PlansAdapter plansAdapter = new PlansAdapter(getActivity(),plans);
        lv_plans.setAdapter(plansAdapter);

        snackBar = new SnackBar(mainActivity);
        snackBar.applyStyle(R.style.SnackBarSingleLine)
                .singleLine(false) //to be sure :D
                .actionText("Отмена")
                .actionClickListener(new SnackBar.OnActionClickListener() {
                    @Override
                    public void onActionClick(SnackBar snackBar, int i) {
                        Toast.makeText(mainActivity, "Отменено", Toast.LENGTH_SHORT).show();
                    }
                })
                .duration(3500)
                .actionTextColor(getResources().getColor(R.color.colorAccent))
                .textColor(Color.WHITE);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_float_third:
                        final TimePickerDialog dialog = (TimePickerDialog) new TimePickerDialog(mainActivity)
                                //.title("Поставить напоминание?")
                                .positiveAction("Поставить").negativeAction("Отмена");
                        dialog.positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackBar.text("Напоминание в " + dialog.getFormattedTime(new SimpleDateFormat("H:mm")));
                                dialog.dismiss();
                                snackBar.show(mainActivity);
                            }
                        });
                        dialog.negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mainActivity, "Отменено", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.btn_float_second:
                        final SimpleDialog simpleDialog = new SimpleDialog(mainActivity);
                        simpleDialog.title("Выберите план");
                        final String[] plans = new String[]{"ПЛАН1", "ПЛАН2", "ПЛАН3", "ПЛАН4", "ПЛАН5", "ПЛАН6", "ПЛАН7", "ПЛАН8"};
                        simpleDialog.items(plans, -1);
                        simpleDialog.positiveAction("Выбрать").positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(mainActivity,
                                        "Выбран план - " + plans[simpleDialog.getSelectedIndex()],
                                        Toast.LENGTH_SHORT).show();
                                simpleDialog.dismiss();
                            }
                        });
                        simpleDialog.negativeAction("Отмена").negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                simpleDialog.dismiss();
                            }
                        });
                        simpleDialog.show();
                        break;
                    case R.id.btn_float_main:
                        Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        mainFAB = (FloatingActionButton) v.findViewById(R.id.btn_float_main);
        mainFAB.setOnClickListener(listener);

        secondFAB = (FloatingActionButton) v.findViewById(R.id.btn_float_second);
        secondFAB.setOnClickListener(listener);

        thirdFAB = (FloatingActionButton) v.findViewById(R.id.btn_float_third);
        thirdFAB.setOnClickListener(listener);

        animateFAB(-1, true);

        return v;
    }

    private void getAllPlans(){
        try {
            ArrayList<WorkoutPlan> workoutPlans = new ArrayList<>(
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().getUniquePlans());
            plans = new String[workoutPlans.size()];
            int i = 0;
            for (WorkoutPlan workoutPlan : workoutPlans){
                plans[i++] = workoutPlan.getName();
            }
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't get all plans");
            throw new RuntimeException(e);
        }
    }

    private void getWorkoutPlan(String name){
        try {
            workoutPlan = new ArrayList<>(HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName(name));
        } catch (SQLException e){
            Log.e("TAG_ERROR","can't get workouts");
            throw new RuntimeException(e);
        }
    }

    private void setAnimation(){
        scaleAnimationIn.setDuration(500);
        scaleAnimationIn.setStartOffset(150);
        scaleAnimationOut.setDuration(500);
        scaleAnimationOut.setStartOffset(150);
    }

    private void animateFAB(int which, boolean in)
    {
        switch (which) {
            case 0: //mainFAB
                if (in){
                    mainFAB.setAnimation(scaleAnimationIn);
                    mainFAB.setVisibility(View.VISIBLE);

                    secondFAB.setVisibility(View.GONE);
                    thirdFAB.setVisibility(View.GONE);
                }else{
                    mainFAB.setAnimation(scaleAnimationOut);
                    mainFAB.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (in) {
                    secondFAB.setAnimation(scaleAnimationIn);
                    secondFAB.setVisibility(View.VISIBLE);
                }else {
                    secondFAB.setAnimation(scaleAnimationOut);
                    secondFAB.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (in) {
                    thirdFAB.setAnimation(scaleAnimationIn);
                    thirdFAB.setVisibility(View.VISIBLE);
                }else {
                    thirdFAB.setAnimation(scaleAnimationOut);
                    thirdFAB.setVisibility(View.GONE);
                }
                break;
            case -1:
                if (in){
                    mainFAB.setAnimation(scaleAnimationIn);
                    mainFAB.setVisibility(View.VISIBLE);

                    secondFAB.setAnimation(scaleAnimationIn);
                    secondFAB.setVisibility(View.VISIBLE);

                    thirdFAB.setAnimation(scaleAnimationIn);
                    thirdFAB.setVisibility(View.VISIBLE);
                }else{
                    mainFAB.setAnimation(scaleAnimationOut);
                    mainFAB.setVisibility(View.GONE);

                    secondFAB.setAnimation(scaleAnimationOut);
                    secondFAB.setVisibility(View.GONE);

                    thirdFAB.setAnimation(scaleAnimationOut);
                    thirdFAB.setVisibility(View.GONE);
                }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_WORKOUT_PLAN_ACTIVITY)
            switch (resultCode)
            {
                case EditTrainingPlanActivity.RESULT_DELETE:
                    //TODO delete plan
                    break;
                default:
                    try {
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings",Context.MODE_PRIVATE);
                        String pickedPlan = sharedPref.getString("pickedplan", "MainPlan");
                        tvPlanNamePicked.setText(pickedPlan);
                        workoutsAdapter = new WorkoutsAdapter(
                                getActivity(),
                                new ArrayList<>(HelperFactory.getDbHelper().getWorkoutPlanDAO()
                                        .getWorkoutPlanByName(pickedPlan)),
                                false
                        );
                        lv_plan_workouts.setAdapter(workoutsAdapter);
                        workoutsAdapter.notifyDataSetChanged();
                    }catch (SQLException e){
                        Log.e("TAG_ERROR","can't get workoutPlan");
                        throw new RuntimeException(e);
                    }
            }
    }
}
