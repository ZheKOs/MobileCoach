package com.evdokimov.eugene.mobilecoach.db.stats;


import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class StatsDAO extends BaseDaoImpl<Stats, Integer> {

    public StatsDAO(ConnectionSource connectionSource, Class<Stats> dataClass) throws SQLException {
        super(connectionSource,dataClass);
    }

    public List<Stats> getAllStats() throws SQLException{
        return this.queryForAll();
    }

    public List<Stats> getStatsByName(String name) throws SQLException{
        QueryBuilder<Stats, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("name",name).and();
        PreparedQuery<Stats> preparedQuery = queryBuilder.prepare();
        List<Stats> stats = query(preparedQuery);
        return stats;
    }

    public Stats getStatsById(int id) throws SQLException{
        QueryBuilder<Stats, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id",id);
        queryBuilder.orderBy("date", false);
        PreparedQuery<Stats> preparedQuery = queryBuilder.prepare();
        Stats stats = queryForFirst(preparedQuery);
        return stats;
    }

    public List<Stats> getStatsFromPeriod(String name, String date, short period) throws SQLException{
        QueryBuilder<Stats, Integer> queryBuilder = queryBuilder();
        String regex;
        if (period == 0) regex = date.substring(0,4);
        else regex = date.substring(0,7);
        queryBuilder.where().eq("name",name).and().like("date","%"+regex+"%");
        PreparedQuery<Stats> preparedQuery = queryBuilder.prepare();
        List<Stats> stats = query(preparedQuery);
        return stats;
    }

    public List<Stats> getAllPickedStats() throws SQLException{

        QueryBuilder<Stats, Integer> queryBuilder = queryBuilder();
        queryBuilder.where().eq("picked",true);
        PreparedQuery<Stats> preparedQuery = queryBuilder.prepare();
        return query(preparedQuery);

    }

}
