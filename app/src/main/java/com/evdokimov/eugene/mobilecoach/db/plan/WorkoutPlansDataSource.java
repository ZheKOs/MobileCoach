package com.evdokimov.eugene.mobilecoach.db.plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.evdokimov.eugene.mobilecoach.db.DBHelper;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

import java.util.ArrayList;

public class WorkoutPlansDataSource {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private String[] allColumns = {
            DBHelper.COLUMN_PLAN_WORKOUT_ID, DBHelper.COLUMN_PLAN_WORKOUT_WORKOUT_ID,
            DBHelper.COLUMN_PLAN_WORKOUT_COUNT
    };

    public WorkoutPlansDataSource(Context context) {dbHelper = new DBHelper(context, 1);}

    public SQLiteDatabase open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return db;
    }

    public void close()
    {
        if (dbHelper != null) dbHelper.close();
    }

    public WorkoutPlan insertWorkoutPlan(long idPlan, long idWorkout, int count)
    {
        ContentValues values = new ContentValues();
        values.put(allColumns[0], idPlan);
        values.put(allColumns[1], idWorkout);
        values.put(allColumns[2], count);


        long insertId = db.insert(DBHelper.TABLE_PLAN_WORKOUT, null, values);
        Cursor cursor = db.query(DBHelper.TABLE_PLAN_WORKOUT, allColumns, DBHelper.COLUMN_PLAN_WORKOUT_ID + " = " + insertId,
                null,null,null,null);
        cursor.moveToFirst();
        WorkoutPlan workoutPlan = new WorkoutPlan(idPlan,getArrayListOfWorkouts(idPlan),count);
        return workoutPlan;
    }

    public void deleteWorkoutPlan(WorkoutPlan workoutPlan)
    {
        long id = workoutPlan.getIdPlan();
        db.delete(dbHelper.TABLE_PLAN_WORKOUT,DBHelper.COLUMN_PLAN_WORKOUT_ID + "=" + id,null);
    }

    private WorkoutPlan cursorToWorkoutPlan(Cursor c)
    {
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setIdPlan(c.getLong(0));
        workoutPlan.setWorkout(getArrayListOfWorkouts(c.getLong(0)));
        workoutPlan.setCount(c.getInt(2));

        return workoutPlan;
    }

    private ArrayList<Workout> getArrayListOfWorkouts(long planId)
    {
        ArrayList<Workout> workoutsTmp = new ArrayList<Workout>();
        Cursor cursor = db.query(
                dbHelper.TABLE_PLAN_WORKOUT,
                new String[]{dbHelper.COLUMN_PLAN_WORKOUT_WORKOUT_ID},
                dbHelper.COLUMN_PLAN_WORKOUT_ID + " = " + planId,null,null,null,null
        );
        return workoutsTmp;
    }


}
