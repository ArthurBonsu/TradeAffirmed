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
             String userid= "";
             userid = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child("Users").child("Customers").child(userid)
                    .child("following");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        followingkey = singleSnapshot.getKey();
                        Log.d(TAG, "onDataChange: found user: " +

                                singleSnapshot.child(followingkey).child("name").getValue());

                        mFollowing.add(singleSnapshot.child(followingkey).getValue().toString());
                    }
                    mFollowing.add(followingkey);
                    //get the photos
                    getPhotos();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void getPhotos(){
        Log.d(TAG, "getPhotos: getting photos");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference thePhotos = FirebaseDatabase.getInstance().getReference().child("Photos");

        for(int i = 0; i < mFollowing.size(); i++){
            final int count = i;
            thePhotosKeykey = thePhotos.getKey();
            Query query = reference
                    .child("Photos")
                    .child(thePhotosKeykey)
                    .orderByChild("tid")
                    .equalTo(mFollowing.get(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Photo photo = new Photo();
                    thePhotosKeykey  =    dataSnapshot.getKey();
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

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
                                .child(thePhotosKeykey).child("Likes").getChildren()){
                            thePhotosKeykey = singleSnapshot.getKey();

                            Like like = new Like();
                            like.setLikeid(dSnapshot.getValue(Like.class).getLikeid());
                            like.setnumber(dSnapshot.getValue(Like.class).getnumber());
                            like.setname(dSnapshot.child("Users").child("name").getValue(Like.class).getname());
                            like.setuid(dSnapshot.child("Users").child("uid").getValue(Like.class).getuid());
                            likes.add(like);
                        }

                        ArrayList<Comment> comments = new ArrayList<Comment>();

                        for (DataSnapshot dSnapshot : dataSnapshot
                                .child(thePhotosKeykey).child("Comments").getChildren()) {
                            thePhotosKeykey = singleSnapshot.getKey();
                            Comment comment = new Comment();
                            comment.setComment(dSnapshot.child("comment").getValue(Comment.class).getComment());

                            comment.setcommentkey(dSnapshot.child("commentkey").getValue(Comment.class).getcommentkey());

                            comment.setname(dSnapshot.child("name").getValue(Comment.class).getname());
                            comment.setuid(dSnapshot.child("uid").getValue(Comment.class).getuid());
                            comments.add(comment);
                        }

                        ArrayList<Tags> tagshere = new ArrayList<Tags>();

                        for (DataSnapshot dSnapshot : dataSnapshot
                                .child(thePhotosKeykey).child("tags").getChildren()) {
                            thePhotosKeykey = singleSnapshot.getKey();
                            Tags tags1 = new Tags();


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
                    if(count >= mFollowing.size() -1){
                        //display our photos
                        displayPhotos();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }





    private void displayPhotos(){
        mPaginatedPhotos = new ArrayList<>();
        if(mPhotos != null){
            try{
                Collections.sort(mPhotos, new Comparator<Photo>() {
                    @Override
                    public int compare(Photo o1, Photo o2) {
                        return o2.getdate().compareTo(o1.getdate());
                    }
                });

                int iterations = mPhotos.size();

                if(iterations > 10){
                    iterations = 10;
                }

                mResults = 10;
                for(int i = 0; i < iterations; i++){
                    mPaginatedPhotos.add(mPhotos.get(i));
                }

                mAdapter = new MainfeedListAdapter(getActivity(), R.layout.layout_mainfeed_listitem, mPaginatedPhotos);
                mListView.setAdapter(mAdapter);

            }catch (NullPointerException e){
                Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage() );
            }catch (IndexOutOfBoundsException e){
                Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage() );
            }
        }
    }

    public void displayMorePhotos(){
        Log.d(TAG, "displayMorePhotos: displaying more photos");

        try{

            if(mPhotos.size() > mResults && mPhotos.size() > 0){

                int iterations;
                if(mPhotos.size() > (mResults + 10)){
                    Log.d(TAG, "displayMorePhotos: there are greater than 10 more photos");
                    iterations = 10;
                }else{
                    Log.d(TAG, "displayMorePhotos: there is less than 10 more photos");
                    iterations = mPhotos.size() - mResults;
                }

                //add the new photos to the paginated results
                for(int i = mResults; i < mResults + iterations; i++){
                    mPaginatedPhotos.add(mPhotos.get(i));
                }
                mResults = mResults + iterations;
                mAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            Log.e(TAG, "displayPhotos: NullPointerException: " + e.getMessage() );
        }catch (IndexOutOfBoundsException e){
            Log.e(TAG, "displayPhotos: IndexOutOfBoundsException: " + e.getMessage() );
        }
    }

}













