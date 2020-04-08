package com.simcoder.bimbo.WorkActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
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
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.HistoryActivity;
import com.simcoder.bimbo.Model.Cart;
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ConfirmFinalOrderActivity extends AppCompatActivity
{
    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;
    String productIDHERE;
    String orderKey;
    String cartkey;
    String userID= "";
    String productImage;
    String productname;
    private String totalAmount = "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        cartkey = getIntent().getStringExtra("cartkey");
        totalAmount = getIntent().getStringExtra("Total Price");
        productIDHERE = getIntent().getStringExtra("pid");
        Toast.makeText(this, "Total Price =  $ " + totalAmount, Toast.LENGTH_SHORT).show();


        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shippment_name);
        phoneEditText = (EditText) findViewById(R.id.shippment_phone_number);
        addressEditText = (EditText) findViewById(R.id.shippment_address);
        cityEditText = (EditText) findViewById(R.id.shippment_city);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(ConfirmFinalOrderActivity.this,
                    new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }



                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE


          if (confirmOrderBtn != null) {
              confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Check();
                  }
              });
          }}



    private void Check()
    {
        if (TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your city name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }



    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());


                 final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders");


                 orderKey =ordersRef.push().getKey();
        final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(orderKey).child("Users").child(userID).child("products").child(productIDHERE);
        final DatabaseReference productImageRef = FirebaseDatabase.getInstance().getReference()
                .child("Product").child(productIDHERE);


        productImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // CURRENT USERNAME HERE
                         if (dataSnapshot.child("image").getValue() != null) {
                             productImage = dataSnapshot.child("image").getValue().toString();
                         }
                         if (dataSnapshot.child("name").getValue() != null) {
                             productname = dataSnapshot.child("name").getValue().toString();
                         }

                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        HashMap<String, Object> ordersMap = new HashMap<>();

        ordersMap.put("amount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid", productIDHERE);
        productMap.put("image", productImage);
        productMap.put("name", productname);
        productRef.updateChildren(productMap);


        Intent intent = new Intent(ConfirmFinalOrderActivity.this, CartActivity.class);
        intent.putExtra("orderkey", orderKey);

        // AFTER EVERYTHING IS SUCCESSFUL MOVE IT FROM CART TO TO ORDERS,
        //clear cut
        ordersRef.child(orderKey).updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {


            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {       if (cartkey != null) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List").child(cartkey)


                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "your final order has been placed successfully.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HistoryActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }            }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            userID = "";
                            userID = user.getUid();
                        }

                        // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                        // WHICH IS CUSTOMER TO BE ADDED.
                        // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                    }
                };


                if (mAuth != null) {
                    mAuth.addAuthStateListener(firebaseAuthListener);
                }
            }
        };
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth !=null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }





}
