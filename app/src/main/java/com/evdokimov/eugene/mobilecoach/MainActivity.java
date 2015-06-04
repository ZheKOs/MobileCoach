package com.evdokimov.eugene.mobilecoach;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("ТРЕНИРОВКА"));
        tabLayout.addTab(tabLayout.newTab().setText("ПИТАНИЕ"));
        tabLayout.addTab(tabLayout.newTab().setText("СТАТИСТИКА"));


        ImageButton btnStart = (ImageButton) findViewById(R.id.btn_set_time);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_set_time:
                        Dialog dialog = new TimePickerDialog(context);

                        dialog.show();
                        break;
                }
            }
        });

    }

}
