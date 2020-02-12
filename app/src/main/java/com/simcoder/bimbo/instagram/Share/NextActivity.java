package com.simcoder.bimbo.instagram.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.FirebaseMethods;
import com.simcoder.bimbo.instagram.Utils.UniversalImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    DatabaseReference PosttypeDatabase;

       String mposttype;
    //widgets
    private EditText mCaption;
    RadioGroup mRadioGroup;

    //vars
    private String mAppend = "file:/";
    private int imageCount = 0;
    private String imgUrl;
    EditText nameofproduct;
    EditText priceofproductedittext;
    String nametextviewofthisproduct;
    String pricetextviewofthisproduct;

    TextView thenameoftheproduct;
    TextView priceoftheproduct;
    String theposttypekey;
    private Bitmap bitmap;
    private Intent intent;
    LinearLayout sellerlayoutdetails;
    public NextActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mFirebaseMethods = new FirebaseMethods(NextActivity.this);
        mCaption = (EditText) findViewById(R.id.caption);
        mRadioGroup = (RadioGroup)findViewById(R.id.postradiogroup);

        setupFirebaseAuth();

        ImageView backArrow = (ImageView) findViewById(R.id.ivBackArrow);
        sellerlayoutdetails = (LinearLayout)findViewById(R.id.selldetailbox);
        if (backArrow != null) {
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: closing the activity");

                    finish();
                }
            });

        }
      //  posttypequote
        //        posttypesell
     //   posttypead
       //         postradiogroup
        TextView share = (TextView) findViewById(R.id.tvShare);
         nameofproduct = (EditText)findViewById(R.id.nameofproductedittext);
         priceofproductedittext = (EditText)findViewById(R.id.priceofproducteditext);
        thenameoftheproduct = (TextView)findViewById(R.id.nameoftheproducttextview);
        priceoftheproduct = (TextView)findViewById(R.id.priceoftheproducttextview);

        if (share != null) {
            share.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: navigating to the final share screen");
                    //upload image to firebase
                    Toast.makeText(NextActivity.this, "Attempting to upload new photo", Toast.LENGTH_SHORT).show();
                     if (mCaption.getText() != null) {
                         if (imageCount != 0){
                         String caption = mCaption.getText().toString();
                                  if (intent != null) {
                                      if (intent.hasExtra(getString(R.string.selected_image))) {
                                         if (imgUrl != null){
                                          imgUrl = intent.getStringExtra(getString(R.string.selected_image));
                                              if (mFirebaseMethods != null){
                                          mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo), caption, imageCount, imgUrl, null);

                                      }}} else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                                          if (intent != null){
                                          bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
                                         if (mFirebaseMethods != null){
                                                                     if (caption != null){
                                                 if (bitmap != null) {

                                                     mFirebaseMethods.uploadNewPhoto(getString(R.string.new_photo), caption, imageCount, null, bitmap);
                                                 }
                                      }}}}}}
                                  }}

            });
        }

        PosttypeDatabase = FirebaseDatabase.getInstance().getReference().child("Photos");
         if (PosttypeDatabase != null) {
             theposttypekey = PosttypeDatabase.getKey();
             setImage();
         }}

    private void someMethod() {

        /**
         * Step 1) Create a data model for Photos
         * Step 2) Add properties to the Photo Objects (caption, date, imageURL, photo_id, tags, user_id)
         * Step 3) Count the number of pre-uploaded photos for a user
         * Step 4)
         * a) Upload photo and insert two new nodes in the Firebase Database
         * b) insert into 'photo' node
         * c) insert into 'user_photos' node
         */
    }

    /**
     * Gets image url from incoming intent and displays selected image
     */
    private void setImage() {
        intent = getIntent();
        ImageView image = (ImageView) findViewById(R.id.imageShare);

        if (intent.hasExtra(getString(R.string.selected_image))) {
            if (intent != null) {
                imgUrl = intent.getStringExtra(getString(R.string.selected_image));
                Log.d(TAG, "setImage: got new image url " + imgUrl);
                if (imgUrl != null) {
                    if (mAppend != null) {
                        UniversalImageLoader.setImage(imgUrl, image, null, mAppend);

                    } else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                        if (intent != null) {
                            if (bitmap != null) {
                                bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
                                Log.d(TAG, "setImage: got a new bitmap");
                                image.setImageBitmap(bitmap);

                            }
                        }
                    }
                }
            }
        }
    }
     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */

// THE FOLLOWING TO SEND THE CELL
    ///MERGE PRODUCT WITH SAME DETAILS


    //AFTER FIRST TIME OF CREATING INFO HE CAN HAVE NO ABILITY TO ALTER UNLESS PROVIDED BY ADMINISTRATOR
    // PERSONAL INFORMATION COULD BE CROSS-CHECKED FOR SECURITY
    // IT IS THE PAYMENT THAT MAKES IT DECENTRALIZED

    private void setRadioButtonInformation() {

        if (mRadioGroup != null) {
            int selectId = mRadioGroup.getCheckedRadioButtonId();

            final RadioButton radioButton = findViewById(selectId);
            if (radioButton != null) {
                if (radioButton.getText() == null) {
                    Toast.makeText(getApplicationContext(), "radio button not checked", Toast.LENGTH_LONG).show();
                    return;
                }
                if (radioButton != null) {
                    if (radioButton.isChecked() && radioButton.getText() == "Sell") {
                        // selldetailbox
                            if (sellerlayoutdetails != null){

                        sellerlayoutdetails.setVisibility(View.VISIBLE);

                        if (nameofproduct == null && priceofproductedittext == null) {
                       if (thenameoftheproduct != null){
                            thenameoftheproduct.setVisibility(View.INVISIBLE);
                     if (priceoftheproduct != null){
                            priceoftheproduct.setVisibility(View.INVISIBLE);
                        } else {
                         Button settotextview = (Button) findViewById(R.id.settotextview);
                         if (settotextview != null) {
                              settotextview.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     if (nameofproduct != null){
                                  if (nameofproduct.getText() != null) {
                                      String nameofthisproduct = nameofproduct.getText().toString();
                                    if (priceofproductedittext != null){
                                    if (priceofproductedittext.getText() != null){
                                      String priceofthisproduct = priceofproductedittext.getText().toString();
                                             if (nameofthisproduct != null){
                                      thenameoftheproduct.setText(nameofthisproduct);
                                      if (priceofthisproduct != null){

                                      priceoftheproduct.setText(priceofthisproduct);
                                      if (thenameoftheproduct != null){
                                      thenameoftheproduct.setVisibility(View.VISIBLE);
                                       if (priceofthisproduct != null){
                                      priceoftheproduct.setVisibility(View.VISIBLE);
                                        if (thenameoftheproduct.getText() != null){
                                              if (thenameoftheproduct.getText() != null){
                                      nametextviewofthisproduct = thenameoftheproduct.getText().toString();
                                       if (priceoftheproduct != null) {
                                            if (pricetextviewofthisproduct != null){
                                                if (pricetextviewofthisproduct != null){
                                           pricetextviewofthisproduct = priceoftheproduct.getText().toString();
                                       }}
                                        }}}}}}}
                                 }}}}}
                             });


                         }

                     }
                    } else {
                        sellerlayoutdetails.setVisibility(View.INVISIBLE);
                    }

                    mposttype = radioButton.getText().toString();

                    if (mposttype != null) {
                        Map userInfo = new HashMap();
                        if (userInfo != null){
                        userInfo.put("posttype", mposttype);
                        userInfo.put("name", nametextviewofthisproduct);
                        userInfo.put("price", pricetextviewofthisproduct);

                        if (theposttypekey != null){
                        if (userInfo != null){
                        PosttypeDatabase.child(theposttypekey).updateChildren(userInfo);


                    }}}}
                } else {

                    Toast.makeText(getApplicationContext(), "Please provide details", Toast.LENGTH_LONG).show();
                }

            }
        }
    }}}}
    // HOW

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mAuth = FirebaseAuth.getInstance();
         if (FirebaseDatabase.getInstance() != null) {
             mFirebaseDatabase = FirebaseDatabase.getInstance();
             myRef = mFirebaseDatabase.getReference();
           if (imageCount !=0){
             Log.d(TAG, "onDataChange: image count: " + imageCount);
              if (mAuthListener != null){
             mAuthListener = new FirebaseAuth.AuthStateListener() {
                 @Override
                 public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                     FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();


                     if (user != null) {
                         // User is signed in
                         Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                     } else {
                         // User is signed out
                         Log.d(TAG, "onAuthStateChanged:signed_out");
                     }
                     // ...
                 }
             };
         }}}

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                 if (imageCount != 0) {
                    if (mFirebaseMethods != null) {
                        imageCount = mFirebaseMethods.getImageCount(dataSnapshot);
                        Log.d(TAG, "onDataChange: image count: " + imageCount);


                    }
                }}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
      if (mAuth != null){
       if (mAuthListener != null){
        mAuth.addAuthStateListener(mAuthListener);
    }}}

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
        if (mAuth != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}}
