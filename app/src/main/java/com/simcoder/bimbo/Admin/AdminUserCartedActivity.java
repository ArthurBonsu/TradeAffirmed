package  com.simcoder.bimbo.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import  com.simcoder.bimbo.Model.Cart;
import  com.simcoder.bimbo.R;
import com.simcoder.bimbo.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EventListener;

public class AdminUserCartedActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;
     private Query mQueryTraderandUserCart;
    private String userID = "";
    String  traderID ="";
    Query QueryUser;
    String  role;
    String productkey;
    String getimage;
    DatabaseReference myreferencetoimage;

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

//
    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


   ImageView admincartimageofprouct;

    TextView         admincarttitlehere;
    TextView admincartquantity;
    TextView admincart_price;


    // HAS TO BE ORDERED BY ADMINID
// THE ADMIN CAN CHECK FOR A PARTICULAR USERS PRODUCT BOUGHT
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cart_recycler);




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



        productsList = findViewById(R.id.theadmincartrecycler);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminUserCartedActivity.this,
                    new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }
        buildGoogleApiClient();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    traderID="";
                    traderID = user.getUid();
                }

                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
            }
        };

        // WE HAVE TO PUT AUTHENTICATIONS ON CARTS
        //WE PICK THE CURRENT ORDER

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List");

             if (traderID != null) {
                 mQueryTraderandUserCart = cartListRef.orderByChild(traderID);
                 if (userID != null) {
                     QueryUser = mQueryTraderandUserCart.orderByChild(userID);
                 }
             }
                    if(QueryUser != null) {
                        myreferencetoimage = QueryUser.getRef().child("User").child("products");
                    }
            if (myreferencetoimage != null) {
                myreferencetoimage.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                            productkey = dataSnapshot.getKey();
                            if (productkey != null) {
                                getimage = dataSnapshot.child(productkey).child("image").getValue(String.class);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }
        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();




    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(QueryUser, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, AdminCartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, AdminCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCartViewHolder holder, int position, @NonNull Cart model) {
                if (model != null) {
                    holder.admincartimageofprouct.setImageResource(Integer.parseInt(getimage));
                    holder.admincartquantity.setText("Quantity = " + model.getquantity());
                    holder.admincart_price.setText("Price " + model.getprice() + "$");
                    holder.admincarttitlehere.setText(model.getname());

                    if (admincartimageofprouct != null) {
                        Glide.with(getApplication()).load((getimage)).into(admincartimageofprouct);
                    }
                }
            }
                @NonNull
                @Override
                public AdminCartViewHolder onCreateViewHolder (@NonNull ViewGroup parent,int viewType)
                {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admincartitemlayout, parent, false);
                    AdminCartViewHolder holder = new AdminCartViewHolder(view);
                    return holder;
                }
            };


            if (productsList !=null) {
                productsList.setAdapter(adapter);

          //  if (adapter != null) {
       //         adapter.startListening();
       //    // }
                    }
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(AdminUserCartedActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class AdminCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public ImageView admincartimageofprouct;

        public TextView admincarttitlehere;
        public TextView admincartquantity;
        public TextView admincart_price;


        public AdminCartViewHolder(View itemView) {
            super(itemView);




            admincartimageofprouct = itemView.findViewById(R.id.admincartimageofprouct);
            admincarttitlehere = itemView.findViewById(R.id.admincarttitlehere);
            admincartquantity = itemView.findViewById(R.id.admincartquantity);
            admincart_price = itemView.findViewById(R.id.admincart_price);


        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // VIEWPAGER BUTTONS

                case R.id.admincartimageofprouct:
                    Intent intent = new Intent(AdminUserCartedActivity.this, AdminProductDetails.class);
                    intent.putExtra("rolefromadmincartadminproductdetails", role);
                    intent.putExtra("fromadmintcatactivitytoadminproductdetails", traderID);
                    intent.putExtra("fromusercartactivitydminproductdetails", userID);
                    intent.putExtra("fromusercartactivitydminproductdetails", productkey);

                    startActivity(intent);

                    break;
                case R.id.admincarttitlehere:
                    Intent viewingtheproductactivityhere = new Intent(AdminUserCartedActivity.this, AdminProductDetails.class);
                    viewingtheproductactivityhere.putExtra("rolefromadmincartadminproductdetails", role);
                    viewingtheproductactivityhere.putExtra("fromadmintcatactivitytoadminproductdetails", traderID);
                    viewingtheproductactivityhere.putExtra("fromuserTHEIDcartactivitydminproductdetails", userID);
                    viewingtheproductactivityhere.putExtra("fromusercartactivitydminproductdetails", productkey);

                    startActivity(viewingtheproductactivityhere);

                    // GRIDVIEW BUTTONS
                    break;

            }
        }


        private void RemoveCartshere(String uID) {
            if (myreferencetoimage.child(uID) != null) {
                myreferencetoimage.child(uID).removeValue();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        //     mProgress.hide();
        if (mAuth !=  null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}


// #BuiltByGOD