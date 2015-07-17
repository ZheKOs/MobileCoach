package com.evdokimov.eugene.mobilecoach.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlanItem;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.util.ArrayList;

public class WorkoutsAdapter extends ArrayAdapter<WorkoutPlan>
{

    private Context context;
    private ArrayList<WorkoutPlan> workoutPlan;
    private boolean editMode;
    private boolean mainWatch;
    private View rowView;

    public WorkoutsAdapter(Context context, ArrayList<WorkoutPlan> workoutPlan, boolean editMode) {
        super(context, R.layout.row_main_training, workoutPlan);

        this.context = context;
        this.workoutPlan = workoutPlan;
        this.editMode = editMode;
        this.mainWatch = true;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (editMode){ //todo!
            rowView = inflater.inflate(R.layout.row_edit_tp,parent,false);
            TextView tvWorkoutName = (TextView) rowView.findViewById(R.id.row_tv_workout_edit);
            TextView tvTimesToDo = (TextView) rowView.findViewById(R.id.row_tv_times_todo_edit);

            tvWorkoutName.setText(workoutPlan.get(position).getWorkout().getName());
            tvTimesToDo.setText(String.valueOf(workoutPlan.get(position).getCount()) + " раз");

        } else {
            if (workoutPlan.size() > position) {
                if(mainWatch) {
                    rowView = inflater.inflate(R.layout.row_main_training, parent, false);

                    TextView tvWorkoutName = (TextView) rowView.findViewById(R.id.main_row_tv_workout);
                    TextView tvTimesToDo = (TextView) rowView.findViewById(R.id.main_row_times);

                    tvWorkoutName.setText(workoutPlan.get(position).getWorkout().getName());
                    tvTimesToDo.setText(String.valueOf(workoutPlan.get(position).getCount()) + " раз");
                }
                else {

                }
            }
        }

        return rowView;
    }

    public ArrayList<WorkoutPlan> getWorkoutPlan()
    {
        return workoutPlan;
    }
    public void setWorkoutPlan(ArrayList<WorkoutPlan> workoutPlan) {
        this.workoutPlan = workoutPlan;
        notifyDataSetChanged();
    }

    public void setMainWatch(boolean newMainWatch)
    {
        this.mainWatch = newMainWatch;
    }

}