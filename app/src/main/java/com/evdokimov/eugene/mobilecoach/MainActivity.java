package com.evdokimov.eugene.mobilecoach;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("ТРЕНИРОВКА"));
        tabLayout.addTab(tabLayout.newTab().setText("ПИТАНИЕ"));
        tabLayout.addTab(tabLayout.newTab().setText("СТАТИСТИКА"));

        String[] workouts = {"УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ"};

        ListView lv_plan_workouts = (ListView) findViewById(R.id.lv_main_workouts);
        WorkoutsAdapter workoutsAdapter = new WorkoutsAdapter(context, workouts);
        lv_plan_workouts.setAdapter(workoutsAdapter);


        ImageButton btnStart = (ImageButton) findViewById(R.id.btn_set_time);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_set_time:
                        Dialog dialog = new TimePickerDialog(context);

                        dialog.show();
                        break;
                }
            }
        });

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
