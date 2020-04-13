
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
        import com.simcoder.bimbo.Model.Products;
        import  com.simcoder.bimbo.R;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class AdminAllProducts extends AppCompatActivity {  //ACTUALLY THIS ACTIVITY IS TO SEE CART ACTIVITY
    private RecyclerView allproductlist;
    private DatabaseReference allproductRef;
    private Query MyproductsQuery;
    String traderID = "";
    String orderkey;
    String userID;

    String role;
    Button viewallcustomers;
    // NEW ORDERS RECEIVED FROM THE USERS
//AUTHENITICATORS
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    public ImageView productimagesfornamehere;
    public TextView productnameforadmin;
    public TextView productpriceforadmin;
    public TextView productcategoryforadmin;
    public TextView producttimeuploaded;
    String productID;

    //AUTHENTICATORS

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Activity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_products);

        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

                    Intent  rolesintent = getIntent();

                  if (rolesintent.getExtras().getString("rolefromadmincategorytoallproducts") != null) {
                      role = rolesintent.getExtras().getString("rolefromadmincategorytoallproducts");
                  }

                    Intent  traderIDIntent = getIntent();
                  if (traderIDIntent.getExtras().getString("fromuserinsingleusertoadminproductdetails") != null) {
                      traderID = traderIDIntent.getExtras().getString("fromuserinsingleusertoadminproductdetails");

                  }
              

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminAllProducts.this,
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
        allproductRef = FirebaseDatabase.getInstance().getReference().child("Product");
        if (allproductRef != null) {
            // that means after the order traderID IS FILLED
            MyproductsQuery = allproductRef.orderByChild("traderID").equalTo(traderID);
            if (MyproductsQuery.getRef() != null) {
                productID = MyproductsQuery.getRef().getKey();

            }
        }
        allproductlist = findViewById(R.id.allproductlist);
        if (allproductlist != null) {
            allproductlist.setLayoutManager(new LinearLayoutManager(this));
        }

        viewallcustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAllProducts.this, AdminAllCustomers.class);
                intent.putExtra("rolefromadminallproducttoadminallcustomer", role);
                intent.putExtra("fromadminallproducttoadminallcustomer", traderID);

                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(MyproductsQuery, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, AdminProductsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, AdminProductsViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull AdminProductsViewHolder holder, int position, @NonNull Products model) {
                        holder.productimagesforname.setImageResource(Integer.parseInt(model.getpimage()));
                        holder.productnameforadmin.setText("Name: " + model.getpname());
                        holder.productpriceforadmin.setText("Phone: " + model.getprice());

                        holder.producttimeuploaded.setText("Order at: " + model.getdate() + "  " + model.gettime());





                        if (productimagesfornamehere != null) {
                            Glide.with(getApplication()).load((model.getpimage())).into(productimagesfornamehere);
                        }
                    }

                    @NonNull
                    @Override
                    public AdminProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminallproductslayout, parent, false);
                        return new AdminProductsViewHolder(view);
                    }
                };
        if (allproductlist != null) {
            allproductlist.setAdapter(adapter);
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

    class AdminProductsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public  ImageView     productimagesforname;
        public  TextView productnameforadmin, productpriceforadmin,productcategoryforadmin, producttimeuploaded;
        public AdminProductsViewHolder(View itemView) {
            super(itemView);

            productimagesforname = itemView.findViewById(R.id.productimagesforadmin);
          productnameforadmin = itemView.findViewById(R.id.productnameforadmin);
            productpriceforadmin = itemView.findViewById(R.id.productpriceforadmin);
           productcategoryforadmin = itemView.findViewById(R.id.productcategoryforadmin);
            producttimeuploaded = itemView.findViewById(R.id.producttimeanddateforadmin);


            //    ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
        }



        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // VIEWPAGER BUTTONS

                case R.id.productimagesforadmin:
                    Intent intent = new Intent(AdminAllProducts.this, AdminProductDetails.class);

                    intent.putExtra("productIDfromallproducttoproductdetails", productID);

                    intent.putExtra("fromtheallproducttoadmiproductdetails", traderID);
                    intent.putExtra("rolefromallproductdetails", role);

                    startActivity(intent);
                    break;
                case R.id.productnameforadmin:
                    // GRIDVIEW BUTTONS
                     intent = new Intent(AdminAllProducts.this, AdminProductDetails.class);

                    intent.putExtra("productIDfromallproducttoproductdetails", productID);


                    intent.putExtra("fromtheallproducttoadmiproductdetails", traderID);
                    intent.putExtra("rolefromallproductdetails", role);

                    startActivity(intent);

                    break;
                case R.id.productpriceforadmin:

                    break;
                case R.id.productcategoryforadmin:
                     intent = new Intent(AdminAllProducts.this, AdminProductDetails.class);

                    intent.putExtra("userID", userID);

                    intent.putExtra("fromtheallproducttoadmiproductdetails", traderID);
                    intent.putExtra("rolefromallproductdetails", role);

                    startActivity(intent);
                    break;
                case R.id.producttimeanddateforadmin:

                    break;


                    // FOOTER BUTTONS BUTTONS


            }

        }
    }


    private void RemoverOrder(String uID)
    {       if ( allproductRef.child(uID) != null) {
        allproductRef.child(uID).removeValue();
    }}
}
