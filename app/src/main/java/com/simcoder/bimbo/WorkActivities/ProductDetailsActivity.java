package com.simcoder.bimbo.WorkActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.simcoder.bimbo.Model.Products;
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity
{
    private Button addToCartButton;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName, tradername;
    private String productID = "", state = "Normal";
    String cartkey;
    String orderkey;
    String thetraderkey;
     String thenameofthetrader;
    String description;

    String traderoruser= "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        tradername = (TextView)findViewById(R.id.thetrader);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(ProductDetailsActivity.this,
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


        Intent intent = new Intent(ProductDetailsActivity.this, ConfirmFinalOrderActivity.class);

        intent.putExtra("cartkey", cartkey);
        if (getIntent() != null) {
            orderkey = getIntent().getStringExtra("orderkey");

            getProductDetails(productID);

            if (addToCartButton != null) {
                addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                            Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                        } else {
                            addingToCartList();
                        }
                    }
                });
            }
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());
            final String userID ="";
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        cartkey = cartListRef.push().getKey();

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
        final DatabaseReference cartintoproductListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child(cartkey).child("Users").child(userID).child("products");
        String productinputedkey = cartintoproductListRef.push().getKey();
         cartListRef.child(cartkey)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            cartintoproductListRef.child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Product").child(productID);
        DatabaseReference traderinformationreference = FirebaseDatabase.getInstance().getReference().child("Product").child(productID);

        if (traderinformationreference != null) {
            traderinformationreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.child("trader") != null) {
                    }
                     if(thetraderkey != null){
                    thetraderkey = dataSnapshot.child("trader").getKey();
                    thenameofthetrader =  dataSnapshot.child("trader").child(thetraderkey).child("name").getValue().toString();
                    description = dataSnapshot.child("desc").getValue().toString();
                }

                ;}


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            productsRef.child(productID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        final Products products = dataSnapshot.getValue(Products.class);

                        productName.setText(products.getname());
                        productPrice.setText(products.getprice());
                        productDescription.setText(description);
                        tradername.setText(thenameofthetrader);
                        Picasso.get().load(products.getimage()).into(productImage);

                        tradername.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                {
                                    Intent intent = new Intent(ProductDetailsActivity.this, TraderProfile.class);
                                    intent.putExtra("pid", products.getpid());
                                    intent.putExtra("fromhomeactivitytotraderprofile", thetraderkey);
                                    intent.putExtra("fromhomeactivitytotraderprofile",thetraderkey);
                                    startActivity(intent);
                                }


                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void CheckOrderState() {
        DatabaseReference ordersRef;
        String userID = "";
        if (orderkey != null) {
            ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderkey);

            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String shippingState = dataSnapshot.child("state").getValue().toString();

                        if (shippingState.equals("shipped")) {
                            state = "Order Shipped";
                        } else if (shippingState.equals("not shipped")) {
                            state = "Order Placed";
                        }
                    }
                }

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

}
