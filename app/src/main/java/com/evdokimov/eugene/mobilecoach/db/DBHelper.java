package com.evdokimov.eugene.mobilecoach.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.evdokimov.eugene.mobilecoach.db.dish.DishDAO;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlanDAO;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlan;
import com.evdokimov.eugene.mobilecoach.db.plan.WorkoutPlanDAO;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.evdokimov.eugene.mobilecoach.db.workout.WorkoutDAO;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    private static final String DB_PATH = "/data/data/com.example.eugene.mobilecoach/databases/";
    private static final String DB_NAME = "mobilecoach.db";
    private static final int DB_VERSION = 1;

    private WorkoutDAO workoutDAO = null;
    private DishDAO dishDAO = null;

    private WorkoutPlanDAO workoutPlanDAO = null;
    private NutritionPlanDAO nutritionPlanDAO = null;

    private SharedPreferences sharedPref;

    /*
    fields and commands for common creating db
     */
//    public static String TABLE_DISHES = "dishes";
//    public static String COLUMN_DISH_ID = "id_dish";
//    public static String COLUMN_DISH_FULL_NAME = "full_name";
//    public static String COLUMN_DISH_RECEIPT = "receipt";
//    public static String COLUMN_DISH_KCALORIES = "kcalories";
//    public static String COLUMN_DISH_IMG_PATH = "img_path";
//    private static String CREATE_DISH_TABLE =
//            "create table "
//                    + TABLE_DISHES + "("
//                    + COLUMN_DISH_ID + " integer primary key autoincrement, "
//                    + COLUMN_DISH_FULL_NAME + " string not null, "
//                    + COLUMN_DISH_RECEIPT + " string, "
//                    + COLUMN_DISH_KCALORIES + " double, "
//                    + COLUMN_DISH_IMG_PATH + " string" +");";
//
//    /////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static String TABLE_WORKOUTS = "workouts";
//    public static String COLUMN_WORKOUT_ID = "id_workout";
//    public static String COLUMN_WORKOUT_FULL_NAME = "full_name";
//    public static String COLUMN_WORKOUT_INSTRUCTION = "instruction";
//    public static String COLUMN_WORKOUT_IMG_PATH = "img_path";
//    private static String CREATE_WORKOUT_TABLE =
//            "create table "
//                    + TABLE_WORKOUTS + "("
//                    + COLUMN_WORKOUT_ID + " integer primary key autoincrement, "
//                    + COLUMN_WORKOUT_FULL_NAME + " string not null, "
//                    + COLUMN_WORKOUT_INSTRUCTION + " string, "
//                    + COLUMN_WORKOUT_IMG_PATH + " string" +");";
//
//    //////////////////////////////////////////////////////////////////////////////////////////
//
//    public static String TABLE_PLAN_WORKOUT = "workout_plans";
//    public static String COLUMN_PLAN_WORKOUT_ID = "id_plan_workout";
//    public static String COLUMN_PLAN_WORKOUT_WORKOUT_ID = "workout_id";
//    public static String COLUMN_PLAN_WORKOUT_COUNT = "count";
//    private static String CREATE_PLAN_WORKOUT =
//            "create table "
//                    + TABLE_PLAN_WORKOUT + "("
//                    + COLUMN_PLAN_WORKOUT_ID + " integer primary key autoincrement, "
//                    + COLUMN_PLAN_WORKOUT_WORKOUT_ID + " integer not null "
//                    + COLUMN_PLAN_WORKOUT_COUNT + " integer not null" + ");";
//
//    public static String TABLE_PLAN_NUTRITION = "nutrition_plans";
//    public static String COLUMN_PLAN_NUTRITION_ID = "id_plan_nutrition";
//    public static String COLUMN_PLAN_NUTRITION_DISH_ID = "dish_id";
//    private static String CREATE_PLAN_NUTRITION =
//            "create table "
//                    + TABLE_PLAN_NUTRITION + "("
//                    + COLUMN_PLAN_NUTRITION_ID + " integer primary key autoincrement, "
//                    + COLUMN_PLAN_NUTRITION_DISH_ID + " integer not null" + ");";



    private SQLiteDatabase mDataBase;
    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database,ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Workout.class);
            TableUtils.createTable(connectionSource, WorkoutPlan.class);
        }catch (SQLException e){
            Log.e(TAG, "error creating DB " + DB_NAME);
            throw new RuntimeException(e);
        }

        Workout squats, pushups, pullups, plank, dolphin, situps;

        try {
            squats = new Workout();
            squats.setName("Приседания");
            getWorkoutDAO().create(squats);

            pushups = new Workout();
            pushups.setName("Отжимания");
            getWorkoutDAO().create(pushups);

            pullups = new Workout();
            pullups.setName("Подтягивание");
            getWorkoutDAO().create(pullups);

            plank = new Workout();
            plank.setName("Планка");
            getWorkoutDAO().create(plank);

            dolphin = new Workout();
            dolphin.setName("Дельфин");
            getWorkoutDAO().create(dolphin);

            situps = new Workout();
            situps.setName("Скручивания");
            getWorkoutDAO().create(situps);

        } catch (SQLException e){
            Log.e("TAG_ERROR", "can't create workouts");
            throw new RuntimeException(e);
        }

        final ArrayList<Workout> workouts = new ArrayList<>();
        workouts.add(squats);
        workouts.add(pushups);
        workouts.add(pullups);
        workouts.add(squats);
        workouts.add(plank);
        workouts.add(dolphin);
        workouts.add(situps);

        ArrayList<Integer> counts = new ArrayList<>();
        counts.add(10); counts.add(5); counts.add(5); counts.add(15); counts.add(30); counts.add(15); counts.add(25);
        try {
            ArrayList<WorkoutPlan> workoutPlan = new ArrayList<>();
            WorkoutPlan wPlan;
            for (int i = 0; i<counts.size();i++){
                wPlan = new WorkoutPlan();
                wPlan.setName("MainPlan");
                wPlan.setOrder(i);
                wPlan.setWorkout(workouts.get(i));
                wPlan.setCount(counts.get(i));
                getWorkoutPlanDAO().create(wPlan);
                workoutPlan.add(wPlan);
            }
            sharedPref = context.getSharedPreferences("mysettings",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pickedplan", "MainPlan");
            editor.commit();
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't create TEST workoutPlan");
            throw new RuntimeException(e);
        }
        /*
            creating db using common method
         */

//        database.execSQL(CREATE_DISH_TABLE);
//        database.execSQL(CREATE_WORKOUT_TABLE);
////        database.execSQL(CREATE_PLAN_WORKOUT);
////        database.execSQL(CREATE_PLAN_NUTRITION);
//
//        //NUTRITION/////////////////////////////////////////////////////////////////////////////////
//
//        //Salad "Spring"
//        Uri imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/salad_spring");
//        String imgPath = imgUri.toString();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.salad_spring));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.sspring_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 41.19);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//        //Chicken stew with prunes
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/chicken_stew_with_prunes");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.chicken_stew_with_prunes));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.chicken_stew_with_prunes_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 123);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//        //Ratatouille
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/ratatouille");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.ratatouille));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.ratatouille_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 28.74);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//        //Cheese Casserole souffle
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/cheese_casserole_souffle");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.cheese_casserole_souffle));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.cheese_casserole_souffle_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 205);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//        //Cauliflower, baked with eggs and soy sauce
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/cauliflower_baked_with_eggs_and_soy_sauce");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.cauliflower_baked_with_eggs_and_soy_sauce));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.cauliflower_baked_with_eggs_and_soy_sauce_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 44.1);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//        //Chicken fillet with tomatoes and olives
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/chicken_fillet_with_tomatoes_and_olives");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.chicken_fillet_with_tomatoes_and_olives));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.chicken_fillet_with_tomatoes_and_olives_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 110);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//        //Salad with oranges, chicken and feta cheese
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/salad_with_oranges_chicken_and_feta_cheese");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_DISH_FULL_NAME, context.getString(R.string.salad_with_oranges_chicken_and_feta_cheese));
//        values.put(COLUMN_DISH_RECEIPT, context.getString(R.string.salad_with_oranges_chicken_and_feta_cheese_recipe));
//        values.put(COLUMN_DISH_KCALORIES, 160);
//        values.put(COLUMN_DISH_IMG_PATH, imgPath);
//        database.insert(TABLE_DISHES,null,values);
//
//
//        //WORKOUTS//////////////////////////////////////////////////////////////////////////////////
//
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/pushups1");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_WORKOUT_FULL_NAME, context.getString(R.string.pushups));
//        values.put(COLUMN_WORKOUT_INSTRUCTION, context.getString(R.string.pushups_info));
//        values.put(COLUMN_WORKOUT_IMG_PATH, imgPath);
//        database.insert(TABLE_WORKOUTS,null,values);
//
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/squat1");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_WORKOUT_FULL_NAME, context.getString(R.string.squats));
//        values.put(COLUMN_WORKOUT_INSTRUCTION, context.getString(R.string.squats_info));
//        values.put(COLUMN_WORKOUT_IMG_PATH, imgPath);
//        database.insert(TABLE_WORKOUTS,null,values);
//
//        imgUri = Uri.parse("android.resource://com.example.eugene.mobilecoach/drawable/sit_up1");
//        imgPath = imgUri.toString();
//        values.clear();
//        values.put(COLUMN_WORKOUT_FULL_NAME, context.getString(R.string.situps));
//        values.put(COLUMN_WORKOUT_INSTRUCTION, context.getString(R.string.situps_info));
//        values.put(COLUMN_WORKOUT_IMG_PATH, imgPath);
//        database.insert(TABLE_WORKOUTS,null,values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource,Workout.class, true);
        } catch (SQLException e){
            Log.e(TAG, "error upgrading db " + DB_NAME + "from version " + oldVersion);
            throw new RuntimeException(e);
        }

//        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
//                + newVersion + ", which will destroy all old data");
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISHES);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN_WORKOUT);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN_NUTRITION);
//        onCreate(db);

    }

//    public void reset()
//    {
//        Log.w(DBHelper.class.getName(), "Resetting database, which will destroy all old data");
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISHES);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN_WORKOUT);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAN_NUTRITION);
//        onCreate(db);
//    }

    public WorkoutDAO getWorkoutDAO() throws SQLException{
        if (workoutDAO == null){
            workoutDAO = new WorkoutDAO(getConnectionSource(),Workout.class);
        }
        return workoutDAO;
    }
    public DishDAO getDishDAO() throws SQLException{
        if (dishDAO == null){
            dishDAO = new DishDAO(getConnectionSource(), Dish.class);
        }
        return dishDAO;
    }

    public WorkoutPlanDAO getWorkoutPlanDAO() throws SQLException{
        if (workoutPlanDAO == null){
            workoutPlanDAO = new WorkoutPlanDAO(getConnectionSource(), WorkoutPlan.class);
        }
        return workoutPlanDAO;
    }
    public NutritionPlanDAO getNutritionPlanDAO() throws SQLException{
        if (nutritionPlanDAO == null){
            nutritionPlanDAO = new NutritionPlanDAO(getConnectionSource(), NutritionPlan.class);
        }
        return nutritionPlanDAO;
    }

    @Override
    public void close() {
        super.close();
        workoutDAO = null;
        dishDAO = null;

        workoutPlanDAO = null;
        nutritionPlanDAO = null;
    }
}