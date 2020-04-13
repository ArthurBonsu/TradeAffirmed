package com.simcoder.bimbo.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import com.simcoder.bimbo.Interface.ItemClickListner;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.WorkActivities.CartActivity;
import com.simcoder.bimbo.instagram.Models.Comment;
import com.simcoder.bimbo.instagram.Models.Like;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.Tags;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;
import com.simcoder.bimbo.instagram.Utils.Heart;
import com.simcoder.bimbo.instagram.Utils.MainfeedListAdapter;
import com.simcoder.bimbo.instagram.Utils.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public  class  AdminAllCustomers extends Fragment {
    private View MessageFeedView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private Button cartthenextactivityhere;
    private TextView txtTotalAmount, txtMsg1;
    CircleImageView theprofileimage;
    private int overTotalPrice = 0;
    String productID = "";
    String userID = "";
    DatabaseReference UserRef;
    ViewPager myviewpager;
    String cartkey = "";
    String orderkey = "";
    String role;
    DatabaseReference UserDetailsRef;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    SquareImageView thefeedimage;
    String caption, date, image, time, uid, name, photoid, tid, pid, price, tagnumber, likenumber, commentnumber;
    //AUTHENTICATORS
    String posttype, traderimage, tradername;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Main Feed Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView cartimageonscreen;
    private ImageView cartproductimageonscreeen;
    private ImageView numberoflikesimage;
    String cartlistkey;
    DatabaseReference CartListRef;
    DatabaseReference PhotoReferences;


    String traderoruser;
    String nameofproduct;
    String productid;
    String quantity;

    String useridentifier;
    String customerid;
    String somerole;
    String key;


    //AUTHENTICATORS


    String traderkey;
    String thetraderhere;

    String thetraderimage;
    android.widget.ImageView profilephoto;
    android.widget.ImageView mainphoto;
    android.widget.ImageView thepicturebeingloaded;
    android.widget.ImageView thetraderpicturebeingloaded;

    DatabaseReference UsersRef;


    FirebaseRecyclerAdapter<Photo, MainFeedViewHolder> feedadapter;
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

    //THE ELEMENTS TO PICK UP FROM THE DATABASE ARENA

    CircleImageView theprofilepicture;
    SquareImageView thephotoimage;
    ImageView theimageheart;
    ImageView thebubbleimage;
    FirebaseDatabase RetrievingDatabase;
    DatabaseReference myretrievalref;
    DatabaseReference mylikesdatabasereference;
    DatabaseReference mycommentFirebaseDatabase;
    String photokey;
    FirebaseDatabase LikesFirebaseDatabase;
    FirebaseDatabase CommentFirebaseDatabase;
    String likeskey;
    String likername, likeimage, likernumber, likeruid, likerlikeid;
    String    pname; String    pimage;

    Query myLikeDatabaseQuery;


    public interface GetmyPhotosCallBack {
        void onCallback(String caption, String date, String image, String name, String photoid, String pid, String posttype, String price, String tid, String traderimage, String tradername);
    }


    public interface GetmyLikersCallBack {
        void onCallback(String image, String name, String uid, String likeid, String number);
    }


    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    public AdminAllCustomers() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MessageFeedView = inflater.inflate(R.layout.stickynoterecycler, container, false);

        if (MessageFeedView != null) {
            recyclerView = MessageFeedView.findViewById(R.id.stickyheaderrecyler);

            //    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            if (recyclerView != null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            if (recyclerView != null) {
                recyclerView.setHasFixedSize(true);

            }

            fetch();

            theprofilepicture = (CircleImageView) MessageFeedView.findViewById(R.id.profile_photo);
            thephotoimage = (SquareImageView) MessageFeedView.findViewById(R.id.post_image);
            theimageheart = (ImageView) MessageFeedView.findViewById(R.id.image_heart_red);
            thebubbleimage = (ImageView) MessageFeedView.findViewById(R.id.speech_bubble);
            //     recyclerView.setAdapter(adapter);

            //    if (recyclerView != null) {
            //       recyclerView.setAdapter(adapter);
            //   }

        }
        if (FirebaseDatabase.getInstance().getReference() != null) {
            UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
            UsersRef.keepSynced(true);
            CartListRef = FirebaseDatabase.getInstance().getReference().child("Cart");
            CartListRef.keepSynced(true);
            cartlistkey = CartListRef.getKey();

            if (FirebaseDatabase.getInstance().getReference() != null) {
                PhotoReferences = FirebaseDatabase.getInstance().getReference().child("Photos");

                Paper.init(getContext());


                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                if (mGoogleApiClient != null) {

                    mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                }

                if (mGoogleApiClient != null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(getContext()).enableAutoManage(getActivity(),
                            new GoogleApiClient.OnConnectionFailedListener() {
                                @Override
                                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                }
                            }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
                }

            }

            mAuth = FirebaseAuth.getInstance();
            firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (FirebaseAuth.getInstance() != null) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            customerid = "";
                            traderoruser = user.getUid();
                        }

                        // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                        // WHICH IS CUSTOMER TO BE ADDED.
                        // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                    }
                }
            };


            if (MessageFeedView != null) {
                FloatingActionButton fab = (FloatingActionButton) MessageFeedView.findViewById(R.id.fab);
                if (fab != null) {
                    fab.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!role.equals("Trader")) {
                                        Intent intent = new Intent(getContext(), CartActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }

            final GetmyPhotosCallBack Gettingmyphotoscallback = new GetmyPhotosCallBack() {
                @Override
                public void onCallback(String caption, String date, String image, String name, String photoid, String pid, String posttype, String price, String tid, String traderimage, String tradername) {

                }
            };

            RetrievingDatabase = FirebaseDatabase.getInstance();
            CommentFirebaseDatabase = FirebaseDatabase.getInstance();


            myretrievalref = RetrievingDatabase.getReference("Photos");
            photokey = myretrievalref.getKey();

// Attach a listener to read the data at our posts reference
            myretrievalref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    photokey = dataSnapshot.getKey();

                    //  System.out.println(post);
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Log.d(TAG, "The Photokey " + photokey);

                        if (dataSnapshot1.child("caption").getValue(String.class) != null) {
                            caption = dataSnapshot1.child("caption").getValue(String.class);
                        }
                        if (dataSnapshot1.child("date").getValue(String.class) != null) {
                            date = dataSnapshot1.child("date").getValue(String.class);

                        }
                        if (dataSnapshot1.child("image").getValue(String.class) != null) {
                            image = dataSnapshot1.child("image").getValue(String.class);

                        }
                        if (dataSnapshot1.child("name").getValue(String.class) != null) {
                            name = dataSnapshot1.child("name").getValue(String.class);

                            if (dataSnapshot1.child("photoid").getValue(String.class) != null) {
                                photoid = dataSnapshot1.child("photoid").getValue(String.class);

                            }
                            if (dataSnapshot1.child("pid").getValue(String.class) != null) {
                                pid = dataSnapshot1.child("pid").getValue(String.class);

                            }
                            if (dataSnapshot1.child("posttype").getValue(String.class) != null) {
                                posttype = dataSnapshot1.child("posttype").getValue(String.class);

                            }
                            if (dataSnapshot1.child("price").getValue(String.class) != null) {
                                price = dataSnapshot1.child("price").getValue(String.class);

                            }
                            if (dataSnapshot1.child("tid").getValue(String.class) != null) {
                                tid = dataSnapshot1.child("tid").getValue(String.class);

                            }
                            if (dataSnapshot1.child("traderimage").getValue(String.class) != null) {
                                traderimage = dataSnapshot1.child("traderimage").getValue(String.class);

                            }
                            if (dataSnapshot1.child("tradername").getValue(String.class) != null) {
                                tradername = dataSnapshot1.child("tradername").getValue(String.class);


                            }

                            Gettingmyphotoscallback.onCallback(caption, date, image, name, photoid, pid, posttype, price, tid, traderimage, tradername);
                            {
                                Log.d(TAG, "After this Call Back " + caption + " " + date + " " + image + " " + name + " " + photoid + " " + pid + " " + posttype + price + tid + traderimage + tradername);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });


            Gettingmyphotoscallback.onCallback(caption, date, image, name, photoid, pid, posttype, price, tid, traderimage, tradername);

            Log.d(TAG, "After Second  Call Back " + caption + " " + date + " " + image + " " + name + " " + photoid + " " + pid + " " + posttype + price + tid + traderimage + tradername);


            final GetmyLikersCallBack getmyLikersCallBack = new GetmyLikersCallBack() {
                @Override
                public void onCallback(String image, String name, String uid, String likeid, String number) {

                }
            };


            LikesFirebaseDatabase = FirebaseDatabase.getInstance();






            //
            if (FirebaseAuth.getInstance() != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
                UserRef.keepSynced(true);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    UserDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    UserDetailsRef.keepSynced(true);

                    UserDetailsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {



                            if (dataSnapshot.exists()) {

                                if (dataSnapshot.child("uid").getValue(String.class) != null) {
                                    useridentifier = dataSnapshot.child("uid").getValue(String.class);
                                    if (dataSnapshot.child("role").getValue(String.class) != null) {
                                        role = dataSnapshot.child("role").getValue(String.class);
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


        if (FirebaseAuth.getInstance() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            PhotoReferences = FirebaseDatabase.getInstance().getReference().child("Photos");
            PhotoReferences.keepSynced(true);


            PhotoReferences.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return MessageFeedView;
    }










    public  class MainFeedViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        CircleImageView mprofileImage;
        String likesString;
        TextView username, timeDetla, caption, likes, comments;
        SquareImageView thefeedimage;
        CircleImageView theprofileimage;
        android.widget.ImageView heartRed, heartWhite, comment;

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();
        StringBuilder users;
        String mLikesString;
        boolean likeByCurrentUser;
        Heart heart;
        GestureDetector detector;
        Photo photo;


        public ItemClickListner listner;

        public MainFeedViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {

                username = (TextView) itemView.findViewById(R.id.username);
                theprofileimage =  (CircleImageView) itemView.findViewById(R.id.profile_photo);
                thefeedimage = (SquareImageView) itemView.findViewById(R.id.post_image);




                heartRed = (android.widget.ImageView) itemView.findViewById(R.id.image_heart_red);
                heartWhite = (android.widget.ImageView) itemView.findViewById(R.id.image_heart);
                comment = (android.widget.ImageView) itemView.findViewById(R.id.speech_bubble);



                likes = (TextView) itemView.findViewById(R.id.image_likes);
                comments = (TextView) itemView.findViewById(R.id.image_comments_link);
                caption = (TextView) itemView.findViewById(R.id.image_caption);
                timeDetla = (TextView) itemView.findViewById(R.id.image_time_posted);
                mprofileImage = (CircleImageView) itemView.findViewById(R.id.profile_photo);

            }
        }


        public void setItemClickListner(ItemClickListner listner) {
            this.listner = listner;
        }




        public void setmainviewusername(String mainviewusername) {
            if (username != null) {
                username.setText(mainviewusername);
            }
        }

        public void setTheLikes(String theLikes) {
            if (likes != null) {
                likes.setText(theLikes);
            }
        }

        public void setthecomment(String thecomments) {
            if (comments != null) {
                comments.setText(thecomments);
            }
        }


        public void setcaptionhere(String thecaptionhere) {
            if (caption != null) {
                caption.setText(thecaptionhere);
            }
        }


        public void setTimeDetla(String timeDetlaview) {
            if (timeDetla != null) {
                timeDetla.setText(timeDetlaview);
            }
        }

        public void setThefeedimage(final Context ctx, final String image) {
            thefeedimage = (SquareImageView) itemView.findViewById(R.id.post_image);
            if (image != null) {
                if (thefeedimage != null) {

                    //
                    Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(thefeedimage, new Callback() {


                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).resize(100, 0).into(thefeedimage);
                        }


                    });

                }
            }
        }


        public void setTheHeartRed(final Context ctx, final String image) {
            final android.widget.ImageView setTheHeartRed = (android.widget.ImageView) itemView.findViewById(R.id.image_heart_red);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(setTheHeartRed, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(setTheHeartRed);
                }


            });


        }

        public void setTheHeartWhite(final Context ctx, final String image) {
            final android.widget.ImageView setimage_heart = (android.widget.ImageView) itemView.findViewById(R.id.image_heart);
            if (setimage_heart != null) {
                Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(setimage_heart, new Callback() {


                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).resize(100, 0).into(setimage_heart);
                    }


                });


            }
        }

        public void setcommentbubble(final Context ctx, final String image) {
            final android.widget.ImageView setthecommentbubble = (android.widget.ImageView) itemView.findViewById(R.id.speech_bubble);
            if (setthecommentbubble != null) {
                Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(setthecommentbubble, new Callback() {


                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).resize(100, 0).into(setthecommentbubble);
                    }


                });


            }

        }

        //GETFOLLOWING WILL PULL FROM DIFFERENT DATASTORE( THE USER DATASTORE)

        public void setTheProfilePhoto(final Context ctx, final String image) {
            theprofileimage = (CircleImageView)itemView.findViewById(R.id.profile_photo);
            if (theprofileimage != null) {
                Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(theprofileimage, new Callback() {


                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).resize(100, 0).into(theprofileimage);
                    }


                });


            }
        }



    }

    private void fetch(){
        @Nullable

        Query queryhere =

                FirebaseDatabase.getInstance().getReference().child("Photos");
        if (queryhere != null) {

            FirebaseRecyclerOptions<Photo> options =
                    new FirebaseRecyclerOptions.Builder<Photo>()
                            .setQuery(queryhere, new SnapshotParser<Photo>() {



                                @Nullable
                                @Override
                                public Photo parseSnapshot(@Nullable DataSnapshot snapshot) {


                                      /*
                                      String commentkey = snapshot.child("Comments").getKey();
                                      String likekey = snapshot.child("Likes").getKey();


*/
                                    Log.i(TAG, "MainfeedSnapshots " + snapshot);

                                    if (snapshot.child("caption").getValue(String.class) != null) {
                                        caption =         snapshot.child("caption").getValue(String.class);
                                    }
                                    if (snapshot.child("date").getValue(String.class) != null) {
                                        date =     snapshot.child("date").getValue(String.class);
                                    }


                                    if (snapshot.child("time").getValue(String.class) != null) {
                                        time =        snapshot.child("time").getValue(String.class);
                                    }

                                    if (snapshot.child("tid").getValue(String.class) != null) {
                                        tid =     snapshot.child("tid").getValue(String.class);
                                    }
                                    if (snapshot.child("traderimage").getValue(String.class) != null) {
                                        thetraderimage =      snapshot.child("traderimage").getValue(String.class);
                                    }



                                    if (snapshot.child("tradername").getValue(String.class) != null) {
                                        tradername =     snapshot.child("tradername").getValue(String.class);
                                    }

                                    if (snapshot.child("photoid").getValue(String.class) != null) {
                                        photoid =   snapshot.child("photoid").getValue(String.class);
                                    }

                                    if (snapshot.child("pname").getValue(String.class) != null) {
                                        pname =    snapshot.child("pname").getValue(String.class);
                                    }

                                    if (snapshot.child("pimage").getValue(String.class) != null) {
                                        pimage =    snapshot.child("pimage").getValue(String.class);
                                    }

                                    if (snapshot.child("pid").getValue(String.class) != null) {
                                        pid = snapshot.child("pid").getValue(String.class);
                                    }

                                    if (snapshot.child("posttype").getValue(String.class) != null) {
                                        posttype =    snapshot.child("posttype").getValue(String.class);
                                    }


                                    if (snapshot.child("price").getValue(String.class) != null) {
                                        price = snapshot.child("price").getValue(String.class);
                                    }



                                    return new Photo(caption, date,time, tid, thetraderimage, tradername, photoid, pname, pimage, pid, posttype, price);


                                }

                            }).build();

            feedadapter = new FirebaseRecyclerAdapter<Photo, MainFeedViewHolder>(options) {
                @Nullable
                @Override
                public MainFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    @Nullable
                    View view = LayoutInflater.from(  parent.getContext())
                            .inflate(R.layout.layout_mainfeed_listitem, parent, false);

                    return new MainFeedViewHolder(view);
                }

                ;

                // recyclerview here must be set
                // holders must be set


                @Override
                public int getItemCount() {
                    return super.getItemCount();
                }

                @Override
                protected void onBindViewHolder(@Nullable final MainFeedViewHolder holder, int position, @Nullable Photo model) {
                    if (model != null) {

                        key = model.getphotoid();
                        traderkey = model.gettid();
                        //   model.setTrader(traderkey);


                        holder.username.setText(name);

                        mylikesdatabasereference = LikesFirebaseDatabase.getReference("Photos").child("Likes");
                        mylikesdatabasereference.keepSynced(true);
                        mylikesdatabasereference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {



                                    photokey = dataSnapshot1.getKey();
                                    Log.d(TAG, "The Photokey " + photokey);

                                    if (dataSnapshot1.child("number").getValue(String.class) != null) {
                                        likenumber = dataSnapshot1.child("number").getValue(String.class);
                                    }
                                    holder.likes.setText("Liked by " +   likenumber + "  " + "number of people");

                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        mycommentFirebaseDatabase = CommentFirebaseDatabase.getReference().child("Photos").child("Comments");
                        mycommentFirebaseDatabase.keepSynced(true);
                        mycommentFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    photokey = dataSnapshot1.getKey();
                                    Log.d(TAG, "The Photokey " + photokey);

                                    if (dataSnapshot1.child("number").getValue(String.class) != null) {
                                        commentnumber = dataSnapshot1.child("number").getValue(String.class);
                                    }
                                    holder.comments.setText("View all comment from" + commentnumber  +"people");

                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        holder.caption.setText(caption);
                        holder.timeDetla.setText(model.gettime());
                        holder.setThefeedimage(getContext(), model.getpimage());
                        holder.setTheProfilePhoto(getContext(),  thetraderimage);
                        holder.heartRed.setImageResource(R.drawable.ic_heart_red);

                        holder.heartWhite.setImageResource(R.drawable.ic_heart_white);






                        if (thefeedimage != null) {
                            Picasso.get().load(model.getimage()).placeholder(R.drawable.profile).into(thefeedimage);
                        }



                        if (theprofileimage != null) {
                            Picasso.get().load( thetraderimage).placeholder(R.drawable.profile).into(theprofileimage);
                        }


                        /*
                          if (thephotoimage != null) {
                              Picasso.get().load(model.getimage()).placeholder(R.drawable.profile).into(thephotoimage);
                          }
*/





                        if (holder != null) {
                            holder.heartRed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                }
                            });
                        }

                        if (holder != null) {
                            holder.thefeedimage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }


                            });



                        }
                    }


                };;


            };




            ;
            if (recyclerView != null){
                recyclerView.setAdapter(feedadapter);
            }

        }

    }
    @Nullable
    @Override
    public void onStart() {
        super.onStart();
        if (feedadapter != null) {
            feedadapter.startListening();
        }
        if (mAuth != null) {
            mAuth.addAuthStateListener(firebaseAuthListener);
        }



    }


    @Override
    public void onStop () {
        super.onStop();
        if (feedadapter != null) {
            feedadapter.stopListening();
        }
        //     mProgress.hide();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

}



// #BuiltByGOD



