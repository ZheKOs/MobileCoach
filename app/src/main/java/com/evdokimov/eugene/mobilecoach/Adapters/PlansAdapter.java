package com.evdokimov.eugene.mobilecoach.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Fragments.TrainingFragment;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Activities.WatchTrainPlanActivity;
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanSelectedListener;
import com.rey.material.widget.Button;

public class PlansAdapter extends ArrayAdapter<String>
{
    private Context context;
    private String[] strings;

    private OnWorkoutPlanSelectedListener mCallback;

    public PlansAdapter(Context context, Fragment fragment, String[] objects) {
        super(context, R.layout.row_main_plan_t_other, objects);

        this.context = context;
        this.strings = objects;

        // This makes sure that the container fragment has implemented
        // the callback interface. If not, it throws an exception
        try{
            mCallback = (OnWorkoutPlanSelectedListener) fragment;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                                        + " must implement OnWorkoutPlanSelectedListener");
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

//            if(position != 0) {
        rowView = inflater.inflate(R.layout.row_main_plan_t_other, parent, false);
        TextView tvPlanName = (TextView) rowView.findViewById(R.id.tv_plan_name);
        tvPlanName.setText(strings[position]);
        Button btn = (Button) rowView.findViewById(R.id.btn_main_plan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, WatchTrainPlanActivity.class);
//                intent.putExtra("planName",strings[position]);
//                context.startActivity(intent);
                mCallback.onWorkoutPlanSelected(position);
            }
        });
//            }else {
//                rowView = inflater.inflate(R.layout.row_main_plan_t_main, parent, false);
//                ListView lv_plan_workouts = (ListView) rowView.findViewById(R.id.lv_main_workouts);
//                WorkoutsAdapter workoutsAdapter = new WorkoutsAdapter(getActivity(), workouts);
//                lv_plan_workouts.setAdapter(workoutsAdapter);
//            }


        return rowView;
    }

}
