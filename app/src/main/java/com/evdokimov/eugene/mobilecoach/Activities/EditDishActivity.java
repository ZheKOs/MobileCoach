package com.evdokimov.eugene.mobilecoach.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class EditDishActivity extends AppCompatActivity{

    int id;
    Dish dish;

    ImageView watchDish;
    EditText etDishName, etKCal, etReceipt;

    String tmpName, tmpReceipt;
    int tmpKcal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_dish_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);

        try {
            dish = HelperFactory.getDbHelper().getDishDAO().getDishById(id);
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't get dish by id " + id);
            throw new RuntimeException(e);
        }

        watchDish = (ImageView) findViewById(R.id.iv_w_dish);
        Picasso.with(this).load(dish.getImgPath()).into(watchDish);
        etDishName = (EditText) findViewById(R.id.et_dish_name);
        etDishName.setText(dish.getName());
        etKCal = (EditText) findViewById(R.id.et_dish_kcal);
        etKCal.setText(String.valueOf(dish.getKcal()));
        etReceipt = (EditText) findViewById(R.id.et_dish_instruction);
        etReceipt.setText(dish.getReceipt());

    }

    //TODO
    private void save(){}

}
