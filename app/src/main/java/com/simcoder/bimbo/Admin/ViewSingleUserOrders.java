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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import  com.simcoder.bimbo.Model.Cart;
import com.simcoder.bimbo.Model.Products;
import  com.simcoder.bimbo.R;
import com.simcoder.bimbo.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.simcoder.bimbo.WorkActivities.CustomerProfile;

public  class ViewSingleUserOrders extends AppCompatActivity
{
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference singleuserordertListRef;
    private Query mQuerySingleUSerOrderedProduct;
    private String userID = "";
    String orderkey="";
    String role;
    String traderID= "";
    Query myQuery;
    Query myUserQuery;
    String quantity;
    ImageView singleuserproductimage;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // HAS TO BE ORDERED BY ADMINID
// THE ADMIN CAN CHECK FOR A PARTICULAR USERS PRODUCT BOUGHT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_orders);


        userID = getIntent().getStringExtra("userID");
        orderkey = getIntent().getStringExtra("orderkey");

        singleuserproductimage =(ImageView)findViewById(R.id.singleuserproductimage);
        TextView singleuserproductname;
        TextView singleuserproductquantity;
        TextView singleuserproductprice;


        //FROM NEW SINGLE USER

        {
            if (getIntent().getExtras().get("rolefromnewordertouserproduct") != null) {
                role = getIntent().getExtras().get("rolefromnewordertouserproduct").toString();
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromnewordertousersproductactivity");
        }
        if (getIntent() != null) {
            userID = getIntent().getStringExtra("neworderUserID");
        }

        {
            if (getIntent().getExtras().get("rolefromviewbuyertoadminuserproductsactivity") != null) {
                role = getIntent().getExtras().get("rolefromviewbuyertoadminuserproductsactivity").toString();
            }
        }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromviewbuyertradertoadminuserproductsactivity");
        }
        if (getIntent() != null) {
            userID = getIntent().getStringExtra("fromviewbuyeruseridtoadminuserproductsactivity");
        }


        productsList = findViewById(R.id.products_list);
        if (productsList != null) {
            productsList.setHasFixedSize(true);
        }
        layoutManager = new LinearLayoutManager(this);
        if (productsList != null) {
            productsList.setLayoutManager(layoutManager);

        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(ViewSingleUserOrders.this,
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
        // WE HAVE TO PUT AUTHENTICATIONS ON CARTS
        //WE PICK THE CURRENT ORDER

        singleuserordertListRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders");
        myQuery = singleuserordertListRef.child("trader").orderByChild(traderID);
        myUserQuery = myQuery.getRef().child("Users").child(userID).child("products");

        DatabaseReference getthequatityreference = myUserQuery.getRef().child("").child("quantity");


        getthequatityreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    quantity = dataSnapshot.getValue().toString();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }
            @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(myUserQuery, Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, SingleUserProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, SingleUserProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SingleUserProductViewHolder holder, int position, @NonNull Products model)
            {       if (model != null){
                holder.singleuserproductimage.setImageResource(Integer.parseInt(model.getimage()));
                holder.singleuserproductquantity.setText("Quantity = " +  quantity);
                holder.singleuserproductprice.setText("Price " + model.getprice() + "$");
                holder.singleuserproductname.setText(model.getname());
                           // USERNAME SHOULD BE ADDED APART FROM THE PRODUCT QUERY
                if (singleuserproductimage != null) {
                    Glide.with(getApplication()).load((model.getimage())).into(singleuserproductimage);
                }
             }}

            @NonNull
            @Override
            public SingleUserProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminsingleuserproductlayout, parent, false);
                SingleUserProductViewHolder holder = new SingleUserProductViewHolder(view);
                return holder;
            }
        };
        if (productsList != null) {
            productsList.setAdapter(adapter);
        }
        if (adapter != null) {
            adapter.startListening();

        }}

    @Override
    protected void onStop() {
        super.onStop();
        //     mProgress.hide();
         if (mAuth !=null) {
             mAuth.removeAuthStateListener(firebaseAuthListener);
         }
    }


    class SingleUserProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView singleuserproductimage;

        public TextView singleuserproductname;
        public TextView singleuserproductquantity;
        public TextView singleuserproductprice;


        public SingleUserProductViewHolder(View itemView) {
            super(itemView);


            singleuserproductimage = itemView.findViewById(R.id.singleuserproductimage);
            singleuserproductname = itemView.findViewById(R.id.singleuserproductname);
            singleuserproductquantity = itemView.findViewById(R.id.singleuserproductquantity);
            singleuserproductprice = itemView.findViewById(R.id.singleuserproductprice);


        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // VIEWPAGER BUTTONS

                case R.id.singleuserproductimage:
                    Intent intent = new Intent(ViewSingleUserOrders.this, AdminProductDetails.class);
                    intent.putExtra("rolefromsingleusertoadminproductdetails", role);
                    intent.putExtra("fromadminsingleusertoadminproductdetails", traderID);
                    intent.putExtra("fromuserinsingleusertoadminproductdetails", userID);

                    startActivity(intent);

                    break;
                case R.id.singleuserproductname:
                    Intent viewingtheproductactivityhere = new Intent(ViewSingleUserOrders.this, AdminProductDetails.class);
                    viewingtheproductactivityhere.putExtra("rolefromsingleusertoadminproductdetails", role);
                    viewingtheproductactivityhere.putExtra("fromadminsingleusertoadminproductdetails", traderID);
                    viewingtheproductactivityhere.putExtra("fromuserinsingleusertoadminproductdetails", userID);
                    startActivity(viewingtheproductactivityhere);

                    // GRIDVIEW BUTTONS
                    break;

            }
        }


        private void RemoveProductBought(String uID) {
            if (singleuserordertListRef.child(uID) != null) {
                singleuserordertListRef.child(uID).removeValue();
            }
        }
    }



}
