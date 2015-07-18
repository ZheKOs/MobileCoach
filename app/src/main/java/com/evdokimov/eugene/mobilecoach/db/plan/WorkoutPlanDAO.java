package com.evdokimov.eugene.mobilecoach.db.plan;


import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.workout.Workout;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanDAO extends BaseDaoImpl<WorkoutPlan, Integer> {

    public WorkoutPlanDAO(ConnectionSource connectionSource, Class<WorkoutPlan> dataClass) throws SQLException{
        super(connectionSource,dataClass);
    }

    public List<WorkoutPlan> getAllWorkoutPlans() throws SQLException{
        return this.queryForAll();
    }

    public List<WorkoutPlan> getWorkoutPlanByName(String name) throws SQLException{
        QueryBuilder<WorkoutPlan, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("planName",name);
        queryBuilder.orderBy("order", true);
        PreparedQuery<WorkoutPlan> preparedQuery = queryBuilder.prepare();
        List<WorkoutPlan> workoutPlanList = query(preparedQuery);
        //List<Workout> workouts = new ArrayList<>();
        for (int i = 0; i < workoutPlanList.size(); i++)
        {
            workoutPlanList.get(i)
                    .setWorkout(HelperFactory.getDbHelper().getWorkoutDAO().getWorkoutById(workoutPlanList.get(i).getWorkout().getId()));
        }
        return workoutPlanList;
    }

    public List<WorkoutPlan> getUniquePlans() throws SQLException{
        QueryBuilder<WorkoutPlan, Integer> queryBuilder = queryBuilder();
        queryBuilder.distinct().selectColumns("planName");
        PreparedQuery<WorkoutPlan> preparedQuery = queryBuilder.prepare();
        List<WorkoutPlan> workoutPlanList = query(preparedQuery);
        return workoutPlanList;
    }

//    public ArrayList<WorkoutPlanItem> getWorkoutsFromPlanByName(String name) throws SQLException{
//        List<WorkoutPlan> workoutPlanList = getWorkoutPlanByName(name);
//        ArrayList<WorkoutPlanItem> workouts = new ArrayList<>();
//
//        if (workoutPlanList.size() > 0) {
//            for (WorkoutPlan workoutPlan : workoutPlanList) {
//                workouts.add(new WorkoutPlanItem(workoutPlan.getWorkout(),workoutPlan.getCount()));
//            }
//        }
//
//        return workouts;
//    }

}
