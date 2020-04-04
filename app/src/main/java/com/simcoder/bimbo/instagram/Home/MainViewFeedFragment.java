package com.simcoder.bimbo.instagram.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import com.simcoder.bimbo.Admin.AdminAddNewProductActivity;
import com.simcoder.bimbo.Admin.AdminAllCustomers;
import com.simcoder.bimbo.Admin.AdminCategoryActivity;
import com.simcoder.bimbo.Admin.AdminMaintainProductsActivity;
import com.simcoder.bimbo.Admin.AdminProductDetails;
import com.simcoder.bimbo.Admin.AdminUserCartedActivity;
import com.simcoder.bimbo.Admin.AdminViewBuyersActivity;
import com.simcoder.bimbo.Admin.ViewSingleUserOrders;
import com.simcoder.bimbo.DriverMapActivity;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Interface.ItemClickListner;
import com.simcoder.bimbo.Model.Cart;
import com.simcoder.bimbo.Model.ProductHere;
import com.simcoder.bimbo.Model.Products;
import com.simcoder.bimbo.Model.ProductsInformationModel;
import com.simcoder.bimbo.Model.TraderWhoPostedProductModel;
import com.simcoder.bimbo.ViewHolder.CartViewHolder;
import com.simcoder.bimbo.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.WorkActivities.CartActivity;
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.WorkActivities.ProductDetailsActivity;
import com.simcoder.bimbo.WorkActivities.TraderProfile;
import com.simcoder.bimbo.instagram.Home.HomeFragment;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public  class  MainViewFeedFragment extends Fragment {
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
    String    caption,  date,  image,  time, uid,  name,   photoid,  tid,  pid, price,  tagnumber, likenumber,commentnumber;
    //AUTHENTICATORS
    String   posttype,  traderimage,  tradername;
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
    String photokey;
FirebaseDatabase LikesFirebaseDatabase;
String likeskey;
  public static   String likername,likeimage,likernumber,likeruid, likerlikeid;

    Query myLikeDatabaseQuery;


    public interface GetmyPhotosCallBack {
        void onCallback(String caption,  String date, String image, String name, String photoid, String pid, String posttype, String price, String tid, String traderimage, String tradername);
    }


    public interface GetmyLikersCallBack {
        void onCallback(String image,  String name, String uid, String likeid, String number );
    }


    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    public MainViewFeedFragment() {


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MessageFeedView = inflater.inflate(R.layout.stickynoterecycler, container, false);

              if(MessageFeedView != null) {
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

                         if(FirebaseDatabase.getInstance().getReference() != null) {
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
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        customerid = "";
                        traderoruser = user.getUid();
                    }

                    // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                    // WHICH IS CUSTOMER TO BE ADDED.
                    // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                }        }
            };



              if (MessageFeedView != null){
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
        }}

                final   GetmyPhotosCallBack Gettingmyphotoscallback = new GetmyPhotosCallBack() {
                      @Override
                      public void onCallback( String caption,String date, String image, String name,String photoid,String  pid, String posttype, String price, String tid,String traderimage, String tradername) {

                      }
                  };

                  RetrievingDatabase = FirebaseDatabase.getInstance();


                  myretrievalref = RetrievingDatabase.getReference("Photos");
                  photokey = myretrievalref.getKey();

// Attach a listener to read the data at our posts reference
                  myretrievalref.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          Photo post = dataSnapshot.getValue(Photo.class);
                          //  System.out.println(post);
                          for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                             photokey =  dataSnapshot.getKey();
                              Log.d(TAG, "The Photokey " + photokey);

                              if (dataSnapshot1.child("caption").getValue() != null) {
                                  caption = dataSnapshot1.child("caption").getValue().toString();
                              }
                              if (dataSnapshot1.child("date").getValue() != null) {
                                  date = dataSnapshot1.child("date").getValue().toString();

                              }
                              if (dataSnapshot1.child("image").getValue() != null) {
                                  image = dataSnapshot1.child("image").getValue().toString();

                              }
                              if (dataSnapshot1.child("name").getValue() != null) {
                                  name = dataSnapshot1.child("name").getValue().toString();

                                  if (dataSnapshot1.child("photoid").getValue() != null) {
                                      photoid = dataSnapshot1.child("photoid").getValue().toString();

                                  }
                                  if (dataSnapshot1.child("pid").getValue() != null) {
                                      pid = dataSnapshot1.child("pid").getValue().toString();

                                  }
                                  if (dataSnapshot1.child("posttype").getValue() != null) {
                                      posttype = dataSnapshot1.child("posttype").getValue().toString();

                                  }
                                  if (dataSnapshot1.child("price").getValue() != null) {
                                      price = dataSnapshot1.child("price").getValue().toString();

                                  }
                                  if (dataSnapshot1.child("tid").getValue() != null) {
                                      tid = dataSnapshot1.child("tid").getValue().toString();

                                  }
                                  if (dataSnapshot1.child("traderimage").getValue() != null) {
                                      traderimage = dataSnapshot1.child("traderimage").getValue().toString();

                                  }
                                  if (dataSnapshot1.child("tradername").getValue() != null) {
                                      tradername = dataSnapshot1.child("tradername").getValue().toString();


                                  }

                                  Gettingmyphotoscallback.onCallback(caption, date,  image,  name, photoid, pid,  posttype,  price, tid,traderimage,  tradername); {
                                      Log.d(TAG, "After Call Back " + caption + " " +  date  + " " +  image + " " +  name + " "  + photoid + " " +  pid + " " +  posttype +  price + tid + traderimage +  tradername );
                              }
                          }}}

                      @Override
                      public void onCancelled(DatabaseError databaseError) {
                          System.out.println("The read failed: " + databaseError.getCode());
                      }
                  });



                  Gettingmyphotoscallback.onCallback(caption, date,  image,  name, photoid, pid,  posttype,  price, tid,traderimage,  tradername);

                  Log.d(TAG, "After Second  Call Back " + caption + " " +  date  + " " +  image + " " +  name + " "  + photoid + " " +  pid + " " +  posttype +  price + tid + traderimage +  tradername );


                 final GetmyLikersCallBack getmyLikersCallBack = new GetmyLikersCallBack() {
                      @Override
                      public void onCallback(String image, String name, String uid, String likeid, String number) {

                      }
                  };




                  LikesFirebaseDatabase = FirebaseDatabase.getInstance();


                  mylikesdatabasereference = LikesFirebaseDatabase.getReference("Photos");
                  myLikeDatabaseQuery =  mylikesdatabasereference.child(photokey).child("Likes");
                  likeskey = mylikesdatabasereference.getKey();

// Attach a listener to read the data at our posts reference
                  myLikeDatabaseQuery.addChildEventListener(new ChildEventListener() {
                      @Override
                      public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                      }

                      @Override
                      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                          Photo post = dataSnapshot.getValue(Photo.class);
                          //  System.out.println(post);
                          for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                 String likekey =         dataSnapshot.getKey();

                              if (dataSnapshot2.child("Users").child("image").getValue() != null) {
                                  likeimage = dataSnapshot2.child("Users").child("image").getValue().toString();
                              }
                              if (dataSnapshot2.child("Users").child("name").getValue() != null) {
                                  likername = dataSnapshot2.child("Users").child("name").getValue().toString();

                              }
                              if (dataSnapshot2.child("Users").child("uid").getValue() != null) {
                                  likeruid = dataSnapshot2.child("Users").child("uid").getValue().toString();

                              }
                              if (dataSnapshot2.child(likekey).child("likeid").getValue() != null) {
                                  likerlikeid = dataSnapshot2.child(likekey).child("likeid").getValue().toString();
                                  Log.d(TAG, "Does it show the likeid " +   likerlikeid );

                                  if (dataSnapshot2.child("number").getValue() != null) {
                                      likernumber = dataSnapshot2.child("number").getValue().toString();

                                  }


                                  getmyLikersCallBack.onCallback(likeimage,  likername,  likeruid, likerlikeid,  likernumber); {

                                  }
                              }}

                      }

                      @Override
                      public void onChildRemoved(DataSnapshot dataSnapshot) {

                      }

                      @Override
                      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {

                      }
                  });


                  getmyLikersCallBack.onCallback(likeimage,  likername,  likeruid, likerlikeid,  likernumber);


                  Log.d(TAG, "After likes " +  likeimage +   likername +  likeruid + likerlikeid +  likernumber);


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
                            if (dataSnapshot.child("uid").getValue() != null) {
                                useridentifier = dataSnapshot.child("uid").getValue().toString();
                                if (dataSnapshot.child("role").getValue() != null) {
                                    role = dataSnapshot.child("role").getValue().toString();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }  }

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
        getFollowing();
        getPhotos();
        displayPhotos();
        displayMorePhotos();
        return MessageFeedView;
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
            if (query != null) {
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
    }



    private void getPhotos() {
        Log.d(TAG, "getPhotos: getting photos");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference thePhotos = FirebaseDatabase.getInstance().getReference().child("Photos");
        if (mFollowing != null) {
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
            theprofileimage = (CircleImageView) itemView.findViewById(R.id.profile_photo);
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


*/                                                                String Photokey = snapshot.getKey();
                                                                  String TheLikeKey= snapshot.child(Photokey).child("Likes").getKey();
                                                                  String Commentkey = snapshot.child(Photokey).child("Comments").getKey();
                                                                  String ReplyKey = snapshot.child(Photokey).child("Replies").getKey();




                                                                 if (snapshot.child("caption").getValue() != null) {
                                                              caption =         snapshot.child("caption").getValue().toString();
                                                                 }
                                                            if (snapshot.child("date").getValue() != null) {
                                                             date =     snapshot.child("date").getValue().toString();
                                                            }

                                                            if (snapshot.child("image").getValue() != null) {
                                                            image =      snapshot.child("image").getValue().toString();
                                                            }
                                                            if (snapshot.child("time").getValue() != null) {
                                                          time =        snapshot.child("time").getValue().toString();
                                                            }

                                                            if (snapshot.child("uid").getValue() != null) {
                                                          uid =     snapshot.child("uid").getValue().toString();
                                                            }

                                                            if (snapshot.child("name").getValue() != null) {
                                                            name =     snapshot.child("name").getValue().toString();
                                                            }

                                                            if (snapshot.child("photoid").getValue() != null) {
                                                              photoid =   snapshot.child("photoid").getValue().toString();
                                                            }

                                                            if (snapshot.child("tid").getValue() != null) {
                                                                tid =    snapshot.child("tid").getValue().toString();
                                                            }



                                                            if (snapshot.child("pid").getValue() != null) {
                                                                pid = snapshot.child("pid").getValue().toString();
                                                            }

                                                            if (snapshot.child("price").getValue() != null) {
                                                                 price = snapshot.child("price").getValue().toString();
                                                            }

                                                            if (snapshot.child("tagnumber").getValue() != null) {
                                                                 tagnumber = snapshot.child("tagnumber").getValue().toString();
                                                            }
                                                            if (snapshot.child("Likes").child("likenumber").getValue() != null) {
                                                            likenumber =     snapshot.child("Likes").child("likenumber").getValue().toString();
                                                            }
                                                    if (snapshot.child("tradername").getValue() != null) {
                                                        tradername = snapshot.child("tradername").getValue().toString();
                                                    }
                                                    if (snapshot.child("traderimage").getValue() != null) {
                                                        thetraderimage =      snapshot.child("traderimage").getValue().toString();
                                                    }

                                                    if (snapshot.child("Comments").child("commentnumber").getValue() != null) {
                                                        commentnumber =      snapshot.child("Comments").child("commentnumber").getValue().toString();
                                                    }

                                                    return new Photo(caption, date,  image,  time, uid,  name,   photoid,  tid,  pid, price,  tagnumber, likenumber, tradername, thetraderimage, commentnumber);


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
                  protected void onBindViewHolder(@Nullable MainFeedViewHolder holder, int position, @Nullable Photo model) {
                      if (model != null) {

                          key = model.getphotoid();
                          traderkey = model.gettid();
                          //   model.setTrader(traderkey);


                          holder.username.setText(likerlikeid);

                          holder.likes.setText("Liked by " +    likername + "  " + "number of people");
                          holder.comments.setText("View all comment from" + commentnumber  +"people");
                          holder.caption.setText(caption);
                          holder.timeDetla.setText(model.gettime());
                          holder.setThefeedimage(getContext(), model.getimage());
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







