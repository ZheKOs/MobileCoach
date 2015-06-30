package com.evdokimov.eugene.mobilecoach.db.plan;

import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.util.ArrayList;

public class WorkoutPlan {

    private long idPlan;
    private String name;
    private long idWorkout;
    private int count; //-1 means that it isn't set

    public WorkoutPlan (){
        this.setIdPlan(-1);
        this.setIdWorkout(-1); //DANGER!!! NULL DETECTED
        this.setCount(-1);
    }

    public WorkoutPlan(long idPlan,long idWorkout, int count)
    {
        this.setIdPlan(idPlan);
        this.setIdWorkout(idWorkout);
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

    public long getIdWorkout() {return idWorkout;}
    public void setIdWorkout(long idWorkout) {this.idWorkout = idWorkout;}

    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}
}
