package com.evdokimov.eugene.mobilecoach.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.evdokimov.eugene.mobilecoach.Activities.EditTrainingPlanActivity;
import com.evdokimov.eugene.mobilecoach.Fragments.TrainingFragment;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Activities.WatchTrainPlanActivity;
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanSelectedListener;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;

import java.text.SimpleDateFormat;

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
        final ImageButton more = (ImageButton) rowView.findViewById(R.id.ib_more_plan_item);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, more);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_training, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(context,
                                item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {
                            case R.id.edit_plan_mt:
                                Intent intent = new Intent(context, EditTrainingPlanActivity.class);
                                intent.putExtra("planName", strings[position]);
                                context.startActivity(intent);
                                break;
//                            case R.id.notify_plan_mt:
//                                final TimePickerDialog dialog = (TimePickerDialog) new TimePickerDialog(mainActivity,R.style.TimePickerDialog)
//                                        //.title("Поставить напоминание?")
//                                        .positiveAction("Поставить").negativeAction("Отмена");
//                                dialog.positiveActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        snackBar.text("Напоминание в " + dialog.getFormattedTime(new SimpleDateFormat("H:mm")));
//                                        dialog.dismiss();
//                                        snackBar.show(mainActivity);
//                                    }
//                                });
//                                dialog.negativeActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Toast.makeText(mainActivity, "Отменено", Toast.LENGTH_SHORT).show();
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                                dialog.show();
//                                break;
//                            case R.id.share_plan_mt:
//                                final SimpleDialog simpleDialog = new SimpleDialog(mainActivity);
//                                simpleDialog.title("Выберите план");
//                                final String[] plans = new String[]{"ПЛАН1", "ПЛАН2", "ПЛАН3", "ПЛАН4", "ПЛАН5", "ПЛАН6", "ПЛАН7", "ПЛАН8"};
//                                simpleDialog.items(plans, -1);
//                                simpleDialog.positiveAction("Выбрать").positiveActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        Toast.makeText(mainActivity,
//                                                "Выбран план - " + plans[simpleDialog.getSelectedIndex()],
//                                                Toast.LENGTH_SHORT).show();
//                                        simpleDialog.dismiss();
//                                    }
//                                });
//                                simpleDialog.negativeAction("Отмена").negativeActionClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        simpleDialog.dismiss();
//                                    }
//                                });
//                                simpleDialog.show();
//                                break;
//                            case R.id.delete_plan_mt:
//                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
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
