package com.evdokimov.eugene.mobilecoach.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evdokimov.eugene.mobilecoach.Activities.MainActivity;
import com.evdokimov.eugene.mobilecoach.Adapters.NutritionAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.rey.material.widget.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;


public class NutritionFragment extends Fragment {

    private Context context;
    private FloatingActionButton fabMain;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rv_nutrition;

    private ArrayList<NutritionPlan> nPlan;

    public static NutritionFragment newInstance(){
        NutritionFragment fragment = new NutritionFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_nutrition,container,false);

        mainActivity = (MainActivity) getActivity();
        try {

            nPlan = new ArrayList<>(
                    HelperFactory.getDbHelper().getNutritionPlanDAO().getNutritionPlanByName("MainNutritionPlan")
            );
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        fabMain = (FloatingActionButton) v.findViewById(R.id.btn_float_main);

        rv_nutrition = (RecyclerView) v.findViewById(R.id.lv_nutrition);
        rv_nutrition.setHasFixedSize(true);


        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_nutrition.setLayoutManager(mLayoutManager);
        mAdapter = new NutritionAdapter(getActivity(),nPlan,fabMain);

        rv_nutrition.setAdapter(mAdapter);

        return v;
    }



}
