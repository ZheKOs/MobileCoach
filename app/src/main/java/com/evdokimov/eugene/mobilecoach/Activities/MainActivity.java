package com.evdokimov.eugene.mobilecoach.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evdokimov.eugene.mobilecoach.Fragments.NutritionFragment;
import com.evdokimov.eugene.mobilecoach.Fragments.StatsFragment;
import com.evdokimov.eugene.mobilecoach.Fragments.TrainingFragment;
import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Utils.OnWorkoutPlanSelectedListener;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.rey.material.app.ToolbarManager;
import com.rey.material.widget.SnackBar;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity{

    private Context context;

    private int REQUEST_ADD_NEW_PLAN = 1;

    private Toolbar mToolbar;
    private ToolbarManager mToolbarManager;

    private DrawerLayout dl_navigator;
    private View draweContent;

    private FrameLayout fl_drawer;
    private ListView lv_drawer;

    private PagerAdapter mPagerAdapter;
    private CustomViewPager vp;

    private SmartTabLayout viewPagerTab;
    //private CustomeTabPageIndicator tpi;

    private SnackBar snackBar;

    private Tab[] mItems = new Tab[]{ //initialize tabs using enum
            Tab.TRAINING, Tab.NUTRITION, Tab.STATS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getApplicationContext(); //save context for using it in specific components

        HelperFactory.setDbHelper(context);

        dl_navigator = (DrawerLayout)findViewById(R.id.main_dl);
        fl_drawer = (FrameLayout) findViewById(R.id.main_fl_drawer);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        mToolbarManager = new ToolbarManager(this, mToolbar, 0, R.style.ToolbarRippleStyle, R.anim.abc_fade_in, R.anim.abc_fade_out);
        mToolbarManager.setNavigationManager(new ToolbarManager.BaseNavigationManager(R.style.NavigationDrawerDrawable, this, mToolbar, dl_navigator) {
            @Override
            public void onNavigationClick() {
                if (mToolbarManager.getCurrentGroup() != 0)
                    mToolbarManager.setCurrentGroup(0);
                else
                    dl_navigator.openDrawer(GravityCompat.START);
            }

            @Override
            public boolean isBackState() {
                return super.isBackState() || mToolbarManager.getCurrentGroup() != 0;
            }

            @Override
            protected boolean shouldSyncDrawerSlidingProgress() {
                return super.shouldSyncDrawerSlidingProgress() && mToolbarManager.getCurrentGroup() == 0;
            }

        });

        vp = (CustomViewPager)findViewById(R.id.main_vp);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        //tpi = (TabPageIndicator)findViewById(R.id.main_tpi);


        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mItems,this);

        final LayoutInflater inflater = LayoutInflater.from(this);
        final Resources res = getResources();

        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, android.support.v4.view.PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.layout_tab_icon, container, false);
                icon.setPadding(3, 3, 3, 3);
                switch (position) {
                    case 0:
                        icon.setImageDrawable(res.getDrawable(R.drawable.dumbbell_white_36));
                        break;
                    case 1:
                        icon.setImageDrawable(res.getDrawable(R.drawable.food_apple_white_36));
                        break;
                    case 2:
                        icon.setImageDrawable(res.getDrawable(R.drawable.chart_areaspline_white_36));
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return icon;
            }
        });

        vp.setAdapter(mPagerAdapter);
        viewPagerTab.setViewPager(vp);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                setSelectedDrawerItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        //tpi.setViewPager(vp);

        vp.setCurrentItem(0);
        setSelectedDrawerItem(0);

        draweContent = findViewById(R.id.drawer_content);
        TextView mc = (TextView) draweContent.findViewById(R.id.tv_nav_mc);
        Typeface nrkis = Typeface.createFromAsset(getAssets(),
                "fonts/nrkis.ttf");
        mc.setTypeface(nrkis);
        //draweContent.setOnClickListener();



    }

    private void setSelectedDrawerItem(int position){
        View v;
        switch (position){
            case 0:
                v = findViewById(R.id.btn_nav_train);
                v.setBackgroundColor(getResources().getColor(R.color.half_dgrey));
                v.setEnabled(false);

                v = findViewById(R.id.btn_nav_nutr);
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setEnabled(true);

                v = findViewById(R.id.btn_nav_stats);
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setEnabled(true);
                break;
            case 1:

                v = findViewById(R.id.btn_nav_train);
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setEnabled(true);

                v = findViewById(R.id.btn_nav_nutr);
                v.setBackgroundColor(getResources().getColor(R.color.half_dgrey));
                v.setEnabled(false);

                v = findViewById(R.id.btn_nav_stats);
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setEnabled(true);

                break;
            case 2:

                v = findViewById(R.id.btn_nav_train);
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setEnabled(true);

                v = findViewById(R.id.btn_nav_nutr);
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setEnabled(true);

                v = findViewById(R.id.btn_nav_stats);
                v.setBackgroundColor(getResources().getColor(R.color.half_dgrey));
                v.setEnabled(false);

                break;
        }
    }

    public void drawerListener(View v){ //it uses in xml code for

        switch (v.getId()){
            case R.id.btn_nav_train:
                Toast.makeText(context,"Тренировка",Toast.LENGTH_SHORT).show();
                vp.setCurrentItem(0);
                setSelectedDrawerItem(0);
                dl_navigator.closeDrawer(fl_drawer);
                break;
            case R.id.btn_nav_nutr:
                Toast.makeText(context,"Питание",Toast.LENGTH_SHORT).show();
                vp.setCurrentItem(1);
                setSelectedDrawerItem(1);
                dl_navigator.closeDrawer(fl_drawer);
                break;
            case R.id.btn_nav_stats:
                Toast.makeText(context,"Статистика",Toast.LENGTH_SHORT).show();
                vp.setCurrentItem(2);
                setSelectedDrawerItem(2);
                dl_navigator.closeDrawer(fl_drawer);
                break;
            case R.id.btn_nav_workouts:
                Toast.makeText(context,"Упражнения",Toast.LENGTH_SHORT).show();
                dl_navigator.closeDrawer(fl_drawer);
                break;
            case R.id.btn_nav_dishes:
                Toast.makeText(context,"Блюда",Toast.LENGTH_SHORT).show();
                dl_navigator.closeDrawer(fl_drawer);
                break;
            case R.id.btn_nav_settings:
                Toast.makeText(context,"Настройки",Toast.LENGTH_SHORT).show();
                dl_navigator.closeDrawer(fl_drawer);
                break;
            case R.id.btn_nav_help:
                Toast.makeText(context,"Справка/отзыв",Toast.LENGTH_SHORT).show();
                dl_navigator.closeDrawer(fl_drawer);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        HelperFactory.releaseDbHelper();
        super.onDestroy();
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


            //dirty way to get reference of cached fragment TODO in proper way
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
        TRAINING ("ТРЕНИНГ"),
        NUTRITION ("ПИТАНИЕ"),
        STATS ("СТАТИСТИКА");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbarManager.createMenu(R.menu.menu_main);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mToolbarManager.onPrepareMenu();
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                int currentItem = vp.getCurrentItem();
                switch (currentItem){
                    case 0:
                        Intent intent = new Intent(context, EditTrainingPlanActivity.class);
                        intent.putExtra("addingMode",true);
                        startActivityForResult(intent, REQUEST_ADD_NEW_PLAN);
                        Toast.makeText(context,"добавление нового плана",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context,"добавление в план блюло",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(context,"добавление статистики",Toast.LENGTH_SHORT).show();
                        break;
                }

                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ADD_NEW_PLAN == requestCode){
            switch (resultCode){
                case EditTrainingPlanActivity.RESULT_CREATED:
                    //this works but it is dirty way to realize refreshing viewPager TODO in proper way
                    mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mItems,this);
                    vp.setAdapter(mPagerAdapter);
                    viewPagerTab.setViewPager(vp);
                    vp.setCurrentItem(0);
                    setSelectedDrawerItem(0);

                    //mPagerAdapter.notifyDataSetChanged(); //not working :(

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
