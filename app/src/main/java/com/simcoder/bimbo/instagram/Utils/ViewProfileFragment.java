package com.simcoder.bimbo.instagram.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.simcoder.bimbo.instagram.Models.Comment;
import com.simcoder.bimbo.instagram.Models.Like;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;
import com.simcoder.bimbo.instagram.Models.UserSettings;
import com.simcoder.bimbo.instagram.Profile.AccountSettingsActivity;
import com.simcoder.bimbo.instagram.Profile.ProfileActivity;
import com.simcoder.bimbo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileFragment extends Fragment {

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


    //widgets
    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription,
    mFollow, mUnfollow;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu, mBackArrow;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;
    private TextView editProfile;

    //vars
    private User mUser;
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;
    String getcommentkey;
    String theuserkey;
    String thecommentkey;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
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
        //toolbar = (Toolbar) view.findViewById(R.id.profileToolbar);
        //profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationViewEx) view.findViewById(R.id.bottomNavViewBar);
        mFollow = (TextView) view.findViewById(R.id.follow);
        mUnfollow = (TextView) view.findViewById(R.id.unfollow);
        editProfile = (TextView) view.findViewById(R.id.textEditProfile);
        mBackArrow = (ImageView) view.findViewById(R.id.backArrow);

        mContext = getActivity();
        Log.d(TAG, "onCreateView: stared.");

        try {
            if (mUser != null) {
                mUser = getUserFromBundle();
                init();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage());
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }

        setupBottomNavigationView();
        //setupToolbar();
        setupFirebaseAuth();

        isFollowing();
        getFollowingCount();
        getFollowersCount();
        getPostsCount();

        if (mFollow != null) {
            mFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUser != null) {
                        Log.d(TAG, "onClick: now following: " + mUser.getname());

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Customers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("following")
                                .setValue(mUser.getuid());

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Drivers")
                                .child("followers")
                                .child(mUser.getuid())
                                .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        setFollowing();

                    }
                }
            });
        }

        if (mUnfollow != null) {
            mUnfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUser != null) {
                        Log.d(TAG, "onClick: now unfollowing: " + mUser.getname());

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Customers")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("following").child(mUser.getuid())
                                .removeValue();

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Drivers")
                                .child("followers")
                                .child(mUser.getuid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .removeValue();
                    }

                    setUnfollowing();
                }
            });
        }
        //setupGridView();
        if (editProfile != null) {
            editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));

                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }                           });
            }
            return view;
        }

    private void init() {
        //set the profile widgets
        if (mUser != null) {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
            Query query1 = reference1.child("Users").child("Customers")
                    .orderByChild("uid").equalTo(mUser.getuid());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: found user:" + singleSnapshot.getValue(UserAccountSettings.class).toString());

                        UserSettings settings = new UserSettings();
                        settings.setUser(mUser);
                        settings.setSettings(singleSnapshot.getValue(UserAccountSettings.class));
                        setProfileWidgets(settings);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        //get the users profile photos
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        if (mUser != null) {
            Query query2 = reference2
                    .child("Photos")
                    .child(mUser.getphotoid());

            query2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ArrayList<Photo> photos = new ArrayList<>();

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        photo.setCaption(objectMap.get("caption").toString());
                        photo.setTags(objectMap.get("tags").toString());
                        photo.setphotoid(objectMap.get("photoid").toString());
                        photo.setuid(objectMap.get("uid").toString());
                        photo.setdate(objectMap.get("date").toString());
                        photo.setimage(objectMap.get("image").toString());

                        ArrayList<Comment> comments = new ArrayList<Comment>();
                        for (DataSnapshot dSnapshot : singleSnapshot
                                .child("Comments").getChildren()) {

                            getcommentkey = dSnapshot.getKey();
                            theuserkey = dSnapshot.child(getcommentkey).child("Users").getKey();
                            thecommentkey = dSnapshot.child(getcommentkey).child("Users").child(theuserkey).child("comments").getKey();
                            Comment comment = new Comment();
                            comment.setuid(dSnapshot.child(getcommentkey).child("Users").child(theuserkey).child("uid").getValue(Comment.class).getuid());
                            comment.setComment(dSnapshot.child(getcommentkey).child("Users").child(theuserkey).child("comments").child(thecommentkey).child("comment").getValue(Comment.class).getComment());
                            comment.setdate(dSnapshot.child(getcommentkey).child("Users").child(theuserkey).child("comments").child(thecommentkey).child("date").getValue(Comment.class).getdate());
                            comments.add(comment);
                        }
                        photo.setComments(comments);

                        List<Like> likesList = new ArrayList<Like>();
                        for (DataSnapshot dSnapshot : singleSnapshot
                                .child("Likes").getChildren()) {
                            Like like = new Like();
                            like.setuid(dSnapshot.child(getcommentkey).child("Users").child(theuserkey).child("uid").getValue(Like.class).getuid());
                            likesList.add(like);
                        }
                        photo.setLikes(likesList);
                        photos.add(photo);

                    }
                    setupImageGrid(photos);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled");

                }
            });
        }
    }
    private User getUserFromBundle() {
        Log.d(TAG, "getUserFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.intent_user));
        } else {
            return null;
        }
    }

    private void isFollowing() {
        Log.d(TAG, "isFollowing: checking if following this users.");
        setUnfollowing();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").child("Customers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following")
                .orderByChild("uid").equalTo(mUser.getuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                 String followingkey =     singleSnapshot.getKey();
                    Log.d(TAG, "onDataChange: found user:" + singleSnapshot.child(followingkey).child("name").getValue());
                    setFollowing();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getFollowersCount() {
        mFollowersCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").child("Customers")
                .child(mUser.getuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found follower:" + singleSnapshot.child("name").getValue());
                 mFollowingCount = Integer.parseInt(singleSnapshot.child("followers").child("number").getValue(Photo.class).getnumber());
                    mFollowersCount++;
                }
                mFollowers.setText(String.valueOf(mFollowersCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getFollowingCount() {
        mFollowingCount = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("Users").child("Customers")
                .child(mUser.getuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found following user:" + singleSnapshot.getValue());
                    mFollowingCount = Integer.parseInt(singleSnapshot.child("following").child("number").getValue(Photo.class).getnumber());

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
        Query query = reference.child("Users").child("Customers").
                orderByChild("uid").equalTo(mUser.getuid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found posts:" + singleSnapshot.child(mUser.getuid()).child("posts").getValue());
                    mPostsCount++;
                }
                mPosts.setText(String.valueOf(mPostsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setFollowing() {
        Log.d(TAG, "setFollowing: updating UI  for following this user");
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.VISIBLE);
        editProfile.setVisibility(View.GONE);
    }

    private void setUnfollowing() {
        Log.d(TAG, "setFollowing: updating UI for unfollowing this user");
        mFollow.setVisibility(View.VISIBLE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.GONE);
    }

    private void setCurrentUsersProfile() {
        Log.d(TAG, "setFollowing: updating UI for showing their own profile");
        mFollow.setVisibility(View.GONE);
        mUnfollow.setVisibility(View.GONE);
        editProfile.setVisibility(View.VISIBLE);
    }

    private void setupImageGrid(final ArrayList<Photo> photos) {
        //setup our image grid
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        ArrayList<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            imgUrls.add(photos.get(i).getimage());
        }
        GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,
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
    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    private void setProfileWidgets(UserSettings userSettings) {
        //Log.d(TAG, "SetProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        //Log.d(TAG, "SetProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());

        //User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getimage(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getimage());
        mUsername.setText(settings.getname());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getdesc());
        mPosts.setText(String.valueOf(settings.getPosts()));
        mFollowers.setText(String.valueOf(settings.getFollowers()));
        mFollowing.setText(String.valueOf(settings.getFollowing()));
        mProgressBar.setVisibility(View.GONE);

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            }
        });
    }

//    /**
//     * Responsible for setting up the profile toolbar
//     */
//    private void setupToolbar(){
//
//        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);
//
//        profileMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating to account settings.");
//                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
//                startActivity(intent);
//
//            }
//        });
//    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, getActivity(), bottomNavigationView);
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