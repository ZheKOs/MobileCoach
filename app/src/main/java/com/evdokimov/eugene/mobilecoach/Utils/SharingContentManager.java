package com.evdokimov.eugene.mobilecoach.Utils;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.sql.SQLException;
import java.util.ArrayList;

public class SharingContentManager {

    private Context context;

    private int typeOfContent;
    private String nameOfContent;

    private StringBuilder contentToSend;

    private boolean isContentReady;

    //base constructor
    /**
    * This class helps to prepare content and share it

     * @param typeOfContent
     * Defined types:
     * Workout - 0;
     * Dish - 1;
     * WorkoutPlan - 2;
     * NutritionPlan - 3;
     * Stats TODO! - 4;
     *
     */
    public SharingContentManager(Context context, int typeOfContent, String nameOfContent){
        this.context = context;

        setTypeOfContent(typeOfContent);
        setNameOfContent(nameOfContent);

        contentToSend = new StringBuilder();
        isContentReady = false;
    }

    //we ask class to handle all preparing of content and share our content
    public void prepareAndShareContent(){
        prepareContent();
        shareContent();
    }

    private void prepareContent(){
        switch (typeOfContent){
            case 0: //Workout
                Workout workout = getWorkoutFromDB();

                if (workout != null) {
                    contentToSend.append("Привет! Посмотри ка на это ")
                            .append("упражнение - " + workout.getName() + ".")
                            .append("\nВот инструкция к нему:\n")
                            .append(workout.getInstruction() + ".")
                            .append("\nMobileCoach - приложение для Вас и вашего тела!");
                    isContentReady = true;
                }else {
                    isContentReady = false;
                }

                break;
            case 1: //Dish
                Dish dish = getDishFromDB();
                if (dish != null){
                    contentToSend.append("Привет! Посмотри ка на это" + " "
                            + "блюдо - " + dish.getName() + "."
                            + " " + "В нём " + String.valueOf(dish.getKcal()) + " " + "калорий."
                            + "\n" + "Вот инструкция к нему:\n"
                            + dish.getReceipt() + "."
                            + "\n" + "MobileCoach - приложение для Вас и вашего тела!");
                    isContentReady = true;
                }else {
                    isContentReady = false;
                }
                break;
            case 2: //WorkoutPlan
                ArrayList<WorkoutPlan> workoutPlanItems = getWorkoutPlanItemsFromDB();
                if (workoutPlanItems != null){
                    contentToSend.append("Привет! У меня есть отличный план тренировки."
                            + "\n" + "Он называется \"" + nameOfContent + "\""
                            + "\n" + "Посмотри на список упражнений:");
                    int i = 1;
                    for (WorkoutPlan workoutPlan : workoutPlanItems){
                        contentToSend.append("\n " + i + " " + workoutPlan.getWorkout().getName() + " " + workoutPlan.getCount() + "раз");
                    }
                    contentToSend.append("\n" + "MobileCoach - приложение для Вас и вашего тела!");
                    isContentReady = true;
                }else {
                    isContentReady = false;
                }

                break;
            case 3: //NutritionPlan
                ArrayList<NutritionPlan> nutritionPlanItems = getNutritionPlanItemsFromDB();
                if (nutritionPlanItems != null){
                    contentToSend.append("Привет! У меня есть отличный план питания."
                            + "\n" + "Он называется \"" + nameOfContent + "\""
                            + "\n" + "Посмотри на список блюд:");
                    int i = 1;
                    for (NutritionPlan nutritionPlan : nutritionPlanItems){
                        contentToSend.append("\n" + i + " " + nutritionPlan.getDish().getName() + " " + nutritionPlan.getDish().getKcal() + " ккал");
                    }
                    contentToSend.append("\n" + "MobileCoach - приложение для Вас и вашего тела!");
                    isContentReady = true;
                }else {
                    isContentReady = false;
                }
                break;
            case 4: //Stats

                isContentReady = true;
                break;
        }
    }


    private Workout getWorkoutFromDB(){
        Workout workout;
        try {
            workout = HelperFactory.getDbHelper().getWorkoutDAO().getWorkoutByName(nameOfContent);
        }catch (SQLException e){
            Log.e("TAG_ERROR", "can't get workout");
            throw new RuntimeException(e);
        }
        return workout;
    }
    private Dish getDishFromDB(){
        Dish dish;
        try {
            dish = HelperFactory.getDbHelper().getDishDAO().getDishByName(nameOfContent);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return dish;
    }

    private ArrayList<WorkoutPlan> getWorkoutPlanItemsFromDB(){
        ArrayList<WorkoutPlan> workoutPlanItems;

        try {
            workoutPlanItems = new ArrayList<>(
                    HelperFactory.getDbHelper().getWorkoutPlanDAO().getWorkoutPlanByName(nameOfContent)
            );
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return workoutPlanItems;
    }
    private ArrayList<NutritionPlan> getNutritionPlanItemsFromDB(){
        ArrayList<NutritionPlan> nutritionPlanItems;

        try {
            nutritionPlanItems = new ArrayList<>(
                    HelperFactory.getDbHelper().getNutritionPlanDAO().getNutritionPlanByName(nameOfContent)
            );
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return nutritionPlanItems;
    }

    //sharing prepared content
    private void shareContent(){
        if (isContentReady) {
            String shareBody = contentToSend.toString();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            context.startActivity(Intent.createChooser(sharingIntent, "share_using"));
        }
    }

    //getters and setters
    public int getTypeOfContent() { return typeOfContent; }
    public void setTypeOfContent(int typeOfContent) { this.typeOfContent = typeOfContent; }

    public String getNameOfContent() { return nameOfContent; }
    public void setNameOfContent(String nameOfContent) { this.nameOfContent = nameOfContent; }
}
