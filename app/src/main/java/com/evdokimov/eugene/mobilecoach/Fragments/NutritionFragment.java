package com.evdokimov.eugene.mobilecoach.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evdokimov.eugene.mobilecoach.Activities.MainActivity;
import com.evdokimov.eugene.mobilecoach.Adapters.NutritionAdapter;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;

import java.sql.SQLException;
import java.util.ArrayList;


public class NutritionFragment extends Fragment {

    private Context context;
    private FloatingActionButton fabMain;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rv_nutrition;

    ImageButton more;

    private ArrayList<NutritionPlan> nPlan;

    public static NutritionFragment newInstance(){
        NutritionFragment fragment = new NutritionFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);

        mainActivity = (MainActivity) getActivity();
        context = mainActivity.getApplicationContext();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        String nutritionName = sharedPref.getString("pickednutrition", "");

        TextView tvNutritionName = (TextView) v.findViewById(R.id.tv_nutrition_name);
        tvNutritionName.setText(nutritionName);

        more = (ImageButton) v.findViewById(R.id.ib_nutr_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(context, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, more);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nutrition, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(context,
                                item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()){
                            case R.id.edit_name_mn:
                                break;
                            case R.id.change_mn:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        try {

            nPlan = new ArrayList<>(
                    HelperFactory.getDbHelper().getNutritionPlanDAO().getNutritionPlanByName(nutritionName)
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
