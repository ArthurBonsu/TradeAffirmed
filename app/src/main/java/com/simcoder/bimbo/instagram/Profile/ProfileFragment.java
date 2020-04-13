package com.simcoder.bimbo.instagram.Profile;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Models.Comment;
import com.simcoder.bimbo.instagram.Models.Like;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.Tags;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;
import com.simcoder.bimbo.instagram.Models.UserSettings;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.BottomNavigationViewHelper;
import com.simcoder.bimbo.instagram.Utils.FirebaseMethods;
import com.simcoder.bimbo.instagram.Utils.GridImageAdapter;
import com.simcoder.bimbo.instagram.Utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";


    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Photo photo, int activityNumber);
    }
    OnGridImageSelectedListener mOnGridImageSelectedListener;

    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    //widgets
    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;
    DatabaseReference mRef;

    //vars
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;
   String thePhotosKeykey;
    private ArrayList<Photo> mPhotos;
    String  thefollowerkey;
  String theuserkeyname;
      String traderorcustomer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        mContext = getActivity();
        mFirebaseMethods = new FirebaseMethods(getActivity());
        Log.d(TAG, "onCreateView: stared.");


        setupBottomNavigationView();
        setupToolbar();

        setupFirebaseAuth();
        setupGridView();

        getFollowersCount();
        getFollowingCount();
        getPostsCount();

        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile);
        if (editProfile != null)
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                 if (intent != null){
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
               if (getActivity() != null){
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }}}
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        try {
            if (getActivity() != null) {
                mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
            }
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
        if (context != null) {
            super.onAttach(context);
        }
    }

    private void setupGridView() {
        Log.d(TAG, "setupGridView: Setting up image grid.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (FirebaseAuth.getInstance() != null) {
            if (user != null) {
                String userid = "";
                userid = user.getUid();
                final ArrayList<Photo> photos = new ArrayList<>();
                //THIS SHOWS ALL THE POSTS THAT YOU MADE
                // TO BE ABLE TO DO THAT YOU SHOULD BE A TRADER
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                if (FirebaseDatabase.getInstance() != null) {
         if (userid != null){
                    Query query = reference.child("Photos").orderByChild("tid").equalTo(userid);
        if (query != null){
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                Photo photo = new Photo();

                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                       if (dataSnapshot != null){
                                    thePhotosKeykey = singleSnapshot.getKey();
                                      if (singleSnapshot != null){
                                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                                     if (photo != null){
                                         if (objectMap.get("caption") != null){
                                    photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());

                                    //     photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                                       if (objectMap.get("photoid") != null){
                                    photo.setphotoid(objectMap.get("photoid").toString());

                                    //     photo.setuid(objectMap.get("uid").toString());

                                           if (objectMap.get("date") != null){
                                    photo.setdate(objectMap.get("date").toString());
                                     if (objectMap.get("image") != null){
                                    photo.setimage(objectMap.get("image").toString());


                                    ArrayList<Like> likes = new ArrayList<Like>();
                                    for (DataSnapshot dSnapshot : singleSnapshot
                                            .child(thePhotosKeykey).child("Likes").getChildren()) {

                                        thePhotosKeykey = singleSnapshot.getKey();
                                        if (singleSnapshot != null) {
                                            String likeid = dSnapshot.getKey();
                                            if (dSnapshot != null) {
                                                if (likeid != null) {
                                                    if (dSnapshot.child(likeid).child("Users") != null) {
                                                    }
                                                    String userkey = dSnapshot.child(likeid).child("Users").getKey();

                                                    Like like = new Like();
                                                    if (dSnapshot.child("likeid").getValue(String.class) != null) {
                                                        like.setLikeid(dSnapshot.child("likeid").getValue(String.class));
                                                        if (dSnapshot.child("number").getValue(String.class) != null) {
                                                            like.setnumber(dSnapshot.child("number").getValue(String.class));
                                                            if (userkey != null) {
                                                                if (dSnapshot.child(likeid).child("Users").child(userkey).child("name").getValue(String.class) != null) {
                                                                    like.setname(dSnapshot.child(likeid).child("Users").child(userkey).child("name").getValue(String.class));
                                                                    like.setuid(dSnapshot.child(likeid).child("Users").child(userkey).child("uid").getValue(String.class));
                                                                    likes.add(like);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                        ArrayList<Comment> comments = new ArrayList<Comment>();
                                                         if (thePhotosKeykey != null){
                                        for (DataSnapshot dSnapshot : dataSnapshot
                                                .child(thePhotosKeykey).child("Comments").getChildren()) {

                                            thePhotosKeykey = singleSnapshot.getKey();
                                            if (singleSnapshot != null) {
                                                String commentkey = dSnapshot.getKey();
                                                   if (dSnapshot != null){
                                                String thecommenteruserkey = dSnapshot.child("Users").getKey();
                                           if (dSnapshot != null){
                                            if ( dSnapshot.child("Users") != null){
                                                Comment comment = new Comment();

                                                comment.setComment(dSnapshot.child(commentkey).child("comment").getValue(String.class));
                                               if (commentkey != null){
                                                if (dSnapshot.child(commentkey).child("comment").getValue(String.class) != null){
                                                     if (dSnapshot.child("commentkey").getValue(String.class) != null){
                                                comment.setcommentkey(dSnapshot.child("commentkey").getValue(String.class));
                                                               if (commentkey != null){

                                                                   if (thecommenteruserkey != null){
                                                if (dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("name")  != null){
                                                if (commentkey != null){
                                                    if (thecommenteruserkey != null){

                                                        if (dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("name") != null){
                                                comment.setname(dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("name").getValue(Comment.class).getname());
                                                 if (dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("uid") != null){
                                                comment.setuid(dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("uid").getValue(Comment.class).getuid());
                                                 if (comment != null){
                                                comments.add(comment);
                                            }
                                        }}}}}}}}}}}}}}}}


                                    ArrayList<Tags> tagshere = new ArrayList<Tags>();
                                          if (thePhotosKeykey != null){
                                              if (dataSnapshot
                                                      .child(thePhotosKeykey).child("tags") != null){
                                    for (DataSnapshot dSnapshot : dataSnapshot
                                            .child(thePhotosKeykey).child("tags").getChildren()) {
                                        if (singleSnapshot != null) {
                                            thePhotosKeykey = singleSnapshot.getKey();

                                            Tags tags1 = new Tags();

                                            //tagvalue as setvalue
                                            if (tags1 != null){

                                                if (dSnapshot.child("image").getValue(String.class) != null){
                                                if (dSnapshot.child("image").getValue(String.class) != null){
                                            tags1.setimage(dSnapshot.child("image").getValue(String.class));
                        if (dSnapshot.child("name").getValue(String.class) != null){
                                            tags1.setname(dSnapshot.child("name").getValue(String.class));
                                          if (dSnapshot.child("uid").getValue(String.class) != null){
                                            tags1.setuid(dSnapshot.child("uid").getValue(String.class));
                                           if (tags1 != null){
                                            tagshere.add(tags1);
                                        }

                                    }}}}}}}
                                    if (comments != null){
                                        if (photo != null){
                                    photo.setComments(comments);
                                    photo.setLikes(likes);
                                    photo.setTag(tagshere);
                                 if (mPhotos != null){
                                    mPhotos.add(photo);

                                }}}}}}}}}}}}}
                            } catch (NullPointerException e) {
                                Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage());
                            }

                            //setup our image grid
                               if (getResources() != null){
                                   if (getResources().getDisplayMetrics() != null){

                            int gridWidth = getResources().getDisplayMetrics().widthPixels;
                            if (gridWidth != 0){

                            if (NUM_GRID_COLUMNS != 0){
                            int imageWidth = gridWidth / NUM_GRID_COLUMNS;
                           if (gridView != null){
                            gridView.setColumnWidth(imageWidth);

                            ArrayList<String> imgUrls = new ArrayList<String>();
                             if (photos != null){
                            for (int i = 0; i < photos.size(); i++) {
                                if (imgUrls != null) {
                                    if (photos != null) {
                                        imgUrls.add(photos.get(i).getimage());
                                    }
                                }
                            }
                                if (getActivity() != null){
                            GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview,
                                    "", imgUrls);
                            if (gridView != null){
                                if (adapter != null){
                            gridView.setAdapter(adapter);
                           if (gridView != null){
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (photos.get(position) != null){
                                    mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
                                    }       }
                            });
                        }
                        }}}}}}}}}}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled: query cancelled.");
                        }
                    });
                }
            }
        }
    }}}
    private void getFollowersCount() {
        mFollowersCount = 0;

        // Should be roles
        if (FirebaseDatabase.getInstance() != null) {

            mRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
             if (myRef != null){
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                         if (mAuth.getCurrentUser() != null){
                             if (dataSnapshot != null){



                    if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("Users").child("Drivers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers");
             if (query != null) {
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                 for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                if (singleSnapshot != null){
                                    thefollowerkey = singleSnapshot.getKey();
                                    if (singleSnapshot.child(thefollowerkey) != null){
                                        if (singleSnapshot.child(thefollowerkey).child("name") != null){
                                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.child(thefollowerkey).child("name").getValue());
                                              if (singleSnapshot.child("number").getValue() != null){

                                    mFollowersCount = Integer.parseInt(singleSnapshot.child("number").getValue(String.class));
                                 if (mFollowersCount != 0){
                                    mFollowersCount++;

                                }
                                //status has to change

                                mFollowers.setText(String.valueOf(mFollowersCount));
                            }
                            }}}}}
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
             }

                    } else {
                                   if (FirebaseAuth.getInstance() != null){
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                              if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Query query = reference.child("Users").child("Customers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers");
                 if (query != null){
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        if (dataSnapshot != null) {
                                            if (singleSnapshot != null){
                                            thefollowerkey = singleSnapshot.getKey();
                                      if (thefollowerkey != null){
                                          if (singleSnapshot.child(thefollowerkey).child("name") != null){
                                            Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.child(thefollowerkey).child("name").getValue());
                                if (singleSnapshot.child("number").getValue() != null){
                                            mFollowersCount = Integer.parseInt(singleSnapshot.child("number").getValue().toString());
                                    if (mFollowersCount != 0){
                                            mFollowersCount++;

                                        }
                                        //status has to change

                                        mFollowers.setText(String.valueOf(mFollowersCount));
                                    }
                                }
                            }}}}}}
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
                }}}}}
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }}
//TRADER or DRIVER
    //CREATING THE DATA AND TRADER PORTION

    private void getFollowingCount() {
        mFollowingCount = 0;
        if (FirebaseDatabase.getInstance() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
      if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Query query = reference.child("Users").child("Drivers")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
       if (query != null){
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if (dataSnapshot != null) {
                            Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.child("name").getValue());
                  if (singleSnapshot.child("name") != null){
                                 if (singleSnapshot.child("numbers").getValue() != null){
                            mFollowingCount = Integer.parseInt(singleSnapshot.child("numbers").getValue(String.class));
                         if (mFollowersCount != 0){
                            mFollowingCount++;
                        }
                         if (mFollowersCount != 0){
                              if (mFollowing != null){
                        mFollowing.setText(String.valueOf(mFollowingCount));

                                 }
                }}}}}}
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }}}
    private void getPostsCount() {
        mPostsCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").child("Drivers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "onDataChange: found posts:" + singleSnapshot.child("name").getValue());
                    mPostsCount = Integer.parseInt(dataSnapshot.child("posts").getValue().toString());
                    if (mPostsCount != 0) {
                        mPostsCount++;
                    }
                    if (mPosts != null) {
                        mPosts.setText(String.valueOf(mPostsCount));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setProfileWidgets(UserSettings userSettings) {
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());


        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();
       if (mProfilePhoto != null){
        UniversalImageLoader.setImage(settings.getimage(), mProfilePhoto, null, "");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
       String userid = "";
                    userid = user.getDisplayName();
       if (mDisplayName != null){
            mDisplayName.setText(userid );
              if (mUsername != null){
            mUsername.setText(settings.getname());
        if (mWebsite != null){
            mWebsite.setText(settings.getWebsite());
         if (mDescription != null){
            mDescription.setText(settings.getdesc());
          if (mProgressBar != null){
            mProgressBar.setVisibility(View.GONE);

        }

    }}}}}}}
    /**
     * Responsible for setting up the profile toolbar
     */
    private void setupToolbar(){

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
            if (mContext != null){
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);

                startActivity(intent);
                if (getActivity() != null){
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }}}
        });
    }

    /**
     * BottomNavigationView setup
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        if (bottomNavigationView != null) {
            BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
            if (mContext != null ){
                    if (getActivity() != null){
            BottomNavigationViewHelper.enableNavigation(mContext, getActivity(), bottomNavigationView);
               if (bottomNavigationView != null){
            Menu menu = bottomNavigationView.getMenu();
          if (menu != null){
              if (ACTIVITY_NUM != 0){
            MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
          if (menuItem != null){

            menuItem.setChecked(true);
        }
    }}}}}}}
      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        if (FirebaseAuth.getInstance() != null) {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            myRef = mFirebaseDatabase.getReference();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();


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

        if (myRef != null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //retrieve user information from the database
      if (mFirebaseMethods != null) {
          if (dataSnapshot != null) {
              setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));
          }
          //retrieve images for the user in question
      }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
    @Override
    public void onStart() {
        super.onStart();

        if (mAuth != null) {
            if (mAuthListener != null) {
                mAuth.addAuthStateListener(mAuthListener);
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}


// #BuiltByGOD