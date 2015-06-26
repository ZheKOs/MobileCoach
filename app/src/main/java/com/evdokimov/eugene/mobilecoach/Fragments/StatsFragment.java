package com.evdokimov.eugene.mobilecoach.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evdokimov.eugene.mobilecoach.Activities.MainActivity;
import com.evdokimov.eugene.mobilecoach.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;


public class StatsFragment extends Fragment {

    private Context context;

    public static StatsFragment newInstance(){
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    private MainActivity mainActivity;
    private View v;

    LineChart chart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_stats,container,false);

        chart = (LineChart) v.findViewById(R.id.chart);
        createChart(getEntryArray());

        mainActivity = (MainActivity) getActivity();

        return v;
    }

    private Entry[] getEntryArray()
    {
        //TODO getEntriesFromDB

        return new Entry[]{new Entry(3, 1), new Entry(5, 2), new Entry(4, 3), new Entry(6, 4)};
    }

    private void createChart(Entry[] entries)
    {
        //TODO if it is Workout => change name of lineDataSet to WorkoutName
        //TODO else name it as calories

        ArrayList<Entry> valsComp1 = new ArrayList<Entry>(Arrays.asList(entries));

        LineDataSet setComp1 = new LineDataSet(valsComp1, "ккал");
        setComp1.setLineWidth(5);
        setComp1.setCircleSize(6);
        setComp1.setColor(getResources().getColor(R.color.colorAccent));
        setComp1.setCircleColor(getResources().getColor(R.color.colorAccent));
        setComp1.setFillColor(Color.WHITE);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>(); //this array contains all lines
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("01."+"МАЙ"); xVals.add("02."+"МАЙ");
        xVals.add("03."+"МАЙ");xVals.add("04."+"МАЙ");
        xVals.add("05."+"МАЙ"); xVals.add("06."+"МАЙ");

        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
    }


}
