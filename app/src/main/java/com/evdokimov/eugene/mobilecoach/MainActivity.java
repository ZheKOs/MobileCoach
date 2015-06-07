package com.evdokimov.eugene.mobilecoach;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TabPageIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl_navigator;

    private FrameLayout fl_drawer;
    private ListView lv_drawer;

    private PagerAdapter mPagerAdapter;
    private CustomViewPager vp;
    private TabPageIndicator tpi;


    private Tab[] mItems = new Tab[]{
            Tab.TRAINING, Tab.NUTRITION, Tab.STATS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (CustomViewPager)findViewById(R.id.main_vp);
        tpi = (TabPageIndicator)findViewById(R.id.main_tpi);


        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mItems,this);
        vp.setAdapter(mPagerAdapter);
        tpi.setViewPager(vp);

        vp.setCurrentItem(0);

    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] mFragments;
        Tab[] mTabs;
        private Context context;

        private static final Field sActiveField;

        static {
            Field f = null;
            try {
                Class<?> c = Class.forName("android.support.v4.app.FragmentManagerImpl");
                f = c.getDeclaredField("mActive");
                f.setAccessible(true);
            } catch (Exception e) {}

            sActiveField = f;
        }

        public PagerAdapter(FragmentManager fm, Tab[] tabs, Context context) {
            super(fm);
            mTabs = tabs;
            mFragments = new Fragment[mTabs.length];
            this.context = context;


            //dirty way to get reference of cached fragment
            try{
                ArrayList<Fragment> mActive = (ArrayList<Fragment>)sActiveField.get(fm);
                if(mActive != null){
                    for(Fragment fragment : mActive){
                        if(fragment instanceof TrainingFragment)
                            setFragment(Tab.TRAINING, fragment);
                        else if(fragment instanceof NutritionFragment)
                            setFragment(Tab.NUTRITION, fragment);
                        else if(fragment instanceof StatsFragment)
                            setFragment(Tab.STATS, fragment);
                    }
                }
            }
            catch(Exception e){}
        }


        private void setFragment(Tab tab, Fragment f){
            for(int i = 0; i < mTabs.length; i++)
                if(mTabs[i] == tab){
                    mFragments[i] = f;
                    break;
                }
        }

        @Override
        public Fragment getItem(int position) {
            if(mFragments[position] == null){
                switch (mTabs[position]) {
                    case TRAINING:
                        mFragments[position] = TrainingFragment.newInstance();
                        break;
                    case NUTRITION:
                        mFragments[position] = NutritionFragment.newInstance();
                        break;
                    case STATS:
                        mFragments[position] = StatsFragment.newInstance();
                        break;
                }
            }

            return mFragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs[position].toString().toUpperCase();
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }

    public enum Tab {
        TRAINING ("ТРЕНИРОВКА"),
        NUTRITION ("ПИТАНИЕ"),
        STATS ("СТАИСТИКА");
        private final String name;

        private Tab(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName != null) && name.equals(otherName);
        }

        public String toString(){
            return name;
        }

    }

}
