package com.evdokimov.eugene.mobilecoach.db.plan;

import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "nutrition_plans")
public class NutritionPlan {

    @DatabaseField(generatedId = true)
    private long idPlan;
    @ForeignCollectionField(eager = true)
    private ArrayList<Dish> dishes;

    public NutritionPlan()
    {

    }
    public NutritionPlan(long idPlan, ArrayList<Dish> dishes)
    {
        this.setIdPlan(idPlan);
        this.setDishes(dishes);
    }


    public long getIdPlan() {return idPlan;}
    public void setIdPlan(long idPlan) {this.idPlan = idPlan;}

    public ArrayList<Dish> getDishes() {return dishes;}
    public void setDishes(ArrayList<Dish> dishes) {this.dishes = dishes;}
}
