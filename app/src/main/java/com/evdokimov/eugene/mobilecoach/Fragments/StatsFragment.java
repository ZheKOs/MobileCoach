package com.evdokimov.eugene.mobilecoach.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    //private Context context;
    private LineChart chart;

    public static StatsFragment newInstance(){
        return new StatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        ListView listView = (ListView) v.findViewById(R.id.lv_chart);

        StatsAdapter adapter = new StatsAdapter(getActivity(),getPickedChartNames());
        listView.setAdapter(adapter);

//        chart = (LineChart) v.findViewById(R.id.chart);
//        createChart(getEntryArray());

        //mainActivity = (MainActivity) getActivity();

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

    private Entry[] getEntryArray()
    {
        return new Entry[]{new Entry(3, 1), new Entry(5, 2), new Entry(4, 3), new Entry(6, 4)};
    }

    private void createChart(Entry[] entries)
    {
        //TODO if it is Workout => change name of lineDataSet to WorkoutName
        //TODO else name it as calories

        ArrayList<Entry> valsComp1 = new ArrayList<>(Arrays.asList(entries));

        LineDataSet setComp1 = new LineDataSet(valsComp1, "ккал");
        setComp1.setLineWidth(5);
        setComp1.setCircleSize(6);
        setComp1.setColor(getResources().getColor(R.color.colorAccent));
        setComp1.setCircleColor(getResources().getColor(R.color.colorAccent));
        setComp1.setCircleColorHole(getResources().getColor(R.color.colorAccent));
        setComp1.setFillColor(Color.WHITE);

        ArrayList<LineDataSet> dataSets = new ArrayList<>(); //this array contains all lines
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("01."+"МАЙ"); xVals.add("02."+"МАЙ");
        xVals.add("03."+"МАЙ");xVals.add("04."+"МАЙ");
        xVals.add("05."+"МАЙ"); xVals.add("06."+"МАЙ");

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
    }


}
