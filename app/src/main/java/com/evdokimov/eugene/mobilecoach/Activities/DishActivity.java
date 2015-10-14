package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Utils.SharingContentManager;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.dish.Dish;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.ImageButton;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class DishActivity extends AppCompatActivity {

    Context context;

    Dish dish;

    ImageView iv;
    TextView dName, kcal, receipt;

    FloatingActionButton fabShare;

    final int EDIT_DISH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_dish_watch);

        context = this;

        final String dishName = getIntent().getStringExtra("dishName");

        try{
            dish = HelperFactory.getDbHelper().getDishDAO().getDishByName(dishName);
        }catch (SQLException e){
            Log.e("TAG_ERROR","can't get dish");
        }

        iv = (ImageView) findViewById(R.id.iv_w_dish);
        Picasso.with(getBaseContext()).load(dish.getImgPath()).into(iv);
        dName = (TextView) findViewById(R.id.tv_dish_name_watch);
        dName.setText(dishName);
        kcal = (TextView) findViewById(R.id.tv_kcal_dish_watch);
        kcal.setText(String.valueOf(dish.getKcal()) + " ккал на 100 грамм");
        receipt = (TextView) findViewById(R.id.tv_dish_receipt_watch);
        receipt.setText(dish.getReceipt());

        ImageButton btnEdit = (ImageButton) findViewById(R.id.btn_edit_dish);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditDishActivity.class);

                intent.putExtra("id",dish.getId());

                startActivityForResult(intent, EDIT_DISH);
            }
        });
        final ImageButton finish = (ImageButton) findViewById(R.id.btn_back_dish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabShare = (FloatingActionButton) findViewById(R.id.btn_float_main);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void share(){
        SharingContentManager scm = new SharingContentManager(this,1,dish.getName());
        scm.prepareAndShareContent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch_dish, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
