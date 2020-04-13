package com.simcoder.bimbo.instagram.Profile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.dialogs.ConfirmPasswordDialog;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;
import com.simcoder.bimbo.instagram.Models.UserSettings;
import com.simcoder.bimbo.instagram.*;
import com.simcoder.bimbo.instagram.Share.ShareActivity;
import com.simcoder.bimbo.instagram.Utils.FirebaseMethods;
import com.simcoder.bimbo.instagram.Utils.UniversalImageLoader;
import com.simcoder.bimbo.instagram.dialogs.ConfirmPasswordDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment implements
        ConfirmPasswordDialog.OnConfirmPasswordListener {

    @Override
    public void onConfirmPassword(String password) {
        if (FirebaseAuth.getInstance() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.

            AuthCredential credential = EmailAuthProvider
                    .getCredential(mAuth.getCurrentUser().getEmail(), password);

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User re-authenticated.");

                                // Check if email is a new field in database
                                mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                        if (task.isSuccessful()) {
                                            try {
                                                if (task.getResult().getProviders().size() == 1) {
                                                    Log.d(TAG, "onComplete: that email is already in use");
                                                    Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.d(TAG, "onComplete: That email is available");

                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    final String email = mEmail.getText().toString();

                                                    // email is available for update actions
                                                    user.updateEmail(email)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Log.d(TAG, "User email address updated.");
                                                                        mFirebaseMethods.updateEmail(email);
                                                                    }
                                                                }
                                                            });
                                                }
                                            } catch (NullPointerException e) {
                                                Log.e(TAG, "onComplete: NullPointerException " + e.getMessage());
                                            }
                                        }
                                    }
                                });


                            } else {
                                Log.d(TAG, "onComplete: re-authentication failed.");
                            }

                        }
                    });

        }
    }

    private static final String TAG = "EditProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;
    String userid;

    //Edit Profile Fragment Widgets
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;

    // vars
    private UserSettings mUserSettings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mDisplayName = (EditText) view.findViewById(R.id.display_name);
        mUsername = (EditText) view.findViewById(R.id.username);
        mWebsite = (EditText) view.findViewById(R.id.website);
        mDescription = (EditText) view.findViewById(R.id.description);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPhoneNumber = (EditText) view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        if (getActivity() != null) {
            mFirebaseMethods = new FirebaseMethods(getActivity());
        }

        //setProfileImage();
        setupFirebaseAuth();

        //back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        if (backArrow != null) {
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: navigating back to ProfileActivity");
                    if (getActivity() != null) {
                        getActivity().finish();

                    }
                }
            });
        }

        // green checkmark icon to update user settings information
        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChanges);
        if (checkmark != null) {
            checkmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: attempting to save changes.");
                    saveProfileSettings();

                }
            });
        }
        return view;

    }


    /**
     * Retrieves the data from widgets and submits it to database
     * Checks that username is unique
     */

    private void saveProfileSettings() {
        if (mDisplayName.getText() != null) {
            final String displayName = mDisplayName.getText().toString();

        if (mUsername.getText() != null) {
            final String username = mUsername.getText().toString();

        if (mDescription.getText() != null) {
            final String description = mDescription.getText().toString();

        if (mWebsite.getText() != null) {
            final String website = mWebsite.getText().toString();

        if (mEmail.getText() != null) {
            final String email = mEmail.getText().toString();

        if (mPhoneNumber.getText() != null) {
            final String phoneNumber = mPhoneNumber.getText().toString();


            // case1: if user changed to same name.
            if (!mUserSettings.getUser().getname().equals(username)) {
                checkIfUsernameExists(username);
            }
            // case2: if user change their email
            if (!mUserSettings.getUser().getemail().equals(email)) {
                //step1 Re-Auth
                //      - Confirm password and email
                ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                if (dialog != null) {
                    if (getFragmentManager() != null) {
                        dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
                    }

                    dialog.setTargetFragment(EditProfileFragment.this, 1);
                    //step2 Check if email already exists


                    //      - 'fetchProvidersForEmail(String email)
                    //step3 change email
                    //      - submit new email to database and authentication

                }
            }

            /**
             * Change fields in Settings that doesn't require unique values
             */

            if (displayName != null) {
                        if (mFirebaseMethods != null){

                            if (mUserSettings != null){


                if (!mUserSettings.getSettings().getname().equals(displayName)) {
                    mFirebaseMethods.updateUserAccountSettings(displayName, null, null, null);
                }
                              if (website != null){
                if (!mUserSettings.getSettings().getWebsite().equals(website)) {
                    mFirebaseMethods.updateUserAccountSettings(null, website, null, null);
                }
                                if (description != null) {
                                    if (!mUserSettings.getSettings().getdesc().equals(description)) {
                                        mFirebaseMethods.updateUserAccountSettings(null, website, description, null);
                                    }
                                }
                                if (phoneNumber != null) {
                                    if (!mUserSettings.getSettings().getimage().equals(phoneNumber)) {
                                        mFirebaseMethods.updateUserAccountSettings(null, website, null, phoneNumber);
                                    }
                                }
            }
        }}}}
}}}}}}
    /**
     * Check if @param username already exists in database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        if (username != null) {
            Log.d(TAG, "checkIfUsernameExists: Checking if " + username + " already exists");
            if (FirebaseAuth.getInstance() != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    userid = "";
                    userid = user.getUid();
                    if (FirebaseDatabase.getInstance() != null) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        if (username != null) {
                            Query query = reference
                                    .child("Users").child("Customers")
                                    .orderByChild("name")
                                    .equalTo(username);
                            if (query != null) {
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {
                                            if (!dataSnapshot.exists()) {

                                                // add the username
                                                if (mFirebaseMethods != null) {
                                                    if (username != null) {
                                                        mFirebaseMethods.updateUsername(username);
                                                        Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                        if (singleSnapshot != null) {
                                                            if (singleSnapshot.exists()) {
                                                                if (singleSnapshot.child(userid).child("name").getValue(String.class) != null) {
                                                                    Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH" + singleSnapshot.child(userid).child("name").getValue(String.class));
                                                                    Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
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
                }
            }
        }
    }
    private void setProfileWidgets(UserSettings userSettings) {
//        Log.d(TAG, "SetProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
//        Log.d(TAG, "SetProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getEmail());
//        Log.d(TAG, "SetProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getUser().getPhone_number());


        mUserSettings = userSettings;
        //User user = userSettings.getUser();

        if (userSettings != null) {
            UserAccountSettings settings = userSettings.getSettings();
            if (settings != null) {
                UniversalImageLoader.setImage(settings.getimage(), mProfilePhoto, null, "");
                if (mDisplayName != null) {
                    mDisplayName.setText(settings.getname());
                    if (mUsername != null) {
                        mUsername.setText(settings.getname());
                        if (mWebsite != null) {
                            mWebsite.setText(settings.getWebsite());
                            if (mDescription != null) {
                                mDescription.setText(settings.getdesc());
                                if (mEmail != null) {
                                    mEmail.setText(userSettings.getUser().getemail());
                                    if (mPhoneNumber != null) {
                                        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getphone()));
                                    }
                                }
                            }
                        }
                    }
                }

                if (mChangeProfilePhoto != null) {
                    mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: changing profile photo");
                            Intent intent = new Intent(getActivity(), ShareActivity.class);
                        if (intent != null) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //268435456
                            if (getActivity() != null) {
                                getActivity().startActivity(intent);
                                if (getActivity() != null) {
                                    getActivity().finish();

                                }
                            }
                        }}
                    });
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
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        if (FirebaseAuth.getInstance() != null) {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            if (mFirebaseDatabase != null) {
                myRef = mFirebaseDatabase.getReference();
                if (mAuth.getCurrentUser() != null) {
                    userID = mAuth.getCurrentUser().getUid();


                    mAuthListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                            if (firebaseAuth != null) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
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
                        }
                    };
                    if (myRef != null) {
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //retrieve user information from the database
                                      if (dataSnapshot != null){
                                if (mFirebaseMethods != null) {
                                    setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));
                                    //retrieve images for the user in question
                                }
                            }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError){
                                               }
                        });
                    }
                }
            }
        }
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
            if (mAuth != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
    }

}