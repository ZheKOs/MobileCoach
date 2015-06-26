package com.evdokimov.eugene.mobilecoach.db.plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.evdokimov.eugene.mobilecoach.db.DBHelper;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;

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

        
    }

}
