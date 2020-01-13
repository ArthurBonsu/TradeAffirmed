
package  com.simcoder.bimbo.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.rey.material.widget.ImageView;
import com.simcoder.bimbo.Model.Users;
import  com.simcoder.bimbo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.WorkActivities.CustomerProfile;

public class AdminViewBuyersActivity extends AppCompatActivity {  //ACTUALLY THIS ACTIVITY IS TO SEE CART ACTIVITY
    private RecyclerView thebuyersforthisproduct;
    private DatabaseReference thebuyersforthisproductRef;
    private Query MybuyersproductQuery;
    private Query thestateofproductquery;
    private Query MyQueryshippedproduct;
    DatabaseReference thebuyersforthisproductdatabase;
    String traderID = "";
    String productID;
    String role;
    String userID;
    Button viewallcustomers;
    String theusers;
    String thetraderinformationandkey;
    // NEW ORDERS RECEIVED FROM THE USERS
//AUTHENITICATORS
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    public ImageView tradersimageonscreen;

    public TextView tradersnameafterbuying;
    public TextView tradersaddressafterbuying;
    public TextView tradersnameafterbuyingphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_buyersforthisproductlayout);

        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();


        {
            if (getIntent().getExtras().get("rolefromadminproductdetailstoviewbuyers") != null) {
                role = getIntent().getExtras().get("rolefromadminproductdetailstoviewbuyers").toString();
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromadminproductdetailstoviewbuyers");
        }

        if (getIntent() != null) {
            productID = getIntent().getStringExtra("productIDfromadminproductdetailstoviewbuyers");
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminViewBuyersActivity.this,
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
        thebuyersforthisproductRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Users");
        // AFTER SETTING SHIPPED IT HAS TO UPDATE AT USERS ORDERS AS WELL-
        thestateofproductquery = thebuyersforthisproductRef.orderByChild("state").equalTo("shipped");
        // get key
        // pass to Datareference
        if (thestateofproductquery != null) {
            // that means after the order traderID IS FILLED
            MybuyersproductQuery = thestateofproductquery.orderByChild(productID);

        }
        thebuyersforthisproduct = findViewById(R.id.thebuyersforthisproductlist);
        if (thebuyersforthisproduct != null) {
            thebuyersforthisproduct.setLayoutManager(new LinearLayoutManager(this));
        }
        // WE HAVE TO CHANGE THIS BUTTON TO BACKPRESSED
         theusers = MybuyersproductQuery.getRef().getKey();
        thebuyersforthisproductdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(theusers);
        thetraderinformationandkey = thebuyersforthisproductdatabase.getKey();
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(thebuyersforthisproductdatabase, Users.class)
                        .build();


        FirebaseRecyclerAdapter<Users, UserProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, UserProductViewHolder>(options) {

                    @NonNull
                    @Override
                    public UserProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewbuyersforthisproductlayout, parent, false);
                        return new UserProductViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull UserProductViewHolder holder, int position, @NonNull Users model) {
                        holder.tradersimageonscreen.setImageResource(Integer.parseInt(model.getimage()));
                        holder.tradersnameafterbuying.setText("Name: " + model.getname());
                        holder.tradersnameafterbuyingphone.setText("Name: " + model.getphone());
                        holder.tradersaddressafterbuying.setText("Address: " + model.getaddress());



                            userID = thetraderinformationandkey;


                            if (tradersimageonscreen != null) {
                                Glide.with(getApplication()).load((model.getimage())).into(tradersimageonscreen);
                            }
                        }




                };
        if (thebuyersforthisproduct != null) {
            thebuyersforthisproduct.setAdapter(adapter);
        }
        if (adapter != null) {
            adapter.startListening();
        }
    }


    // show all the products Button
                        /*    holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    orderkey = getRef(position).getKey();

                                    Intent intent = new Intent(AdminAllProducts.this, AdminUserCartedActivity.class);
                                    intent.putExtra("orderkey", orderkey);
                                    intent.putExtra("userID", userID);

                                    intent.putExtra("fromnewordertousersproductactivity", traderID);
                                    intent.putExtra("rolefromnewordertouserproduct", role);

                                    startActivity(intent);
                                }
                            }); */


    @Override
    protected void onStop() {
        super.onStop();
        //     mProgress.hide();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

    class UserProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView tradersimageonscreen;

        public TextView tradersnameafterbuying;
        public TextView tradersaddressafterbuying;
        public TextView tradersnameafterbuyingphone;


        public UserProductViewHolder(View itemView) {
            super(itemView);


            tradersimageonscreen = itemView.findViewById(R.id.tradersimageonscreen);
            tradersnameafterbuying = itemView.findViewById(R.id.tradersnameafterbuying);
            tradersaddressafterbuying = itemView.findViewById(R.id.tradersaddressafterbuying);
            tradersnameafterbuyingphone = itemView.findViewById(R.id.tradersnameafterbuyingphone);

            //    ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // VIEWPAGER BUTTONS

                case R.id.tradersimageonscreen:
                    Intent intent = new Intent(AdminViewBuyersActivity.this, CustomerProfile.class);
                    intent.putExtra("rolefromviewbuyertocustomerprofilehere", role);
                    intent.putExtra("fromadminvuewbuyertocustomerprofilehere", traderID);
                    intent.putExtra("fromviewbuyerusertocustomerprofilehere", userID);

                    startActivity(intent);

                    break;
                case R.id.tradersnameafterbuying:
                    Intent adminuserproductactivity = new Intent(AdminViewBuyersActivity.this, ViewSingleUserOrders.class);
                    adminuserproductactivity.putExtra("rolefromviewbuyertoadminuserproductsactivity", role);
                    adminuserproductactivity.putExtra("fromviewbuyertradertoadminuserproductsactivity", traderID);
                    adminuserproductactivity.putExtra("fromviewbuyeruseridtoadminuserproductsactivity", userID);
                    startActivity(adminuserproductactivity);

                    // GRIDVIEW BUTTONS
                    break;

            }
        }


        private void RemoverOrder(String uID) {
            if (thebuyersforthisproductRef.child(uID) != null) {
                thebuyersforthisproductRef.child(uID).removeValue();
            }
        }
    }
}