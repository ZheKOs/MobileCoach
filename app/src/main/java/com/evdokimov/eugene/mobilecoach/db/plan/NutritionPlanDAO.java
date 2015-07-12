package com.evdokimov.eugene.mobilecoach.db.plan;

import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;


public class NutritionPlanDAO extends BaseDaoImpl<NutritionPlan,Integer> {
    public NutritionPlanDAO(ConnectionSource connectionSource, Class<NutritionPlan> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<NutritionPlan> getAllWorkoutPlans() throws SQLException{
        return this.queryForAll();
    }

    public List<NutritionPlan> getWorkoutPlanById(long id) throws SQLException{
        QueryBuilder<NutritionPlan, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id",id);
        PreparedQuery<NutritionPlan> preparedQuery = queryBuilder.prepare();
        List<NutritionPlan> nutritionPlanList = query(preparedQuery);
        return nutritionPlanList;
    }

}
