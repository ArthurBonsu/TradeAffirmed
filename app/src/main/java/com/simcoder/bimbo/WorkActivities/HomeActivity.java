package com.simcoder.bimbo.WorkActivities;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.Admin.AdminMaintainProductsActivity;
import com.simcoder.bimbo.DriverMapActivity;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Model.Products;
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.simcoder.bimbo.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.simcoder.bimbo.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference ProductsRef;
    private DatabaseReference Userdetails;
    private DatabaseReference ProductsRefwithproduct;
    private RecyclerView recyclerView;
    DatabaseReference ProductsRefwithproductTrader;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference TraderDetails;
    String productkey;

    private String type = "";
    String traderoruser = "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String ProductID;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_home);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        // TYPE IS THE SAME AS ROLE


        {
            if (getIntent().getExtras().get("rolefromcustomermapactivitytohomeactivity") != null) {
                type = getIntent().getExtras().get("rolefromcustomermapactivitytohomeactivity").toString();
            }
        }
        traderoruser = getIntent().getStringExtra("fromcustomermapactivitytohomeactivity");


        {
            if (getIntent().getExtras().get("roledrivermapactivitytohomeactivity") != null) {
                type = getIntent().getExtras().get("roledrivermapactivitytohomeactivity").toString();
            }
        }
        traderoruser = getIntent().getStringExtra("fromdrivermapactivitytohomeactivity");


        //KEY PASSESS FOR TRADER

        {
            if (getIntent().getExtras().get("rolefromadmincategory") != null) {
                type = getIntent().getExtras().get("rolefromadmincategory").toString();
            }
        }
        if (traderoruser != null) {
            traderoruser = getIntent().getStringExtra("fromadmincategoryactivity");
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Product");
        ProductsRefwithproduct = FirebaseDatabase.getInstance().getReference().child("Product");
        productkey = ProductsRefwithproduct.getKey();
        ProductsRefwithproductTrader = FirebaseDatabase.getInstance().getReference().child("Product").child(productkey).child("trader");
        thetraderkey =ProductsRefwithproductTrader.getKey();
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, Object> map = new HashMap<String, Object>(); //Object is containing String
                map = (Map<String, Object>) dataSnapshot.getValue();


                Map<String, Object> traderinfo = new HashMap<String, Object>(); //Object is containing String
                traderinfo = (Map<String, Object>) dataSnapshot.child(productkey).child("trader").getValue(Users.class);


                Map<String, Object> traderdescription = new HashMap<String, Object>(); //Object is containing String

                traderdescription = (Map<String, Object>) dataSnapshot.child(productkey).child("desc").getValue(Users.class);



                Map<String, String> newMap = new HashMap<String, String>();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() != null) {
                        if (entry.getValue() instanceof String) {

                            try {
                                newMap.put(entry.getKey(), (String) entry.getValue().toString());
                                traderinfo.put(entry.getKey(), (String) entry.getValue().toString());
                                traderdescription.put(entry.getKey(), (String) entry.getValue().toString());


                            } catch (ClassCastException e) {
                                System.out.println("ERROR: " + entry.getKey() + " -> " + entry.getValue() +
                                        " not added, as " + entry.getValue() + " is not a String");
                            }
                        }
                        if (productkey != null) {


                            if (traderinfo != null) {
                                thenameofthetrader = (traderinfo.get("name").toString());
                            }

                            if (traderdescription != null) {
                                description = (traderdescription.get("desc").toString());
                            }


                            //       productkey = dataSnapshot.getKey();
                    //       thetraderkey = dataSnapshot.child(productkey).child("trader").getKey();
                    //       thenameofthetrader = dataSnapshot.child(productkey).child("trader").child(thetraderkey).child("name").getValue(Products.class).toString();
                    //        description = dataSnapshot.child(productkey).child("desc").getValue(Products.class).toString();
                        }
                    }

                    ;
                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
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

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    traderoruser = "";
                    traderoruser = user.getUid();
                }

                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
            }
        };
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (traderoruser != null) {
                Userdetails = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(traderoruser);

            }
            if (!type.equals("Trader"))

                if (user != null) {
                    traderoruser = "";
                    traderoruser = user.getUid();
                }


            {
                if (user.getDisplayName() != null) {
                    if (user.getDisplayName() != null) {
                        userNameTextView.setText(user.getDisplayName());

                        Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
                    }
                }
            }


            recyclerView = findViewById(R.id.recycler_menu);
            if (recyclerView != null) {
                recyclerView.setHasFixedSize(true);

            }
            layoutManager = new LinearLayoutManager(this);
            if (recyclerView != null) {
                recyclerView.setLayoutManager(layoutManager);
            }
        }

    }



    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class)
                .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        if (holder != null) {
                            holder.txtProductName.setText(model.getname());
                            holder.txtProductDescription.setText(description);
                            holder.txtProductPrice.setText("Price = " + model.getprice() + "$");
                            holder.tradername.setText("Trader of Goods+" + thetradername);
                        }
                        if (model != null) {
                            Picasso.get().load(model.getimage()).into(holder.imageView);
                        }


                        if (holder != null) {
                            holder.tradername.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (type.equals("Trader")) {
                                        Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                                        intent.putExtra("pid", productkey);
                                        intent.putExtra("fromhomeactivitytotraderprofile", thetraderkey);

                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                                        intent.putExtra("pid", productkey);
                                        intent.putExtra("fromhomeactivitytotraderprofile", thetraderkey);

                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                        if (holder != null) {
                            holder.txtProductName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (type.equals("Trader")) {
                                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                        if (intent != null) {
                                            intent.putExtra("pid", productkey);
                                        }
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                        if (intent != null) {
                                            intent.putExtra("pid", productkey);
                                        }
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }}
                    ;

        if (recyclerView !=null) {
            recyclerView.setAdapter(adapter);
        }
        if (adapter != null) {
            adapter.startListening();
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
             getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
         }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();




        if (id == R.id.viewmap)
        {
            if (!type.equals("Trader"))
            {

                Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.CustomerMapActivity.class);
                     if (intent != null) {
                         intent.putExtra("roledhomeactivitytocustomermapactivity", type);
                         intent.putExtra("fromhomeactivitytocustomermapactivity", traderoruser);
                         startActivity(intent);
                         finish();
                     }
            } else{

                Intent intent = new Intent(HomeActivity.this, DriverMapActivity.class);
                 if (intent != null) {
                     intent.putExtra("rolefromhomeactivitytodrivermapactivity", type);
                     intent.putExtra("fromhomeactivitytodrivermapactivity", traderoruser);
                     startActivity(intent);
                     finish();
                 }
            }


        }
        if (id == R.id.nav_cart)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                 if (intent != null) {
                     startActivity(intent);
                 }}

    }

        if (id == R.id.viewproducts)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                 if (intent != null) {
                     startActivity(intent);
                 }}else{}

        }
        if (id == R.id.nav_search)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                       if (intent != null){
                startActivity(intent);}
            }else{}
        }

        if (id == R.id.nav_logout)
        {

                         if (FirebaseAuth.getInstance() != null){
            FirebaseAuth.getInstance().signOut();
            if (mGoogleApiClient != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener(HomeActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }}
            Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.MainActivity.class);
             if (intent != null){
                         startActivity(intent);
            finish();
        }}

        if (id == R.id.nav_settings)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.WorkActivities.SettinsActivity.class);
            if (intent != null) {
                startActivity(intent);
            }}else{}
        }
        else if (id == R.id.nav_history)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
              if (intent != null) {
                  startActivity(intent);
              }}else{}
        }
        else if (id == R.id.nav_categories)
        {

        }
        else if (id == R.id.nav_viewprofilehome)
        {
            if (!type.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                     if (user != null){
                    String cusomerId = "";

                    cusomerId = user.getUid();
                    Intent intent = new Intent(HomeActivity.this, CustomerProfile.class);
                         if (intent != null){
                    intent.putExtra("traderorcustomer", traderoruser);
                    intent.putExtra("role", type);
                    startActivity(intent);
                }}
            }}
            else {
                       if (FirebaseAuth.getInstance() !=null){

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                      if (user != null){
                String cusomerId = "";
                cusomerId = user.getUid();

                Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                 if (intent != null) {
                     intent.putExtra("traderorcustomer", traderoruser);
                     intent.putExtra("role", type);
                     startActivity(intent);
                 }
            }
        }}}
        else if (id == R.id.nav_paymenthome)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         if (drawer != null) {
             drawer.closeDrawer(GravityCompat.START);
         }
             return true;
         }

    @Override
    protected void onStop() {
        super.onStop();
         //     mProgress.hide();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}
