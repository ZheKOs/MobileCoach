package com.evdokimov.eugene.mobilecoach.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Activities.MainActivity;
import com.evdokimov.eugene.mobilecoach.Activities.DishActivity;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.j256.ormlite.logger.Log;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class NutritionFragment extends Fragment {

    private Context context;
    private FloatingActionButton fabMain;

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

        GridView lv_nutrition = (GridView) v.findViewById(R.id.lv_nutrition);
        final NutritionAdapter nutritionAdapter = new NutritionAdapter(getActivity(), nPlan);
        lv_nutrition.setAdapter(nutritionAdapter);

        fabMain = (FloatingActionButton) v.findViewById(R.id.btn_float_main);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nutritionAdapter.clearChecks();
                fabMain.setVisibility(View.GONE);
            }
        });

        return v;
    }

    class NutritionAdapter extends ArrayAdapter<NutritionPlan>
    {

        private Context context;
        private ArrayList<NutritionPlan> nutritionPlan;
        private boolean[] checks;

        public NutritionAdapter(Context context, ArrayList<NutritionPlan> nPlan) {
            super(context, R.layout.row_nutrition_card, nPlan);

            this.context = context;
            this.nutritionPlan = nPlan;
            this.checks = new boolean[nPlan.size()];

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_nutrition_card,parent,false);

            ImageView iv = (ImageView) rowView.findViewById(R.id.iv_nutrition_card);
            TextView tvDishName = (TextView) rowView.findViewById(R.id.tv_dish_name);
            TextView tvKcal = (TextView) rowView.findViewById(R.id.tv_kcal_dish);
            final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox_nc);
            final TransitionDrawable transition = (TransitionDrawable) checkBox.getBackground();
            Button btnInfo = (Button) rowView.findViewById(R.id.btn_info_nutrition_card);
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DishActivity.class);
                    startActivity(intent);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    checks[position] = checked;
                    if (checked) {
                        transition.startTransition(150);
                        fabMain.setVisibility(View.VISIBLE);
                    } else {
                        transition.reverseTransition(150);
                        if (allFalse())
                            fabMain.setVisibility(View.GONE);
                    }

                }
            });
            checkBox.setChecked(checks[position]);
            if (checkBox.isChecked())
                transition.startTransition(150);
            tvDishName.setText(nutritionPlan.get(position).getDish().getName());
            tvKcal.setText(String.valueOf(nutritionPlan.get(position).getDish().getKcal()));

            Picasso.with(context).load(nutritionPlan.get(position).getDish().getImgPath()).into(iv);

            return rowView;
        }

        protected void clearChecks()
        {
            Arrays.fill(checks, false);
            this.notifyDataSetChanged();
        }
        protected boolean allFalse()
        {
            for (boolean checked : checks) {
                if (checked)
                    return false;
            }
            return true;
        }
    }

}
