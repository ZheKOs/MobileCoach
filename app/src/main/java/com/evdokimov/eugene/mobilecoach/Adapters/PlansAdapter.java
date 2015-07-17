package com.evdokimov.eugene.mobilecoach.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Activities.WatchTrainPlanActivity;
import com.rey.material.widget.Button;

public class PlansAdapter extends ArrayAdapter<String>
{
    private Context context;
    private String[] strings;

    public PlansAdapter(Context context, String[] objects) {
        super(context, R.layout.row_main_plan_t_other, objects);

        this.context = context;
        this.strings = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
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
                Intent intent = new Intent(context, WatchTrainPlanActivity.class);
                context.startActivity(intent);
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
