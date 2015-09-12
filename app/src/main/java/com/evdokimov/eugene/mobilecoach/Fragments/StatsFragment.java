package com.evdokimov.eugene.mobilecoach.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.evdokimov.eugene.mobilecoach.Adapters.StatsAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.stats.Stats;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class StatsFragment extends Fragment {

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rvStats;


    public static StatsFragment newInstance(){
        return new StatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        rvStats = (RecyclerView) v.findViewById(R.id.rv_chart);
        rvStats.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvStats.setLayoutManager(mLayoutManager);

        mAdapter = new StatsAdapter(getActivity(),getPickedChartNames());
        rvStats.setAdapter(mAdapter);

        return v;
    }

    private String[] getPickedChartNames(){
        ArrayList<String> s;
        ArrayList<Stats> stats;

        try {
            stats = new ArrayList<>(HelperFactory.getDbHelper().getStatsDAO().getAllPickedStats());
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        s = new ArrayList<>();
        for (Stats stat: stats) {
            s.add(stat.getName());
        }

        s = removeDuplicates(s);

        return s.toArray(new String[s.size()]);
    }

    private ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

}
