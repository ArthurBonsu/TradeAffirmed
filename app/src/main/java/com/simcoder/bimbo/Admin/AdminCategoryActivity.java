package  com.simcoder.bimbo.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.MainActivity;
import  com.simcoder.bimbo.R;

import java.util.HashMap;
import java.util.Map;

public class AdminCategoryActivity extends AppCompatActivity
{
    private ImageView tShirts, sportsTShirts, femaleDresses, sweathers;
    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;

    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn, HomeBtn, AllProducts;
    private DatabaseReference RoleReference;
    String role;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    String traderID;

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);
        HomeBtn = (Button) findViewById(R.id.homebuttonhere);
        AllProducts = (Button)findViewById(R.id.allproducts);
        RoleReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(traderID).child("role");

        if (RoleReference != null) {
            RoleReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        role = dataSnapshot.getValue().toString();

                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        if (maintainProductsBtn != null) {
            maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SHOULD HAVE TRADER ID  AND PASS IT TO TYPE TO SEE WHETHER ADMIN OR NOT, SO WE HAVE TO SET A PARAMETER
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminMaintainProductsActivity.class);
                    intent.putExtra("maintainrolefromadmincategory", role);
                    intent.putExtra("maintainfromadmincategoryactivity", traderID);
                    startActivity(intent);
                }
            });

        }
        if (LogoutBtn != null) {
            LogoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    finish();
                }
            });
        }
        if (CheckOrdersBtn != null) {
            CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                    intent.putExtra("rolefromadmincategorytoadminneworder", role);
                    intent.putExtra("fromadmincategoryactivityadminnewordder", traderID);


                    startActivity(intent);
                }
            });

        }

        if (AllProducts != null) {
            AllProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAllProducts.class);
                    intent.putExtra("rolefromadmincategorytoallproducts", role);
                    intent.putExtra("fromadmincategorytoallproducts", traderID);


                    startActivity(intent);
                }
            });

        }
        if (HomeBtn != null) {
            HomeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //SHOULD HAVE TRADER ID  AND PASS IT TO TYPE TO SEE WHETHER ADMIN OR NOT, SO WE HAVE TO SET A PARAMETER
                    Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                    intent.putExtra("rolefromadmincategory", role);
                    intent.putExtra("fromadmincategoryactivity", traderID);
                    startActivity(intent);
                }
            });

        }


        //AUTHENTICATORS
        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminCategoryActivity.this,
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


        tShirts = (ImageView) findViewById(R.id.t_shirts);
        sportsTShirts = (ImageView) findViewById(R.id.sports_t_shirts);
        femaleDresses = (ImageView) findViewById(R.id.female_dresses);
        sweathers = (ImageView) findViewById(R.id.sweathers);

        glasses = (ImageView) findViewById(R.id.glasses);
        hatsCaps = (ImageView) findViewById(R.id.hats_caps);
        walletsBagsPurses = (ImageView) findViewById(R.id.purses_bags_wallets);
        shoes = (ImageView) findViewById(R.id.shoes);

        headPhonesHandFree = (ImageView) findViewById(R.id.headphones_handfree);
        Laptops = (ImageView) findViewById(R.id.laptop_pc);
        watches = (ImageView) findViewById(R.id.watches);
        mobilePhones = (ImageView) findViewById(R.id.mobilephones);

        if (tShirts != null) {
            tShirts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "tShirts");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);

                    startActivity(intent);
                }
            });
        }

        if (sportsTShirts != null) {
            sportsTShirts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Sports tShirts");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });

        }

        if (femaleDresses != null) {
            femaleDresses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Female Dresses");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });

        }

        if (sweathers != null) {
            sweathers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Sweathers");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });

        }
        if (glasses != null) {
            glasses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Glasses");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });
        }

        if (hatsCaps != null) {

            hatsCaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Hats Caps");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });

        }
        if (walletsBagsPurses != null) {

            walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Wallets Bags Purses");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });
        }

        if (shoes != null) {
            shoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Shoes");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });


        }
        if (headPhonesHandFree != null) {
            headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "HeadPhones HandFree");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });
        }
        if (Laptops != null) {
            Laptops.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Laptops");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });
        }

        if (watches != null) {
            watches.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Watches");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
                }
            });

        }

        if (mobilePhones != null) {
            mobilePhones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                    intent.putExtra("category", "Mobile Phones");
                    intent.putExtra("rolefromadmincategorytoaddadmin", role);
                    intent.putExtra("fromadmincategoryactivitytoaddadmin", traderID);
                    startActivity(intent);
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
          if (mAuth != null){
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }}


}
