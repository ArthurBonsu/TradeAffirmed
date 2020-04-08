package  com.simcoder.bimbo.Admin;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.simcoder.bimbo.Model.Products;
import com.simcoder.bimbo.R;

import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.WorkActivities.ProductDetailsActivity;
import com.simcoder.bimbo.WorkActivities.TraderProfile;
import com.simcoder.bimbo.instagram.Profile.ProfileActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pee on 8/5/2016.
 */

public class AdminAddNewProductActivityII extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int GALLERY_REQUEST2 = 2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private ImageButton mEventImage;
    private EditText mEventtitle;
    private EditText mEventDescription;
    private EditText mEventDate;

    String mPostKey;
    String churchkey;
    private Button msubmitButton;
    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCHURCHCHOSEN;
    private ProgressDialog mProgress;
    private FirebaseAuth Auth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private DatabaseReference ProductsTraderRef;
    private ProgressDialog loadingBar;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String traderID;
    String role;
    String traderkeryhere;
    FirebaseUser user;
    Uri ImageStore;
    Intent intent;
    String date;
    String time;
    String userid;
    String productkey;
    String mytraderimage;

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ImageView imagetobesetto;
    ImageButton setimagebutton;
    ImageButton AddimageButon;

   FirebaseDatabase productsfirebasedatabase;
   String titleval;
   String descval;
    String  price;
    String tradername;
    String myphotoimage;
    String traderid;
    String pimage;


    public AdminAddNewProductActivityII() {
        super();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

              Intent sendcategory = getIntent();
              if( sendcategory.getExtras().getString("category") != null) {
                CategoryName = sendcategory.getExtras().getString("category");
            }
            // KEYS PASSED IN FROM ADMINCATEGORY
                  Intent roleintent = getIntent();

            if (roleintent.getExtras().getString("rolefromadmincategorytoaddadmin") != null) {
                role = roleintent.getExtras().getString("rolefromadmincategorytoaddadmin");
            }
                Intent fromaddadmincategorytrader = getIntent();
            if (fromaddadmincategorytrader.getExtras().getString("fromadmincategoryactivitytoaddadmin") != null){
                traderID = fromaddadmincategorytrader.getExtras().getString("fromadmincategoryactivitytoaddadmin");
            }



        AddimageButon = (ImageButton) findViewById(R.id.addingtoproductsbutton);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);

        msubmitButton = (Button) findViewById(R.id.add_new_product);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        user = mAuth.getCurrentUser();
        if (user != null) {
            traderID = "";
            traderID = user.getUid();
        }
        if (ProductsRef.push() != null) {
            productRandomKey = ProductsRef.push().getKey();

        }

        mStorage = FirebaseStorage.getInstance().getReference().child("product_images");
        productsfirebasedatabase = FirebaseDatabase.getInstance();
        ProductsRef = productsfirebasedatabase.getReference().child("Product");

        ProductsRef.keepSynced(true);

        //I have to  check to ensure that gallery intent is not placed here for the other classes
        mProgress = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminAddNewProductActivityII.this,
                    new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    }).addApi(com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API, gso).build();
        }
        buildGoogleApiClient();




    }


    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(AdminAddNewProductActivityII.this)
                    .addOnConnectionFailedListener(AdminAddNewProductActivityII.this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }


        AddimageButon.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                                 galleryIntent.setType("image/*");
                                                 startActivityForResult(galleryIntent, GALLERY_REQUEST);

                                             }


                                         });


        msubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startPosting();
                } catch (Exception e) {

                }
            }
        });

    }







    private void startPosting() {
      titleval = InputProductName.getText().toString();
      descval = InputProductDescription.getText().toString();
       price = InputProductPrice.getText().toString();
        user = mAuth.getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");

        if (currentDate != null) {
            saveCurrentDate = currentDate.format(calendar.getTime()).toString();

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            if (currentTime != null) {
                saveCurrentTime = currentTime.format(calendar.getTime());

            }


            if (!TextUtils.isEmpty(titleval) && !TextUtils.isEmpty(descval) && !TextUtils.isEmpty(price) && mImageUri != null) {
                mProgress.setMessage("Adding your new Product To the List");

                mProgress.show();
                StorageReference filepath = mStorage.child(mImageUri.getLastPathSegment());

                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                          traderid = user.getUid();
                                          tradername = user.getDisplayName();
                                          pimage = downloadUrl.toString();

                                   Uri myphoto = user.getPhotoUrl();
                                           mytraderimage = myphoto.toString();
                                       productkey =     ProductsRef.push().getKey();
                        Products producttobesent = new Products ( titleval,pimage,descval, price, productkey, saveCurrentDate, saveCurrentTime, traderid, tradername, mytraderimage);

                        ProductsRef.child(productkey).setValue(producttobesent, new
                                DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference
                                            databaseReference) {
                                        Toast.makeText(getApplicationContext(), "Product Added", Toast.LENGTH_SHORT).show();
                                        Intent addadminproductactivity = new Intent(AdminAddNewProductActivityII.this, HomeActivity.class);

                                        startActivity(addadminproductactivity);

                                    }
                                });


                                          }

                                      });


                                      mProgress.dismiss();

            }


        }}



    // OnActivity result is lacking behind, I have to get the URi from it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);


        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();

                InputProductImage.setImageURI(mImageUri);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }

        }
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    traderID = "";
                    traderID = user.getUid();
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
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth !=null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }



}
