package com.evdokimov.eugene.mobilecoach.Fragments;

import android.content.Context;
import android.content.Intent;
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
import com.evdokimov.eugene.mobilecoach.Utils.SharingContentManager;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlanDAO;
import com.rey.material.app.Dialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;

import java.sql.SQLException;
import java.util.ArrayList;


public class NutritionFragment extends Fragment {

    private FloatingActionButton fabMain;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rv_nutrition;

    ImageButton more;

    TextView tvNutritionName;
    String nutritionName;

    private ArrayList<NutritionPlan> nPlan;

    public static NutritionFragment newInstance(){
        NutritionFragment fragment = new NutritionFragment();
        return fragment;
    }

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_nutrition, container, false);

        mainActivity = (MainActivity) getActivity();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        nutritionName = sharedPref.getString("pickednutrition", "");

        tvNutritionName = (TextView) v.findViewById(R.id.tv_nutrition_name);
        tvNutritionName.setText(nutritionName);

        more = (ImageButton) v.findViewById(R.id.ib_nutr_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(mainActivity, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, more);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_nutrition, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.edit_name_mn:
                                final Dialog dialog = new Dialog(mainActivity);
                                View dContentView = View.inflate(mainActivity, R.layout.dialog_edit_nutr_name, null);
                                final EditText etNameNutr = (EditText) dContentView.findViewById(R.id.et_dialog_edit_nutr_name);
                                etNameNutr.setText(nutritionName);
                                dialog.setContentView(dContentView);
                                dialog.positiveAction("Изменить")
                                .positiveActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        SharedPreferences sharedPref = getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        String str = etNameNutr.getText().toString();
                                        editor.putString("pickednutrition", str);
                                        editor.apply();

                                        updateNutritionPlanName(nutritionName, str);
                                        nutritionName = str;
                                        tvNutritionName.setText(str);
                                        dialog.dismiss();
                                    }
                                });
                                dialog.negativeAction("Отмена")
                                .negativeActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                break;
                            case R.id.share_plan_mn:
                                //TODO list of the plan
                                SharingContentManager sharingContentManager
                                        = new SharingContentManager(mainActivity, 3, nutritionName);
                                sharingContentManager.prepareAndShareContent();
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

    private void updateNutritionPlanName(String oldName, String newName){
        try {
            NutritionPlanDAO npDAO = HelperFactory.getDbHelper().getNutritionPlanDAO();
            for (NutritionPlan nPlanItem : nPlan){
                nPlanItem.setName(newName);
                npDAO.update(nPlanItem);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }



}
