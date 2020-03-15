package com.simcoder.bimbo.instagram.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Models.Like;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.Tags;
import com.simcoder.bimbo.instagram.Utils.MainfeedListAdapter;
import com.simcoder.bimbo.instagram.Models.Comment;
import com.simcoder.bimbo.instagram.Models.Photo;

import com.simcoder.bimbo.instagram.Utils.MainfeedListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    //vars
    private ArrayList<Photo> mPhotos;
    private ArrayList<Photo> mPaginatedPhotos;
    private ArrayList<String> mFollowing;
    //LIST VIEW AS THE SUBJECT IS NOT REALLY NECESSARY

    private ListView mListView;
    private MainfeedListAdapter mAdapter;
    private int mResults;
    String followingkey;
    String thePhotosKeykey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);


        mFollowing = new ArrayList<>();
        mPhotos = new ArrayList<>();
        //   mPhotos = (ArrayList<Photo>) new ArrayList<>();
        //   mPhotos = (ArrayList<Photo>) new ArrayList<>();

        getFollowing();

        return view;
    }

    private void getFollowing() {
        Log.d(TAG, "getFollowing: searching for following");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userid = "";
            userid = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference
                    .child("Users").child("Customers").child(userid)
                    .child("following");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if (followingkey != null) {
                            if (singleSnapshot != null) {
                                followingkey = singleSnapshot.getKey();

                                if (singleSnapshot.child(followingkey).child("name") != null) {
                                    Log.d(TAG, "onDataChange: found user: " +

                                            singleSnapshot.child(followingkey).child("name").getValue());
                                    if (singleSnapshot.child(followingkey).getValue() != null) {
                                        mFollowing.add(singleSnapshot.child(followingkey).getValue().toString());
                                    }
                                }
                            }
                        }
                    }
                    if (followingkey != null) {
                        mFollowing.add(followingkey);
                    }
                    //get the photos
                    getPhotos();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getPhotos() {
        Log.d(TAG, "getPhotos: getting photos");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference thePhotos = FirebaseDatabase.getInstance().getReference().child("Photos");

        for (int i = 0; i < mFollowing.size(); i++) {
            final int count = i;
            if (thePhotosKeykey != null) {
                if (thePhotos != null) {
                    thePhotosKeykey = thePhotos.getKey();
                }
                Query query = reference
                        .child("Photos")
                        .child(thePhotosKeykey)
                        .orderByChild("tid")
                        .equalTo(mFollowing.get(i));

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Photo photo = new Photo();
                        if (dataSnapshot != null) {
                            thePhotosKeykey = dataSnapshot.getKey();

                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                if (singleSnapshot != null) {
                                    thePhotosKeykey = singleSnapshot.getKey();


                                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                                    if (photo != null) {
                                        if (objectMap.get("caption") != null) {
                                            photo.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                                        }
                                        //     photo.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                                        if (objectMap.get("photoid") != null) {
                                            photo.setphotoid(objectMap.get("photoid").toString());
                                        }
                                        //     photo.setuid(objectMap.get("uid").toString());
                                        if (objectMap.get("date") != null) {
                                            photo.setdate(objectMap.get("date").toString());
                                        }
                                        if (objectMap.get("name") != null) {
                                            photo.setname(objectMap.get("name").toString());
                                        }

                                        if (objectMap.get("image") != null) {
                                            photo.setimage(objectMap.get("image").toString());
                                        }
                                    }
                                    ArrayList<Like> likes = new ArrayList<Like>();
                                    if (thePhotosKeykey != null) {
                                        for (DataSnapshot dSnapshot : singleSnapshot
                                                .child(thePhotosKeykey).child("Likes").getChildren()) {
                                            if (singleSnapshot != null) {
                                                thePhotosKeykey = singleSnapshot.getKey();
                                            }
                                            Like like = new Like();
                                            if (like != null) {
                                                if (dataSnapshot != null) {


                                                    if (dSnapshot.getValue(Like.class) != null) {
                                                        like.setLikeid(dSnapshot.getValue(Like.class).getLikeid());
                                                    }
                                                    if (dSnapshot.getValue(Like.class) != null) {
                                                        like.setnumber(dSnapshot.getValue(Like.class).getnumber());
                                                    }
                                                    if (dSnapshot.child("Users").child("name").getValue(Like.class) != null) {
                                                        like.setname(dSnapshot.child("Users").child("name").getValue(Like.class).getname());
                                                    }
                                                    if (dSnapshot.child("Users").child("uid").getValue(Like.class) != null) {
                                                        like.setuid(dSnapshot.child("Users").child("uid").getValue(Like.class).getuid());
                                                    }
                                                    if (like != null) {
                                                        likes.add(like);
                                                    }
                                                }
                                            }
                                        }
                                        ArrayList<Comment> comments = new ArrayList<Comment>();
                                        if (comments != null) {
                                            if (thePhotosKeykey != null) {
                                                for (DataSnapshot dSnapshot : dataSnapshot
                                                        .child(thePhotosKeykey).child("Comments").getChildren()) {
                                                    if (dataSnapshot
                                                            .child(thePhotosKeykey).child("Comments") != null) {
                                                        if (singleSnapshot != null) {
                                                            thePhotosKeykey = singleSnapshot.getKey();
                                                        }

                                                        Comment comment = new Comment();
                                                        if (dSnapshot.child("comment").getValue(Comment.class) != null) {
                                                            comment.setComment(dSnapshot.child("comment").getValue(Comment.class).getComment());
                                                        }
                                                        if (dSnapshot.child("commentkey").getValue(Comment.class) != null) {
                                                            comment.setcommentkey(dSnapshot.child("commentkey").getValue(Comment.class).getcommentkey());
                                                        }
                                                        if (dSnapshot.child("name").getValue(Comment.class) != null) {
                                                            comment.setname(dSnapshot.child("name").getValue(Comment.class).getname());
                                                        }
                                                        if (dSnapshot.child("uid").getValue(Comment.class) != null) {
                                                            comment.setuid(dSnapshot.child("uid").getValue(Comment.class).getuid());
                                                        }
                                                        if (comment != null) {
                                                            comments.add(comment);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        ArrayList<Tags> tagshere = new ArrayList<Tags>();
                                        if (thePhotosKeykey != null) {
                                            if (dataSnapshot
                                                    .child(thePhotosKeykey).child("tags") != null) {
                                                for (DataSnapshot dSnapshot : dataSnapshot
                                                        .child(thePhotosKeykey).child("tags").getChildren()) {
                                                    thePhotosKeykey = singleSnapshot.getKey();
                                                    Tags tags1 = new Tags();
                                                    if (tags1 != null) {
                                                        if (dSnapshot != null) {
                                                            if (dSnapshot.child("image").getValue(Tags.class) != null) {
                                                                tags1.setimage(dSnapshot.child("image").getValue(Tags.class).getimage());
                                                            }
                                                            if (dSnapshot.child("name").getValue(Tags.class) != null) {
                                                                tags1.setname(dSnapshot.child("name").getValue(Tags.class).getname());
                                                            }
                                                            if (dSnapshot.child("uid").getValue(Tags.class) != null) {
                                                                tags1.setuid(dSnapshot.child("uid").getValue(Tags.class).getuid());
                                                            }
                                                            if (tags1 != null) {
                                                                tagshere.add(tags1);
                                                            }
                                                        }

                                                        if (photo != null) {
                                                            if (comments != null) {
                                                                photo.setComments(comments);
                                                                if (likes != null) {
                                                                    photo.setLikes(likes);
                                                                }
                                                                if (tagshere != null) {
                                                                    photo.setTag(tagshere);
                                                                }
                                                                mPhotos.add(photo);
                                                            }
                                                            if (count >= mFollowing.size() - 1) {
                                                                //display our photos
                                                                displayPhotos();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }


    }


    private void displayPhotos() {
        mPaginatedPhotos = new ArrayList<>();

        if (mPhotos != null) {
            try {
                Collections.sort(mPhotos, new Comparator<Photo>() {
                    @Override
                    public int compare(Photo o1, Photo o2) {
                        return o2.getdate().compareTo(o1.getdate());
                    }
                });

                int iterations = mPhotos.size();

                if (iterations > 10) {
                    iterations = 10;
                }

                mResults = 10;
                for (int i = 0; i < iterations; i++) {
                    if (mPhotos != null) {
                        mPaginatedPhotos.add(mPhotos.get(i));
                    }
                }
                if (getActivity() != null) {
                    mAdapter = new MainfeedListAdapter(getActivity(), R.layout.layout_mainfeed_listitem, mPaginatedPhotos);

                }


                if (mAdapter != null) {
                    mListView.setAdapter(mAdapter);
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage());
            }
        }
    }

    public void displayMorePhotos() {
        Log.d(TAG, "displayMorePhotos: displaying more photos");

        try {
            if (mPhotos != null) {
                if (mResults != 0) {
                    if (mPhotos.size() > mResults && mPhotos.size() > 0) {

                        int iterations;
                        if (mResults != 0) {
                            if (mPhotos.size() > (mResults + 10)) {
                                Log.d(TAG, "displayMorePhotos: there are greater than 10 more photos");
                                iterations = 10;
                            } else {
                                Log.d(TAG, "displayMorePhotos: there is less than 10 more photos");
                                iterations = mPhotos.size() - mResults;
                            }

                            //add the new photos to the paginated results
                            for (int i = mResults; i < mResults + iterations; i++) {
                                mPaginatedPhotos.add(mPhotos.get(i));
                            }

                            mResults = mResults + iterations;
          if (mAdapter != null) {
              mAdapter.notifyDataSetChanged();
          }
          }
                    }
                }
            }
        }
            catch(NullPointerException e){
                Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage());
            }catch(IndexOutOfBoundsException e){
                Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage());
            }
        }

    }













