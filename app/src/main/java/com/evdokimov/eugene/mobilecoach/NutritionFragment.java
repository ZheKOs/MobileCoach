package com.evdokimov.eugene.mobilecoach;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NutritionFragment extends Fragment {

    private Context context;

    public static NutritionFragment newInstance(){
        NutritionFragment fragment = new NutritionFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.nutrition_fragment,container,false);

        mainActivity = (MainActivity) getActivity();

        return v;
    }

}
