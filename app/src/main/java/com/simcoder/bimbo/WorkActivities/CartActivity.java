package com.simcoder.bimbo.WorkActivities;

import android.content.Context;
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
import com.simcoder.bimbo.Admin.ViewAllCarts;
import com.simcoder.bimbo.Admin.ViewSpecificUsersCart;
import com.simcoder.bimbo.Admin.ViewYourPersonalProduct;
import com.simcoder.bimbo.DriverMapActivity;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Interface.ItemClickListner;
import com.simcoder.bimbo.Model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.Model.Products;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public  class  CartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private  Button cartthenextactivityhere;
    public TextView txtTotalAmount, txtMsg1;

    public final  int overTotalPrice = 0;
    String productID = "";
    String userID = "";
    DatabaseReference UserRef;

    String cartkey = "";
    String orderkey = "";
    String role;
    DatabaseReference UserDetailsRef;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseRecyclerAdapter adapter;
    TextView therealnumberoflikes;

     ViewHolder holders;

    //AUTHENTICATORS


    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ImageView cartimageonscreen;
    private ImageView cartproductimageonscreeen;
    private ImageView numberoflikesimage;
    String cartlistkey;
    DatabaseReference CartListRef;
    String traderoruser;
    String nameofproduct;
    String productid;
    public  String quantity;
    public  String image;
    public String price;
    String users;
    String time;
    String useridentifier;
    String customerid;
    String somerole;
    String key;
    String uid;
    public  int oneTyprProductTPrice;
    TextView cartquantity;
    String traderkey;
    String thetraderhere;
    String tradername;
    String thetraderimage;
    android.widget.ImageView thepicturebeingloaded;
    android.widget.ImageView  thetraderpicturebeingloaded;
    FirebaseDatabase myfirebasedatabase;
    DatabaseReference UsersRef;
    FirebaseUser user;
    String  numberoflikes;
    Query cartquery;
    Query thelikequery;
    String productkey;
    DatabaseReference myProducts;
    String date, desc, discount, name, photoid,pid, pimage, pname,tid, traderimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                (R.layout.stickynoterecycler));
        cartthenextactivityhere = (Button) findViewById(R.id.cartnextbutton);


        recyclerView = findViewById(R.id.stickyheaderrecyler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);

        }

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Cart Activity");
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
        }
        thepicturebeingloaded = (android.widget.ImageView) findViewById(R.id.cartproductimageonscreeen);
        thetraderpicturebeingloaded = (android.widget.ImageView) findViewById(R.id.cartimageonscreen);

        therealnumberoflikes = (TextView) findViewById(R.id.therealnumberoflikes);
        cartquantity = (TextView) findViewById(R.id.cartquantity);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();


            myfirebasedatabase = FirebaseDatabase.getInstance();
            UsersRef = myfirebasedatabase.getReference().child("Users");
            UsersRef.keepSynced(true);
            CartListRef = myfirebasedatabase.getReference().child("Cart");
            CartListRef.keepSynced(true);
            myProducts = myfirebasedatabase.getReference().child("Products");
            myProducts.keepSynced(true);


            fetch();

        //     recyclerView.setAdapter(adapter);

        //    if (recyclerView != null) {
        //       recyclerView.setAdapter(adapter);
        //   }




            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            if (mGoogleApiClient != null) {

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            }

            if (mGoogleApiClient != null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(CartActivity.this,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
            }


            // USER

            cartthenextactivityhere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartactivityintent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    if (cartactivityintent != null) {
                        cartactivityintent.putExtra("totalprice", overhypenumber);
                        startActivity(cartactivityintent);
                    }
                }
            });


        }}


    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView carttheproductname;
        public TextView carttheproductprice;
        public TextView carttradernamehere;
        public TextView cartdescriptionhere;
        public TextView cartquantity;
        public  TextView  therealnumberoflikes;


        public android.widget.ImageView cartimageonscreen;
        public android.widget.ImageView cartproductimageonscreeen;
        public android.widget.ImageView numberoflikesimage;
        public ItemClickListner listner;

        public ViewHolder(View itemView) {
            super(itemView);
            carttheproductname = itemView.findViewById(R.id.carttheproductname);
            carttheproductprice = itemView.findViewById(R.id.carttheproductprice);
            carttradernamehere = itemView.findViewById(R.id.carttradernamehere);
            cartdescriptionhere = itemView.findViewById(R.id.cartdescriptionhere);
            cartquantity = itemView.findViewById(R.id.cartquantity);

            //cartimage referst to the trader of the product
            cartimageonscreen = itemView.findViewById(R.id.cartimageonscreen);
            cartproductimageonscreeen = itemView.findViewById(R.id.cartproductimageonscreeen);
            numberoflikesimage = itemView.findViewById(R.id.numberoflikesimage);
            therealnumberoflikes =  itemView.findViewById(R.id.therealnumberoflikes);

        }

        public void setItemClickListner(ItemClickListner listner) {
            this.listner = listner;
        }

        public void setcartproductname(String cartproductname) {

            carttheproductname.setText(cartproductname);
        }

        public void setproductprice(String price) {

            carttheproductprice.setText(price);
        }

        public void settradername(String tradername) {

            carttradernamehere.setText(tradername);
        }


        public void setcartdescriptionhere(String cartdescription) {

            cartdescriptionhere.setText(cartdescription);
        }


        public void setcartquantity(String quantity) {

            cartquantity.setText(quantity);
        }


        public void setImage(final Context ctx, final String image) {
        cartproductimageonscreeen = (android.widget.ImageView) itemView.findViewById(R.id.cartproductimageonscreeen);
                                 if (image != null) {
                                     if (cartproductimageonscreeen != null) {

                                      //
                                         Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(cartproductimageonscreeen, new Callback() {


                                             @Override
                                             public void onSuccess() {

                                             }

                                             @Override
                                             public void onError(Exception e) {
                                                 Picasso.get().load(image).resize(100, 0).into(cartproductimageonscreeen);
                                             }


                                         });

                                     }}
        }

       public void setTraderImage(final Context ctx, final String image) {
            final android.widget.ImageView cartimageonscreen = (android.widget.ImageView) itemView.findViewById(R.id.cartimageonscreen);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(cartimageonscreen, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(cartimageonscreen);
                }


            });


        }

        public void setNumberofimage(final Context ctx, final String image) {
            final android.widget.ImageView numberoflikesimage = (android.widget.ImageView) itemView.findViewById(R.id.numberoflikesimage);

            Picasso.get().load(image).resize(400, 0).networkPolicy(NetworkPolicy.OFFLINE).into(numberoflikesimage, new Callback() {


                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(image).resize(100, 0).into(numberoflikesimage);
                }


            });


        }




}

    private void fetch() {


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Cart");
        if (query != null) {

            FirebaseRecyclerOptions<Cart> options =
                    new FirebaseRecyclerOptions.Builder<Cart>()
                            .setQuery(query, new SnapshotParser<Cart>() {


                                @NonNull
                                @Override
                                public Cart parseSnapshot(@NonNull DataSnapshot snapshot) {


                                    if (snapshot.child("pid").getValue() != null) {
                                        pid = snapshot.child("pid").getValue(String.class);
                                    }
                                    if (snapshot.child("tid").getValue() != null) {
                                        tid = snapshot.child("tid").getValue(String.class);
                                    }

                                    if (snapshot.child("quantity").getValue() != null) {
                                        quantity = snapshot.child("quantity").getValue(String.class);
                                    }
                                    if (snapshot.child("price").getValue() != null) {
                                        price = snapshot.child("price").getValue(String.class);
                                    }

                                    if (snapshot.child("desc").getValue() != null) {
                                        desc = snapshot.child("desc").getValue(String.class);
                                    }


                                    if (snapshot.child("pimage").getValue() != null) {
                                        pimage = snapshot.child("pimage").getValue(String.class);
                                    }
                                    if (snapshot.child("pname").getValue() != null) {
                                        pname = snapshot.child("pname").getValue(String.class);
                                    }


                                    if (snapshot.child("discount").getValue() != null) {
                                        discount = snapshot.child("discount").getValue(String.class);
                                    }

                                    if (snapshot.child("name").getValue() != null) {
                                        name = snapshot.child("name").getValue(String.class);
                                    }


                                    if (snapshot.child("image").getValue() != null) {
                                        image = snapshot.child("image").getValue(String.class);
                                    }

                                    if (snapshot.child("tradername").getValue() != null) {
                                        tradername = snapshot.child("tradername").getValue(String.class);
                                    }

                                    if (snapshot.child("traderimage").getValue() != null) {
                                        traderimage = snapshot.child("traderimage").getValue(String.class);
                                    }


                                    return new Cart(pid, tid, quantity, price, desc, pimage, pname, discount, name, image, tradername, traderimage);


                                }

                            }).build();


            adapter = new FirebaseRecyclerAdapter<Cart, ViewHolder>(options) {
                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.cart_items_layout, parent, false);

                    return new ViewHolder(view);
                }


                @Override
                protected void onBindViewHolder(final ViewHolder holder, final int position, final Cart model) {

                         holders = holder;
                    holder.carttheproductname.setText(pname);
                    holder.carttheproductprice.setText("Price = " + price + "$");
                    holder.cartdescriptionhere.setText(desc);
                    holder.cartquantity.setText(quantity);
                    holder.carttradernamehere.setText(tradername);
                    myfirebasedatabase = FirebaseDatabase.getInstance();

                    myProducts = myfirebasedatabase.getReference().child("Products");
                    myProducts.keepSynced(true);
                    Query firebasequery =  myfirebasedatabase.getReference().child("Products").orderByChild("pid").equalTo(pid);

                    firebasequery.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {



                               productkey  = dataSnapshot1.getKey();
                                Log.d(TAG, "Datasnapshot productkey " + productkey);

                                Log.d(TAG, "UserID here  " + uid );
                                if (dataSnapshot1.child("pid").getValue() != null) {
                                    numberoflikes = dataSnapshot1.child("pid").getValue(String.class);
                                    holders.therealnumberoflikes.setText(numberoflikes);
                                    Log.d(TAG, "The number of likes " + numberoflikes);



                                      }
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    key = model.getpid();
                    traderkey = model.gettid();

                    if (thepicturebeingloaded != null) {
                        Picasso.get().load(pimage).placeholder(R.drawable.profile).into(thepicturebeingloaded);
                    }

                    if (thetraderpicturebeingloaded != null) {
                        Picasso.get().load(traderimage).placeholder(R.drawable.profile).into(thetraderpicturebeingloaded);
                    }


                    holder.setImage(getApplicationContext(), pimage);
                    holder.setTraderImage(getApplication(), traderimage);




                    if (holder != null) {
                        holder.carttheproductname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (role.equals("Trader")) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    if (intent != null) {
                                        intent.putExtra("pid", key);
                                        intent.putExtra("fromthehomeactivitytraderkey", traderkey);
                                        intent.putExtra("fromthehomeactivityname", name);
                                        intent.putExtra("fromthehomeactivityprice", price);
                                        intent.putExtra("fromthehomeactivitydesc", desc);
                                        intent.putExtra("fromthehomeactivityname", thetraderhere);
                                        intent.putExtra("fromthehomeactivityimage", pimage);

                                    }
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    if (intent != null) {
                                        intent.putExtra("fromthehomeactivitytoproductdetails", traderkey);
                                    }
                                    startActivity(intent);
                                }
                            }
                        });

                    }

                     oneTyprProductTPrice = ((Integer.valueOf(price))) * Integer.valueOf(quantity);
                    Integer.valueOf(overTotalPrice) = overTotalPrice + oneTyprProductTPrice;


                    productID = pid;

                    if (holder != null) {
                        holder.carttradernamehere.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (role.equals("Trader")) {
                                    Intent intent = new Intent(CartActivity.this, TraderProfile.class);
                                    intent.putExtra("pid", key);
                                    intent.putExtra("fromhomeactivitytotraderprofile", traderkey);

                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(CartActivity.this, TraderProfile.class);
                                    intent.putExtra("pid", key);
                                    intent.putExtra("fromhomeactivitytotraderprofile", traderkey);

                                    startActivity(intent);
                                }
                            }
                        });
                    }


                }

            };
            recyclerView.setAdapter(adapter);
        }
        ;


    }



    @Override
    protected void onStart() {
        super.onStart();


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user =  mAuth.getCurrentUser();
                if (user != null) {
                    customerid = "";
                    traderoruser = user.getUid();
                }

                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
            }
        };

        adapter.startListening();
        if (mAuth != null) {
            mAuth.addAuthStateListener(firebaseAuthListener);
        }



    }


    @Override
    protected void onStop () {
        super.onStop();
        adapter.stopListening();
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
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.addnewproducthere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, AdminAddNewProductActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminAddNewProductActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.singeuserorderhere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, ViewYourPersonalProduct.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, ViewYourPersonalProduct.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.viewbuyershere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, ViewSpecificUsersCart.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, ViewSpecificUsersCart.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.usercartedactivityhere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, ViewAllCarts.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, ViewAllCarts.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.newproductdetailshere) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, AdminProductDetails.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminProductDetails.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.Maintainnewproducts) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, AdminMaintainProductsActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminMaintainProductsActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allcategorieshere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, AdminCategoryActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminCategoryActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allproductshere) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", customerid);
                            intent.putExtra("role", role);
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
            if (!role.equals("Trader")) {

                Intent intent = new Intent(CartActivity.this, com.simcoder.bimbo.CustomerMapActivity.class);
                if (intent != null) {
                    intent.putExtra("roledhomeactivitytocustomermapactivity", role);
                    intent.putExtra("fromhomeactivitytocustomermapactivity", customerid);
                    startActivity(intent);
                    finish();
                }
            } else {

                Intent intent = new Intent(CartActivity.this, DriverMapActivity.class);
                if (intent != null) {
                    intent.putExtra("rolefromhomeactivitytodrivermapactivity", role);
                    intent.putExtra("fromhomeactivitytodrivermapactivity", customerid);
                    startActivity(intent);
                    finish();
                }
            }


        }


        if (id == R.id.checkfeed) {
            if (!role.equals("Trader")) {

                Intent intent = new Intent(CartActivity.this, InstagramHomeActivity.class);
                if (intent != null) {
                    intent.putExtra("roledhomeactivitytocustomermapactivity", role);
                    intent.putExtra("fromhomeactivitytocustomermapactivity", customerid);
                    startActivity(intent);
                    finish();
                }
            } else {

                Intent intent = new Intent(CartActivity.this, InstagramHomeActivity.class);
                if (intent != null) {
                    intent.putExtra("rolefromhomeactivitytodrivermapactivity", role);
                    intent.putExtra("fromhomeactivitytodrivermapactivity", customerid);
                    startActivity(intent);
                    finish();
                }
            }


        }
        if (id == R.id.nav_cart) {
            if (!role.equals("Trader")) {
                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            }

        }
        //   checkfeed

        if (id == R.id.viewproducts) {
            if (!role.equals("Trader")) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }

        }
        if (id == R.id.nav_search) {
            if (!role.equals("Trader")) {
                Intent intent = new Intent(CartActivity.this, SearchProductsActivity.class);
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
                    mGoogleSignInClient.signOut().addOnCompleteListener(CartActivity.this,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                }
            }
            Intent intent = new Intent(CartActivity.this, com.simcoder.bimbo.MainActivity.class);
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }

        if (id == R.id.nav_settings) {
            if (!role.equals("Trader")) {
                Intent intent = new Intent(CartActivity.this, com.simcoder.bimbo.WorkActivities.SettinsActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }
        if (id == R.id.nav_history) {
            if (!role.equals("Trader")) {
                Intent intent = new Intent(CartActivity.this, HistoryActivity.class);
                if (intent != null) {
                    startActivity(intent);
                }
            } else {
            }
        }
        if (id == R.id.nav_categories) {

        }
        if (id == R.id.nav_viewprofilehome) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, CustomerProfile.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, TraderProfile.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
        if (id == R.id.viewallcustomershere) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.addnewproducthere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminAddNewProductActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }

        //   checkfeed
        if (id == R.id.singeuserorderhere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, ViewYourPersonalProduct.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.viewbuyershere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, ViewSpecificUsersCart.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.usercartedactivityhere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, ViewAllCarts.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.newproductdetailshere) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminProductDetails.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.Maintainnewproducts) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminMaintainProductsActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allcategorieshere) {

            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminCategoryActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }


        if (id == R.id.allproductshere) {
            if (!role.equals("Trader")) {
                if (FirebaseAuth.getInstance() != null) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String cusomerId = "";

                        cusomerId = user.getUid();
                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
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

                        Intent intent = new Intent(CartActivity.this, AdminAllCustomers.class);
                        if (intent != null) {
                            intent.putExtra("traderorcustomer", cusomerId);
                            intent.putExtra("role", role);
                            startActivity(intent);
                        }
                    }
                }
            }
        }

 /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
   */
        return true;
    }

}
