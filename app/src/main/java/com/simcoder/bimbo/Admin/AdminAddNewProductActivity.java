package  com.simcoder.bimbo.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.CustomerLoginActivity;
import com.simcoder.bimbo.CustomerMapActivity;
import  com.simcoder.bimbo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private  DatabaseReference ProductsTraderRef;
    private ProgressDialog loadingBar;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String traderID;
    String role;
    String traderkeryhere;
    FirebaseUser user;

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

             if (getIntent().getExtras().get("category") != null) {
            CategoryName = getIntent().getExtras().get("category").toString();
        }
             // KEYS PASSED IN FROM ADMINCATEGORY

        { if (getIntent().getExtras().get("rolefromadmincategorytoaddadmin") != null) {
            role = getIntent().getExtras().get("rolefromadmincategorytoaddadmin").toString();     } }

             if (getIntent() != null) {
                 traderID = getIntent().getStringExtra("fromadmincategoryactivitytoaddadmin");
             }
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("product_images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Product");
        ProductsTraderRef =FirebaseDatabase.getInstance().getReference().child("Product").child("trader");



            ;


        user = FirebaseAuth.getInstance().getCurrentUser();
           if (user != null) {
               traderID = "";
            traderID = user.getUid();
           }
                       if (ProductsRef.push() != null) {
                           productRandomKey = ProductsRef.push().getKey();
                           if (ProductsTraderRef.push() != null) {
                               traderkeryhere = ProductsTraderRef.push().getKey();
                           }
                       }


        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);

        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminAddNewProductActivity.this,
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
                    traderID = "";
                    traderID = user.getUid();
                }

                // I HAVE TO TRY TO GET THE SETUP INFORMATION , IF THEY ARE ALREADY PROVIDED WE TAKE TO THE NEXT STAGE
                // WHICH IS CUSTOMER TO BE ADDED.
                // PULLING DATABASE REFERENCE IS NULL, WE CHANGE BACK TO THE SETUP PAGE ELSE WE GO STRAIGHT TO MAP PAGE
            }
        };
             if (InputProductImage !=null) {
                 InputProductImage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         OpenGallery();
                     }
                 });
             }

             if (AddNewProductButton != null) {
                 AddNewProductButton.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         ValidateProductData();
                     }
                 });
             }
    }







    @Override
    protected void onStart() {
        super.onStart();
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



    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        if (galleryIntent != null) {
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GalleryPick);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            if (ImageUri != null) {
                if (data != null) {
                    ImageUri = data.getData();

                    if (InputProductImage != null) {
                        InputProductImage.setImageURI(ImageUri);
                    }
                }
            }


        }
    }


    private void ValidateProductData() {
        if (InputProductDescription != null) {
            Description = InputProductDescription.getText().toString();
            if (InputProductPrice != null) {
                Price = InputProductPrice.getText().toString();
            if (InputProductName != null){
                Pname = InputProductName.getText().toString();


                if (ImageUri == null) {
                    Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Description)) {
                    Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Price)) {
                    Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Pname)) {
                    Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
                } else {
                    StoreProductInformation();
                }}
            }
        }

    }
    private void StoreProductInformation() {
        if (loadingBar != null) {
            loadingBar.setTitle("Add New Product");
            loadingBar.setMessage("Dear Trader, please wait while we are adding the new product.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            if (currentDate != null) {
                saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                if (currentTime != null) {
                    saveCurrentTime = currentTime.format(calendar.getTime());


                    final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

                                  if (filePath !=null){
                    final UploadTask uploadTask = filePath.putFile(ImageUri);


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            if (loadingBar != null) {
                                loadingBar.dismiss();
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();


                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                      if (filePath != null) {
                                          downloadImageUrl = filePath.getDownloadUrl().toString();
                                      }
                                    return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                                            if (task != null) {
                                                                downloadImageUrl = task.getResult().toString();
                                                            }
                                        Toast.makeText(AdminAddNewProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                        SaveProductInfoToDatabase();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }}

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        final HashMap<String, Object> traderhashmap = new HashMap<>();
         productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("desc", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("categoryID", CategoryName);
        productMap.put("price", Price);
        productMap.put("name", Pname);




        traderhashmap.put("name",user.getDisplayName()   );
        traderhashmap.put("tid", user.getUid());
        traderhashmap.put("image",user.getPhotoUrl() );


        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ProductsTraderRef.child(traderkeryhere).updateChildren(traderhashmap);

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            if (task !=null) {
                                String message = task.getException().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }}
                });

    }
}
