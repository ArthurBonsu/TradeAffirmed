package com.simcoder.bimbo.WorkActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.Admin.AdminCategoryActivity;
import com.simcoder.bimbo.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword, InputEmail;
    private ProgressDialog loadingBar;
    DatabaseReference UserorTraderDatabase;
    DatabaseReference RegisterDatabase;

    String traderoruser= "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private TextView TraderLink, CustomerLink;
    private String parentDbName = "";

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton GoogleBtn;
    String role;
    String usertraderskey;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        CreateAccountButton = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
        InputEmail = (EditText)findViewById(R.id.register_email_input);

        TraderLink = (TextView) findViewById(R.id.mytrader_panel_link);
        CustomerLink = (TextView) findViewById(R.id.customer_panel_link);

        loadingBar = new ProgressDialog(this);
        GoogleBtn =findViewById(R.id.eccommercegoogleregister);
        GoogleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                signIn();
              }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(RegisterActivity.this,
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
                    traderoruser="";
                    traderoruser = user.getUid();
                }

                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
            }
        };

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateAccount();
            }
        });

        TraderLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountButton.setText("Create Account Trader");
                TraderLink.setVisibility(View.INVISIBLE);
                CustomerLink.setVisibility(View.VISIBLE);
                parentDbName = "Customer";
            }
        });

        CustomerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountButton.setText("Create Account Customer");
                TraderLink.setVisibility(View.VISIBLE);
                CustomerLink.setVisibility(View.INVISIBLE);
                parentDbName = "Driver";
            }
        });


    }

    private  void signIn(){
        //    signInIntent  = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user != null) {
                                traderoruser = "";
                                traderoruser = user.getUid();

                                final DatabaseReference RootRef;

                                RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
                                UserorTraderDatabase =  FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName);



                                usertraderskey = UserorTraderDatabase.push().getKey();


                                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {


                                                                           @Override
                                                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                               if (!dataSnapshot.child(parentDbName).child(traderoruser).exists()) {
                                                                                   //    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                                                                                   // GETTING THE TYPE OF TRADER


                                                                                   //  if (usersData.getPhone().equals(phone))
                                                                                   {
                                                                                       //    if (usersData.getPassword().equals(password))

                                                                                       {
                                                                                           if (parentDbName.equals("Drivers")) {
                                                                                               firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                                                                                   @Override
                                                                                                   public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                                                                                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                                                                                       if (user != null) {
                                                                                                           traderoruser = "";
                                                                                                           traderoruser = user.getUid();
                                                                                                           role="Trader";
                                                                                                           UserorTraderDatabase.child(traderoruser).setValue("role", role);

                                                                                                           if (FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName).child(traderoruser) != null && role.equals("Trader")) {




                                                                                                               Toast.makeText(RegisterActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                                                                                               loadingBar.dismiss();
                                                                                                               Intent intent = new Intent(RegisterActivity.this, AdminCategoryActivity.class);
                                                                                                               startActivity(intent);
                                                                                                               finish();

                                                                                                           }
                                                                                                       } else {

                                                                                                           loadingBar.dismiss();
                                                                                                           Toast.makeText(RegisterActivity.this, "Trader " + acct.getDisplayName() + "already exists", Toast.LENGTH_SHORT).show();

                                                                                                       }

                                                                                                       // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                                                                                       // WHICH IS CUSTOMER TO BE ADDED.
                                                                                                       // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE

                                                                                                   }

                                                                                                   ;
                                                                                               };

                                                                                               // Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                                                                               //   startActivity(intent);
                                                                                           } else if (parentDbName.equals("Customers")) {


                                                                                               firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                                                                                   @Override
                                                                                                   public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                                                                                       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                                                       if (user != null) {
                                                                                                           traderoruser = "";
                                                                                                           traderoruser = user.getUid();
                                                                                                           role="Customers";
                                                                                                           UserorTraderDatabase.child(traderoruser).setValue("role", role);


                                                                                                           if (FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName).child(traderoruser) != null && role.equals("Customer")); {

                                                                                                               Toast.makeText(RegisterActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                                                                                               loadingBar.dismiss();
                                                                                                               Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                                                                                               startActivity(intent);
                                                                                                               finish();

                                                                                                           }} else {
                                                                                                           loadingBar.dismiss();
                                                                                                           Toast.makeText(RegisterActivity.this, "User with" + acct.getDisplayName() + "already exist", Toast.LENGTH_SHORT).show();
                                                                                                           // WE HAVE TO UPDATE THE CURRENT UI AND GO TO THE NEXT ACTIVITY WHICH IS MAP

                                                                                                       }
                                                                                                   }







                                                                                               };
                                                                                           };





                                                                                           // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                                                                           // WHICH IS CUSTOMER TO BE ADDED.
                                                                                           // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                                                                                           ;





                                                                                           ;}}}}

                                                                           @Override
                                                                           public void onCancelled(DatabaseError databaseError) {

                                                                           }
                                                                       }
                                );
                            }



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
        String email = InputEmail.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, phone, password, email);
        }
    }



    private void ValidatephoneNumber(final String name, final String phone, final String password, final String email)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            traderoruser = "";
            traderoruser = user.getUid();

            final DatabaseReference RegRef;


            RegisterDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
            UserorTraderDatabase =  FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName);



            usertraderskey = UserorTraderDatabase.push().getKey();

            RegisterDatabase.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(parentDbName).child(traderoruser).exists()) {
                        //    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                        // GETTING THE TYPE OF TRADER


                        //  if (usersData.getPhone().equals(phone))
                        {
                            //    if (usersData.getPassword().equals(password))

                            {
                                if (parentDbName.equals("Drivers")) {
                                    firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                            if (user != null) {
                                                traderoruser = "";
                                                traderoruser = user.getUid();
                                                    role="Trader";
                                                UserorTraderDatabase.child(traderoruser).setValue("role", role);
                                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                                    userdataMap.put("phone", phone);
                                                    userdataMap.put("password", password);
                                                    userdataMap.put("name", name);
                                                    userdataMap.put("email", email);

                                                    RootRef.child("Users").child(parentDbName).child(traderoruser).updateChildren(userdataMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {
                                                                    if (task.isSuccessful())
                                                                    {
                                                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();

                                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                        startActivity(intent);
                                                                    }
                                                                    else
                                                                    {
                                                                        loadingBar.dismiss();
                                                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                                else
                                                {
                                                    Toast.makeText(RegisterActivity.this, "This " + user.getDisplayName() + " already exists.", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                    Toast.makeText(RegisterActivity.this, "Please try again using another email", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                                }
                                            };

                                            // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                            // WHICH IS CUSTOMER TO BE ADDED.
                                            // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                                        }



                                    // Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                    //   startActivity(intent);
                                 else if (parentDbName.equals("Customers")) {
                                    firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                            if (user != null) {
                                                traderoruser = "";
                                                traderoruser = user.getUid();
                                                role="Customers";
                                                UserorTraderDatabase.child(traderoruser).setValue("role", role);

                                                HashMap<String, Object> userdataMap = new HashMap<>();
                                                userdataMap.put("phone", phone);
                                                userdataMap.put("password", password);
                                                userdataMap.put("name", name);
                                                userdataMap.put("email", email);

                                                RootRef.child("Users").child(parentDbName).child(traderoruser).updateChildren(userdataMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if (task.isSuccessful())
                                                                {
                                                                    Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                                                    loadingBar.dismiss();

                                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                                else
                                                                {
                                                                    loadingBar.dismiss();
                                                                    Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                }

                                            }


                                        };
                                    };





                                    // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                    // WHICH IS CUSTOMER TO BE ADDED.
                                    // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                                    ;





                                    ;}}}}

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
        mAuth.removeAuthStateListener(firebaseAuthListener);

    }
}
