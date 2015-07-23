package com.evdokimov.eugene.mobilecoach.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;

import com.rey.material.app.Dialog;
import com.rey.material.widget.EditText;

import java.sql.SQLException;
import java.util.ArrayList;

public class WorkoutsAdapter extends ArrayAdapter<WorkoutPlan>
{

    private Context context;
    private ArrayList<WorkoutPlan> workoutPlan;
    private boolean editMode;
    private boolean mainWatch;
    private View rowView;

    public WorkoutsAdapter(Context context, ArrayList<WorkoutPlan> workoutPlan, boolean editMode) {
        super(context, R.layout.row_workout_plan, workoutPlan);

        this.context = context;
        this.workoutPlan = workoutPlan;
        this.editMode = editMode;
        this.mainWatch = true;

    }

    public WorkoutsAdapter(Context context, ArrayList<WorkoutPlan> workoutPlan, boolean editMode, boolean mainWatch) {
        super(context, R.layout.row_workout_plan, workoutPlan);

        this.context = context;
        this.workoutPlan = workoutPlan;
        this.editMode = editMode;
        this.mainWatch = mainWatch;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (!workoutPlan.isEmpty()) {
            if (editMode) {
                rowView = inflater.inflate(R.layout.row_edit_tp, parent, false);
                final TextView tvWorkoutNameEdit = (TextView) rowView.findViewById(R.id.row_tv_workout_edit);
                final TextView tvTimesToDoEdit = (TextView) rowView.findViewById(R.id.row_tv_times_todo_edit);

                rowView.setBackgroundColor(Color.WHITE);
                tvWorkoutNameEdit.setTextColor(Color.BLACK);
                tvTimesToDoEdit.setBackgroundColor(context.getResources().getColor(R.color.dgrey));

                tvWorkoutNameEdit.setText(workoutPlan.get(position).getWorkout().getName());

                tvTimesToDoEdit.setText(String.valueOf(workoutPlan.get(position).getCount()) + " " + "раз");
                tvTimesToDoEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.title("Количество для упражнения")
                                .positiveAction("Ввести")
                                .negativeAction("Отмена");
                        View contentView = View.inflate(getContext(), R.layout.dialog_edit_workout_count, null);
                        final EditText etCount = (EditText) contentView.findViewById(R.id.et_dialog_edit_workout_count);
                        etCount.setText(String.valueOf(workoutPlan.get(position).getCount()));
                        dialog.setContentView(contentView);
                        dialog.positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (etCount.getText().length() > 0 && !etCount.getText().toString().equals("0")) {
                                    int count = Integer.valueOf(etCount.getText().toString());
                                    try {
                                        WorkoutPlan wPlan = HelperFactory.getDbHelper().getWorkoutPlanDAO()
                                                .getWorkoutPlanByOrder(position);
                                        wPlan.setCount(count);
                                        HelperFactory.getDbHelper().getWorkoutPlanDAO().update(wPlan);
                                        workoutPlan.get(position).setCount(count);
                                        notifyDataSetChanged();
                                        dialog.dismiss();

                                    } catch (SQLException e) {
                                        Log.e("TAG_ERROR", "can't get workout");
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        });
                        dialog.negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                });

            } else {
                if (workoutPlan.size() > position) {
                    rowView = inflater.inflate(R.layout.row_workout_plan, parent, false);

                    TextView tvWorkoutName = (TextView) rowView.findViewById(R.id.row_tv_workout);
                    TextView tvTimesToDo = (TextView) rowView.findViewById(R.id.row_tv_times_todo);

                    tvWorkoutName.setText(workoutPlan.get(position).getWorkout().getName());
                    tvTimesToDo.setText(String.valueOf(workoutPlan.get(position).getCount()) + " раз");

                    rowView.setBackgroundColor(Color.WHITE);
                    tvWorkoutName.setTextColor(Color.BLACK);
                    tvTimesToDo.setBackgroundColor(context.getResources().getColor(R.color.dgrey));

                    if (mainWatch) {
                        rowView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        tvWorkoutName.setTextColor(Color.WHITE);
                        tvTimesToDo.setBackgroundColor(context.getResources().getColor(R.color.colorAccentDark));
                    }
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
    }

    public void setMainWatch(boolean newMainWatch)
    {
        this.mainWatch = newMainWatch;
    }

}