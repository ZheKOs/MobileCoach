package com.evdokimov.eugene.mobilecoach.db.plan;

import android.os.Parcel;
import android.os.Parcelable;

import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "workout_plans")
public class WorkoutPlan implements Parcelable {

    @DatabaseField(generatedId = true)
    private int idPlan;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "planName")
    private String name;
    @DatabaseField(canBeNull = true, dataType = DataType.INTEGER)
    private int order;
    @DatabaseField(foreign = true, canBeNull = false)
    private Workout workout;
    @DatabaseField(dataType = DataType.INTEGER)
    private int count; //-1 means that it isn't set

    public WorkoutPlan (){

    }

    public WorkoutPlan(String name, int order, Workout workouts, int count)
    {
        this.setName(name);
        this.setOrder(order);
        this.setWorkout(workout);
        this.setCount(count);
    }

    public int getIdPlan() {return idPlan;}
    public void setIdPlan(int idPlan) {this.idPlan = idPlan;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getOrder() {return order;}
    public void setOrder(int order) {this.order = order;}

    public Workout getWorkout() {return workout;}
    public void setWorkout(Workout workout) {this.workout = workout;}

    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.idPlan);
//        dest.writeString(this.name);
//        dest.writeParcelable(this.workout, 0);
//        dest.writeInt(this.count);
//    }
//
//    protected WorkoutPlan(Parcel in) {
//        this.idPlan = in.readInt();
//        this.name = in.readString();
//        this.workout = in.readParcelable(Workout.class.getClassLoader());
//        this.count = in.readInt();
//    }
//
//    public static final Creator<WorkoutPlan> CREATOR = new Creator<WorkoutPlan>() {
//        public WorkoutPlan createFromParcel(Parcel source) {
//            return new WorkoutPlan(source);
//        }
//
//        public WorkoutPlan[] newArray(int size) {
//            return new WorkoutPlan[size];
//        }
//    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idPlan);
        dest.writeString(this.name);
        dest.writeParcelable(this.workout, 0);
        dest.writeInt(this.count);
    }

    protected WorkoutPlan(Parcel in) {
        this.idPlan = in.readInt();
        this.name = in.readString();
        this.workout = in.readParcelable(Workout.class.getClassLoader());
        this.count = in.readInt();
    }

    public static final Creator<WorkoutPlan> CREATOR = new Creator<WorkoutPlan>() {
        public WorkoutPlan createFromParcel(Parcel source) {
            return new WorkoutPlan(source);
        }

        public WorkoutPlan[] newArray(int size) {
            return new WorkoutPlan[size];
        }
    };
}
