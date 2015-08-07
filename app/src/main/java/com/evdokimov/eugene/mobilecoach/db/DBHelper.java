package com.evdokimov.eugene.mobilecoach.db;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.evdokimov.eugene.mobilecoach.R;
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

            TableUtils.createTable(connectionSource, Dish.class);
            TableUtils.createTable(connectionSource, NutritionPlan.class);
        }catch (SQLException e){
            Log.e(TAG, "error creating DB " + DB_NAME);
            throw new RuntimeException(e);
        }

        Resources res = context.getResources();

        Workout squats, pushups, pullups, plank, dolphin, situps;

        try {
            squats = new Workout();
            squats.setName("Приседания");
            squats.setInstruction(res.getString(R.string.squats_info));
            getWorkoutDAO().create(squats);

            pushups = new Workout();
            pushups.setName("Отжимания");
            pushups.setInstruction(res.getString(R.string.pushups_info));
            getWorkoutDAO().create(pushups);

            pullups = new Workout();
            pullups.setName("Подтягивание");
            getWorkoutDAO().create(pullups);

            plank = new Workout();
            plank.setName("Планка");
            plank.setInstruction(res.getString(R.string.plank_info));
                    getWorkoutDAO().create(plank);

            dolphin = new Workout();
            dolphin.setName("Отжимания «Дельфин»");
            dolphin.setInstruction(res.getString(R.string.Pushups_Dolphin_info));
            getWorkoutDAO().create(dolphin);

            situps = new Workout();
            situps.setName("Скручивания");
            situps.setInstruction(res.getString(R.string.situps_info));
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
            editor.putString("pickednutrition","MainNutritionPlan");
            editor.commit();
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't create TEST workoutPlan");
            throw new RuntimeException(e);
        }

        Dish cauliflowerBakedWithEggsAndSoySauce, chickenBreastWithLemonAndChili,
                chickenCutletsWithCheese,chickenFilletWithTomatoesAndOlives,eggsBakedWithChickenAndVegetables,
                fishStewWithOnionsAndSourCream,fruitPizza,lahanorizo,ricePilafWithTomatoesAndBasil,
                saladShopski,saladSpring;

        Uri uri;
        try {

            cauliflowerBakedWithEggsAndSoySauce = new Dish();
            cauliflowerBakedWithEggsAndSoySauce.setName("Цветная капуста запеченная с яйцами и соевом соусе");
            cauliflowerBakedWithEggsAndSoySauce.setReceipt(res.getString(R.string.cauliflower_baked_with_eggs_and_soy_sauce_recipe));
            cauliflowerBakedWithEggsAndSoySauce.setKcal(0.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.cauliflower_baked_with_eggs_and_soy_sauce);
            cauliflowerBakedWithEggsAndSoySauce.setImgPath(uri.toString());
            getDishDAO().create(cauliflowerBakedWithEggsAndSoySauce);


            chickenBreastWithLemonAndChili = new Dish();
            chickenBreastWithLemonAndChili.setName("Куриная грудка с лимоном и перцем чили");
            chickenBreastWithLemonAndChili.setReceipt(res.getString(R.string.chicken_breast_with_lemon_and_chilli_recipe));
            chickenBreastWithLemonAndChili.setKcal(0.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.chicken_breast_with_lemon_and_chilli);
            chickenBreastWithLemonAndChili.setImgPath(uri.toString());
            getDishDAO().create(chickenBreastWithLemonAndChili);

            chickenCutletsWithCheese = new Dish();
            chickenCutletsWithCheese.setName("Куриные котлеты с сыром");
            chickenCutletsWithCheese.setReceipt(res.getString(R.string.chicken_cutlets_with_cheese_recipe));
            chickenCutletsWithCheese.setKcal(193.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.chicken_cutlets_with_cheese);
            chickenCutletsWithCheese.setImgPath(uri.toString());
            getDishDAO().create(chickenCutletsWithCheese);

            chickenFilletWithTomatoesAndOlives = new Dish();
            chickenFilletWithTomatoesAndOlives.setName("Куриное филе с томатами и оливками");
            chickenFilletWithTomatoesAndOlives.setReceipt(res.getString(R.string.chicken_fillet_with_tomatoes_and_olives_recipe));
            chickenFilletWithTomatoesAndOlives.setKcal(110.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.chicken_fillet_with_tomatoes_and_olives);
            chickenFilletWithTomatoesAndOlives.setImgPath(uri.toString());
            getDishDAO().create(chickenFilletWithTomatoesAndOlives);

            eggsBakedWithChickenAndVegetables = new Dish();
            eggsBakedWithChickenAndVegetables.setName("Яйца, запеченные с курицей и овощами");
            eggsBakedWithChickenAndVegetables.setReceipt(res.getString(R.string.eggs_baked_with_chicken_and_vegetables_recipe));
            eggsBakedWithChickenAndVegetables.setKcal(107.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.eggs_baked_with_chicken_and_vegetables);
            eggsBakedWithChickenAndVegetables.setImgPath(uri.toString());
            getDishDAO().create(eggsBakedWithChickenAndVegetables);

            fishStewWithOnionsAndSourCream = new Dish();
            fishStewWithOnionsAndSourCream.setName("Рыба тушеная с луком и сметаной");
            fishStewWithOnionsAndSourCream.setReceipt(res.getString(R.string.fish_stew_with_onions_and_sour_cream_recipe));
            fishStewWithOnionsAndSourCream.setKcal(150.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.fish_stew_with_onions_and_sour_cream);
            fishStewWithOnionsAndSourCream.setImgPath(uri.toString());
            getDishDAO().create(fishStewWithOnionsAndSourCream);

            fruitPizza = new Dish();
            fruitPizza.setName("Фруктовая пицца");
            fruitPizza.setReceipt(res.getString(R.string.fruit_pizza_recipe));
            fruitPizza.setKcal(200.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.fruit_pizza);
            fruitPizza.setImgPath(uri.toString());
            getDishDAO().create(fruitPizza);

            lahanorizo = new Dish();
            lahanorizo.setName("Лаханоризо");
            lahanorizo.setReceipt(res.getString(R.string.lahanorizo_recipe));
            lahanorizo.setKcal(65.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.lahanorizo);
            lahanorizo.setImgPath(uri.toString());
            getDishDAO().create(lahanorizo);

            ricePilafWithTomatoesAndBasil = new Dish();
            ricePilafWithTomatoesAndBasil.setName("Рисовый пилав с помидорами и базиликом");
            ricePilafWithTomatoesAndBasil.setReceipt(res.getString(R.string.rice_pilaf_with_tomatoes_and_basil_recipe));
            ricePilafWithTomatoesAndBasil.setKcal(79.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.rice_pilaf_with_tomatoes_and_basil);
            ricePilafWithTomatoesAndBasil.setImgPath(uri.toString());
            getDishDAO().create(ricePilafWithTomatoesAndBasil);

            saladShopski = new Dish();
            saladShopski.setName("Салат по шопски");
            saladShopski.setReceipt(res.getString(R.string.salad_in_shopski_recipe));
            saladShopski.setKcal(0.0);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.salad_in_shopski);
            saladShopski.setImgPath(uri.toString());
            getDishDAO().create(saladShopski);

            saladSpring = new Dish();
            saladSpring.setName("Салат \"Весенний\"");
            saladSpring.setReceipt(res.getString(R.string.sspring_recipe));
            saladSpring.setKcal(41.19);
            uri = Uri.parse("android.resource://com.evdokimov.eugene.mobilecoach/" + R.drawable.salad_spring);
            saladSpring.setImgPath(uri.toString());
            getDishDAO().create(saladSpring);

        }catch (SQLException e){
            Log.e("TAG_ERROR","can't create test dish");
            throw new RuntimeException(e);
        }

        ArrayList<Dish> dishes = new ArrayList<>();
        dishes.add(chickenBreastWithLemonAndChili);
        dishes.add(chickenCutletsWithCheese);;
        dishes.add(eggsBakedWithChickenAndVegetables);
        dishes.add(ricePilafWithTomatoesAndBasil);
        dishes.add(saladSpring);

        NutritionPlan nPlan;
        try {
            for (int i = 0; i < dishes.size(); i++){
                nPlan = new NutritionPlan();
                nPlan.setName("MainNutritionPlan");
                nPlan.setDish(dishes.get(i));
                getNutritionPlanDAO().create(nPlan);
            }
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't create test nutrition plan");
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