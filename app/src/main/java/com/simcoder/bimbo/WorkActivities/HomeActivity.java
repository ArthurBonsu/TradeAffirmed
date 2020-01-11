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
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.simcoder.bimbo.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.simcoder.bimbo.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference ProductsRef;
    private DatabaseReference Userdetails;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference TraderDetails;
    String productkey;

    private String type = "";
    String traderoruser = "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String ProductID;


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

        traderoruser = getIntent().getStringExtra("fromadmincategoryactivity");


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Product");


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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        holder.tradername.setText("Trader of Goods+" + model.getTrader() );
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        productkey    =    model.getUid();





                        holder.tradername.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Trader"))
                                {
                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });

                        holder.txtProductName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                if (type.equals("Trader"))
                                {
                                    Intent intent = new Intent(HomeActivity.this, AdminMaintainProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        if (recyclerView !=null) {
            recyclerView.setAdapter(adapter);
        }
        if (adapter != null) {
            adapter.startListening();
        }}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_home_drawer, menu);
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
                intent.putExtra("roledhomeactivitytocustomermapactivity", type);
                intent.putExtra("fromhomeactivitytocustomermapactivity", traderoruser);
                startActivity(intent);
                finish();
            } else{

                Intent intent = new Intent(HomeActivity.this, DriverMapActivity.class);
                intent.putExtra("rolefromhomeactivitytodrivermapactivity", type);
                intent.putExtra("fromhomeactivitytodrivermapactivity", traderoruser);
                startActivity(intent);
                finish();

            }


        }
        if (id == R.id.nav_cart)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }

    }

        if (id == R.id.viewproducts)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }else{}

        }
        if (id == R.id.nav_search)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                startActivity(intent);
            }else{}
        }

        if (id == R.id.nav_logout)
        {


            FirebaseAuth.getInstance().signOut();
            if (mGoogleApiClient != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener(HomeActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }
            Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (id == R.id.nav_settings)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, com.simcoder.bimbo.WorkActivities.SettinsActivity.class);
                startActivity(intent);
            }else{}
        }
        else if (id == R.id.nav_history)
        {
            if (!type.equals("Trader"))
            {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
            }else{}
        }
        else if (id == R.id.nav_categories)
        {

        }
        else if (id == R.id.nav_viewprofilehome)
        {
            if (!type.equals("Trader"))
            { FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               String cusomerId = "";
                 cusomerId = user.getUid();
                Intent intent = new Intent(HomeActivity.this, CustomerProfile.class);
                intent.putExtra("traderorcustomer", traderoruser);
                intent.putExtra("role", type);
                startActivity(intent);
            }

            else {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String cusomerId = "";
                cusomerId = user.getUid();
                Intent intent = new Intent(HomeActivity.this, TraderProfile.class);
                intent.putExtra("traderorcustomer", traderoruser);
                intent.putExtra("role", type);
                startActivity(intent);

            }
        }
        else if (id == R.id.nav_paymenthome)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
