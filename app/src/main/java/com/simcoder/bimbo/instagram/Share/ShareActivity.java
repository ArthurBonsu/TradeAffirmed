package com.simcoder.bimbo.instagram.Share;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.BottomNavigationViewHelper;
import com.simcoder.bimbo.instagram.Utils.Permissions;
import com.simcoder.bimbo.instagram.Utils.SectionsPagerAdapter;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.BottomNavigationViewHelper;
import com.simcoder.bimbo.instagram.Utils.Permissions;
import com.simcoder.bimbo.instagram.Utils.SectionsPagerAdapter;

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";

    //Constants
    public static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    private Context mContext = ShareActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started");


            if (checkPermissionsArray(Permissions.PERMISSIONS)) {
                setupViewPager();
            } else {
                verifyPermissions(Permissions.PERMISSIONS);
            }
        }

    /**
     * return the current tab number
     * @return
     */
    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }


    /**
     * setup viewpager for managing the tabs
     */
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        if (getSupportFragmentManager() != null) {
      if (adapter != null){
            adapter.addFragment(new GalleryFragment());
            adapter.addFragment(new PhotoFragment());

            mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
           if (mViewPager != null){
            mViewPager.setAdapter(adapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
           if (tabLayout != null){
            tabLayout.setupWithViewPager(mViewPager);
          if (tabLayout != null){
              if (tabLayout.getTabAt(0) != null){
            tabLayout.getTabAt(0).setText(getString(R.string.gallery));
             if (tabLayout.getTabAt(1) != null){
            tabLayout.getTabAt(1).setText(getString(R.string.photo));
        }}}}}}}
    }
    public int getTask() {
        if (getIntent() != null){
        if (getIntent().getFlags() != 0) {
            Log.d(TAG, "getTask: TASK: " + getIntent().getFlags());

        }   }
          if (getIntent() != null){
            return getIntent().getFlags();
    }
        return 0;
    }
        /**
         * Verify permissions sent to Array
         * @param permissions
         */

    public void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions");

           ActivityCompat.requestPermissions(
                   ShareActivity.this,
                   permissions,
                   VERIFY_PERMISSIONS_REQUEST
           );

    }

    /**
     * Check an Array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permissions array");

        for (int i = 0; i < permissions.length; i++) {
            if (permissions != null) {
                String check = permissions[i];
                if (checkPermissions(check)) {
                    return true;
                }
            }        }
            return false;
        }

    /**
     * Check a single permission for verification
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission) {

            Log.d(TAG, "checkPermissions: checking permissions: " + permission);

            int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

            if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
                return false;
            } else {
                Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
                return true;
            }

        }


    //* Bottom Nav View setup *
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setup up BottomNavView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        if (bottomNavigationViewEx != null){
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
           if (bottomNavigationViewEx != null) {
               Menu menu = bottomNavigationViewEx.getMenu();
             if (menu != null){
               MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
              if (menuItem != null) {
                  menuItem.setChecked(true);
              }
           }}
    }}
}
