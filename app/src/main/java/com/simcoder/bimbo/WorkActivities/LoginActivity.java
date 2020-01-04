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
import com.simcoder.bimbo.Admin.AdminCategoryActivity;
import com.simcoder.bimbo.CustomerLoginActivity;
import com.simcoder.bimbo.CustomerMapActivity;
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.simcoder.bimbo.R;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity
{
    private EditText InputPhoneNumber, InputPassword, Emailaccess;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink, ForgetPasswordLink;

    private String parentDbName = "";


    private CheckBox chkBoxRememberMe;

    String traderoruser= "";
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String role;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton GoogleBtn;

    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        Emailaccess = (EditText) findViewById(R.id.login_email_input);

        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        ForgetPasswordLink = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);

        GoogleBtn = findViewById(R.id.eccommercegooglelogin);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        // I HAVE TO BUILD A GOOGLE BUTTON ON TOP OF THE USERNAME
        GoogleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                signIn();

            }
        });

        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(LoginActivity.this,
                    new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }


        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        // FORGET PASSWORD PROBLEMS
        ForgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Trader");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Customer";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
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
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
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


                                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child(parentDbName).child(traderoruser).exists()) {
                                            //    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                                            // GETTING THE TYPE OF TRADER
                                            role = dataSnapshot.child(parentDbName).child(traderoruser).child("role").getValue().toString();

                                            //  if (usersData.getPhone().equals(phone))
                                            {
                                                //    if (usersData.getPassword().equals(password))

                                                {
                                                    if (parentDbName.equals("Drivers") && role.equals("Trader")) {
                                                        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                                            @Override
                                                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                                                if (user != null) {
                                                                    traderoruser = "";
                                                                    traderoruser = user.getUid();

                                                                    if (FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName).child(traderoruser) != null && role.equals("Trader")) {
                                                                         Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();
                                                                        Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                                                        startActivity(intent);
                                                                        finish();

                                                                    }
                                                                } else {

                                                                    loadingBar.dismiss();
                                                                    Toast.makeText(LoginActivity.this, "Trader " + acct.getDisplayName() + "does does not exist", Toast.LENGTH_SHORT).show();

                                                                }

                                                                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                                                // WHICH IS CUSTOMER TO BE ADDED.
                                                                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE

                                                            }

                                                            ;
                                                        };

                                                        // Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                                        //   startActivity(intent);
                                                    } else if (parentDbName.equals("Customers") && role.equals("Customers") ) {


                                                        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                                            @Override
                                                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                if (user != null) {
                                                                    traderoruser = "";
                                                                    traderoruser = user.getUid();

                                                                    if (FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName).child(traderoruser) != null && role.equals("Customer")); {

                                                                        Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                                                        loadingBar.dismiss();
                                                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                                        startActivity(intent);
                                                                        finish();

                                                                         }} else {
                                                                                    loadingBar.dismiss();
                                                                                    Toast.makeText(LoginActivity.this, "User with" + acct.getDisplayName() + "does does not exist", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
// HERE  WE CAN DO THE GOOGLE IMPLEMENTATION HERE

    private void LoginUser()
    {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();
       String email =Emailaccess.getText().toString();
        // WE HAVE TO BUILD AN EMAIL BUTTON


        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your your email...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password, email);
        }


    }



    private void AllowAccessToAccount(final String phone, final String password, final String email) {
        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            traderoruser = "";
            traderoruser = user.getUid();

            final DatabaseReference RootRef;

            RootRef = FirebaseDatabase.getInstance().getReference().child("Users");


            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(parentDbName).child(traderoruser).exists()) {
                        //    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                        // GETTING THE TYPE OF TRADER
                        role = dataSnapshot.child(parentDbName).child(traderoruser).child("role").getValue().toString();

                        //  if (usersData.getPhone().equals(phone))
                        {
                            //    if (usersData.getPassword().equals(password))

                            {
                                if (parentDbName.equals("Drivers") && role.equals("Trader")) {
                                    firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                            if (user != null) {
                                                traderoruser = "";
                                                traderoruser = user.getUid();

                                                if (FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName).child(traderoruser) != null && role.equals("Trader")) {

                                                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "signInWithEmail:success");
                                                                String user_id = mAuth.getCurrentUser().getUid();

                                                                Toast.makeText(LoginActivity.this, "Welcome Trader, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                                                loadingBar.dismiss();

                                                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                                return;
                                                            } else {
                                                                Toast.makeText(LoginActivity.this, "Trader with" + user.getDisplayName() + "does does not exist", Toast.LENGTH_SHORT).show();
                                                                // WE HAVE TO UPDATE THE CURRENT UI AND GO TO THE NEXT ACTIVITY WHICH IS MAP

                                                            }
                                                        }
                                                    });
                                                }
                                            }

                                            // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                            // WHICH IS CUSTOMER TO BE ADDED.
                                            // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                                        }
                                    };


                                    // Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                    //   startActivity(intent);
                                } else if (parentDbName.equals("Customers") && role.equals("Customers")) {
                                    Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (user != null) {
                                                traderoruser = "";
                                                traderoruser = user.getUid();

                                                if (FirebaseDatabase.getInstance().getReference().child("Users").child(parentDbName).child(traderoruser) != null && role.equals("Customer")) {

                                                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                                            if (task.isSuccessful()) {
                                                                Log.d(TAG, "signInWithEmail:success");
                                                                String user_id = mAuth.getCurrentUser().getUid();

                                                                Toast.makeText(LoginActivity.this, "Welcome Trader, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                                                loadingBar.dismiss();

                                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                                finish();


                                                                return;
                                                            } else {
                                                                loadingBar.dismiss();
                                                                Toast.makeText(LoginActivity.this, "User with" + user.getDisplayName() + "does does not exist", Toast.LENGTH_SHORT).show();
                                                                // WE HAVE TO UPDATE THE CURRENT UI AND GO TO THE NEXT ACTIVITY WHICH IS MAP

                                                            }
                                                        }
                                                    });

                                                }

                                            }


                                        }
                                    };





                                    // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                                    // WHICH IS CUSTOMER TO BE ADDED.
                                    // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
                            ;





                                    ;}}}}}

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