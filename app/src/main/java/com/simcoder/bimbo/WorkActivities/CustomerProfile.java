package com.simcoder.bimbo.WorkActivities;


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
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.R;
import com.squareup.picasso.Picasso;

public class CustomerProfile extends AppCompatActivity  implements  View.OnClickListener{
    private Button ViewBuyers;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";
    String role;
    String traderID;
    String customerkey;
    String  customerimage;
    String coverimage;
    String customerquote;
    Button customerfollowbutton;

    String customerfollowers;
    String customername;
    String customerphoneaddress;
    String customerjob;

    Query myproductsdetails;
    DatabaseReference mDatabaseLikeCount;
    DatabaseReference mDatabaseCustomerFollowers;

    ImageView customerimageonscreen;
    ImageView customercoverprofile;
    TextView customerquotes;
    TextView customerprofilename;
    TextView customerprofilejob;
    TextView customernumberoffollowers;
    TextView customerprofilephoneadress;
    String followersID;


    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    DatabaseReference mDatabaseCustomer;
    DatabaseReference LikeRef;
    Boolean mProcessLike;
    String userID;
    boolean increment;
    Button message;
    Button call;
    Users usershere;
    String customerkeywehave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerprofile);
        if (getIntent() != null) {
            productID = getIntent().getStringExtra("pid");
        }
        customerimageonscreen = (ImageView) findViewById(R.id.customerimageonscreen);
        customercoverprofile = (ImageView) findViewById(R.id.customercoverprofile);
        customerquotes = (TextView) findViewById(R.id.customerquotes);
        customerprofilename = (TextView) findViewById(R.id.customerprofilename);
        customerprofilejob = (TextView) findViewById(R.id.customerprofilejob);
        customernumberoffollowers = (TextView) findViewById(R.id.customernumberoffollowers);
        customerfollowbutton = (Button) findViewById(R.id.customerfollowbutton);

        // FROM ADMINVIEWUSERS HERE
        {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().get("rolefromviewbuyertocustomerprofilehere") != null) {
                    role = getIntent().getExtras().get("rolefromviewbuyertocustomerprofilehere").toString();
                }
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromadminvuewbuyertocustomerprofilehere");
        }


        if (getIntent() != null) {
            userID = getIntent().getStringExtra("fromviewbuyerusertocustomerprofilehere");
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(com.simcoder.bimbo.WorkActivities.CustomerProfile.this,
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
        getCustomerInformation();

        mDatabaseCustomer = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");

        mDatabaseCustomer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    if (dataSnapshot != null) {
                        dataSnapshot.getValue(Users.class);

                        if (userID != null) {
                            customerkey = dataSnapshot.child(userID).getKey();
                            customerimage = dataSnapshot.child(userID).child("image").getValue(String.class);
                            coverimage = dataSnapshot.child(userID).child("coverimage").getValue(String.class);
                            customerquote = dataSnapshot.child(userID).child("quote").getValue(String.class);

                            customerfollowers = dataSnapshot.child(userID).child("followers").child("number").getValue(String.class);
                            customername = dataSnapshot.child(userID).child("name").getValue(String.class);
                            customerphoneaddress = dataSnapshot.child(userID).child("address").getValue(String.class);
                            customerjob = dataSnapshot.child(userID).child("job").getValue(String.class);
                        }


                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        // WE HAVE TO ADD ALL PROFILE DETAILS AND PROFILE IMAGES AND COVERS AS WELL

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            followersID = "";
            followersID = user.getUid();


        if (customerfollowbutton != null) {
            if (mAuth != null){
            customerfollowbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userID != null) {
                        mDatabaseCustomerFollowers = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID);
                        if (mDatabaseCustomerFollowers != null) {
                            mDatabaseCustomerFollowers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        dataSnapshot.getValue(Users.class);

                                        if (dataSnapshot.child("followers") != null && dataSnapshot.child("followers").hasChild("number")) {
                                            Log.i("Number of followers", ".");
                                            mDatabaseCustomerFollowers.child("followers").child(followersID).removeValue();
                                            updateCounter(false);
                                            mProcessLike = false;
                                        } else {
                                            Log.i("Number of follwowers", "Followers liked");
                                            mDatabaseCustomerFollowers.child("followers").child(followersID).setValue(mAuth.getCurrentUser().getUid());
                                            updateCounter(true);
                                            Log.i(dataSnapshot.getKey(), dataSnapshot.getChildrenCount() + "Count");
                                            mProcessLike = false;
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
                    Boolean increments;

                private void updateCounter(final Boolean increment) {
                    this.increments = increment;
                    DatabaseReference mDatabaseLikeCount;
                    mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(userID).child("followers").child("number");
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
    }}}
    @Override
    protected void onStart() {
        super.onStart();

        getCustomerInformation();
    }


    private void getCustomerInformation() {

        if (mDatabaseCustomer != null) {
            mDatabaseCustomer.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot != null) {
                            dataSnapshot.getValue(Users.class);
                             if (userID != null){
                                 customerkeywehave=   dataSnapshot.child(userID).getValue(String.class);


                            customerimageonscreen.setImageResource(Integer.parseInt(customerimage));
                            customercoverprofile.setImageResource(Integer.parseInt(coverimage));


                            customerquotes.setText(customerquote);
                            customerprofilename.setText(customername);
                            customerprofilejob.setText(customerjob);
                            customerprofilephoneadress.setText(customerphoneaddress);

                            customernumberoffollowers.setText(customerfollowers);

                            Picasso.get().load(customerimage).into(customerimageonscreen);

                            Picasso.get().load(coverimage).into(customercoverprofile);


                        }


                    }
                }}
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

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
