package com.zohaltech.app.grewords.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zohaltech.app.grewords.classes.WebApiClient;
import com.zohaltech.app.grewords.data.Examples;
import com.zohaltech.app.grewords.data.Vocabularies;
import com.zohaltech.app.grewords.entities.Example;
import com.zohaltech.app.grewords.entities.Vocabulary;
import com.zohaltech.app.grewords.fragments.DrawerFragment;
import com.zohaltech.app.grewords.fragments.SearchFragment;
import com.zohaltech.app.grewords.fragments.LessonsFragment;

import com.zohaltech.app.grewords.R;

import java.util.ArrayList;

import widgets.MySnackbar;


public class MainActivity extends EnhancedActivity {
    long startTime;
    private DrawerLayout   drawerLayout;
    private DrawerFragment drawerFragment;
    private Fragment       fragment;

    @Override
    protected void onCreated() {
        setContentView(R.layout.activity_main);
//        getExamples();
        startTime = System.currentTimeMillis() - 5000;
        WebApiClient.sendUserData();
    }

    @Override
    protected void onToolbarCreated() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(drawerLayout, toolbar);
        drawerFragment.setMenuVisibility(true);
        displayView(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                displayView(0);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                return true;  // Return true to expand action view
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (fragment != null && fragment instanceof SearchFragment) {
                    ((SearchFragment) fragment).search(newText);
                }
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayView(1);
            }
        });

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