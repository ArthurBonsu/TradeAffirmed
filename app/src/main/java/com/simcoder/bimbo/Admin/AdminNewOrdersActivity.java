package  com.simcoder.bimbo.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import  com.simcoder.bimbo.Model.AdminOrders;
import  com.simcoder.bimbo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity
{  //ACTUALLY THIS ACTIVITY IS TO SEE CART ACTIVITY
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private Query MyordersQuery;
    String traderID= "";
    String orderkey;
    String userID;
    String role;
    String username;
    String userkey;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();


        { if (getIntent().getExtras().get("rolefromadmincategorytoadminneworder") != null) {
            role = getIntent().getExtras().get("rolefromadmincategorytoadminneworder").toString();     } }

        if (getIntent() != null) {
            traderID = getIntent().getStringExtra("fromadmincategoryactivityadminnewordder");
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        if (mGoogleApiClient != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(AdminNewOrdersActivity.this,
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
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
                  if (ordersRef != null) {
                      // that means after the order traderID IS FILLED
                      MyordersQuery = ordersRef.orderByChild("traderID").equalTo(traderID);
                  }
        ordersList = findViewById(R.id.orders_list);
              if (ordersList != null) {
                  ordersList.setLayoutManager(new LinearLayoutManager(this));
              }
    }


    @Override
    protected void onStart()
    {
        super.onStart();

             DatabaseReference userreference  = ordersRef.getRef().child("Users");
        userreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                     userkey  = dataSnapshot.getValue().toString();
                     username = dataSnapshot.child("name").getValue().toString();


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(MyordersQuery, AdminOrders.class)
                .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        holder.userName.setText("Name: " + username);
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount =  $" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at: " + model.getDate() + "  " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + ", " + model.getCity());
                        holder.productnameordered.setText("Product Orderer" + model.getName());

                        if (model != null) {
                            orderkey = model.getUid();


                                holder.ShowOrdersBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        orderkey = getRef(position).getKey();

                                        Intent intent = new Intent(AdminNewOrdersActivity.this, ViewSingleUserOrders.class);
                                        intent.putExtra("orderkey", orderkey);
                                        intent.putExtra("neworderUserID", userkey);


                                        intent.putExtra("fromnewordertousersproductactivity", traderID);
                                        intent.putExtra("rolefromnewordertouserproduct", role);

                                        startActivity(intent);
                                    }
                                });
                           holder.showCartsofUser.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   orderkey = getRef(position).getKey();

                                   Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserCartedActivity.class);
                                   intent.putExtra("orderkey", orderkey);
                                   intent.putExtra("userkey", userkey);


                                   intent.putExtra("fromnewordertouseradmincartedactivity", traderID);
                                   intent.putExtra("rolefromnewordertoadmincartedactivity", role);

                                   startActivity(intent);
                               }
                           });
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CharSequence options[] = new CharSequence[]
                                                {
                                                        "Yes",
                                                        "No"
                                                };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                        builder.setTitle("Have you shipped this order products ?");

                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (i == 0) {
                                                    String uID = getRef(position).getKey();

                                                    RemoverOrder(uID);
                                                } else {
                                                    finish();
                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }
                        }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
              if (ordersList != null) {
                  ordersList.setAdapter(adapter);
              }
           if (adapter != null) {
               adapter.startListening();

           }}


    @Override
    protected void onStop() {
        super.onStop();
        //     mProgress.hide();
             if (mAuth != null) {
                 mAuth.removeAuthStateListener(firebaseAuthListener);
             }
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, userPhoneNumber,productnameordered, userTotalPrice, userDateTime, userShippingAddress;
        public Button ShowOrdersBtn, showCartsofUser;


        public AdminOrdersViewHolder(View itemView)
        {
            super(itemView);


            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
            showCartsofUser =itemView.findViewById(R.id.showallcarteduserproducts);
            productnameordered = itemView.findViewById(R.id.productnameordered);
        }
    }




    private void RemoverOrder(String uID)
    {       if ( ordersRef.child(uID) != null) {
        ordersRef.child(uID).removeValue();
    }}
}
