package com.evdokimov.eugene.mobilecoach.db.plan;


import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

public class WorkoutPlanItem {

    private Workout workout;
    private int count;

    public WorkoutPlanItem(Workout workout, int count){

        this.setWorkout(workout);
        this.setCount(count);

    }

    public Workout getWorkout() {
        return workout;
    }
    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
