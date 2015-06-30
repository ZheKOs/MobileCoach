package com.evdokimov.eugene.mobilecoach.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.util.ArrayList;

public class WorkoutsAdapter extends ArrayAdapter<Workout>
{

    private Context context;
    private ArrayList<Workout> workouts;
    private boolean editMode;
    private View rowView;

    public WorkoutsAdapter(Context context, ArrayList<Workout> workouts, boolean editMode) {
        super(context, R.layout.row_main_training, workouts);

        this.context = context;
        this.workouts = workouts;
        this.editMode = editMode;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (editMode){ //todo!
            rowView = inflater.inflate(R.layout.row_edit_tp,parent,false);
            TextView tvWorkoutName = (TextView) rowView.findViewById(R.id.row_tv_workout_edit);
            TextView tvTimesToDo = (TextView) rowView.findViewById(R.id.row_tv_times_todo_edit);

            tvWorkoutName.setText(workouts.get(position).getName());

        } else {
            if (workouts.size() > position) {
                rowView = inflater.inflate(R.layout.row_main_training, parent, false);

                TextView tvWorkoutName = (TextView) rowView.findViewById(R.id.main_row_tv_workout);
                TextView tvTimesToDo = (TextView) rowView.findViewById(R.id.main_row_times);


                tvWorkoutName.setText(workouts.get(position).getName());
            }
        }

        return rowView;
    }

    public ArrayList<Workout> getWorkouts()
    {
        return workouts;
    }
    public void setWorkouts(ArrayList<Workout> workouts) { this.workouts = workouts; }

}