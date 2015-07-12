package com.evdokimov.eugene.mobilecoach.db.plan;

import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;

@DatabaseTable(tableName = "workout_plans")
public class WorkoutPlan {

    @DatabaseField(generatedId = true)
    private long idPlan;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;
    @ForeignCollectionField(eager = true)
    private ArrayList<Workout> workouts;
    @DatabaseField()
    private ArrayList<Integer> counts; //-1 means that it isn't set

    public WorkoutPlan (){
//        this.setIdPlan(-1);
//        this.setWorkout(null); //DANGER!!! NULL DETECTED
//        this.setCount(-1);
    }

    public WorkoutPlan(long idPlan, ArrayList<Workout> workouts, ArrayList<Integer> counts)
    {
        this.setIdPlan(idPlan);
        this.setWorkout(workouts);
        this.setCount(counts);
    }

    public void addWorkout(Workout value){
        //value.setPlan(this);
        try {
            HelperFactory.getDbHelper().getWorkoutDAO().create(value);
            workouts.add(value);
        }catch (SQLException e){}
    }
    public void removeWorkout(Workout value){
        try {
            workouts.remove(value);
            HelperFactory.getDbHelper().getWorkoutDAO().delete(value);
        }catch (SQLException e){}
    }

    public long getIdPlan() {return idPlan;}
    public void setIdPlan(long idPlan) {this.idPlan = idPlan;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Workout> getWorkout() {return workouts;}
    public void setWorkout(ArrayList<Workout> workouts) {this.workouts = workouts;}

    public ArrayList<Integer> getCount() {return counts;}
    public void setCount(ArrayList<Integer> counts) {this.counts = counts;}
}
