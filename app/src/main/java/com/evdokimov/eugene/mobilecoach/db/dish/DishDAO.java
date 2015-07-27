package com.evdokimov.eugene.mobilecoach.db.dish;

import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class DishDAO extends BaseDaoImpl<Dish, Integer> {

    public DishDAO(ConnectionSource connectionSource, Class<Dish> dataClass) throws SQLException{
        super(connectionSource,dataClass);
    }

    List<Dish> getAllDish() throws SQLException{
        return this.queryForAll();
    }

    public Dish getDishByName(String name) throws SQLException{
        QueryBuilder<Dish, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("name",name);
        PreparedQuery<Dish> preparedQuery = queryBuilder.prepare();
        Dish dish = queryForFirst(preparedQuery);
        return dish;
    }

    public Dish getDishById(int id) throws SQLException{
        QueryBuilder<Dish, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id",id);
        PreparedQuery<Dish> preparedQuery = queryBuilder.prepare();
        Dish dish = queryForFirst(preparedQuery);
        return dish;
    }

}
