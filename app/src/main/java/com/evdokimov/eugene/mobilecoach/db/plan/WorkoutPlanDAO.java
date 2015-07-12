package com.evdokimov.eugene.mobilecoach.db.plan;


import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class WorkoutPlanDAO extends BaseDaoImpl<WorkoutPlan, Integer> {

    public WorkoutPlanDAO(ConnectionSource connectionSource, Class<WorkoutPlan> dataClass) throws SQLException{
        super(connectionSource,dataClass);
    }

    public List<WorkoutPlan> getAllWorkoutPlans() throws SQLException{
        return this.queryForAll();
    }

    public List<WorkoutPlan> getWorkoutPlanById(long id) throws SQLException{
        QueryBuilder<WorkoutPlan, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id",id);
        PreparedQuery<WorkoutPlan> preparedQuery = queryBuilder.prepare();
        List<WorkoutPlan> workoutPlanList = query(preparedQuery);
        return workoutPlanList;
    }

}
