package com.simcoder.bimbo.instagram.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
import com.simcoder.bimbo.instagram.Login.InstagramLoginActivity;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;

import com.simcoder.bimbo.instagram.Utils.BottomNavigationViewHelper;
import com.simcoder.bimbo.instagram.Utils.MainfeedListAdapter;
import com.simcoder.bimbo.instagram.Utils.SectionsPagerAdapter;
import com.simcoder.bimbo.instagram.Utils.UniversalImageLoader;
import com.simcoder.bimbo.instagram.Utils.ViewCommentsFragment;

public class InstagramHomeActivity extends AppCompatActivity implements
        MainfeedListAdapter.OnLoadMoreItemsListener{

    @Override
    public void onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: displaying more photos");
        com.simcoder.bimbo.instagram.Home.HomeFragment fragment = (com.simcoder.bimbo.instagram.Home.HomeFragment)getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.viewpager_container + ":" + mViewPager.getCurrentItem());
        if(fragment != null){
            fragment.displayMorePhotos();
        }
    }

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 1;

    private Context mContext = InstagramHomeActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instagramactivity_home);
        Log.d(TAG, "onCreate: starting.");
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutParent);

        setupFirebaseAuth();

        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();

    }



    public void onCommentThreadSelected(Photo photo, String callingActivity){
        Log.d(TAG, "onCommentThreadSelected: selected a coemment thread");

        ViewCommentsFragment fragment  = new ViewCommentsFragment();

        Bundle args = new Bundle();
         if (args != null) {
             if (photo != null) {
                 args.putParcelable(getString(R.string.photo), photo);
                 args.putString(getString(R.string.instagramhomeactivity), getString(R.string.instagramhomeactivity
                 ));
                 if (fragment != null) {
                     fragment.setArguments(args);
                 }
             }
         }
             if(getSupportFragmentManager() != null) {
                 FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                 if (transaction != null) {
                     transaction.replace(R.id.container, fragment);
                     transaction.addToBackStack(getString(R.string.view_comments_fragment));
                     transaction.commit();
                 }

             }}

    public void hideLayout(){
        Log.d(TAG, "hideLayout: hiding layout");
         if (mRelativeLayout != null){        mRelativeLayout.setVisibility(View.GONE);}
         if (mFrameLayout != null) {
             mFrameLayout.setVisibility(View.VISIBLE);
         }
         }


    public void showLayout(){
        Log.d(TAG, "hideLayout: showing layout");
         if (mRelativeLayout != null) {
             mRelativeLayout.setVisibility(View.VISIBLE);
         }
         if (mFrameLayout != null) {
             mFrameLayout.setVisibility(View.GONE);

         }}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
                 if (mFrameLayout != null){
        if(mFrameLayout.getVisibility() == View.VISIBLE) {
        }
            showLayout();
        }
    }


    private void initImageLoader() {
        if (mContext != null) {

            UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
            if (ImageLoader.getInstance() != null) {
                if (universalImageLoader.getConfig() != null) {
                    ImageLoader.getInstance().init(universalImageLoader.getConfig());
                }
            }
        }
    }
    /**
     * Responsible for adding the 3 tabs: Camera, Home, Messages
     */
    private void setupViewPager() {
        if (getSupportFragmentManager() != null) {
            SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
            if (adapter != null) {
                if (new CameraFragment() != null) {
                    adapter.addFragment(new CameraFragment()); //index 0
                }
                if (new HomeFragment() != null) {
                    adapter.addFragment(new HomeFragment()); //index 1
                }
                if (new MessagesFragment() != null) {
                    adapter.addFragment(new MessagesFragment()); //index 2
                }
                if (mViewPager != null) {
                    mViewPager.setAdapter(adapter);
                }
            }
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
               if (mViewPager != null) {
                   tabLayout.setupWithViewPager(mViewPager);
               }
                  if (tabLayout.getTabAt(0) != null) {

                      tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
                  }
                  if (tabLayout.getTabAt(1) != null) {
                      tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram_black);
                  }
                  if (tabLayout.getTabAt(2) != null) {
                      tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
                  }
                  }
    }
    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        if (bottomNavigationViewEx != null) {

            {
                BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
            }
            if (mContext != null) {

                BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);

                if (bottomNavigationViewEx != null) {
                    ;
                    Menu menu = bottomNavigationViewEx.getMenu();
                    if (menu != null) {
                        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
                        if (menuItem != null) {
                            menuItem.setChecked(true);
                        }
                    }
                }
            }
        }
    }
     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * checks to see if the @param 'user' is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null) {
            if (mContext != null) {
                Intent intent = new Intent(mContext, InstagramLoginActivity.class);
                startActivity(intent);
            }
        }}
    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
          if (user != null) {
              checkCurrentUser(user);
          }
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null) {
            if (mAuthListener != null) {
                mAuth.addAuthStateListener(mAuthListener);
            }
        }
        if (mViewPager != null) {

            mViewPager.setCurrentItem(HOME_FRAGMENT);
        }
        if (mAuth.getCurrentUser() != null) {
            checkCurrentUser(mAuth.getCurrentUser());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
          if (mAuth != null) {
              mAuth.removeAuthStateListener(mAuthListener);
          }    }
    }


}