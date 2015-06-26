package com.evdokimov.eugene.mobilecoach.db.plan;

public class WorkoutPlan {

    private long idPlan;
    private long idWorkout;
    private int count;       //-1 means it isn't set

    public WorkoutPlan (){
        this.setIdPlan(-1);
        this.setIdWorkout(-1);
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

    public long getIdWorkout() {return idWorkout;}
    public void setIdWorkout(long idWorkout) {this.idWorkout = idWorkout;}

    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}

}
