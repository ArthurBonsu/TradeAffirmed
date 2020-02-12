package com.simcoder.bimbo.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Models.Photo;

import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.*;
import com.simcoder.bimbo.instagram.Utils.ViewCommentsFragment;
import com.simcoder.bimbo.instagram.Utils.ViewPostFragment;
import com.simcoder.bimbo.instagram.Utils.ViewProfileFragment;

/**
 * Created by User on 5/28/2017.
 */
public abstract class ProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnGridImageSelectedListener ,
        ViewPostFragment.OnCommentThreadSelectedListener,
        ViewProfileFragment.OnGridImageSelectedListener{

    private static final String TAG = "ProfileActivity";

    @Override
    public void onCommentThreadSelectedListener(Photo photo) {
        Log.d(TAG, "onCommentThreadSelectedListener:  selected a comment thread");

        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();

        if (args != null) {
            args.putParcelable(getString(R.string.photo), photo);
            if (fragment != null) {

                fragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (transaction != null) {
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(getString(R.string.view_comments_fragment));
                    transaction.commit();
                }
            }
        }
    }
    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        Log.d(TAG, "onGridImageSelected: selected an image gridview: " + photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        if (args != null) {
            args.putParcelable(getString(R.string.photo), photo);

            args.putInt(getString(R.string.activity_number), activityNumber);
            if (fragment != null) {
                fragment.setArguments(args);
                if (getSupportFragmentManager() != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (transaction != null) {

                        transaction.replace(R.id.container, fragment);
                        transaction.addToBackStack(getString(R.string.view_post_fragment));
                        transaction.commit();

                    }
                }
            }
        }
    }
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        init();


    }

    private void init(){
        Log.d(TAG, "init: inflating " + getString(R.string.profile_fragment));

        Intent intent = getIntent();
        if (intent != null){
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "init: searching for user object attached as intent extra");
            if(intent.hasExtra(getString(R.string.intent_user))){
                User user = intent.getParcelableExtra(getString(R.string.intent_user));
                if (user != null ){
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                if(!user.getuid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Log.d(TAG, "init: inflating view profile");
                    ViewProfileFragment fragment = new ViewProfileFragment();

                    Bundle args = new Bundle();
     if (args != null) {
         args.putParcelable(getString(R.string.intent_user),
                 intent.getParcelableExtra(getString(R.string.intent_user)));
         fragment.setArguments(args);
     }
                    if (getSupportFragmentManager() != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                          if (transaction != null) {
                              transaction.replace(R.id.container, fragment);
                              transaction.addToBackStack(getString(R.string.view_profile_fragment));
                              transaction.commit();
                          }
                    }}else{
                    Log.d(TAG, "init: inflating Profile");
                    ProfileFragment fragment = new ProfileFragment();
                       if (fragment != null) {
                         if (getSupportFragmentManager() != null) {
                             FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
                           if (transaction != null) {
                               transaction.replace(R.id.container, fragment);
                               transaction.addToBackStack(getString(R.string.profile_fragment));
                               transaction.commit();
                           }  }}}
            }else{
                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }

        }else{
            Log.d(TAG, "init: inflating Profile");
            ProfileFragment fragment = new ProfileFragment();
                if (   ProfileActivity.this.getSupportFragmentManager() != null){
            FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
       if (transaction != null) {
           transaction.replace(R.id.container, fragment);
           transaction.addToBackStack(getString(R.string.profile_fragment));
           transaction.commit();
       } }
    }}
}}}}