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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


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

        String[] dishes = {"БЛЮДО","БЛЮДО","БЛЮДО","БЛЮДО","БЛЮДО","БЛЮДО","БЛЮДО"};

        ListView lv_nutrition = (ListView) v.findViewById(R.id.lv_nutrition);
        NutritionAdapter nutritionAdapter = new NutritionAdapter(getActivity(), dishes);
        lv_nutrition.setAdapter(nutritionAdapter);

        return v;
    }

    class NutritionAdapter extends ArrayAdapter<String>
    {

        private Context context;
        private String[] strings;

        public NutritionAdapter(Context context, String[] objects) {
            super(context, R.layout.row_main_training, objects);

            this.context = context;
            this.strings = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.row_nutrition_card,parent,false);

            TextView tvDishName = (TextView) rowView.findViewById(R.id.tv_dish_name);
            TextView tvKcal = (TextView) rowView.findViewById(R.id.tv_kcal_dish);

            tvDishName.setText(strings[position]);

            return rowView;
        }
    }

}
