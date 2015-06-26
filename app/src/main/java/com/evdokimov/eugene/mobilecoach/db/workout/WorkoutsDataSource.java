package com.evdokimov.eugene.mobilecoach.db.workout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.evdokimov.eugene.mobilecoach.db.DBHelper;

public class WorkoutsDataSource {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private String[] allColumns = {
            DBHelper.COLUMN_WORKOUT_ID, DBHelper.COLUMN_WORKOUT_FULL_NAME,
            DBHelper.COLUMN_WORKOUT_IMG_PATH
    };

    public WorkoutsDataSource(Context context) {dbHelper = new DBHelper(context, 1);}

    public SQLiteDatabase open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return db;
    }
    public void close()
    {
        if (dbHelper != null) dbHelper.close();
    }

    public Workout insertWorkout(Workout w)
    {
        ContentValues values = new ContentValues();
        values.put(allColumns[1], w.getName());
        values.put(allColumns[2], w.getInstruction());
        values.put(allColumns[3], w.getImgPath());

        long insertId = db.insert(DBHelper.TABLE_DISHES, null, values);

        return w;

    }
    public Workout insertWorkout(String name, String instruction, String imgPath)
    {
        ContentValues values = new ContentValues();
        values.put(allColumns[1], name);
        values.put(allColumns[2], instruction);
        values.put(allColumns[3], imgPath);

        long insertId = db.insert(DBHelper.TABLE_WORKOUTS, null, values);
        Cursor cursor = db.query(DBHelper.TABLE_WORKOUTS, allColumns, DBHelper.COLUMN_WORKOUT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Workout newComment = cursorToWorkout(cursor);
        cursor.close();

        Workout w = new Workout(insertId, name, instruction, imgPath);
        return w;
    }

    public void deleteWorkout(Workout w)
    {
        long id = w.getId();
        db.delete(dbHelper.TABLE_WORKOUTS, DBHelper.COLUMN_WORKOUT_ID + " = " + id, null);
    }

    private Workout cursorToWorkout(Cursor c)
    {
        Workout w = new Workout();
        w.setId(c.getLong(0));
        w.setName(c.getString(1));
        w.setInstruction(c.getString(2));
        w.setImgPath(c.getString(3));
        return w;
    }
}