package com.evdokimov.eugene.mobilecoach.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;

public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ViewHolder> {

    Context context;
    boolean isWorkoutMode;

    public AllItemsAdapter(Context mContext, boolean isWorkoutMode){
        this.context = mContext;
        this.isWorkoutMode = isWorkoutMode;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ImageView iv;
        public TextView tvDishName;
        public TextView tvKcal;
        public CheckBox checkBox;
        //final TransitionDrawable transition = (TransitionDrawable) checkBox.getBackground();
        public Button btnInfo;
        public ViewHolder(View v) {
            super(v);
            //mTextView = v;
            iv = (ImageView) v.findViewById(R.id.iv_nutrition_card);
            tvDishName = (TextView) v.findViewById(R.id.tv_dish_name);
            tvKcal = (TextView) v.findViewById(R.id.tv_kcal_dish);
            checkBox = (CheckBox) v.findViewById(R.id.checkbox_nc);
            btnInfo = (Button) v.findViewById(R.id.btn_info_nutrition_card);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
