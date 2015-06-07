package com.evdokimov.eugene.mobilecoach;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

        return v;
    }


}
