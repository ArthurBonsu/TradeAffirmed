package com.simcoder.bimbo.instagram.Login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
import com.simcoder.bimbo.instagram.Profile.ProfileActivity;
import com.simcoder.bimbo.R;

/**
 * Created by User on 6/19/2017.
 */

public class InstagramLoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private TextView mPleaseWait;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
     if (mContext != null) {
         mContext = InstagramLoginActivity.this;

         Log.d(TAG, "onCreate: started.");
     }
       if (mPleaseWait != null) {
           mPleaseWait.setVisibility(View.GONE);
       }
       if (mProgressBar != null) {
           mProgressBar.setVisibility(View.GONE);
       }
        setupFirebaseAuth();
        init();

    }

    private boolean isStringNull(String string){
        Log.d(TAG, "isStringNull: checking string if null.");

        if(string.equals("")){
            return true;
        }
        else{
            return false;
        }
    }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    private void init() {

        //initialize the button for logging in
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: attempting to log in.");
                    if (mEmail.getText() != null) {
                        String email = mEmail.getText().toString();

                        if (mPassword.getText() != null) {
                            String password = mPassword.getText().toString();


                            if (isStringNull(email) && isStringNull(password)) {
                                Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                            } else {
                                if (mProgressBar != null) {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                }
                                if (mPleaseWait != null) {
                                    mPleaseWait.setVisibility(View.VISIBLE);
                                }
                                if (mAuth != null)
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(InstagramLoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task != null) {
                                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                                    }
                                                    if (mAuth != null) {
                                                        FirebaseUser user = mAuth.getCurrentUser();

                                                        // If sign in fails, display a message to the user. If sign in succeeds
                                                        // the auth state listener will be notified and logic to handle the
                                                        // signed in user can be handled in the listener.
                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "signInWithEmail:failed", task.getException());

                                                            Toast.makeText(InstagramLoginActivity.this, getString(R.string.auth_failed),
                                                                    Toast.LENGTH_SHORT).show();
                                                            if (mProgressBar != null) {
                                                                mProgressBar.setVisibility(View.GONE);
                                                            }
                                                            if (mPleaseWait != null) {
                                                                mPleaseWait.setVisibility(View.GONE);
                                                            }
                                                        } else {
                                                            try {
                                                                      if (user != null) {
                                                                          if (user.isEmailVerified()) {
                                                                              Log.d(TAG, "onComplete: success. email is verified.");
                                                                              Intent intent = new Intent(InstagramLoginActivity.this, InstagramHomeActivity.class);
                                                                              startActivity(intent);
                                                                          } else {
                                                                              Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                                                                              if (mProgressBar != null) {
                                                                                  mProgressBar.setVisibility(View.GONE);
                                                                              }
                                                                              if (mPleaseWait != null) {
                                                                                  mPleaseWait.setVisibility(View.GONE);
                                                                              }
                                                                              if (mAuth != null) {
                                                                                  mAuth.signOut();
                                                                              }}
                                                                      }
                                                            } catch (NullPointerException e) {
                                                                Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage());
                                                                }
                                                        }
                                                    }

                                                    // ...
                                                }
                                            });
                            }
                        }
                    }

                }
            });

        }
        TextView linkSignUp = (TextView) findViewById(R.id.link_signup);
        if (linkSignUp != null) {
            linkSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: navigating to register screen");
                    Intent intent = new Intent(InstagramLoginActivity.this, InstagramRegisterActivity.class);
                    startActivity(intent);
                }
            });
        }
         /*
         If the user is logged in then navigate to HomeActivity and call 'finish()'
          */
        if (mAuth != null) {
            if (mAuth.getCurrentUser() != null) {

                Intent intent = new Intent(InstagramLoginActivity.this, InstagramHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        if (mAuth != null) {
            mAuth = FirebaseAuth.getInstance();
            if (mAuthListener != null) {
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth != null) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (user != null) {
                                // User is signed in
                                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                            } else {
                                // User is signed out
                                Log.d(TAG, "onAuthStateChanged:signed_out");
                            }
                            // ...
                        }
                    }

                    ;

            };}}
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null) {
            if (mAuthListener != null) {
                mAuth.addAuthStateListener(mAuthListener);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
        if (mAuth != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }}
}

