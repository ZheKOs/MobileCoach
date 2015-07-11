package com.evdokimov.eugene.mobilecoach.db.dish;

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

    List<Dish> getDishByName(String name) throws SQLException{
        QueryBuilder<Dish, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("name",name);
        PreparedQuery<Dish> preparedQuery = queryBuilder.prepare();
        List<Dish> dishList = query(preparedQuery);
        return dishList;
    }

}
