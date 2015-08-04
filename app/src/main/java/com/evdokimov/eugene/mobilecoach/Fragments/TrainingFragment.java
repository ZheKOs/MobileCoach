package com.evdokimov.eugene.mobilecoach.Fragments;

import com.evdokimov.eugene.mobilecoach.Activities.EditTrainingPlanActivity;
import com.evdokimov.eugene.mobilecoach.Activities.WatchTrainPlanActivity;
import com.evdokimov.eugene.mobilecoach.Adapters.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanChangedListener;
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanSelectedListener;
import com.evdokimov.eugene.mobilecoach.db.DBHelper;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.SnackBar;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TrainingFragment extends Fragment implements OnWorkoutPlanSelectedListener, OnWorkoutPlanChangedListener {



    final int REQUEST_EDIT_WORKOUT_PLAN_ACTIVITY = 600;
    final int REQUEST_WATCH_WORKOUT_PLAN = 900;

    private final String EMPTY_PICKED_PLAN = "_empty_";

    LayoutInflater mInflater;
    ViewGroup mContainer;

    View mView;

    ListView lv_plan_workouts;
    WorkoutsAdapter workoutsAdapter;

    ListView lv_plans;
    PlansAdapter plansAdapter;

    View mTop;

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
        mInflater = inflater;
        mContainer = container;

        mView = mInflater.inflate(R.layout.fragment_training, mContainer, false);

        String workoutPlanName = getSelectedPlan();

        getWorkoutPlan(workoutPlanName);

//        setAnimation();

        getAllPlans();

        initializeAllList(workoutPlanName);

        initSnackBar();

//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    case R.id.btn_float_third:
//                        final TimePickerDialog dialog = (TimePickerDialog) new TimePickerDialog(mainActivity,R.style.TimePickerDialog)
//                                //.title("Поставить напоминание?")
//                                .positiveAction("Поставить").negativeAction("Отмена");
//                        dialog.positiveActionClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                snackBar.text("Напоминание в " + dialog.getFormattedTime(new SimpleDateFormat("H:mm")));
//                                dialog.dismiss();
//                                snackBar.show(mainActivity);
//                            }
//                        });
//                        dialog.negativeActionClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(mainActivity, "Отменено", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        });
//
//                        dialog.show();
//                        break;
//                    case R.id.btn_float_second:
//                        final SimpleDialog simpleDialog = new SimpleDialog(mainActivity);
//                        simpleDialog.title("Выберите план");
//                        final String[] plans = new String[]{"ПЛАН1", "ПЛАН2", "ПЛАН3", "ПЛАН4", "ПЛАН5", "ПЛАН6", "ПЛАН7", "ПЛАН8"};
//                        simpleDialog.items(plans, -1);
//                        simpleDialog.positiveAction("Выбрать").positiveActionClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Toast.makeText(mainActivity,
//                                        "Выбран план - " + plans[simpleDialog.getSelectedIndex()],
//                                        Toast.LENGTH_SHORT).show();
//                                simpleDialog.dismiss();
//                            }
//                        });
//                        simpleDialog.negativeAction("Отмена").negativeActionClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                simpleDialog.dismiss();
//                            }
//                        });
//                        simpleDialog.show();
//                        break;
//                    case R.id.btn_float_main:
//                        Intent intent = new Intent(getActivity(), WorkoutActivity.class);
//                        startActivity(intent);
//                        break;
//                }
//            }
//        };

//        mainFAB = (FloatingActionButton) mView.findViewById(R.id.btn_float_main);
//        mainFAB.setOnClickListener(listener);

//        secondFAB = (FloatingActionButton) mView.findViewById(R.id.btn_float_second);
//        secondFAB.setOnClickListener(listener);
//
//        thirdFAB = (FloatingActionButton) mView.findViewById(R.id.btn_float_third);
//        thirdFAB.setOnClickListener(listener);

//        animateFAB(-1, true);

        return mView;
    }

    /**
    *   Returns picked plan from SharedPreferences
     **/
    private String getSelectedPlan(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings",Context.MODE_PRIVATE);
        String workoutPlanName = sharedPref.getString("pickedplan", EMPTY_PICKED_PLAN);
        return workoutPlanName;
    }

    private void initSnackBar(){
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
    }

    //This method init header and whole body of list
    //can handle empty top
    private void initializeAllList(final String workoutPlanName){
        if (mTop != null) lv_plans.removeHeaderView(mTop);
        if (!workoutPlanName.equals(EMPTY_PICKED_PLAN)) {

            mTop = mInflater.inflate(R.layout.row_main_plan_t_main, null);
            tvPlanNamePicked = (TextView) mTop.findViewById(R.id.textViewPlanNamePicked);
            tvPlanNamePicked.setText(workoutPlanName);
            final ImageButton morePlan = (ImageButton) mTop.findViewById(R.id.main_btn_more_plan);
            morePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context wrapper = new ContextThemeWrapper(mainActivity.getBaseContext(), R.style.MyPopupMenu);
                    PopupMenu popup = new PopupMenu(wrapper, morePlan);
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_training, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(mainActivity.getBaseContext(),
                                    item.getTitle(),
                                    Toast.LENGTH_SHORT).show();
                            switch (item.getItemId()) {
                                case R.id.remove_pick:
                                    SharedPreferences sharedPref = mainActivity.getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("pickedplan", EMPTY_PICKED_PLAN);
                                    editor.apply();

                                    getAllPlans();
                                    initializeAllList(EMPTY_PICKED_PLAN);
                                    break;
                                case R.id.edit_plan_mt:
                                    Intent intent = new Intent(getActivity(), EditTrainingPlanActivity.class);
                                    intent.putExtra("planName", workoutPlanName);
                                    startActivityForResult(intent, REQUEST_EDIT_WORKOUT_PLAN_ACTIVITY);
                                    break;
                                case R.id.notify_plan_mt:
                                    final TimePickerDialog dialog = (TimePickerDialog) new TimePickerDialog(mainActivity,R.style.TimePickerDialog)
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
                                case R.id.share_plan_mt:
                                    //TODO list of the plan
                                    String shareBody = "Here is the share content body";
                                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(sharingIntent, "share_using"));
                                    break;
                                case R.id.delete_plan_mt:
                                    final Dialog deleteDialog = new Dialog(mainActivity);
                                    deleteDialog
                                            .title("Удалить план")
                                            .positiveActionTextColor(Color.parseColor("#F44336"))
                                            .positiveAction("УДАЛИТЬ")
                                            .positiveActionClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DBHelper dbHelper = HelperFactory.getDbHelper();
                                                    try {
                                                        dbHelper.getWorkoutPlanDAO().delete(
                                                                dbHelper.getWorkoutPlanDAO().getWorkoutPlanByName(workoutPlanName)
                                                        );
                                                    } catch (SQLException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    SharedPreferences sharedPref = mainActivity.getSharedPreferences("mysettings", Context.MODE_PRIVATE);

                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("pickedplan", EMPTY_PICKED_PLAN);
                                                    editor.apply();

                                                    deleteDialog.dismiss();

                                                    getAllPlans();
                                                    initializeAllList(EMPTY_PICKED_PLAN);
                                                }
                                            })
                                            .negativeActionTextColor(Color.parseColor("#727272"))
                                            .negativeAction("ОТМЕНА")
                                            .negativeActionClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    deleteDialog.dismiss();
                                                }
                                            });
                                    deleteDialog.show();

                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });

            Button start = (Button) mTop.findViewById(R.id.btn_start_plan_top);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                    startActivity(intent);
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
            //changing height programmatically
            ViewGroup.LayoutParams params = lv_plan_workouts.getLayoutParams();
            if (workoutPlan.size()>=4){
                params.height = 2*55*4-32; // -32 to let user know that there is something to scroll for
            }else {
                params.height = 2*55*workoutPlan.size();
            }
            lv_plan_workouts.setLayoutParams(params);
            lv_plan_workouts.requestLayout();

        } else {
            mTop = mInflater.inflate(R.layout.row_main_plan_t_main_empty,null);
        }
        lv_plans = (ListView) mView.findViewById(R.id.lv_main_plans);
        lv_plans.addHeaderView(mTop);
        plansAdapter = new PlansAdapter(getActivity(), this, plans);
        lv_plans.setAdapter(plansAdapter);
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
        if (!name.equals(EMPTY_PICKED_PLAN)) {
            try {
                workoutPlan = new ArrayList<>(HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName(name));
            } catch (SQLException e) {
                Log.e("TAG_ERROR", "can't get workouts");
                throw new RuntimeException(e);
            }
        }else{
            workoutPlan = null;
        }
    }

//    private void setAnimation(){
//        scaleAnimationIn.setDuration(500);
//        scaleAnimationIn.setStartOffset(150);
//        scaleAnimationOut.setDuration(500);
//        scaleAnimationOut.setStartOffset(150);
//    }
//    private void animateFAB(int which, boolean in){
//        switch (which) {
//            case 0: //mainFAB
//                if (in){
//                    mainFAB.setAnimation(scaleAnimationIn);
//                    mainFAB.setVisibility(View.VISIBLE);
//
////                    secondFAB.setVisibility(View.GONE);
////                    thirdFAB.setVisibility(View.GONE);
//                }else{
//                    mainFAB.setAnimation(scaleAnimationOut);
//                    mainFAB.setVisibility(View.GONE);
//                }
//                break;
////            case 1:
////                if (in) {
////                    secondFAB.setAnimation(scaleAnimationIn);
////                    secondFAB.setVisibility(View.VISIBLE);
////                }else {
////                    secondFAB.setAnimation(scaleAnimationOut);
////                    secondFAB.setVisibility(View.GONE);
////                }
////                break;
////            case 2:
////                if (in) {
////                    thirdFAB.setAnimation(scaleAnimationIn);
////                    thirdFAB.setVisibility(View.VISIBLE);
////                }else {
////                    thirdFAB.setAnimation(scaleAnimationOut);
////                    thirdFAB.setVisibility(View.GONE);
////                }
////                break;
//            case -1:
//                if (in){
//                    mainFAB.setAnimation(scaleAnimationIn);
//                    mainFAB.setVisibility(View.VISIBLE);
//
////                    secondFAB.setAnimation(scaleAnimationIn);
////                    secondFAB.setVisibility(View.VISIBLE);
////
////                    thirdFAB.setAnimation(scaleAnimationIn);
////                    thirdFAB.setVisibility(View.VISIBLE);
//                }else{
//                    mainFAB.setAnimation(scaleAnimationOut);
//                    mainFAB.setVisibility(View.GONE);
//
////                    secondFAB.setAnimation(scaleAnimationOut);
////                    secondFAB.setVisibility(View.GONE);
////
////                    thirdFAB.setAnimation(scaleAnimationOut);
////                    thirdFAB.setVisibility(View.GONE);
//                }
//
//        }
//    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EDIT_WORKOUT_PLAN_ACTIVITY) {
            switch (resultCode) {
                case EditTrainingPlanActivity.RESULT_DELETE:
                    //here deletes PICKED plan

                    if (workoutPlan.size() > 0) {
                        workoutPlan.clear();
                    }
                    workoutsAdapter.notifyDataSetInvalidated();

                    getAllPlans();
                    initializeAllList(EMPTY_PICKED_PLAN);
                    plansAdapter.notifyDataSetChanged();

                    break;
                case EditTrainingPlanActivity.RESULT_SAVE:
                    //refreshing list after changing plan
                    String pickedPlan = getSelectedPlan();
                    if (!pickedPlan.equals(EMPTY_PICKED_PLAN)) {

                        getWorkoutPlan(pickedPlan);

                        tvPlanNamePicked.setText(pickedPlan);
                        workoutsAdapter = new WorkoutsAdapter(getActivity(), workoutPlan, false);
                        lv_plan_workouts.setAdapter(workoutsAdapter);
                        workoutsAdapter.notifyDataSetChanged();

                        getAllPlans();
                        initializeAllList(pickedPlan);
                        plansAdapter.notifyDataSetChanged();

                    } else {
                        //TODO here is instruction
                    }
                    break;
            }
        } else if(requestCode == REQUEST_WATCH_WORKOUT_PLAN){
            SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
            String pickedPlan = sharedPref.getString("pickedplan", EMPTY_PICKED_PLAN);
            switch (resultCode) {
                case EditTrainingPlanActivity.RESULT_DELETE:
                    if (pickedPlan.equals(EMPTY_PICKED_PLAN))
                    {
                        if (workoutPlan.size() > 0) {
                            workoutPlan.clear();
                        }
                        workoutsAdapter.notifyDataSetInvalidated();

                        getAllPlans();
                        initializeAllList(EMPTY_PICKED_PLAN);
                        plansAdapter.notifyDataSetChanged();
                        break;
                    }
                    //ELSE do the same as in RESULT_SAVE. just continue moving...

                case EditTrainingPlanActivity.RESULT_SAVE:

                    if (!pickedPlan.equals(EMPTY_PICKED_PLAN)) {

                        getWorkoutPlan(pickedPlan);

                        workoutsAdapter = new WorkoutsAdapter(getActivity(), workoutPlan, false);
                        lv_plan_workouts.setAdapter(workoutsAdapter);
                        workoutsAdapter.notifyDataSetChanged();

                        getAllPlans();
                        initializeAllList(pickedPlan);
                        plansAdapter.notifyDataSetChanged();
                    } else {
                        getAllPlans();
                        initializeAllList(EMPTY_PICKED_PLAN);
                        plansAdapter.notifyDataSetChanged();
                    }
                    break;
            }

        }
    }

    @Override
    public void onWorkoutPlanSelected(int position) {
        Intent intent = new Intent(mainActivity, WatchTrainPlanActivity.class);
        intent.putExtra("planName",plans[position]);
        startActivityForResult(intent, REQUEST_WATCH_WORKOUT_PLAN);
    }

    @Override
    public void onWorkoutPlanChanged() {
        getWorkoutPlan(getSelectedPlan());
        getAllPlans();
        initializeAllList(getSelectedPlan());
        plansAdapter.notifyDataSetChanged();
    }
}
