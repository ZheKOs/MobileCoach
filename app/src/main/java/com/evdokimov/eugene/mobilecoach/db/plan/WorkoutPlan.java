package com.evdokimov.eugene.mobilecoach.db.plan;

import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.util.ArrayList;

public class WorkoutPlan {

    private long idPlan;
    private String name;
    private ArrayList<Workout> workout;
    private int count; //-1 means that it isn't set

    public WorkoutPlan (){
        this.setIdPlan(-1);
        this.setWorkout(null); //DANGER!!! NULL DETECTED
        this.setCount(-1);
    }

    public WorkoutPlan(long idPlan, ArrayList<Workout> workout, int count)
    {
        this.setIdPlan(idPlan);
        this.setWorkout(workout);
        this.setCount(count);
    }


    public long getIdPlan() {return idPlan;}
    public void setIdPlan(long idPlan) {this.idPlan = idPlan;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Workout> getWorkout() {return workout;}
    public void setWorkout(ArrayList<Workout> workout) {this.workout = workout;}

    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}
}
