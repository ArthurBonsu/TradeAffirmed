package com.simcoder.bimbo.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.WorkActivities.CartActivity;
import com.simcoder.bimbo.WorkActivities.CustomerProfile;
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.DriverMapActivity;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Interface.ItemClickListner;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.WorkActivities.SearchProductsActivity;
import com.simcoder.bimbo.WorkActivities.TraderProfile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public  class  AdminAllCustomers extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference ProductsRef;
    private DatabaseReference Userdetails;
    private DatabaseReference ProductsRefwithproduct;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference UsersRef;
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

    public  ViewHolder holders;

    public FirebaseRecyclerAdapter feedadapter;

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    TextView allcustomersname;
    TextView allcustomersphonenumber;
    TextView allcustomersjob;
    ImageView allcustomersimage;
   String traderkey;
    String key;
    String tradename;
    String traderimage;
    FirebaseUser user;


    String categoryname, date, desc, discount, time, pid, pimage, pname, price, image, name, size, tradername, tid;
    String thetraderimage;
    String address;
    String amount;
    String city;
    String delivered;
    String distance;
    String uid;
    String mode;

    String number;
    String phone;
    String quantity;
    String shippingcost;
    String state;
    String thecustomersjob;

    Getmyfollowings getmyfollowingsagain;
    String userkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                (R.layout.activityhomeforadmin));


        recyclerView = findViewById(R.id.recycler_menu);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
     /*  if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);

        }
*/


        allcustomersname = findViewById(R.id.allcustomersname);
        allcustomersphonenumber = findViewById(R.id.allcustomersphonenumber);

        allcustomersjob = findViewById(R.id.allcustomersjob);


        allcustomersimage = (ImageView) findViewById(R.id.allcustomersimage);


        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.hometoolbar);
        if (toolbar != null) {
            toolbar.setTitle("All Customers");
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

            if (mAuth != null) {
                FirebaseUser user = mAuth.getCurrentUser();


                if (user.getDisplayName() != null) {
                    if (user.getDisplayName() != null) {
                        userNameTextView.setText(user.getDisplayName());

                        Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
                    }
                }


                myfirebaseDatabase = FirebaseDatabase.getInstance();

                UsersRef = myfirebaseDatabase.getReference().child("Users");


                userkey = UsersRef.getKey();
                // GET FROM FOLLOWING KEY


                fetch();
                recyclerView.setAdapter(feedadapter);


                if (mAuth != null) {
                    user = mAuth.getCurrentUser();
                    if (user != null) {
                        traderoruser = user.getUid();

                    }


//        setSupportActionBar(toolbar);

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                    if (mGoogleApiClient != null) {

                        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                    }

                    if (mGoogleApiClient != null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminAllCustomers.this,
                                new GoogleApiClient.OnConnectionFailedListener() {
                                    @Override
                                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                    }
                                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
                    }


                    // USER
                    user = mAuth.getCurrentUser();


                }
            }
        }
    }


    public interface Getmyfollowings {

        void onCallback(String followingid, String followingname, String followingimage);


    }





    //GETFOLLOWING WILL PULL FROM DIFFERENT DATASTORE( THE USER DATASTORE)


    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;





        public TextView allcustomersname;
        public TextView allcustomersphonenumber;

        public TextView allcustomersjob;

        public android.widget.ImageView allcustomersimage;
       public ItemClickListner listner;

        public ViewHolder(View itemView) {
            super(itemView);




            allcustomersname = itemView.findViewById(R.id.allcustomersname);
            allcustomersphonenumber = itemView.findViewById(R.id.allcustomersphonenumber);
            allcustomersjob = itemView.findViewById(R.id.allcustomersjob);




        }

        public void setItemClickListner(ItemClickListner listner) {
            this.listner = listner;
        }

        public void setallcustomersshown(String allcustomershownname) {

            allcustomersname.setText(allcustomershownname);
        }

        public void setallcustomerphoneinformation(String customersphonenumber) {

            allcustomersphonenumber.setText(customersphonenumber);
        }

        public void settradername(String setthecustomerjob) {

            allcustomersjob.setText(setthecustomerjob);
        }



        public void setallcustomersimage(final Context ctx, final String image) {
            final android.widget.ImageView allcustomersimage = (android.widget.ImageView) itemView.findViewById(R.id.allcustomersimage);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(allcustomersimage, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(allcustomersimage);
                }


            });
        }




                };


    public void useValue (String yourValue){

        Log.d(TAG, "countryNameCode: " + yourValue);

    }


    private void fetch() {
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            if (user != null) {
                traderoruser = user.getUid();

            }

            @Nullable

            Query queryhere =

                    FirebaseDatabase.getInstance().getReference().child("Orders").orderByChild("tid").equalTo(traderoruser);
            if (queryhere != null) {

                FirebaseRecyclerOptions<Users> options =
                        new FirebaseRecyclerOptions.Builder<Users>()
                                .setQuery(queryhere, new SnapshotParser<Users>() {


                                    @Nullable
                                    @Override
                                    public Users parseSnapshot(@Nullable DataSnapshot snapshot) {


                                      /*
                                      String commentkey = snapshot.child("Comments").getKey();
                                      String likekey = snapshot.child("Likes").getKey();


*/
                                        Log.i(TAG, "AdminAllCustomers " + snapshot);


                                        if (snapshot.child("date").getValue(String.class) != null) {
                                            date = snapshot.child("date").getValue(String.class);
                                        }

                                        if (snapshot.child("time").getValue(String.class) != null) {
                                            time = snapshot.child("time").getValue(String.class);
                                        }

                                        if (snapshot.child("tid").getValue(String.class) != null) {
                                            tid = snapshot.child("tid").getValue(String.class);
                                        }
                                        if (snapshot.child("traderimage").getValue(String.class) != null) {
                                            thetraderimage = snapshot.child("traderimage").getValue(String.class);
                                        }


                                        if (snapshot.child("tradername").getValue(String.class) != null) {
                                            tradername = snapshot.child("tradername").getValue(String.class);
                                        }


                                        if (snapshot.child("address").getValue(String.class) != null) {
                                            address = snapshot.child("address").getValue(String.class);
                                        }


                                        if (snapshot.child("amount").getValue(String.class) != null) {
                                            amount = snapshot.child("amount").getValue(String.class);
                                        }


                                        if (snapshot.child("city").getValue(String.class) != null) {
                                            city = snapshot.child("city").getValue(String.class);
                                        }


                                        if (snapshot.child("delivered").getValue(String.class) != null) {
                                            delivered = snapshot.child("delivered").getValue(String.class);
                                        }

                                        if (snapshot.child("distance").getValue(String.class) != null) {
                                            distance = snapshot.child("distance").getValue(String.class);
                                        }


                                        if (snapshot.child("image").getValue(String.class) != null) {
                                            image = snapshot.child("image").getValue(String.class);
                                        }
                                        if (snapshot.child("uid").getValue(String.class) != null) {
                                            uid = snapshot.child("uid").getValue(String.class);
                                        }
                                        if (snapshot.child("name").getValue(String.class) != null) {
                                            name = snapshot.child("name").getValue(String.class);
                                        }
                                        if (snapshot.child("mode").getValue(String.class) != null) {
                                            mode = snapshot.child("mode").getValue(String.class);
                                        }


                                        if (snapshot.child("number").getValue(String.class) != null) {
                                            number = snapshot.child("number").getValue(String.class);
                                        }

                                        if (snapshot.child("phone").getValue(String.class) != null) {
                                            phone = snapshot.child("phone").getValue(String.class);
                                        }
                                        if (snapshot.child("quantity").getValue(String.class) != null) {
                                            quantity = snapshot.child("quantity").getValue(String.class);
                                        }

                                        if (snapshot.child("shippingcost").getValue(String.class) != null) {
                                            shippingcost = snapshot.child("shippingcost").getValue(String.class);
                                        }

                                        if (snapshot.child("state").getValue(String.class) != null) {
                                            state = snapshot.child("state").getValue(String.class);
                                        }


                                        return new Users(date, time, tid, thetraderimage, tradername, address, amount, city, delivered, distance, image, uid, name, mode,

                                                number, phone, quantity, shippingcost, state);


                                    }

                                }).build();


                feedadapter = new FirebaseRecyclerAdapter<Users, ViewHolder>(options) {
                    @Nullable
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        @Nullable
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.admincustomeractivity, parent, false);

                        return new ViewHolder(view);
                    }


                    @Override
                    public int getItemCount() {
                        return super.getItemCount();
                    }

                    @Override
                    protected void onBindViewHolder(@Nullable final ViewHolder holder, int position, @Nullable Users model) {
                        if (model != null) {
                            holders = holder;

                            holder.allcustomersname.setText(name);
                            holder.allcustomersphonenumber.setText(phone);

                            Log.d(TAG, "The Customers here " + name + phone );
                            holder.setallcustomersimage(getApplicationContext(), thetraderimage);


                            myfirebaseDatabase = FirebaseDatabase.getInstance();

                            UsersRef = myfirebaseDatabase.getReference().child("Users").child("Customers");
                            UsersRef.keepSynced(true);
                            Query firebasequery =  myfirebaseDatabase.getReference().child("Users").child("Customers").orderByChild("uid").equalTo(uid);

                            firebasequery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {



                                        userkey = dataSnapshot1.getKey();
                                        Log.d(TAG, "The Photokey " + userkey);

                                        Log.d(TAG, "UserID here  " + uid );
                                        if (dataSnapshot1.child("job").getValue() != null) {
                                            thecustomersjob = dataSnapshot1.child("job").getValue(String.class);
                                            holders.allcustomersjob.setText(thecustomersjob);
                                            Log.d(TAG, "The CustomerJob " + thecustomersjob);



                                            useValue (thecustomersjob);
                                            Log.d(TAG, "The UseValueJob " + thecustomersjob);
                                        }
                                    }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                            if (allcustomersimage != null) {
                                Picasso.get().load(thetraderimage).placeholder(R.drawable.profile).into(allcustomersimage);
                            }

                            if (holder.allcustomersname != null) {
                                holder.allcustomersname.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent allcustomernameesintent = new Intent(AdminAllCustomers.this, CustomerProfile.class);

                                        startActivity(allcustomernameesintent);

                                    }
                                });
                            }
                            if (holder.allcustomersimage != null) {
                                holder.allcustomersimage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent allcustomerimagesintent = new Intent(AdminAllCustomers.this, CustomerProfile.class);

                                            startActivity(allcustomerimagesintent);

                                    }
                                });
                            }


                        }
                    }


                };



            }




            if (recyclerView != null) {
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
            }    };


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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllCustomers.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllCustomers.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminAddNewProductActivityII.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminAddNewProductActivityII.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, ViewYourPersonalProduct.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, ViewYourPersonalProduct.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, ViewCurrentCartPlaced.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, ViewCurrentCartPlaced.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, ViewAllCarts.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, ViewAllCarts.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminProductDetails.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminProductDetails.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminMaintainProductsActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminMaintainProductsActivity.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminCategoryActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminCategoryActivity.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllProducts.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllProducts.class);
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

                Intent intent = new Intent(AdminAllCustomers.this, com.simcoder.bimbo.CustomerMapActivity.class);
                if (intent != null) {
                    intent.putExtra("roledhomeactivitytocustomermapactivity", type);
                    intent.putExtra("fromhomeactivitytocustomermapactivity", traderoruser);
                    startActivity(intent);
                    finish();
                }
            } else {

                Intent intent = new Intent(AdminAllCustomers.this, DriverMapActivity.class);
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
                Intent intent = new Intent(AdminAllCustomers.this, CartActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }

        }

        if (id == R.id.viewproducts) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }

        }
        if (id == R.id.nav_search) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(AdminAllCustomers.this, SearchProductsActivity.class);
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
                    mGoogleSignInClient.signOut().addOnCompleteListener(AdminAllCustomers.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }
            Intent intent = new Intent(AdminAllCustomers.this, com.simcoder.bimbo.MainActivity.class);
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }

        if (id == R.id.nav_settings) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(AdminAllCustomers.this, com.simcoder.bimbo.WorkActivities.SettinsActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }
        if (id == R.id.nav_history) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(AdminAllCustomers.this, HistoryActivity.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, CustomerProfile.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, TraderProfile.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllCustomers.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminAddNewProductActivityII.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, ViewYourPersonalProduct.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, ViewCurrentCartPlaced.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, ViewAllCarts.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminProductDetails.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminMaintainProductsActivity.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, HomeActivity.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminCategoryActivity.class);
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
                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllProducts.class);
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

                        Intent intent = new Intent(AdminAllCustomers.this, AdminAllProducts.class);
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



// #BuiltByGOD



