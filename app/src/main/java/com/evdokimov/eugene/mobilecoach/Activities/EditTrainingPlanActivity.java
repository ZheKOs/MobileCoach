package com.evdokimov.eugene.mobilecoach.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.mobeta.android.dslv.DragSortListView;
import com.rey.material.widget.ImageButton;

import java.util.List;

public class EditTrainingPlanActivity extends AppCompatActivity {

    private int mode; //define situation: edit or create

    private WorkoutsAdapter workoutsAdapter;

    //private RelativeLayout instruction;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    String item=workoutsAdapter.getItem(from);

                    workoutsAdapter.notifyDataSetChanged();
                    workoutsAdapter.remove(item);
                    workoutsAdapter.insert(item, to);
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tp);

        final String[] workouts = {"УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ","УПРАЖНЕНИЕ"};

        //instruction = (RelativeLayout) findViewById(R.id.instruction); //TODO Instruction what to do if list is empty

        DragSortListView lv = (DragSortListView) findViewById(R.id.lv_edit_tp);

        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setDragScrollProfile(ssProfile);

        workoutsAdapter = new WorkoutsAdapter(this,workouts,true);
        lv.setAdapter(workoutsAdapter);

//        if (workoutsAdapter.getCount()>0)
//            instruction.setVisibility(View.GONE);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back_etp);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btn_back_etp:
                        finish();
                        break;
                }
            }
        });

    }
}
