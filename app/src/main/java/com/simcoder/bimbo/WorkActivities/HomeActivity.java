package com.simcoder.bimbo.WorkActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.TextView;

import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.Admin.ViewSingleUserOrders;
import com.simcoder.bimbo.DriverMapActivity;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Interface.ItemClickListner;
import com.simcoder.bimbo.Model.Cart;
import com.simcoder.bimbo.Model.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
import com.simcoder.bimbo.instagram.Home.MainViewFeedFragment;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public  class  HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference ProductsRef;
    private DatabaseReference Userdetails;
    private DatabaseReference ProductsRefwithproduct;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference TraderDetails;
    DatabaseReference FollowerDatabaseReference;
    String productkey;
    String traderkeyhere;
    private String type = "";
    String traderoruser = "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String ProductID;
    FirebaseDatabase myfirebaseDatabase;
    FirebaseDatabase FollowerDatabase;


    public FirebaseRecyclerAdapter adapter;
    String thetraderkey;
    String thenameofthetrader;
    String description;
    String thetradername;

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    String productdescription;
    ImageView theproductimageview;
    TextView thetraderview;
    String keyhere;
    String thetraderhere;
    String traderkey;
    String key;
    String tradename;
    String traderimage;
    FirebaseUser user;
    Query QueryFollowingsshere;
     String followingid;
     String followingname;
     String followingimage;
    //product_name
    // product_imagehere
    //  product_price

    //product_description
    //thetraderiknow
 String   categoryname,date, desc,discount, time, pid, pimage,pname,price,image,name,size, tradername,tid;
    android.widget.ImageView product_imagehere;
 android.widget.ImageView thetraderimageforproduct;
    Getmyfollowings gettingmyfollowingshere;
    Getmyfollowings  gettingmyfollowingshereaswell;
    Getmyfollowings  getmyfollowingsagain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                (R.layout.activity_home));




            if (getIntent().getExtras().get("rolefromcustomermapactivitytohomeactivity") != null) {
                type = getIntent().getExtras().get("rolefromcustomermapactivitytohomeactivity").toString();
            }

        traderoruser = getIntent().getStringExtra("fromcustomermapactivitytohomeactivity");


        if (getIntent().getExtras().get("roledrivermapactivitytohomeactivity") != null) {
            type = getIntent().getExtras().get("roledrivermapactivitytohomeactivity").toString();
        }
        traderoruser = getIntent().getStringExtra("fromdrivermapactivitytohomeactivity");


        //KEY PASSESS FOR TRADER


        if (getIntent().getExtras().get("rolefromadmincategory") != null) {
            type = getIntent().getExtras().get("rolefromadmincategory").toString();
        }

        if (traderoruser != null) {
            traderoruser = getIntent().getStringExtra("fromadmincategoryactivity");
        }



        recyclerView = findViewById(R.id.recycler_menu);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
     /*  if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);

        }
*/


        thetraderview = findViewById(R.id.thetraderiknow);

        product_imagehere = (ImageView) findViewById(R.id.product_imagehere);
        thetraderimageforproduct = (ImageView)findViewById(R.id.thetraderimageforproduct);

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.hometoolbar);
        if (toolbar != null) {
            toolbar.setTitle("Home");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            if (toggle != null) {
                toggle.syncState();
            }

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView != null) {
                navigationView.setNavigationItemSelectedListener(this);
            }
            View headerView = navigationView.getHeaderView(0);
            TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
            CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

            // USER



            mAuth = FirebaseAuth.getInstance();


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




            if (user.getDisplayName() != null) {
                if (user.getDisplayName() != null) {
                    userNameTextView.setText(user.getDisplayName());

                    Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
                }
            }}

        myfirebaseDatabase = FirebaseDatabase.getInstance();
        FollowerDatabase = FirebaseDatabase.getInstance();
        ProductsRef = myfirebaseDatabase.getReference().child("Product");
        FollowerDatabaseReference = FollowerDatabase.getReference().child("Following");

        productkey = ProductsRef.getKey();
        // GET FROM FOLLOWING KEY

        productkey = ProductsRef.getKey();

        fetch();
        recyclerView.setAdapter(adapter);

             ProductsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // This method is called once with the initial value and again

                        tradename = ds.child("tradername").getValue(String.class);
                        traderimage = ds.child("traderimage").getValue(String.class);
                        Log.d(TAG, "Value is 1: " + tradename + traderimage);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                   if (mAuth != null){
             user = mAuth.getCurrentUser();
             if (user != null){
                 traderoruser = user.getUid();

        QueryFollowingsshere = FollowerDatabaseReference.orderByChild("uid").equalTo(traderoruser);

         QueryFollowingsshere.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                     if (dataSnapshot1.child("tid").getValue() != null) {
                         followingid = dataSnapshot1.child("tid").getValue(String.class);
                         if (dataSnapshot1.child("tradername").getValue() != null) {
                             followingname = dataSnapshot1.child("tradername").getValue(String.class);
                             if (dataSnapshot1.child("traderimage").getValue() != null) {
                                 followingimage = dataSnapshot1.child("traderimage").getValue(String.class);

                                 if (getmyfollowingsagain != null) {
                                     if (followingid != null && followingname != null && followingimage != null) {

                                         getmyfollowingsagain.onCallback(followingid, followingname, followingimage);
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

          if (getmyfollowingsagain != null){

              getmyfollowingsagain.onCallback(followingid, followingname, followingimage);

             Log.i("Followerinfo" , followingid + followingname + followingimage);


//        setSupportActionBar(toolbar);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            if (mGoogleApiClient != null) {

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            }

            if (mGoogleApiClient != null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(HomeActivity.this,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
            }



            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!type.equals("Trader")) {
                                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });




                // USER
                user = mAuth.getCurrentUser();



                    if (user != null) {
                        traderoruser = "";
                        traderoruser = user.getUid();
                    }


            }}}}}


    public interface Getmyfollowings {

        void onCallback(String followingid, String followingname, String followingimage);


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        //product_name
        // product_imagehere
        //  product_price

        //product_description
        //thetraderiknow

        public TextView product_name;
        public TextView product_price;

        public TextView product_description;
        public TextView thetraderiknow;

        public android.widget.ImageView product_imagehere;
        public android.widget.ImageView thetraderimageforproduct;
        public ItemClickListner listner;

        public ViewHolder(View itemView) {
            super(itemView);

            //product_name
            // product_imagehere
            //  product_price

            //product_description
            //thetraderiknow


            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_description = itemView.findViewById(R.id.product_description);
            thetraderiknow = itemView.findViewById(R.id.thetraderiknow);


            //cartimage referst to the trader of the product
            product_imagehere = itemView.findViewById(R.id.product_imagehere);
            thetraderimageforproduct = itemView.findViewById(R.id.thetraderimageforproduct);


        }

        public void setItemClickListner(ItemClickListner listner) {
            this.listner = listner;
        }

        public void setcurrentproductname(String currentproductname) {

            product_name.setText(currentproductname);
        }

        public void setproductprice(String price) {

            product_price.setText(price);
        }

        public void settradername(String tradername) {

            thetraderiknow.setText(tradername);
        }


        public void setcartdescriptionhere(String currentdescription) {

            product_description.setText(currentdescription);
        }

        public void setTraderImage(final Context ctx, final String image) {
            final android.widget.ImageView thetraderimageforproduct = (android.widget.ImageView) itemView.findViewById(R.id.thetraderimageforproduct);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(thetraderimageforproduct, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(thetraderimageforproduct);
                }


            });
        }



            public void setImage(final Context ctx, final String image) {
            product_imagehere = (android.widget.ImageView) itemView.findViewById(R.id.product_imagehere);
            if (image != null) {
                if (product_imagehere != null) {

                    //
                    Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(product_imagehere, new Callback() {


                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).resize(100, 0).into(product_imagehere);
                        }


                    });

                }}}};


    public void fetch() {
        FirebaseUser user;
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            if (user != null) {
                traderoruser = user.getUid();
                ProductsRef = FirebaseDatabase.getInstance().getReference().child("Product");
                ProductsRef.keepSynced(true);
                productkey = ProductsRef.getKey();


                if (traderoruser != null) {
                    if (ProductsRef != null) {
                        Query ProductsQuery = ProductsRef.orderByChild("tid").equalTo(traderoruser);
                        //    Query ProductsQuery = ProductsRef;


                        if (ProductsQuery != null) {

                            FirebaseRecyclerOptions<Products> options =
                                    new FirebaseRecyclerOptions.Builder<Products>()
                                            .setQuery(ProductsQuery, new SnapshotParser<Products>() {


                                                @NonNull
                                                @Override
                                                public Products parseSnapshot(@NonNull DataSnapshot snapshot) {


                                                    if (snapshot.child("categoryname").getValue() != null) {
                                                        categoryname = snapshot.child("categoryname").getValue(String.class);
                                                    }
                                                    if (snapshot.child("date").getValue() != null) {
                                                        date = snapshot.child("date").getValue(String.class);
                                                    }

                                                    if (snapshot.child("desc").getValue() != null) {
                                                        desc = snapshot.child("desc").getValue(String.class);
                                                    }
                                                    if (snapshot.child("discount").getValue() != null) {
                                                        discount = snapshot.child("discount").getValue(String.class);
                                                    }

                                                    if (snapshot.child("time").getValue() != null) {
                                                        time = snapshot.child("time").getValue(String.class);
                                                    }


                                                    if (snapshot.child("pimage").getValue() != null) {
                                                        pimage = snapshot.child("pimage").getValue(String.class);
                                                    }
                                                    if (snapshot.child("pname").getValue() != null) {
                                                        pname = snapshot.child("pname").getValue(String.class);
                                                    }
                                                    if (snapshot.child("pid").getValue() != null) {
                                                        pid = snapshot.child("pid").getValue(String.class);
                                                    }

                                                    if (snapshot.child("price").getValue() != null) {
                                                        price = snapshot.child("price").getValue(String.class);
                                                    }

                                                    if (snapshot.child("image").getValue() != null) {
                                                        image = snapshot.child("image").getValue(String.class);
                                                    }
                                                    if (snapshot.child("name").getValue() != null) {
                                                        name = snapshot.child("name").getValue(String.class);
                                                    }

                                                    if (snapshot.child("size").getValue() != null) {
                                                        size = snapshot.child("size").getValue(String.class);
                                                    }

                                                    if (snapshot.child("pname").getValue() != null) {
                                                        pname = snapshot.child("pname").getValue(String.class);
                                                    }

                                                    if (snapshot.child("pimage").getValue() != null) {
                                                        pimage = snapshot.child("pimage").getValue(String.class);
                                                    }

                                                    if (snapshot.child("pid").getValue() != null) {
                                                        pid = snapshot.child("pid").getValue(String.class);
                                                    }

                                                    if (snapshot.child("tradername").getValue() != null) {
                                                        tradername = snapshot.child("tradername").getValue(String.class);
                                                    }

                                                    if (snapshot.child("traderimage").getValue() != null) {
                                                        traderimage = snapshot.child("traderimage").getValue(String.class);
                                                    }

                                                    if (snapshot.child("tid").getValue() != null) {
                                                        tid = snapshot.child("tid").getValue(String.class);
                                                    }


                                                    return new Products(categoryname, date, desc, discount, time, pid, pimage, pname, price, image, name, size, tradername, traderimage, tid);


                                                }

                                            }).build();

                            //product_name
                            // product_imagehere
                            //  product_price

                            //product_description
                            //thetraderiknow


                            adapter = new FirebaseRecyclerAdapter<Products, ViewHolder>(options) {
                                @Override
                                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.product_items_layout, parent, false);

                                    return new ViewHolder(view);
                                }

                                @Override
                                public int getItemCount() {
                                    return super.getItemCount();
                                }

                                @Override
                                protected void onBindViewHolder(ViewHolder holder, final int position, final Products model) {


                                    holder.product_name.setText(pname);

                                    holder.thetraderiknow.setText(tradername);

                                    holder.product_description.setText(desc);
                                    holder.product_price.setText("Price = " +  "$" +price );





                                    //   thetraderimageforproduct
                                    if (thetraderimageforproduct != null) {
                                        Picasso.get().load(traderimage).placeholder(R.drawable.profile).into(thetraderimageforproduct);
                                    }
                                    if (product_imagehere != null) {
                                        Picasso.get().load(pimage).placeholder(R.drawable.profile).into(product_imagehere);
                                    }

                                    holder.setTraderImage(getApplication(), traderimage);
                                    holder.setImage(getApplicationContext(), pimage);



                                    holder.product_imagehere.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (type.equals("Trader")) {
                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                                if (intent != null) {
                                                    intent.putExtra("pid", key);
                                                    intent.putExtra("fromthehomeactivitytraderkey", traderkey);
                                                    intent.putExtra("fromthehomeactivityname", model.getname());
                                                    intent.putExtra("fromthehomeactivityprice", model.getprice());
                                                    intent.putExtra("fromthehomeactivitydesc", model.getdesc());
                                                    intent.putExtra("fromthehomeactivityname", thetraderhere);
                                                    intent.putExtra("fromthehomeactivityimage", model.getimage());

                                                }
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                                if (intent != null) {
                                                    intent.putExtra("fromthehomeactivitytoproductdetails", traderkey);
                                                }
                                                startActivity(intent);
                                            }
                                        }
                                    });


                                    holder.thetraderimageforproduct.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (type.equals("Trader")) {
                                                Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                                                intent.putExtra("pid", key);
                                                intent.putExtra("fromhomeactivitytotraderprofile", traderkey);

                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                                                intent.putExtra("pid", key);
                                                intent.putExtra("fromhomeactivitytotraderprofile", traderkey);

                                                startActivity(intent);
                                            }

                                        }

                                    });



                                }


                            };

                        }

                        }

                }


            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 user = mAuth.getCurrentUser();
                if (mAuth != null) {
                    if (user != null) {

                        traderoruser = user.getUid();
                    }

                    // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                    // WHICH IS CUSTOMER TO BE ADDED.
                    // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                }
            }    }

            ;

            if (mAuth != null) {
                mAuth.addAuthStateListener(firebaseAuthListener);
            }

        if (adapter != null) {
            adapter.startListening();

        }


    }

    @Override
    protected void onStop () {
        super.onStop();
        if (adapter != null){
        adapter.stopListening();
        //     mProgress.hide();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }}
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getMenuInflater() != null) {
            getMenuInflater().inflate(R.menu.traderscontrol, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        if (id == R.id.viewallcustomershere) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.addnewproducthere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminAddNewProductActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminAddNewProductActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.singeuserorderhere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, ViewSingleUserOrders.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, ViewSingleUserOrders.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.viewbuyershere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminViewBuyersActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminViewBuyersActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.usercartedactivityhere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminUserCartedActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminUserCartedActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.newproductdetailshere) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminProductDetails.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminProductDetails.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.Maintainnewproducts) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allcategorieshere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminCategoryActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminCategoryActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allproductshere) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }}}

        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected (MenuItem item)
    {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.viewmap) {
            if (!type.equals("Trader")) {

                Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.CustomerMapActivity.class);
                if (intent != null) {
                    intent.putExtra("roledhomeactivitytocustomermapactivity", type);
                    intent.putExtra("fromhomeactivitytocustomermapactivity", traderoruser);
                    startActivity(intent);
                    finish();
                }
            } else {

                Intent intent = new Intent(HomeActivity.this, DriverMapActivity.class);
                if (intent != null) {
                    intent.putExtra("rolefromhomeactivitytodrivermapactivity", type);
                    intent.putExtra("fromhomeactivitytodrivermapactivity", traderoruser);
                    startActivity(intent);
                    finish();
                }
            }


        }
        if (id == R.id.nav_cart) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }

        }

        if (id == R.id.viewproducts) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }

        }
        if (id == R.id.nav_search) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }

        if (id == R.id.nav_logout) {

            if (FirebaseAuth.getInstance() != null) {
                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient != null) {
                    mGoogleSignInClient.signOut().addOnCompleteListener(HomeActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }
            Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.MainActivity.class);
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }

        if (id == R.id.nav_settings) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.WorkActivities.SettinsActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }
        if (id == R.id.nav_history) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }
        if (id == R.id.nav_categories) {

        }
        if (id == R.id.nav_viewprofilehome) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, CustomerProfile.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
        if (id == R.id.viewallcustomershere) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.addnewproducthere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminAddNewProductActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.singeuserorderhere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, ViewSingleUserOrders.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.viewbuyershere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminViewBuyersActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.usercartedactivityhere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminUserCartedActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.newproductdetailshere) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminProductDetails.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.Maintainnewproducts) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allcategorieshere) {

            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminCategoryActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allproductshere) {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            } else {
                if (FirebaseAuth.getInstance() != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";
                        cusomerId = user.getUid();

                        Intent intent = new Intent(HomeActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", traderoruser);
                            intent.putExtra("role", type);
                            startActivity(intent);
                        }
                    }
                }
            }
        }



        return true;
    }

}
