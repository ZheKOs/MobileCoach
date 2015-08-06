package com.evdokimov.eugene.mobilecoach.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanChangedListener;
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanSelectedListener;
import com.evdokimov.eugene.mobilecoach.Utils.SharingContentManager;
import com.evdokimov.eugene.mobilecoach.db.DBHelper;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.SnackBar;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class PlansAdapter extends ArrayAdapter<String>
{
    private Context context;
    private String[] strings;

    private OnWorkoutPlanSelectedListener onWPSelectedCallback;
    private OnWorkoutPlanChangedListener onWPChangedCallback;

    public PlansAdapter(Context context, Fragment fragment, String[] objects) {
        super(context, R.layout.row_main_plan_t_other, objects);

        this.context = context;
        this.strings = objects;

        // This makes sure that the container fragment has implemented
        // the callback interface. If not, it throws an exception
        try{
            onWPSelectedCallback = (OnWorkoutPlanSelectedListener) fragment;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                                        + " must implement OnWorkoutPlanSelectedListener");
        }
        try{
            onWPChangedCallback = (OnWorkoutPlanChangedListener) fragment;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnWorkoutPlanChangedListener");
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;

        final String curPlan = strings[position];

//            if(position != 0) {
        rowView = inflater.inflate(R.layout.row_main_plan_t_other, parent, false);
        TextView tvPlanName = (TextView) rowView.findViewById(R.id.tv_plan_name);
        tvPlanName.setText(curPlan);
        final ImageButton more = (ImageButton) rowView.findViewById(R.id.ib_more_plan_item);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SnackBar snackbar = new SnackBar(context);
                snackbar.applyStyle(R.style.SnackBarSingleLine)
                        .singleLine(false) //to be sure :D
                        .actionText("Отмена")
                        .actionClickListener(new SnackBar.OnActionClickListener() {
                            @Override
                            public void onActionClick(SnackBar snackBar, int i) {
                                Toast.makeText(context, "Отменено", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .duration(3500)
                        .actionTextColor(context.getResources().getColor(R.color.colorAccent))
                        .textColor(Color.WHITE);

                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, more);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_training_item, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.set_pickedplan:
                                SharedPreferences sharedPref = context.getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putString("pickedplan", curPlan);
                                editor.apply();

                                onWPChangedCallback.onWorkoutPlanChanged();
                                break;
                            case R.id.edit_plan_mt:
                                Intent intent = new Intent(context, EditTrainingPlanActivity.class);
                                intent.putExtra("planName", curPlan);
                                context.startActivity(intent);
                                break;
                            case R.id.notify_plan_mt:
                                final TimePickerDialog dialog = (TimePickerDialog) new TimePickerDialog(context,R.style.TimePickerDialog)
                                        //.title("Поставить напоминание?")
                                        .positiveAction("Поставить").negativeAction("Отмена");
                                dialog.positiveActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.text("Напоминание в " + dialog.getFormattedTime(new SimpleDateFormat("H:mm")));
                                        dialog.dismiss();
                                        snackbar.show((Activity) context);
                                    }
                                });
                                dialog.negativeActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(context, "Отменено", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                                break;
                            case R.id.share_plan_mt:
                                SharingContentManager sharingContentManager = new SharingContentManager(context, 2, curPlan);
                                sharingContentManager.prepareAndShareContent();
                                break;
                            case R.id.delete_plan_mt:
                                final Dialog deleteDialog = new Dialog(context);
                                deleteDialog
                                        .title("Удалить план")
                                        .positiveActionTextColor(Color.parseColor("#F44336"))
                                        .positiveAction("УДАЛИТЬ")
                                        .positiveActionClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                deletePlan(position);
                                                deleteDialog.dismiss();
                                            }
                                        })
                                        .negativeActionTextColor(Color.parseColor("#727272"))
                                        .negativeAction("ОТМЕНА")
                                        .negativeActionClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                deleteDialog.dismiss();
                                            }
                                        });
                                deleteDialog.show();
                                break;
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
                onWPSelectedCallback.onWorkoutPlanSelected(position);
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

    private void deletePlan(int pos){
        DBHelper dbHelper = HelperFactory.getDbHelper();
        try {
            dbHelper.getWorkoutPlanDAO().delete(
                    dbHelper.getWorkoutPlanDAO().getWorkoutPlanByName(strings[pos])
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SharedPreferences sharedPref = context.getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        String workoutPlanName = sharedPref.getString("pickedplan", "_empty_");
        if (strings[pos].equals(workoutPlanName)){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pickedplan", "_empty_");
            editor.apply();
        }
        onWPChangedCallback.onWorkoutPlanChanged();
        this.notifyDataSetInvalidated();
    }

}
