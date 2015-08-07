package com.evdokimov.eugene.mobilecoach.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Activities.DishActivity;
import com.evdokimov.eugene.mobilecoach.Activities.WorkoutWatchActivity;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.evdokimov.eugene.mobilecoach.db.dish.DishDAO;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.evdokimov.eugene.mobilecoach.db.workout.WorkoutDAO;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ViewHolder> {

    Context context;
    boolean isWorkoutMode;

    ArrayList<Workout> workouts;
    ArrayList<Dish> dishes;

    int resizeTargetWidth, resizeTargetHeight;

    public AllItemsAdapter(Context mContext, boolean isWorkoutMode){
        this.context = mContext;
        this.isWorkoutMode = isWorkoutMode;

        if (isWorkoutMode){
            setWorkouts();
        }else {
            setDishes();
        }

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        resizeTargetWidth = metrics.widthPixels / 2 - 30;
        resizeTargetHeight = resizeTargetWidth;
    }

    public void setWorkouts(){
        try {
            WorkoutDAO workoutDAO = HelperFactory.getDbHelper().getWorkoutDAO();
            this.workouts = new ArrayList<>(
                    workoutDAO.getAllWorkouts()
            );
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't get all workouts");
            throw new RuntimeException(e);
        }
    }
    public void setDishes(){
        try {
            DishDAO dishDAO = HelperFactory.getDbHelper().getDishDAO();
            this.dishes = new ArrayList<>(
                    dishDAO.getAllDish()
            );
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't get all workouts");
            throw new RuntimeException(e);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView tvWorkout;
        public Button btnWorkout;

        public ImageView iv;
        public TextView tvDishName;
        public TextView tvKcal;
        public CheckBox checkBox;
        //final TransitionDrawable transition = (TransitionDrawable) checkBox.getBackground();
        public Button btnInfo;
        public ViewHolder(View v) {
            super(v);

            //workouts
            tvWorkout = (TextView) v.findViewById(R.id.row_tv_workout_item);
            btnWorkout = (Button) v.findViewById(R.id.btn_workout_item);

            //dishes
            iv = (ImageView) v.findViewById(R.id.iv_nutrition_card);
            tvDishName = (TextView) v.findViewById(R.id.tv_dish_name);
            tvKcal = (TextView) v.findViewById(R.id.tv_kcal_dish);
            btnInfo = (Button) v.findViewById(R.id.btn_info_nutrition_card);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        if (isWorkoutMode){
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_workout, parent, false);
        }else {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_nutrition_card, parent, false);
        }

        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (isWorkoutMode){
            holder.tvWorkout.setText(workouts.get(position).getName());
            holder.btnWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WorkoutWatchActivity.class);
                    intent.putExtra("workoutName", workouts.get(position).getName());
                    context.startActivity(intent);
                }
            });
        }else {

            holder.btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DishActivity.class);
                    intent.putExtra("dishName", dishes.get(position).getName());
                    context.startActivity(intent);
                }
            });

            holder.tvDishName.setText(dishes.get(position).getName());
            holder.tvKcal.setText(String.valueOf(dishes.get(position).getKcal()));

            Picasso.with(context)
                    .load(dishes.get(position).getImgPath())
                    .resize(resizeTargetWidth,resizeTargetHeight)
                    .centerCrop()
                    .into(holder.iv);
        }
    }

    @Override
    public int getItemCount() {
        if (isWorkoutMode){
            return workouts.size();
        }
        return dishes.size();
    }



}
