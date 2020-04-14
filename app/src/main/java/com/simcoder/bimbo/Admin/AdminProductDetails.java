package com.simcoder.bimbo.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.location.LocationServices;
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

public class AdminProductDetails extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Button ViewBuyers;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";
    String role;
    String traderID;
    Query myproductsdetails;
    DatabaseReference mDatabaseLikeCount;
String    productdetailsimage;

            String productdetailsname;
    String productdetailsdescription;
            String productdetailsnumber;
    String LikeRefkey;
 String photokey;

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

   FirebaseDatabase myfirebasedatabase;
   FirebaseDatabase likesgetdatabase;
    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    DatabaseReference productsRef;
    DatabaseReference photodatabase;
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
                   Intent roleintent = getIntent();
                     if( roleintent.getExtras().getString("rolefromsingleusertoadminproductdetails") != null) {
                 role = roleintent.getExtras().getString("rolefromsingleusertoadminproductdetails");
                   }
                  /*
                    if (getIntent() != null) {
                        if (getIntent().getStringExtra("rolefromsingleusertoadminproductdetails") != null) {
                            role = getIntent().getStringExtra("rolefromsingleusertoadminproductdetails").toString();
                        }
*/
                     Intent traderintent = getIntent();
                   if( traderintent.getExtras().getString("fromadminsingleusertoadminproductdetails") != null) {
                       traderID = traderintent.getExtras().getString("fromadminsingleusertoadminproductdetails");
                     }
                   /*
                        if (getIntent().getStringExtra("fromadminsingleusertoadminproductdetails") != null) {
                            traderID = getIntent().getStringExtra("fromadminsingleusertoadminproductdetails");
                        }

                    */
        Intent userintent = getIntent();
        if( userintent.getExtras().getString("neworderUserID") != null) {
            userID = userintent.getExtras().getString("neworderUserID");
        }


        /*
                        if (getIntent().getStringExtra("neworderUserID") != null) {
                            userID = getIntent().getStringExtra("neworderUserID");
                        }
*/

                        // FROM ALL PRODUCTS
        Intent productIDintent = getIntent();
        if( productIDintent.getExtras().getString("productIDfromallproducttoproductdetails") != null) {
            productID = productIDintent.getExtras().getString("productIDfromallproducttoproductdetails");
        }


        /*                if (getIntent().getStringExtra("productIDfromallproducttoproductdetails") != null) {
                            productID = getIntent().getStringExtra("productIDfromallproducttoproductdetails");
                        }
*/
        Intent roleintentfromproduct = getIntent();
        if( roleintentfromproduct.getExtras().getString("rolefromallproductdetails") != null) {
            role = roleintentfromproduct.getExtras().getString("rolefromallproductdetails");
        }
      /*
        if (getIntent().getStringExtra("rolefromallproductdetails") != null) {
                            role = getIntent().getStringExtra("rolefromallproductdetails").toString();
                        }

*/


        Intent traderidfromallproducttoproductdetailsintent = getIntent();
        if( traderidfromallproducttoproductdetailsintent.getExtras().getString("fromtheallproducttoadmiproductdetails") != null) {
            traderID = traderidfromallproducttoproductdetailsintent.getExtras().getString("fromtheallproducttoadmiproductdetails");
        }

        /*
                        if (getIntent().getStringExtra("fromtheallproducttoadmiproductdetails") != null) {
                            traderID = getIntent().getStringExtra("fromtheallproducttoadmiproductdetails");
                        }
*/

                        // FROM CART ACTIVITY


        Intent productintent = getIntent();
        if( productintent.getExtras().getString("fromusercartactivitydminproductdetails") != null) {
            productID = productintent.getExtras().getString("fromusercartactivitydminproductdetails");
        }
                       /*
                        if (getIntent().getStringExtra("fromusercartactivitydminproductdetails") != null) {
                            productID = getIntent().getStringExtra("fromusercartactivitydminproductdetails");
                        }
                        */


        Intent useridintent = getIntent();
        if( useridintent.getExtras().getString("fromuserTHEIDcartactivitydminproductdetails") != null) {
            userID = useridintent.getExtras().getString("fromuserTHEIDcartactivitydminproductdetails");
        }
                      /*  if (getIntent().getStringExtra("fromuserTHEIDcartactivitydminproductdetails") != null) {
                            userID = getIntent().getStringExtra("fromuserTHEIDcartactivitydminproductdetails");
                        }
*/

        Intent userrole = getIntent();
        if( userrole.getExtras().getString("rolefromadmincartadminproductdetails") != null) {
            role = userrole.getExtras().getString("rolefromadmincartadminproductdetails");
        }
        /*
                        if (getIntent().getStringExtra("rolefromadmincartadminproductdetails") != null) {
                            role = getIntent().getStringExtra("rolefromadmincartadminproductdetails").toString();
                        }
*/
        Intent carttrader = getIntent();
        if( carttrader.getExtras().getString("fromadmintcatactivitytoadminproductdetails") != null) {
            traderID = carttrader.getExtras().getString("fromadmintcatactivitytoadminproductdetails");
        }
        /*
                        if (getIntent().getStringExtra("fromadmintcatactivitytoadminproductdetails") != null) {
                            traderID = getIntent().getStringExtra("fromadmintcatactivitytoadminproductdetails");
                        }
*/



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
        buildGoogleApiClient();
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
              if (productID != null) {
                  getProductDetails(productID);

     //   mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("Product").child(productID).child("Likes").child("count");

              }
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

                myfirebasedatabase = FirebaseDatabase.getInstance();
                likesgetdatabase = FirebaseDatabase.getInstance();



                 photodatabase = myfirebasedatabase.getReference().child("Photo");
                 photokey = photodatabase.getKey();


                LikeRef = likesgetdatabase.getReference().child("Photo").child(photokey).child("Likes");
                      LikeRefkey = LikeRef.getKey();
                if (LikeRef != null) {
                    LikeRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                if (dataSnapshot.child("uid").getValue(String.class) != null && dataSnapshot.hasChild(LikeRefkey)) {
                                    Log.i("Product Unliked", ".");
                                    LikeRef.child(LikeRefkey).removeValue();
                                    updateCounter(false);
                                    mProcessLike = false;
                                } else {
                                    Log.i("Product Liked", "User Liked");
                                    LikeRef.push().setValue(mAuth.getCurrentUser().getUid());
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
                mDatabaseLikeCount = FirebaseDatabase.getInstance().getReference().child("Photos").child(photokey).child("Likes").child("number");
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


    private void getProductDetails(final String productID) {
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        if (productID != null) {
            productsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                           if (dataSnapshot.child(productID).child("pimage").getValue(String.class) != null) {
                                productdetailsimage = dataSnapshot.child(productID).child("pimage").getValue(String.class);
                           }

                           if (dataSnapshot.child(productID).child("pname").getValue(String.class) != null) {
                               productdetailsname = dataSnapshot.child(productID).child("pname").getValue(String.class);
                           }

                           if (dataSnapshot.child(productID).child("desc").getValue(String.class) != null) {
                               productdetailsdescription = dataSnapshot.child(productID).child("desc").getValue(String.class);
                           }
                           if (dataSnapshot.child(productID).child("number").getValue(String.class) != null) {
                                productdetailsnumber = dataSnapshot.child(productID).child("number").getValue(String.class);
                           }

                        ImageView adminproductdetailsimage = (ImageView) findViewById(R.id.adminproductdetailsimage);
                        TextView adminproductimageproductname = (TextView) findViewById(R.id.adminproductimageproductname);
                        TextView adminproductimagedescription = (TextView) findViewById(R.id.adminproductimagedescription);
                        TextView adminproductimagenumberoflikes = (TextView) findViewById(R.id.adminproductimagenumberoflikes);


                        adminproductdetailsimage.setImageResource(Integer.parseInt(productdetailsimage));
                        adminproductimageproductname.setText(productdetailsname);
                        adminproductimagedescription.setText(productdetailsdescription);
                        adminproductimagenumberoflikes.setText(productdetailsnumber);
                        Picasso.get().load(productdetailsimage).into(adminproductdetailsimage);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(AdminProductDetails.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onClick(View v) {

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

    protected void onStop() {
        super.onStop();
        //     mProgress.hide();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

// #BuiltByGOD