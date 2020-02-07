package com.simcoder.bimbo.instagram.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simcoder.bimbo.instagram.Models.UserSettings;
import com.simcoder.bimbo.instagram.Home.InstagramHomeActivity;
import com.simcoder.bimbo.instagram.Models.Photo;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;
import com.simcoder.bimbo.instagram.Models.UserSettings;
import com.simcoder.bimbo.instagram.Profile.AccountSettingsActivity;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.FilePaths;
import com.simcoder.bimbo.instagram.Utils.ImageManager;
import com.simcoder.bimbo.instagram.Utils.StringManipulation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private String userID;
    int mPostsCount;
    TextView mPosts;
    int count;

    //vars
    private Context mContext;
    private double mPhotoUploadProgress = 0;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public void uploadNewPhoto(String photoType, final String caption, final int count, final String imgUrl, Bitmap bm) {
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        FilePaths filePaths = new FilePaths();
        //case1) new photo
        if (photoType.equals(mContext.getString(R.string.new_photo))) {
            Log.d(TAG, "uploadNewPhoto: uploading NEW photo.");

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference storageReference = mStorageReference
                        .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1));

                //convert image url to bitmap
                if (bm == null) {
                    bm = ImageManager.getBitmap(imgUrl);
                }

                byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

                UploadTask uploadTask = null;
                uploadTask = storageReference.putBytes(bytes);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                        Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                        //add the new photo to 'photos' node and 'user_photos' node
                        addPhotoToDatabase(caption, firebaseUrl.toString());

                        //navigate to the main feed so the user can see their photo
                        Intent intent = new Intent(mContext, InstagramHomeActivity.class);
                        mContext.startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Photo upload failed.");
                        Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        if (progress - 15 > mPhotoUploadProgress) {
                            Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                            mPhotoUploadProgress = progress;
                        }

                        Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                    }
                });

            }
            //case new profile photo
            else if (photoType.equals(mContext.getString(R.string.profile_photo))) {
                Log.d(TAG, "uploadNewPhoto: uploading new PROFILE photo");

                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                StorageReference storageReference = mStorageReference
                        .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/profile_photo");

                //convert image url to bitmap
                if (bm == null) {
                    bm = ImageManager.getBitmap(imgUrl);
                }
                byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

                UploadTask uploadTask = null;
                uploadTask = storageReference.putBytes(bytes);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri firebaseUrl = taskSnapshot.getDownloadUrl();

                        Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();

                        //insert into 'user_account_settings' node
                        setProfilePhoto(firebaseUrl.toString());

                        ((AccountSettingsActivity) mContext).setViewPager
                                (((AccountSettingsActivity) mContext).pagerAdapter
                                        .getFragmentNumber(mContext.getString(R.string.edit_profile_fragment))
                                );

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Photo upload failed.");
                        Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        if (progress - 15 > mPhotoUploadProgress) {
                            Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                            mPhotoUploadProgress = progress;
                        }

                        Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                    }
                });
            }
        }
    }


    private void setProfilePhoto(String url) {
        Log.d(TAG, "setProfilePhoto: setting new profile image: " + url);

        myRef.child("Users").child("Customers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("image")
                .setValue(url);
    }

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }


    private void addPhotoToDatabase(String caption, String url) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");

        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = myRef.child("Photos").push().getKey();
        String productkey = myRef.child("Product").push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setdate(getTimestamp());

        photo.setimage(url);
        photo.setTags(tags);
        photo.setuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setphotoid(newPhotoKey);

        //insert into database
        myRef.child("Products")
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child(newPhotoKey).setValue(photo);
        myRef.child("Photos").child(newPhotoKey).setValue(photo);
        HashMap theproductshashmap = new HashMap();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            theproductshashmap.put("desc", caption);
            theproductshashmap.put("image", url);
            //You have to provide the price and the name of the product before you caption it
            theproductshashmap.put("name", caption);
            theproductshashmap.put("pid", newPhotoKey);
            theproductshashmap.put("price", "");
            theproductshashmap.put("tid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            theproductshashmap.put("time", getTimestamp());


            myRef.child("Products")
                    .child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).child(newPhotoKey).setValue(photo);
        }
        if (myRef != null) {
            myRef.child("Products").child(newPhotoKey).updateChildren(theproductshashmap);
        }
    }

    public int getImageCount(DataSnapshot dataSnapshot) {
        count = 0;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Photos").orderByChild("tid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "onDataChange: found posts:" + singleSnapshot.child("posts").getValue());
                    count = Integer.parseInt(dataSnapshot.child("posts").getValue().toString());
                    count++;
                }
                mPosts.setText(String.valueOf(mPostsCount));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        return count;


    }

    /**
     * Update 'user_account_settings' node for the current user
     *
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, String phoneNumber) {

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if (displayName != null) {
            myRef.child("Users")
                    .child("Drivers")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name")
                    .setValue(displayName);
        }


        if (website != null) {
            myRef.child("Users")
                    .child("Drivers")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("website")
                    .setValue(website);
        }

        if (description != null) {
            myRef.child("Users").child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("desc")
                    .setValue(description);
        }

        if (phoneNumber != null) {
            myRef.child("Users").child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone")
                    .setValue(phoneNumber);
        }
    }

    /**
     * update username in the 'users' node and 'user_account_settings' node
     *
     * @param username
     */
    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: upadting username to: " + username);

        myRef.child("Users")
                .child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("name")
                .setValue(username);

   /*     myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    */
    }


    /**
     * update the email in the 'user's' node
     *
     * @param email
     */
    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: upadting email to: " + email);

        myRef.child("Users")
                .child("Drivers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email")
                .setValue(email);

    }

//    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot){
//        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists.");
//
//        User user = new User();
//
//        for (DataSnapshot ds: datasnapshot.child(userID).getChildren()){
//            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);
//
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());
//
//            if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
//                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getUsername());
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Register a new email and password to Firebase Authentication
     *
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        } else if (task.isSuccessful()) {
                            //send verificaton email
                            sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }

    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Add information to the users nodes
     * Add information to the user_account_settings node
     *
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo) {

        User user = new User(email, StringManipulation.condenseUsername(username), description, website, profile_photo);

        UserAccountSettings settings = new UserAccountSettings(description, profile_photo, StringManipulation.condenseUsername(username), website, userID);


        myRef.child("Users").child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user);


   /*     myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);
*/
    }


    /**
     * Retrieves the account settings for teh user currently logged in
     * Database: user_acount_Settings node
     *
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {

        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        final UserAccountSettings settings = new UserAccountSettings();
        final User user = new User();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Users");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                     String thesettingskey =         ds.getKey();
                    try {
                        // user_account_settings node
                        if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                            Log.d(TAG, "getUserAccountSettings: user account settings node datasnapshot: " + ds);

                            String displayinfo = String.valueOf(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue(UserAccountSettings.class).getname());

                            String websiteinfo = String.valueOf(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue(UserAccountSettings.class).getWebsite());

                            String description = ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("desc").getValue(UserAccountSettings.class).getdesc();

                            String image = String.valueOf(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").getValue(UserAccountSettings.class).getimage());

                            String following = String.valueOf(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").getValue(UserAccountSettings.class).getFollowing());
                            String followers = String.valueOf(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("fofllowing").getValue(UserAccountSettings.class).getFollowing());



                            settings.setname(displayinfo);
                            settings.setWebsite(websiteinfo);
                            settings.setdesc(description);
                            settings.setimage(image);
                            settings.setFollowing(following);
                            settings.setFollowers(followers);
                            settings.setPosts(
                                    ds.child("Photos")
                                            .getValue(UserAccountSettings.class)
                                            .getPosts()
                            );



                            Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
                            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                                user.setname(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name")
                                                .getValue(User.class)
                                                .getname()
                                );

                                user.setemail(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email")
                                                .getValue(User.class)
                                                .getemail()
                                );
                                user.setphone(ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("phone")
                                                .getValue(User.class)
                                                .getphone()
                                );
                                user.setuid(
                                        ds.child(thesettingskey).child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid")
                                                .getValue(User.class)
                                                .getuid()
                                );

                                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
                            }

                        }
                        Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                    } catch (Exception e) {
                        Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                    }


                }




    }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
return new UserSettings(user, settings);

         }}





/*
        for(DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: user account settings node datasnapshot: " + ds);

                try {

                    settings.setname(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setname(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }


            // users node
            Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);

    }
*/

