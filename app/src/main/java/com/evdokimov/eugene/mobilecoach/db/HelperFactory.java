package com.evdokimov.eugene.mobilecoach.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory {

    private static DBHelper dbHelper;

    public static DBHelper getDbHelper(){
        return dbHelper;
    }

    public static void setDbHelper(Context context){
        dbHelper = OpenHelperManager.getHelper(context,DBHelper.class);
    }

    public static void releaseDbHelper(){
        OpenHelperManager.releaseHelper();
        dbHelper = null;
    }

}
