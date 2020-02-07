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
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    private void setupGridView() {
        Log.d(TAG, "setupGridView: Setting up image grid.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
                String userid = "";
                 userid = user.getUid();
            final ArrayList<Photo> photos = new ArrayList<>();
            //THIS SHOWS ALL THE POSTS THAT YOU MADE
            // TO BE ABLE TO DO THAT YOU SHOULD BE A TRADER
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("Photos").orderByChild("tid").equalTo(userid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  try{  Photo photo = new Photo();

                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        thePhotosKeykey = singleSnapshot.getKey();

                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());

                        //     photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());

                        photo.setphotoid(objectMap.get("photoid").toString());

                        //     photo.setuid(objectMap.get("uid").toString());

                        photo.setdate(objectMap.get("date").toString());
                        photo.setimage(objectMap.get("image").toString());


                        ArrayList<Like> likes = new ArrayList<Like>();
                        for (DataSnapshot dSnapshot : singleSnapshot
                                .child(thePhotosKeykey).child("Likes").getChildren()) {
                            thePhotosKeykey = singleSnapshot.getKey();
                              String likeid = dSnapshot.getKey();
                                String userkey = dSnapshot.child(likeid).child("Users").getKey();
                            Like like = new Like();
                            like.setLikeid(dSnapshot.child("likeid").getValue(Like.class).getLikeid());
                            like.setnumber(dSnapshot.child("number").getValue(Like.class).getnumber());

                            like.setname(dSnapshot.child(likeid).child("Users").child(userkey).child("name").getValue(Like.class).getname());
                            like.setuid(dSnapshot.child(likeid).child("Users").child(userkey).child("uid").getValue(Like.class).getuid());
                            likes.add(like);
                        }

                        ArrayList<Comment> comments = new ArrayList<Comment>();

                        for (DataSnapshot dSnapshot : dataSnapshot
                                .child(thePhotosKeykey).child("Comments").getChildren()) {
                            thePhotosKeykey = singleSnapshot.getKey();

                                  String commentkey  = dSnapshot.getKey();
                                  String thecommenteruserkey = dSnapshot.child("Users").getKey();
                            Comment comment = new Comment();
                            comment.setComment(dSnapshot.child(commentkey).child("comment").getValue(Comment.class).getComment());

                            comment.setcommentkey(dSnapshot.child("commentkey").getValue(Comment.class).getcommentkey());

                            comment.setname(dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("name").getValue(Comment.class).getname());
                            comment.setuid(dSnapshot.child(commentkey).child("Users").child(thecommenteruserkey).child("uid").getValue(Comment.class).getuid());
                            comments.add(comment);
                        }

                        ArrayList<Tags> tagshere = new ArrayList<Tags>();

                        for (DataSnapshot dSnapshot : dataSnapshot
                                .child(thePhotosKeykey).child("tags").getChildren()) {

                            thePhotosKeykey = singleSnapshot.getKey();

                            Tags tags1 = new Tags();

                                //tagvalue as setvalue
                            tags1.setimage(dSnapshot.child("image").getValue(Tags.class).getimage());

                            tags1.setname(dSnapshot.child("name").getValue(Tags.class).getname());
                            tags1.setuid(dSnapshot.child("uid").getValue(Tags.class).getuid());
                            tagshere.add(tags1);
                        }


                        photo.setComments(comments);
                        photo.setLikes(likes);
                        photo.setTag(tagshere);

                        mPhotos.add(photo);

                    }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage());
                        }

                    //setup our image grid
                    int gridWidth = getResources().getDisplayMetrics().widthPixels;
                    int imageWidth = gridWidth / NUM_GRID_COLUMNS;
                    gridView.setColumnWidth(imageWidth);

                    ArrayList<String> imgUrls = new ArrayList<String>();
                    for (int i = 0; i < photos.size(); i++) {
                        imgUrls.add(photos.get(i).getimage());
                    }
                    GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview,
                            "", imgUrls);
                    gridView.setAdapter(adapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });
        }
    }
    private void getFollowersCount() {
        mFollowersCount = 0;

        // Should be roles

            mRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("Users").child("Drivers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers");

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                                    thefollowerkey = singleSnapshot.getKey();
                                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.child(thefollowerkey).child("name").getValue());

                                    mFollowersCount = Integer.parseInt(singleSnapshot.child("number").getValue().toString());
                                    mFollowersCount++;

                                }
                                //status has to change

                                mFollowers.setText(String.valueOf(mFollowersCount));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }

                    else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("Users").child("Customers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers");

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                                    thefollowerkey = singleSnapshot.getKey();
                                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.child(thefollowerkey).child("name").getValue());

                                    mFollowersCount = Integer.parseInt(singleSnapshot.child("number").getValue().toString());
                                    mFollowersCount++;

                                }
                                //status has to change

                                mFollowers.setText(String.valueOf(mFollowersCount));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



    }
//TRADER or DRIVER
    //CREATING THE DATA AND TRADER PORTION

    private void getFollowingCount() {
        mFollowingCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Users").child("Drivers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.child("name").getValue());
                      mFollowingCount = Integer.parseInt(singleSnapshot.child("numbers").getValue().toString());
                    mFollowingCount++;
                }
                mFollowing.setText(String.valueOf(mFollowingCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getPostsCount() {
        mPostsCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").child("Drivers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                    Log.d(TAG, "onDataChange: found posts:" +  singleSnapshot.child("name").getValue());
                  mPostsCount = Integer.parseInt(dataSnapshot.child("posts").getValue().toString());
                    mPostsCount++;
                }
                mPosts.setText(String.valueOf(mPostsCount));
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

        UniversalImageLoader.setImage(settings.getimage(), mProfilePhoto, null, "");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
       String userid = "";
                    userid = user.getDisplayName();
            mDisplayName.setText(userid );
            mUsername.setText(settings.getname());
            mWebsite.setText(settings.getWebsite());
            mDescription.setText(settings.getdesc());
            mProgressBar.setVisibility(View.GONE);

        }

    }
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
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    /**
     * BottomNavigationView setup
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext,getActivity() ,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

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


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}