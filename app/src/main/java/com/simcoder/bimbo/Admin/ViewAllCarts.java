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
import com.google.firebase.database.Query;
import com.rey.material.widget.ImageView;
import com.simcoder.bimbo.WorkActivities.CartActivity;
import com.simcoder.bimbo.WorkActivities.CustomerProfile;
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.DriverMapActivity;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Interface.ItemClickListner;
import com.simcoder.bimbo.Model.Cart;
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


public  class ViewAllCarts extends AppCompatActivity
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

    private RecyclerView productsList;
    private DatabaseReference cartListRef;
    private Query mQueryTraderandUserCart;
    private String userID = "";
    String  traderID ="";
    Query QueryUser;
    String  role;
String cartkey;
String photoid;
    String getimage;
    DatabaseReference myreferencetoimage;


//
    //AUTHENTICATORS
android.widget.ImageView admincartimageofproduct;
    TextView admincartproductid;
    TextView admincarttitlehere;
       TextView     admincartquantity;
   TextView admincart_price;
    TextView admincarttime;
    ImageView admincartimageofuser;
    TextView            admincartusername;


    ImageView admincartimageofprouct;



    // HAS TO BE ORDERED BY ADMINID
// THE ADMIN CAN CHECK FOR A PARTICULAR USERS PRODUCT BOUGHT



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                (R.layout.stickynoterecycler));

        Intent userintent = getIntent();
        if( userintent.getExtras().getString("userID") != null) {
            userID = userintent.getExtras().getString("userID");
        }

        Intent userIDintent = getIntent();
        if( userIDintent.getExtras().getString("userkey") != null) {
            userID = userIDintent.getExtras().getString("userkey");
        }

        Intent roleidintent = getIntent();
        if( roleidintent.getExtras().getString("rolefromnewordertouserproduct") != null) {
            role = roleidintent.getExtras().getString("rolefromnewordertouserproduct");
        }



            /*
        if (getIntent().getStringExtra("rolefromnewordertouserproduct") != null) {
                    role = getIntent().getStringExtra("rolefromnewordertouserproduct").toString();
                }
*/
        Intent traderIDintent = getIntent();
        if( traderIDintent.getExtras().getString("fromnewordertousersproductactivity") != null) {
            traderID = traderIDintent.getExtras().getString("fromnewordertousersproductactivity");
        }
               /* if (getIntent().getStringExtra("fromnewordertousersproductactivity") != null) {
                    traderID = getIntent().getStringExtra("fromnewordertousersproductactivity");
                }
            */




        recyclerView = findViewById(R.id.stickyheaderrecyler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);

        }
     /*  if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);

        }
*/



        admincartproductid = findViewById(R.id.admincartproductid);
        admincartquantity = findViewById(R.id.admincartquantity);

        admincart_price = findViewById(R.id.admincart_price);
               admincarttime = findViewById(R.id.admincarttime);

        admincartimageofprouct = (ImageView) findViewById(R.id.admincartimageofproduct);
        admincartimageofuser = (ImageView) findViewById(R.id.admincartimageofuser);
                admincartusername = (TextView) findViewById(R.id.admincartusername);


        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("User Activity");
        }
//        setSupportActionBar(toolbar);


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


                if (mAuth != null) {
                    user = mAuth.getCurrentUser();
                    if (user != null) {
                        userID = user.getUid();

                    }

                    myfirebaseDatabase = FirebaseDatabase.getInstance();

                    UsersRef = myfirebaseDatabase.getReference().child("Cart");


                    userkey = UsersRef.getKey();
                    // GET FROM FOLLOWING KEY



                    fetch();
                    recyclerView.setAdapter(feedadapter);



//        setSupportActionBar(toolbar);

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                    if (mGoogleApiClient != null) {

                        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                    }

                    if (mGoogleApiClient != null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(ViewAllCarts.this,
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




        public TextView admincartproductid;
        public TextView admincarttitlehere;

        public TextView admincartquantity;
        public TextView admincart_price;
        TextView admincarttime;
        public android.widget.ImageView admincartimageofproduct;
        public android.widget.ImageView admincartimageofuser;
        TextView admincartusername;


        public ItemClickListner listner;

        public ViewHolder(View itemView) {
            super(itemView);



            admincartproductid = itemView.findViewById(R.id.admincartproductid);
            admincarttitlehere = itemView.findViewById(R.id.admincarttitlehere);
            admincartquantity = itemView.findViewById(R.id.admincartquantity);
            admincart_price = itemView.findViewById(R.id.admincart_price);

            admincarttime = itemView.findViewById(R.id.admincarttime);

            admincartimageofuser = itemView.findViewById(R.id.admincartimageofuser);

            admincartusername = itemView.findViewById(R.id.admincartusername);


        }

        public void setItemClickListner(ItemClickListner listner) {
            this.listner = listner;
        }

        public void setalladmincartproductid(String alladmincartproductid) {

            admincartproductid.setText(alladmincartproductid);
        }

        public void setalladmincarttitlehere(String alladmincarttitlehere) {

            admincarttitlehere.setText(alladmincarttitlehere);
        }

        public void setadmincart_price(String alladmincart_price) {

            admincart_price.setText(alladmincart_price);
        }

        public void setadmincarttime(String alladmincarttime) {

            admincarttime.setText(alladmincarttime);
        }

        public void setadmincartusername(String alladmincartusername) {

            admincarttime.setText(alladmincartusername);
        }



        public void setadmincartimageofproduct(final Context ctx, final String image) {
            final android.widget.ImageView admincartimageofproduct = (android.widget.ImageView) itemView.findViewById(R.id.admincartimageofproduct);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(admincartimageofproduct, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(admincartimageofproduct);
                }


            });
        }



        public void setadmincartimageofuser(final Context ctx, final String image) {
            final android.widget.ImageView admincartimageofuser = (android.widget.ImageView) itemView.findViewById(R.id.admincartimageofuser);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(admincartimageofuser, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(admincartimageofuser);
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

                    FirebaseDatabase.getInstance().getReference().child("Cart").orderByChild("tid").equalTo(traderoruser);
            if (queryhere != null) {

                FirebaseRecyclerOptions<Cart> options =
                        new FirebaseRecyclerOptions.Builder<Cart>()
                                .setQuery(queryhere, new SnapshotParser<Cart>() {


                                    @Nullable
                                    @Override
                                    public Cart parseSnapshot(@Nullable DataSnapshot snapshot) {


                                      /*
                                      String commentkey = snapshot.child("Comments").getKey();
                                      String likekey = snapshot.child("Likes").getKey();


*/
                                        Log.i(TAG, "AdminSeeUserCart " + snapshot);


                                        if (snapshot.child("cartkey").getValue() != null) {
                                            cartkey = snapshot.child("cartkey").getValue(String.class);
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


                                        if (snapshot.child("image").getValue() != null) {
                                            image = snapshot.child("image").getValue(String.class);
                                        }


                                        if (snapshot.child("name").getValue() != null) {
                                            name = snapshot.child("name").getValue(String.class);
                                        }



                                        if (snapshot.child("photoid").getValue() != null) {
                                            photoid = snapshot.child("photoid").getValue(String.class);
                                        }


                                        if (snapshot.child("pid").getValue() != null) {
                                            pid = snapshot.child("pid").getValue(String.class);
                                        }


                                        if (snapshot.child("pimage").getValue() != null) {
                                            pimage = snapshot.child("pimage").getValue(String.class);
                                        }

                                        if (snapshot.child("pname").getValue() != null) {
                                            pname = snapshot.child("pname").getValue(String.class);
                                        }


                                        if (snapshot.child("price").getValue() != null) {
                                            price = snapshot.child("price").getValue(String.class);
                                        }
                                        if (snapshot.child("quantity").getValue() != null) {
                                            quantity = snapshot.child("quantity").getValue(String.class);
                                        }
                                        if (snapshot.child("tid").getValue() != null) {
                                            tid = snapshot.child("tid").getValue(String.class);
                                        }
                                        if (snapshot.child("time").getValue() != null) {
                                            time = snapshot.child("time").getValue(String.class);
                                        }


                                        if (snapshot.child("traderimage").getValue() != null) {
                                            traderimage = snapshot.child("traderimage").getValue(String.class);
                                        }

                                        if (snapshot.child("tradername").getValue() != null) {
                                            tradername = snapshot.child("tradername").getValue(String.class);
                                        }
                                        if (snapshot.child("uid").getValue() != null) {
                                            uid = snapshot.child("uid").getValue(String.class);
                                        }



                                        return new Cart( cartkey,date,desc,discount,image, name, photoid, pid, pimage, pname, price, quantity, tid, time, traderimage,tradername, uid);


                                    }

                                }).build();


                feedadapter = new FirebaseRecyclerAdapter<Cart, ViewHolder>(options) {
                    @Nullable
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        @Nullable
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.admincartitemlayout, parent, false);

                        return new ViewHolder(view);
                    }


                    @Override
                    public int getItemCount() {
                        return super.getItemCount();
                    }

                    @Override
                    protected void onBindViewHolder(@Nullable final ViewHolder holder, int position, @Nullable Cart model) {
                        if (model != null) {




                            holder.admincartproductid.setText("Prouct ID        " + pid);
                            holder.admincarttitlehere.setText("Product Name     " +pname);
                            holder.admincartquantity.setText("Product Quantity  " +quantity);
                            holder.admincart_price.setText("Product Price       " +price);
                            holder.admincarttime.setText("Product Time          " + time);
                            holder.admincartusername.setText("Users Name        " + name );

                            Log.d(TAG, "The Product here " + pname + price );
                            holder.setadmincartimageofproduct(getApplicationContext(), pimage);
                             holder.setadmincartimageofuser(getApplicationContext(), image);

                            if (admincartimageofproduct != null) {
                                Picasso.get().load(pimage).placeholder(R.drawable.profile).into(admincartimageofproduct);
                            }

                            if (admincartimageofuser != null) {
                                Picasso.get().load(traderimage).placeholder(R.drawable.profile).into(admincartimageofuser);
                            }
                            holder.admincartusername.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intentviewbuyers = new Intent(ViewAllCarts.this, ViewCurrentCartPlaced.class);
                                    intentviewbuyers.putExtra("userIDfromcart", uid);

                                    startActivity(intentviewbuyers);
                                }
                            });
                            if (holder.admincartimageofproduct != null) {
                                holder.admincartimageofproduct.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(ViewAllCarts.this, AdminProductDetails.class);
                                        intent.putExtra("rolefromadmincartadminproductdetails", role);
                                        intent.putExtra("fromadmintcatactivitytoadminproductdetails", tid);
                                        intent.putExtra("fromusercartactivitydminproductdetails", pid);
                                        intent.putExtra("fromuserTHEIDcartactivitydminproductdetails", traderoruser);

                                        startActivity(intent);


                                    }
                                });
                            }
                            if (holder.admincarttitlehere != null) {
                                holder.admincarttitlehere.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent viewingtheproductactivityhere = new Intent(ViewAllCarts.this, AdminProductDetails.class);
                                        viewingtheproductactivityhere.putExtra("rolefromadmincartadminproductdetails", role);
                                        viewingtheproductactivityhere.putExtra("fromadmintcatactivitytoadminproductdetails", tid);
                                        viewingtheproductactivityhere.putExtra("fromuserTHEIDcartactivitydminproductdetails", traderoruser);
                                        viewingtheproductactivityhere.putExtra("fromusercartactivitydminproductdetails", pid);

                                        startActivity(viewingtheproductactivityhere);

                                    }
                                });
                            }


                        }
                    }


                };



            }




//            if (recyclerView != null) {
  //              recyclerView.setAdapter(feedadapter);
    //        }

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
                        Intent intent = new Intent(ViewAllCarts.this, AdminAllCustomers.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminAllCustomers.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, AdminAddNewProductActivityII.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminAddNewProductActivityII.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, ViewYourPersonalProduct.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, ViewYourPersonalProduct.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, ViewCurrentCartPlaced.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, ViewCurrentCartPlaced.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, ViewAllCarts.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, ViewAllCarts.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, AdminProductDetails.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminProductDetails.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, AdminMaintainProductsActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminMaintainProductsActivity.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, AdminCategoryActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminCategoryActivity.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, AdminAllProducts.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminAllProducts.class);
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

                Intent intent = new Intent(ViewAllCarts.this, com.simcoder.bimbo.CustomerMapActivity.class);
                if (intent != null) {
                    intent.putExtra("roledhomeactivitytocustomermapactivity", type);
                    intent.putExtra("fromhomeactivitytocustomermapactivity", traderoruser);
                    startActivity(intent);
                    finish();
                }
            } else {

                Intent intent = new Intent(ViewAllCarts.this, DriverMapActivity.class);
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
                Intent intent = new Intent(ViewAllCarts.this, CartActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }

        }

        if (id == R.id.viewproducts) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }

        }
        if (id == R.id.nav_search) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(ViewAllCarts.this, SearchProductsActivity.class);
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
                    mGoogleSignInClient.signOut().addOnCompleteListener(ViewAllCarts.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }
            Intent intent = new Intent(ViewAllCarts.this, com.simcoder.bimbo.MainActivity.class);
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }

        if (id == R.id.nav_settings) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(ViewAllCarts.this, com.simcoder.bimbo.WorkActivities.SettinsActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }
        if (id == R.id.nav_history) {
            if (!type.equals("Trader")) {
                Intent intent = new Intent(ViewAllCarts.this, HistoryActivity.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, CustomerProfile.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, TraderProfile.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminAllCustomers.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminAddNewProductActivityII.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, ViewYourPersonalProduct.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, ViewCurrentCartPlaced.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, ViewAllCarts.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminProductDetails.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminMaintainProductsActivity.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, HomeActivity.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminCategoryActivity.class);
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
                        Intent intent = new Intent(ViewAllCarts.this, AdminAllProducts.class);
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

                        Intent intent = new Intent(ViewAllCarts.this, AdminAllProducts.class);
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



