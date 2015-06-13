package com.evdokimov.eugene.mobilecoach;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.LineChartView;


public class StatsFragment extends Fragment {

    private Context context;

    public static StatsFragment newInstance(){
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.stats_fragment,container,false);

        mainActivity = (MainActivity) getActivity();

        LineChartView lineChartView = (LineChartView) v.findViewById(R.id.linechart);
        LineSet lineSet = new LineSet();
        lineSet.addPoint(new Point("label 1", 1f));
        lineChartView.addData(lineSet);

        return v;
    }


}
