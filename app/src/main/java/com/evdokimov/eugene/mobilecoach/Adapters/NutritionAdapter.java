package com.evdokimov.eugene.mobilecoach.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.Activities.DishActivity;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.plan.NutritionPlan;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder>
{

    private Context context;
    private ArrayList<NutritionPlan> nutritionPlan;
    private boolean[] checks;

    private FloatingActionButton fabMain;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
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

    // Provide a suitable constructor (depends on the kind of dataset)
    public NutritionAdapter(Context mContext, ArrayList<NutritionPlan> nPlan, FloatingActionButton fab) {
        nutritionPlan = nPlan;
        context = mContext;
        fabMain = fab;
        checks = new boolean[nPlan.size()];
        clearChecks();

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearChecks();
                fabMain.setVisibility(View.GONE);
            }
        });

    }

    // Create new views (invoked by the layout manager)
    @Override
    public NutritionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_nutrition_card, parent, false);

        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final TransitionDrawable transition = (TransitionDrawable) holder.checkBox.getBackground();

        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DishActivity.class);
                intent.putExtra("dishName",nutritionPlan.get(position).getDish().getName());
                context.startActivity(intent);
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        holder.checkBox.setChecked(checks[position]);
        if (holder.checkBox.isChecked())
            transition.startTransition(150);
        holder.tvDishName.setText(nutritionPlan.get(position).getDish().getName());
        holder.tvKcal.setText(String.valueOf(nutritionPlan.get(position).getDish().getKcal()));

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int resizeTargetWidth = metrics.widthPixels / 2 - 30;
        int resizeTargetHeight = resizeTargetWidth;

        Picasso.with(context)
                .load(nutritionPlan.get(position).getDish().getImgPath())
                .resize(resizeTargetWidth,resizeTargetHeight)
                .centerCrop()
                .into(holder.iv);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return nutritionPlan.size();
    }


    //@Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.row_nutrition_card,parent,false);
//
//        ImageView iv = (ImageView) rowView.findViewById(R.id.iv_nutrition_card);
//        TextView tvDishName = (TextView) rowView.findViewById(R.id.tv_dish_name);
//        TextView tvKcal = (TextView) rowView.findViewById(R.id.tv_kcal_dish);
//        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox_nc);
//        final TransitionDrawable transition = (TransitionDrawable) checkBox.getBackground();
//        Button btnInfo = (Button) rowView.findViewById(R.id.btn_info_nutrition_card);
//        btnInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), DishActivity.class);
//                startActivity(intent);
//            }
//        });
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                checks[position] = checked;
//                if (checked) {
//                    transition.startTransition(150);
//                    fabMain.setVisibility(View.VISIBLE);
//                } else {
//                    transition.reverseTransition(150);
//                    if (allFalse())
//                        fabMain.setVisibility(View.GONE);
//                }
//
//            }
//        });
//        checkBox.setChecked(checks[position]);
//        if (checkBox.isChecked())
//            transition.startTransition(150);
//        tvDishName.setText(nutritionPlan.get(position).getDish().getName());
//        tvKcal.setText(String.valueOf(nutritionPlan.get(position).getDish().getKcal()));
//
//        Picasso.with(context).load(nutritionPlan.get(position).getDish().getImgPath()).into(iv);
//
//        return rowView;
//    }

    private void clearChecks()
    {
        Arrays.fill(checks, false);
        this.notifyDataSetChanged();
    }
    private boolean allFalse()
    {
        for (boolean checked : checks) {
            if (checked)
                return false;
        }
        return true;
    }
}