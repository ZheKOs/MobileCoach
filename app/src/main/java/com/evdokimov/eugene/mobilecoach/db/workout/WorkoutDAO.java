package com.evdokimov.eugene.mobilecoach.db.workout;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;


public class WorkoutDAO extends BaseDaoImpl<Workout, Integer> {

    public WorkoutDAO(ConnectionSource connectionSource,Class<Workout> dataClass) throws SQLException{
        super(connectionSource, dataClass);
    }

    public List<Workout> getAllWorkouts() throws SQLException{
        return this.queryForAll();
    }

    public Workout getWorkoutByName(String name) throws SQLException{
        QueryBuilder<Workout, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("name",name);
        PreparedQuery<Workout> preparedQuery = queryBuilder.prepare();
        Workout workout = queryForFirst(preparedQuery);
        return workout;
    }

    public Workout getWorkoutById(int id) throws SQLException{
        QueryBuilder<Workout, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id",id);
        PreparedQuery<Workout> preparedQuery = queryBuilder.prepare();
        Workout workout = queryForFirst(preparedQuery);
        return workout;
    }

}
