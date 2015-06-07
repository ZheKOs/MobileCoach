package com.evdokimov.eugene.mobilecoach;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.ImageButton;


public class TrainingFragment extends Fragment{

    public static TrainingFragment newInstance(){
        TrainingFragment fragment = new TrainingFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.training_fragment,container,false);

        String[] workouts = {"УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ"};

        ListView lv_plan_workouts = (ListView) v.findViewById(R.id.lv_main_workouts);
        WorkoutsAdapter workoutsAdapter = new WorkoutsAdapter(getActivity(), workouts);
        lv_plan_workouts.setAdapter(workoutsAdapter);


        ImageButton btnStart = (ImageButton) v.findViewById(R.id.btn_set_time);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_set_time:
                        Dialog dialog = new TimePickerDialog(getActivity());

                        dialog.show();
                        break;
                }
            }
        });

        mainActivity = (MainActivity) getActivity();

        return v;
    }

    class WorkoutsAdapter extends ArrayAdapter<String>
    {

        private Context context;
        private String[] strings;

        public WorkoutsAdapter(Context context, String[] objects) {
            super(context, R.layout.row_main_training, objects);

            this.context = context;
            this.strings = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_main_training,parent,false);

            TextView tvWorkoutName = (TextView) rowView.findViewById(R.id.main_row_tv_workout);
            TextView tvTimesToDo = (TextView) rowView.findViewById(R.id.main_row_times);

            tvWorkoutName.setText(strings[position]);

            return rowView;
        }
    }

}
