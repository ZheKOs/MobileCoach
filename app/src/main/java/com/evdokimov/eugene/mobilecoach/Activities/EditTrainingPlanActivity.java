package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.evdokimov.eugene.mobilecoach.Adapters.WorkoutsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.mobeta.android.dslv.DragSortListView;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditTrainingPlanActivity extends AppCompatActivity {

    public static final int RESULT_SAVE = 3;
    public static final int RESULT_DELETE = 4;


    private int mode; //define situation: edit or create

    private WorkoutsAdapter workoutsAdapter;

    private FloatingActionButton btnAdd;

    //private RelativeLayout instruction; //layout is instruction for empty list

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    Workout item = workoutsAdapter.getItem(from);

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

        //Now let's grab ArrayList which should be "packed" and send using Parcel
        final ArrayList<Workout> workouts =
                new ArrayList<Workout>(getIntent()
                        .<Workout>getParcelableArrayListExtra("workouts")); //yeap, this is how it "unpacks" in real code. See Workout.class

        //instruction = (RelativeLayout) findViewById(R.id.instruction); //TODO Instruction what to do if list is empty

        DragSortListView lv = (DragSortListView) findViewById(R.id.dslv_edit_tp);

        lv.setDropListener(onDrop);
        lv.setRemoveListener(onRemove);
        lv.setDragScrollProfile(ssProfile);

        workoutsAdapter = new WorkoutsAdapter(this,workouts,true);
        lv.setAdapter(workoutsAdapter);

//        if (workoutsAdapter.getCount()>0)
//            instruction.setVisibility(View.GONE);

        View.OnClickListener listener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent;
                switch (view.getId())
                {
                    case R.id.btn_back_etp:
                        setResult(RESULT_CANCELED);
                        finish();
                        break;
                    case R.id.btn_save_plan_etp:
                        resultIntent = new Intent();
                        resultIntent.putExtra("workouts_back",workoutsAdapter.getWorkouts());
                        setResult(RESULT_SAVE, resultIntent);
                        finish();
                        break;
                    case R.id.btn_delete_plan_etp:
                        setResult(RESULT_DELETE);
                        break;
                }
            }
        };

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back_etp);
        btnBack.setOnClickListener(listener);
        ImageButton btnSave = (ImageButton) findViewById(R.id.btn_save_plan_etp);
        btnSave.setOnClickListener(listener);

        btnAdd = (FloatingActionButton) findViewById(R.id.btn_edit_tp_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workoutsAdapter.add(new Workout(0, "Test", "", ""));
                workoutsAdapter.notifyDataSetChanged();
            }
        });

    }
}
