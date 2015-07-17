package com.evdokimov.eugene.mobilecoach.db.plan;

import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;

@DatabaseTable(tableName = "nutrition_plans")
public class NutritionPlan {

    @DatabaseField(generatedId = true)
    private int idPlan;
    @DatabaseField(canBeNull = false, dataType = DataType.STRING)
    private String name;
    @DatabaseField(foreign = true, canBeNull = false)
    private Dish dish;

    public NutritionPlan()
    {

    }
    public NutritionPlan(String name, Dish dish)
    {
        this.setName(name);
        this.setDish(dish);
    }

    public int getIdPlan() {return idPlan;}
    public void setIdPlan(int idPlan) {this.idPlan = idPlan;}

    public Dish getDish() {return dish;}
    public void setDish(Dish dish) {this.dish = dish;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
