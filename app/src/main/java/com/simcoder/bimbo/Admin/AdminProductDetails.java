package com.simcoder.bimbo.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.Model.Products;
import com.simcoder.bimbo.R;
import com.squareup.picasso.Picasso;

public class AdminProductDetails extends AppCompatActivity  implements  View.OnClickListener{
    private Button ViewBuyers;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";
    String role;
    String traderID;
    Query myproductsdetails;
    DatabaseReference mDatabaseLikeCount;


    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    DatabaseReference productsRef;
    DatabaseReference LikeRef;
    Boolean mProcessLike;
    String userID;
    boolean increment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminproductdetailslayout);

        productID = getIntent().getStringExtra("pid");


        TextView ViewCustomers = (Button) findViewById(R.id.ViewCustomers);
        ElegantNumberButton adminproductimagelikebutton = (ElegantNumberButton) findViewById(R.id.adminproductimagelikebutton);
        ImageView adminproductdetailsimage = (ImageView) findViewById(R.id.adminproductdetailsimage);
        TextView adminproductimageproductname = (TextView) findViewById(R.id.adminproductimageproductname);
        TextView adminproductimagedescription = (TextView) findViewById(R.id.adminproductimagedescription);
        TextView adminproductimagenumberoflikes = (TextView) findViewById(R.id.adminproductimagenumberoflikes);


         // FROM ALL PRODUCTS
        {
            if (getIntent().getExtras().get("rolefromsingleusertoadminproductdetails") != null) {
                role = getIntent().getExtras().get("rolefromsingleusertoadminproductdetails").toString();
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromadminsingleusertoadminproductdetails");
        }
        if (getIntent() != null) {
            userID = getIntent().getStringExtra("neworderUserID");
        }



         // FROM ALL PRODUCTS

        if (getIntent() != null) {
            productID = getIntent().getStringExtra("productIDfromallproducttoproductdetails");
        }

        {
            if (getIntent().getExtras().get("rolefromallproductdetails") != null) {
                role = getIntent().getExtras().get("rolefromallproductdetails").toString();
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromtheallproducttoadmiproductdetails");
        }


         // FROM CART ACTIVITY
        if (getIntent() != null) {
            productID = getIntent().getStringExtra("fromusercartactivitydminproductdetails");
        }
        if (getIntent() != null) {
            userID = getIntent().getStringExtra("fromuserTHEIDcartactivitydminproductdetails");
        }

        {
            if (getIntent().getExtras().get("rolefromadmincartadminproductdetails") != null) {
                role = getIntent().getExtras().get("rolefromadmincartadminproductdetails").toString();
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromadmintcatactivitytoadminproductdetails");
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(com.simcoder.bimbo.Admin.AdminProductDetails.this,
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
                    traderID = "";
                    traderID = user.getUid();
                }

                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
            }
        };
        getProductDetails(productID);

        mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("Product").child(productID).child("Likes").child("count");


        if (ViewBuyers != null) {
            ViewBuyers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminProductDetails.this, AdminViewBuyersActivity.class);

                    intent.putExtra("productIDfromadminproductdetailstoviewbuyers", productID);

                    intent.putExtra("fromadminproductdetailstoviewbuyers", traderID);
                    intent.putExtra("rolefromadminproductdetailstoviewbuyers", role);

                    startActivity(intent);
                }
            });
        }

        adminproductimagelikebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeRef = FirebaseDatabase.getInstance().getReference().child("Products");
                if (LikeRef != null) {
                    LikeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                if (dataSnapshot.child(productID) != null && dataSnapshot.child(productID).child("Likes").hasChild(traderID)) {
                                    Log.i("Product Unliked", ".");
                                    LikeRef.child(productID).child("Likes").child(traderID).removeValue();
                                    updateCounter(false);
                                    mProcessLike = false;
                                } else {
                                    Log.i("Product Liked", "User Liked");
                                    LikeRef.child(productID).child("Likes").setValue(mAuth.getCurrentUser().getUid());
                                    updateCounter(true);
                                    Log.i(dataSnapshot.getKey(), dataSnapshot.getChildrenCount() + "Count");
                                    mProcessLike = false;
                                }
                            }
                            ;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }


            Boolean increments;

            private void updateCounter(final Boolean increment) {
                this.increments = increment;
                DatabaseReference mDatabaseLikeCount;
                mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("Product").child(productID).child("Likes").child("count");
                mDatabaseLikeCount.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() != null) {
                            int value = mutableData.getValue(Integer.class);
                            if (increment) {
                                value++;
                            } else {
                                value--;
                            }
                            mutableData.setValue(value);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b,
                                           DataSnapshot dataSnapshot) {
                        // Transaction completed
                        Log.d(TAG, "likeTransaction:onComplete:" + databaseError);
                    }
                });
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        getProductDetails(productID);
    }


    private void getProductDetails(String productID) {
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);


                    ImageView adminproductdetailsimage = (ImageView) findViewById(R.id.adminproductdetailsimage);
                    TextView adminproductimageproductname = (TextView) findViewById(R.id.adminproductimageproductname);
                    TextView adminproductimagedescription = (TextView) findViewById(R.id.adminproductimagedescription);
                    TextView adminproductimagenumberoflikes = (TextView) findViewById(R.id.adminproductimagenumberoflikes);


                    adminproductdetailsimage.setImageResource(Integer.parseInt(products.getimage()));
                    adminproductimageproductname.setText(products.getname());
                    adminproductimagedescription.setText(products.getdesc());
                    adminproductimagenumberoflikes.setText(products.getnumber());
                    Picasso.get().load(products.getimage()).into(adminproductdetailsimage);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        //     mProgress.hide();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
