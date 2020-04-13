package com.simcoder.bimbo.instagram.Likes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.BottomNavigationViewHelper;

public class LikesActivity extends AppCompatActivity {
    private static final String TAG = "LikesActivity";
    public static final int ACTIVITY_NUM = 3;

    private Context mContext = LikesActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instagramactivity_home);
        Log.d(TAG, "onCreate: started");

        setupBottomNavigationView();
    }

    //* Bottom Nav View setup *
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setup up BottomNavView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
                          if (bottomNavigationViewEx != null) {
                              BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);

                              if (bottomNavigationViewEx != null) {
                                  BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
                              }
                              if (bottomNavigationViewEx != null) {
                                  Menu menu = bottomNavigationViewEx.getMenu();
                                  if (menu != null) {
                                      if (ACTIVITY_NUM != 0) {

                                          MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
                                          if (menuItem != null) {
                                              menuItem.setChecked(true);
                                          }
                                      }
                                  }
                              }}
    }
}

// #BuiltByGOD
