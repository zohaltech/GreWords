package com.zohaltech.app.grewords.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.messaging.FirebaseMessaging;
import com.zohaltech.app.grewords.BuildConfig;
import com.zohaltech.app.grewords.R;
import com.zohaltech.app.grewords.api.Webservice;
import com.zohaltech.app.grewords.classes.App;
import com.zohaltech.app.grewords.classes.DialogManager;
import com.zohaltech.app.grewords.classes.Helper;
import com.zohaltech.app.grewords.classes.ReminderManager;
import com.zohaltech.app.grewords.data.SystemSettings;
import com.zohaltech.app.grewords.entities.SystemSetting;
import com.zohaltech.app.grewords.fragments.DrawerFragment;
import com.zohaltech.app.grewords.fragments.LessonsFragment;
import com.zohaltech.app.grewords.fragments.SearchFragment;
import com.zohaltech.app.grewords.serializables.ReminderSettings;

import java.util.Objects;

import widgets.MySnackbar;


public class MainActivity extends EnhancedActivity {
    
    private final String APP_VERSION = "APP_VERSION";
    
    long startTime;
    private DrawerLayout   drawerLayout;
    private DrawerFragment drawerFragment;
    private Fragment       fragment;
    
    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_main);
        
        try {
            for (int i = 106; i < BuildConfig.VERSION_CODE; i++) {
                try {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("" + i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FirebaseMessaging.getInstance().subscribeToTopic("public");
            FirebaseMessaging.getInstance().subscribeToTopic("" + BuildConfig.VERSION_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //        getExamples();
        startTime = System.currentTimeMillis() - 5000;
        if (App.preferences.getInt(APP_VERSION, 0) != BuildConfig.VERSION_CODE) {
            SystemSetting setting = SystemSettings.getCurrentSettings(this);
            Objects.requireNonNull(setting).setInstalled(false);
            SharedPreferences.Editor editor = App.preferences.edit();
            editor.putString(ReminderManager.REMINDER_SETTINGS, null);
            editor.putInt(APP_VERSION, BuildConfig.VERSION_CODE);
            editor.apply();
        }
        
        new Webservice().callCheckForUpdate(this);
        
        if (!App.preferences.getBoolean("RATED", false)) {
            App.preferences.edit().putInt("APP_RUN_COUNT", App.preferences.getInt("APP_RUN_COUNT", 0) + 1).apply();
        }
        
        if (!App.preferences.getBoolean("SCHEDULED", false)) {
            App.preferences.edit().putInt("APP_RUN_COUNT_FOR_SCHEDULE", App.preferences.getInt("APP_RUN_COUNT_FOR_SCHEDULE", 0) + 1).apply();
        }
    }
    
    @Override
    protected void onToolbarCreated() {
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        Objects.requireNonNull(drawerFragment).setUp(drawerLayout, toolbar);
        drawerFragment.setMenuVisibility(true);
        displayView(0);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        int runCountForSchedule = App.preferences.getInt("APP_RUN_COUNT_FOR_SCHEDULE", 0);
        boolean scheduled = App.preferences.getBoolean("SCHEDULED", false);
        if (runCountForSchedule != 0 && runCountForSchedule % 3 == 0 && !scheduled && ReminderManager.getReminderSettings().getStatus() == ReminderSettings.Status.STOP) {
            DialogManager.getPopupDialog(this, "Start Scheduler", "You have not set any reminder yet, Would you like to try it?", "Set it Now", "Remind me Later", null, () -> {
                App.preferences.edit().putBoolean("SCHEDULED", true).apply();
                Intent intent = new Intent(MainActivity.this, SchedulerActivity.class);
                startActivity(intent);
            }, () -> {
                //do nothing
            }).show();
        }
        
        int runCount = App.preferences.getInt("APP_RUN_COUNT", 0);
        boolean rated = App.preferences.getBoolean("RATED", false);
        if (runCount != 0 && runCount % 5 == 0 && !rated) {
            App.preferences.edit().putInt("APP_RUN_COUNT", App.preferences.getInt("APP_RUN_COUNT", 0) + 1).apply();
            Dialog dialog = DialogManager.getPopupDialog(this, "Rate App", "If " + getString(R.string.app_name) + " is useful to you, would you like to rate?", "Yes, I rate it", "Not now!", null, () -> Helper.rateApp(MainActivity.this), () -> {
                //do nothing
            });
            dialog.show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                return true;  // Return true to expand action view
            }
            
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                displayView(0);
                return true;  // Return true to collapse action view
            }
        });
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (fragment instanceof SearchFragment) {
                    ((SearchFragment) fragment).search(newText);
                }
                return false;
            }
        });
        
        searchView.setOnSearchClickListener(v -> displayView(1));
        
        return true;
    }
    
    private void displayView(int position) {
        fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new LessonsFragment();
                title = "Lessons";
                break;
            case 1:
                fragment = new SearchFragment();
                title = "Search";
                break;
            default:
                break;
        }
        
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            //fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();
            
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if ((System.currentTimeMillis() - startTime) > 2000) {
            startTime = System.currentTimeMillis();
            MySnackbar.show(drawerLayout, getString(R.string.press_back_again_to_exit), Snackbar.LENGTH_SHORT);
        } else {
            super.onBackPressed();
        }
    }
    
    //public void getExamples(){
    //    ArrayList<Vocabulary> vocabularies= Vocabularies.select();
    //
    //    for (Vocabulary vocabulary:vocabularies) {
    //        String[] examples=vocabulary.getExamples().split("\n");
    //
    //        int k=1;
    //        for (String ex:examples) {
    //            String[] exampleArr=ex.split(".");
    //            if(!ex.equals("\n") && !ex.equals("\r")) {
    //                //String exStr = ex.substring(3).replace("\n","");
    //                String exStr = ex.replace("\n","").replace("\r", "");
    //
    //                Example e = new Example(vocabulary.getId(), k, exStr);
    //                Examples.insert(e);
    //                k++;
    //            }
    //
    //
    //
    //
    //        }
    //        String s="";
    //
    //    }
    //
    //}
}