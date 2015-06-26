package com.evdokimov.eugene.mobilecoach.db.dish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.evdokimov.eugene.mobilecoach.db.DBHelper;

public class DishesDataSource {

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private String[] allColumns = {
            DBHelper.COLUMN_DISH_ID, DBHelper.COLUMN_DISH_FULL_NAME,
            DBHelper.COLUMN_DISH_RECEIPT, DBHelper.COLUMN_DISH_KCALORIES,
            DBHelper.COLUMN_DISH_IMG_PATH
    };

    public DishesDataSource(Context context) {dbHelper = new DBHelper(context, 1);}

    public SQLiteDatabase open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return db;
    }
    public void close()
    {
        if (dbHelper != null) dbHelper.close();
    }

    public Dish insertDish(Dish d)
    {
        ContentValues values = new ContentValues();
        values.put(allColumns[1], d.getName());
        values.put(allColumns[2], d.getReceipt());
        values.put(allColumns[3], d.getKcal());
        values.put(allColumns[4], d.getImgPath());

        long insertId = db.insert(DBHelper.TABLE_DISHES, null, values);

        return d;

    }
    public Dish insertDish(String name, String receipt, double kcal, String img_path)
    {
        ContentValues values = new ContentValues();
        values.put(allColumns[1], name);
        values.put(allColumns[2], receipt);
        values.put(allColumns[3], kcal);
        values.put(allColumns[4], img_path);

        long insertId = db.insert(DBHelper.TABLE_DISHES, null, values);
        Cursor cursor = db.query(DBHelper.TABLE_DISHES, allColumns, DBHelper.COLUMN_DISH_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Dish newComment = cursorToDish(cursor);
        cursor.close();

        Dish d = new Dish(insertId, name, receipt, kcal, img_path);
        return d;
    }

    public void deleteDish(Dish d)
    {
        long id = d.getId();
        db.delete(dbHelper.TABLE_DISHES, DBHelper.COLUMN_DISH_ID + " = " + id, null);
    }

    private Dish cursorToDish(Cursor c)
    {
        Dish d = new Dish();
        d.setId(c.getLong(0));
        d.setName(c.getString(1));
        d.setReceipt(c.getString(2));
        d.setKcal(c.getDouble(3));
        d.setImgPath(c.getString(4));
        return d;
    }

}
