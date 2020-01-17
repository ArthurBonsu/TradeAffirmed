package com.simcoder.bimbo;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.auth.ui.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.WorkActivities.CartActivity;
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.WorkActivities.SearchProductsActivity;
import com.simcoder.bimbo.WorkActivities.SettinsActivity;
import com.simcoder.bimbo.historyRecyclerView.HistoryObject;
import com.squareup.picasso.Picasso;
;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.paypal.android.sdk.fs.K;

public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {
    // THERE HAS TO BE A SEARCH BOX TO QUERY FROM PRODUCT TABLE
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Location driveLocation;
    LocationRequest mLocationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button mLogout, mRequest, mSettings, mHistory;

    private LatLng pickupLocation;
    private LatLng DriverLocationPoint;
    LatLng latLng;
    private Boolean requestBol = false;

    private Marker pickupMarker;
    String role;
    FirebaseUser user;

    private SupportMapFragment mapFragment;

    private String destination, requestService;
    float distance;
    private LatLng destinationLatLng;
        String thedriverkey;
    private LinearLayout mDriverInfo;

    private ImageView mDriverProfileImage;
    private ImageButton VlayoutNavigation;
    private String driverFoundID;
    private static final String TAG = "Google Activity";
    private TextView mDriverName, mDriverPhone, mDriverCar;
    private ValueEventListener getwhereveravailabledriverislocationListener;
    private DatabaseReference getwhereveravailabledriverislocation;
    private ValueEventListener NameoftheDriversontheMapListener;
    private ValueEventListener PictureofTraderonMapListener;

    String myTradersPic;
    private RadioGroup mRadioGroup;
    DatabaseReference driverRef;
    private RatingBar mRatingBar;
    String rideId;
    String customerId;
    DatabaseReference customerRef;
    DatabaseReference requestRef;
    String customerRequestKey;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private int radius = 1;
    private Boolean driverFound = false;
    String myTradersName;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    Bitmap mydriverbitmap;
    GeoQuery geoQuery;
    DrawerLayout drawer;
    ImageButton myvlayoutnavigationalview;
    String thelocationkey;
    String service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costumer_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mDriverInfo = findViewById(R.id.driverInfo);

        mDriverProfileImage = findViewById(R.id.driverProfileImage);

        VlayoutNavigation = findViewById(R.id.zoom_in);

        mDriverName = findViewById(R.id.driverName);
        mDriverPhone = findViewById(R.id.driverPhone);
        mDriverCar = findViewById(R.id.driverCar);

        mRatingBar = findViewById(R.id.ratingBar);
        FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup.check(R.id.UberX);


        mRequest = findViewById(R.id.request);
        myvlayoutnavigationalview = findViewById(R.id.myvlayoutnavigationalview);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        user = FirebaseAuth.getInstance().getCurrentUser();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Paper.init(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Customer MapView");
            toolbar.collapseActionView();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                      if (drawer != null) {
                          drawer.addDrawerListener(toggle);
                      }}



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);
            if (headerView != null) {
                TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
                CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);


                // USER
                       if (drawer !=null){
                           if (toggle != null) {
                               toggle.syncState();
                               setupDrawer();
                           }
                {
                    if (user.getDisplayName() != null) {
                        if (user.getDisplayName() != null) {
                            userNameTextView.setText(user.getDisplayName());

                            Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.profile).into(profileImageView);
                        }
                    }
                }}


                if (user != null) {
                    customerId = "";
                    customerId = user.getUid();
                }
                Intent intent = new Intent(CustomerMapActivity.this, com.simcoder.bimbo.WorkActivities.HomeActivity.class);
                    if (intent != null) {
                        intent.putExtra("ecommerceuserkey", customerId);
                    }

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                } else {
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this);
                    }
                }
                destinationLatLng = new LatLng(0.0, 0.0);
                mLastLocation = new Location("");
                if (mGoogleApiClient != null) {
                    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                }
                // HAS TO DECIDE WHETHER HE OR SHE WANTS DELIVERY OR MEETUP OR STATIONARY

                fusedLocationProviderClient = new FusedLocationProviderClient(CustomerMapActivity.this);
                if (mGoogleApiClient != null) {
                    mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(CustomerMapActivity.this,
                            new GoogleApiClient.OnConnectionFailedListener() {
                                @Override
                                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                }
                            }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
                }

                final DatabaseReference roleDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId);
                roleDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                              if (role != null){
                            role = dataSnapshot.child("role").getValue(Users.class).toString();

                        }}
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                {
                    if (getIntent().getExtras().get("roledhomeactivitytocustomermapactivity") != null) {
                        role = getIntent().getExtras().get("roledhomeactivitytocustomermapactivity").toString();
                    }
                }
                              if (getIntent() != null) {
                                  customerId = getIntent().getStringExtra("fromhomeactivitytocustomermapactivity");
                              }

                if (VlayoutNavigation != null) {
                    VlayoutNavigation.setOnClickListener(new View.OnClickListener() {


                        @Override
                        public void onClick(View v) {
                            if (mMap != null) {


                                if (mLastLocation != null) {
                                    double latitude = mLastLocation.getLatitude();
                                    double longitude = mLastLocation.getLongitude();


                                    latLng = new LatLng(latitude, longitude);
                                    ;

                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                                }
                                if (!getDriversAroundStarted)
                                    getDriversAround();

                            }
                        }

                        ;


                    });



                }
            }
        }
        // SELECT , WE MUST SELECT THE OPTION OF WHETHER THE PRODUCT IS BROUGOHT AS A MEETYUP OR DELIVERY RADIO BUTTON

        if (mRequest != null) {


            mRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (requestBol) {

                        endRide();
                        // WE HAVE TO ALSO ENDTRADE()

                    } else {

                        //SELECT THE TYPE OF SERVICCE WE WANT

                        if (mRadioGroup != null) {
                            int selectId = mRadioGroup.getCheckedRadioButtonId();

                            final RadioButton radioButton = findViewById(selectId);

                            // if (radioButton.getText() == null) {
                            //    return;
                            // }
                            if (radioButton != null) {
                                requestService = radioButton.getText().toString();
                                // REQUEST SERVICE HAS TO BE CHANGED TO CASES
                                //if service == stationary do this
                                //if service == meetup do this
                                //if service == delivery do this
                            }
                            requestBol = true;
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                String userId = "";
                                customerId = "";

                                userId = user.getUid();
                                customerId = user.getUid();

                                // we have to send the request to customer request
                                //WE MUST REALIZE THAT CUSTOMERREQUEST IS NOT THE SAME AS THE HISTROY KEY
                                // IF THE REQUEST IS ACCEPTED, THERE IS A PARAMETER CALLED RIDE ID WHERE EVERYTHING IS INPUTED FROM

                                if (driverFoundID == null) {
                                    driverFoundID = "No Trader";
                                         requestBol =false;
                                    Toast.makeText(getApplicationContext(), "No Driver Found Yet", Toast.LENGTH_LONG).show();
                                    return;

                                }
                                if (driverFoundID != null) {
                                         if (getIntent().getExtras() != null){
                                    rideId = getIntent().getExtras().getString("rideId");
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
                                    driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("customerRequest");
                                    customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId).child("customerRequest");

                                    GeoFire geoFire = new GeoFire(ref);
                                    //ERROR IS IT IS NOT GETTING LATITITUDES
                                    //WHENEVER I PRESS THIS I PUT THE USER INTO THE DATABASE
                                    // IT SEARCHES FOR THE LOCATION

                                    if (mLastLocation != null) {
                                        double latitude = mLastLocation.getLatitude();
                                        double longitude = mLastLocation.getLongitude();
                                        if (geoFire != null) {
                                            geoFire.setLocation(userId, new GeoLocation(latitude, longitude));

                                            // AND PUTS THEM HERE IN LATITUDES
                                            pickupLocation = new LatLng(latitude, longitude);
                                            if (mMap != null) {
                                                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));
                                                    if (mRequest != null) {
                                                        mRequest.setText("Getting your Trader....");
                                                    }
                                            }
                                        }
                                    }
                                }
                            }}
                            getClosestDriver();
                            //CREATING NULL IN CLOSEST DRIVER
                                 requestBol = false;
                        }

                        //HOW DOES HE ACCEPT AND THEN COMMUNICATION WIL TAKE PLACE
                    }
                }
            });
        }



        }
        //THE CUSTOMER USES THE PLACE AUTOOOMPLETE FRAGMENT TO SET THE DESTINATION

    /*
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                //GETS THE NAME OF THE SEARCHED FOR PLACE AS WELL AS THEIR LATITUDE
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    destination = place.getName().toString();
                    if (destinationLatLng != null) {
                        destinationLatLng = place.getLatLng();
                    }
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                }
            });

        }
*/

    private void setActionBar(Toolbar toolbar) {
    }
    // SO WE CAN USE THIS TO GET THE CLOSEST TRADER YEAH
    // BUT WHAT ABOUT LOCATION ..SUPPOSE I WANT GOODS CLOSEST FROM A PARTICULAR PLACE IN ACCRA AND I SPECIFY THE PLACE
    // CAN I GET THE RADIUS OF THE PLACE AND THE AVAILABLE GOODS AROUND THERE TOO?

    // GOING DEEPER.. WHAT OF A PARTICULAR LOCATION AAND A PARTICULAR PRODUCT
    private void getClosestDriver() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");

        Toast.makeText(CustomerMapActivity.this, "Started Search", Toast.LENGTH_LONG).show();
        //  Query firebaseSearchQuery = mUSerDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");


        GeoFire geoFire = new GeoFire(driverLocation);
        // THIS GUESSES FROM WHERE I PRESSED MY PICKUP, IT THEN CHECKS WITHIN A RADIUS FOR AVAILABLE ENTITIES
        // MEASUREMENT OF PLACES WHEN IT COMES TO NEARBY IS ALWAYS AROUND MY PICKUP POINT
        if (geoFire != null) {
            geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);

            if (geoQuery != null) {
                geoQuery.removeAllListeners();

                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                        thelocationkey = key;
                        if (!driverFound && requestBol) {
                            //WE GET THE DRIVER KEY FRO HERE
                            // ON KEY ENTERED MEANS IF WE SELECT THAT PARTICULAR DRIVER, WE CAN PULL OUT HIS KEY
                            DatabaseReference mDriversDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(key).child(key);
                               //    thedriverkey = mDriversDatabase.push().getKey();
                            mDriversDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                                    /*    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                            String service = ds.child("service").getValue(String.class);
                                            Log.d("TAG", service);
                                        //    Users service = messageSnapshot.getValue(Users.class);

                                        }
*/
                                  //      if (newMap.get("service") != null) {
                                     //      service = newMap.get("service");
                                     //   }


                                            }


                                        if (driverFound){
                                            return;
                                        }

                                        if(requestService.equals("here")){
                                            driverFound = true;
                                            driverFoundID = dataSnapshot.getKey();

                                            // I CAN GET TEHE KEY TO PASS THIS WAY


                                          driverFoundID = dataSnapshot.getKey();

                                            if (driverFoundID == null) {
                                                // THIS IS SO THAT THE PARAPMETERS IS NOT LEFT EMPTY AND CAUSE AN ERROR // ANOTHER WAY IS TO CATCH THE EXCEPTION
                                                driverFoundID = "No Trader";
                                                Toast.makeText(getApplicationContext(), "No Driver Found Yet", Toast.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (driverFoundID != null) {

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                if (user != null) {
                                                    customerId = "";
                                                    customerId = user.getUid();

                                                   driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("customerRequest");
                                                    customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId).child("customerRequest");
                                                    requestRef = FirebaseDatabase.getInstance().getReference().child("customerRequest");
                                                    //WHERE IS RIDE ID PARSED FROM , I HAVE FORGOTTEN
                                                    //SEEMS LIKE THE RIDE ID IS SORT OF DIFFERENT FROM CUSTOMER REQUEST RIGHT?
                                                    if (getIntent().getExtras() != null) {
                                                        rideId = getIntent().getExtras().getString("rideId");
                                                        if (user != null) {
                                                            customerId = user.getUid();
                                                        }
                                                    }
                                                    if (requestRef != null) {
                                                        customerRequestKey = requestRef.push().getKey();
                                                        if (driverRef != null) {
                                                            driverRef.child(customerRequestKey).setValue("customerId", customerId);
                                                            driverRef.child(customerRequestKey).setValue("customerRideId", rideId);
                                                            driverRef.child(customerRequestKey).setValue("destination", destination);
                                                            driverRef.child(customerRequestKey).setValue("driverFoundID", driverFoundID);
                                                            driverRef.child(customerRequestKey).setValue("destinationLat", destinationLatLng.latitude);
                                                            driverRef.child(customerRequestKey).setValue("destinationLng", destinationLatLng);

                                                        }
                                                        if (customerRef != null) {
                                                            customerRef.child(customerRequestKey).setValue("customerId", customerId);
                                                            customerRef.child(customerRequestKey).setValue("customerRideId", rideId);
                                                            customerRef.child(customerRequestKey).setValue("destination", destination);
                                                            customerRef.child(customerRequestKey).setValue("driverFoundID", driverFoundID);
                                                            customerRef.child(customerRequestKey).setValue("destinationLat", destinationLatLng.latitude);
                                                            customerRef.child(customerRequestKey).setValue("destinationLng", destinationLatLng);
                                                        }
                                                        if (requestRef != null) {
                                                            requestRef.child(customerRequestKey).setValue("customerId", customerId);
                                                            requestRef.child(customerRequestKey).setValue("customerRideId", rideId);
                                                            requestRef.child(customerRequestKey).setValue("destination", destination);
                                                            requestRef.child(customerRequestKey).setValue("driverFoundID", driverFoundID);
                                                            requestRef.child(customerRequestKey).setValue("destinationLat", destinationLatLng.latitude);
                                                            requestRef.child(customerRequestKey).setValue("destinationLng", destinationLatLng);
                                                        }








                                                 /*
                                                        HashMap<String, Object> map = new HashMap();
                                                        map.put("customerRideId", customerId);
                                                        map.put("destination", destination);
                                                        map.put("destinationLat", destinationLatLng.latitude);
                                                        map.put("destinationLng", destinationLatLng.longitude);
                                                        driverRef.updateChildren(map);
                                                 */

                                                    }}}
                                                getDriverLocation();
                                                //WE GET DRIVER INFO HERE
                                                getDriverInfo();
                                                // WE CHECK TO SEE IF DRIVER HAS ENDED
                                                getHasRideEnded();
                                                if (mRequest != null) {
                                                    mRequest.setText("Looking for Driver Location....");
                                                }
                                            } else {
                                                mRequest.setText("No Driver found yet");
                                            }
                                        }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                        }
                    }

                    @Override
                    public void onKeyExited(String key) {

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {
                        if (!driverFound) {
                            radius++;
                            getClosestDriver();
                        }
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });
            }

    /*-------------------------------------------- Map specific functions -----
    |  Function(s) getDriverLocation
    |
    |  Purpose:  Get's most updated driver location and it's always checking for movements.
    |
    |  Note:
    |	   Even tho we used geofire to push the location of the driver we can use a normal
    |      Listener to get it's location with no problem.
    |
    |      0 -> Latitude
    |      1 -> Longitudde
    |
    *-------------------------------------------------------------------*/

            // PRODUCT TYPE CAN BE THE PARAMETER FOR THE PRODUCT TYPE
            // WE NEED FIREBASE SEARCH TO COMPLEMENT US ON IT
        }
    }

    private Marker mDriverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationRefListener;

    private void getDriverLocation() {
        driverLocationRef = FirebaseDatabase.getInstance().getReference().child("driversWorking").child(driverFoundID);
        driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {

                 //   Map<String, Object> map = new HashMap<String, Object>(); //Object is containing String
                  //          map = (List<Object>) dataSnapshot.getValue();
                 //   ArrayList<Object> pee = new ArrayList<Object>();


                    List<Object> map = (List<Object>) dataSnapshot.child("l").getValue();
                    ArrayList newmap = new ArrayList(map.size());
                    Iterator mapposition = map.iterator();





                         while(mapposition.hasNext()) {
                             Object mymapvalue = mapposition.next();;
                             newmap.add((mymapvalue));
            }
                        double locationLat = 0;
                        double locationLng = 0;
                        //WE NEED TO ONLY GET THE PARAMETER OF THE TYPE OF ID ;

                        // THIS IS WHERE YOU GET THE LONGITUDE AND LATITUDE OF THE DRIVER LOCATIONN
                        if (newmap.get(0) != null) {
                            locationLat = Double.parseDouble(map.get(0).toString());
                        }
                        if (newmap.get(0) != null) {
                            locationLng = Double.parseDouble(map.get(1).toString());
                        }
                        LatLng driverLatLng = new LatLng(locationLat, locationLng);
                        if (mDriverMarker != null) {
                            mDriverMarker.remove();
                        }
                        Location loc1 = new Location("");

                        if (pickupLocation != null) {
                            loc1.setLatitude(pickupLocation.latitude);
                            loc1.setLongitude(pickupLocation.longitude);
                        }

                        Location loc2 = new Location("");
                        if (loc2 != null) {
                            loc2.setLatitude(driverLatLng.latitude);
                            loc2.setLongitude(driverLatLng.longitude);
                        }
                        float distance = loc1.distanceTo(loc2);

                        // WE HAVE THE KNOW WHAT KIND OF PEOPLE GEOFIRE IS TRYING TO QUERY, WHETHER THEIR PRODUCT IS PART OF IT


                        if (distance < 100) {
                            if (mRequest != null) {
                                mRequest.setText("Driver's Here");
                            } else {
                                mRequest.setText("Driver Found: " + distance);
                            }

                        }

                        if (mMap != null) {
                            mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("your driver").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));
                            //we will pass the driver info and product to this function
                        }

                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /*-------------------------------------------- getDriverInfo -----
    |  Function(s) getDriverInfo
    |
    |  Purpose:  Get all the user information that we can get from the user's database.
    |
    |  Note: --
    |
    *-------------------------------------------------------------------*/
    private void getDriverInfo() {
        mDriverInfo.setVisibility(View.VISIBLE);
        DatabaseReference mDriverIcalledDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
        if (mDriverIcalledDatabase != null) {
            mDriverIcalledDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        if (dataSnapshot.child("name") != null) {
                            if (mDriverName != null) {
                                mDriverName.setText(dataSnapshot.child("name").getValue(Users.class).toString());
                            } else {
                                mDriverName.setText("No Trader Expected");
                            }
                        }
                        if (dataSnapshot.child("phone") != null) {
                            if (mDriverPhone != null) {
                                mDriverPhone.setText(dataSnapshot.child("phone").getValue(Users.class).toString());
                            } else {
                                mDriverPhone.setText("No Number ");
                            }
                        }
                        if (dataSnapshot.child("car") != null) {
                            if (mDriverCar != null) {
                                mDriverCar.setText(dataSnapshot.child("car").getValue(Users.class).toString());
                            } else {
                                mDriverCar.setText("Product Unavailable");
                            }
                        }
                        if (dataSnapshot.child("image") != null) {
                            Glide.with(getApplication()).load(dataSnapshot.child("image").getValue(Users.class).toString()).into(mDriverProfileImage);
                        }


                        int ratingSum = 0;
                        float ratingsTotal = 0;
                        float ratingsAvg = 0;
                        for (DataSnapshot child : dataSnapshot.child("rating").getChildren()) {
                            ratingSum = ratingSum + Integer.valueOf(child.getValue(Users.class).toString());
                            ratingsTotal++;
                        }
                        if (ratingsTotal != 0) {
                            ratingsAvg = ratingSum / ratingsTotal;
                            if (mRatingBar != null) {
                                mRatingBar.setRating(ratingsAvg);
                            }
                        }
                    }
                    // WE MUST SHOW THE PRICES AND PRODUCT PERSON IS LOOKING FOR.CAN IN APPEAR ON THE MAP?
                    //LIKE PRODUCT IMAGE, PRICE AND PRODUCT NAME , WE CAN VIEW THE IMAGE
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private DatabaseReference driveHasEndedRef;
    private ValueEventListener driveHasEndedRefListener;

    private void getHasRideEnded() {
        if (driverFoundID != null) {
            driveHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("customerRequest").child("customerRideId");
            if (driveHasEndedRef != null) {
                driveHasEndedRefListener = driveHasEndedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                        } else {
                            endRide();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    private void endRide() {
        requestBol = false;
        if (geoQuery != null) {
            geoQuery.removeAllListeners();
            driverLocationRef.removeEventListener(driverLocationRefListener);
            driveHasEndedRef.removeEventListener(driveHasEndedRefListener);

            if (driverFoundID != null) {
                driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID).child("customerRequest");
                if (driverRef != null) {
                    driverRef.removeValue();
                    driverFoundID = null;

                }
            }
            driverFound = false;
            radius = 1;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                String userId = "";
                userId = user.getUid();


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");

                // SO WE PUT THIS DATABASE INTO GEOFIRE
                GeoFire geoFire = new GeoFire(ref);
                //WHAT IS THE MEANING OF REMOVE LOCATION?
                geoFire.removeLocation(userId);

                if (pickupMarker != null) {
                    pickupMarker.remove();
                }
                if (mDriverMarker != null) {
                    mDriverMarker.remove();
                }
                if (mRequest != null) {
                    mRequest.setText("call Trader");
                }
                if (mDriverInfo != null) {
                    mDriverInfo.setVisibility(View.GONE);
                }
                if (mDriverName != null) {
                    mDriverName.setText("");
                }
                if (mDriverPhone != null) {
                    mDriverPhone.setText("");
                }
                if (mDriverCar != null) {
                    mDriverCar.setText("Destination: --");
                }
                if (mDriverProfileImage != null) {
                    mDriverProfileImage.setImageResource(R.mipmap.ic_default_user);
                }
            }
        }
    }

    /*-------------------------------------------- Map specific functions -----
    |  Function(s) onMapReady, buildGoogleApiClient, onLocationChanged, onConnected
    |
    |  Purpose:  Find and update user's location.
    |
 |  Note:
    |	   The update interval is set to 1000Ms and the accuracy is set to PRIORITY_HIGH_ACCURACY,
    |      If you're having trouble with battery draining too fast then change these to lower values
    |
    |
    *-------------------------------------------------------------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        buildGoogleApiClient();
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    // I HAVE TO PASS IN THE VALUES OVER TO PRODUCT ACTIVITY/ HOME ACTIVITY OR PRODUCT CATEGORY ACTIVITY
    // THE USER NAME HAS TO BE PASSED.


    // WE CAN CREATE BUTTONS TO ANIMATE CAMERA
    @Override
    //ONLACATION CHANGED IS THE RELATIONSHIP BETWEEN NMAP AND GEOFIRE
    public void onLocationChanged(Location location) {

        if (getApplicationContext() != null) {
            mLastLocation = location;

            if (mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();


                latLng = new LatLng(latitude, longitude);

                //THIS IS WHERE WE PLAY WITH CAMERA, CAN PROVIDE MANY CAMERA FEATURES
                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    if (!getDriversAroundStarted)
                        getDriversAround();

                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override

    //THIS JUST WORKS AROUND TO PROVIDE FASTER LOCATION INFORMATION
    public void onConnected(@Nullable Bundle bundle) {
        if (mLocationRequest != null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // mLocationRequest.setSmallestDisplacement(10);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomerMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
            //THIS IS THE PART WE MUST CHANGE LOCATION UPDATE
            // WE HAVE TO SET THE LOCATION TO A CERTAIN DEGREE



        /*
            *
            *  if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                checkLocationPermission();
            }
        }
             */
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
      /*  fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.e(TAG, "lat:" + locationResult.getLastLocation().getLatitude() + locationResult.getLastLocation().getLongitude());
            }
        }, getMainLooper());
    */
        }

    /*
        *
        * WE CAN USE FUSED API CLIENT IF THERE IS AN ISSUE TO
        *  mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

        LocationCallback mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location : locationResult.getLocations()){
                    if(getApplicationContext()!=null){
                        mLastLocation = location;

                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        if(!getDriversAroundStarted)
                            getDriversAround();
                    }
                }
            }
        };*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    /*-------------------------------------------- onRequestPermissionsResult -----
    |  Function onRequestPermissionsResult
    |
    |  Purpose:  Get permissions for our app if they didn't previously exist.
    |
    |  Note:
    |	requestCode: the nubmer assigned to the request that we've made. Each
    |                request has it's own unique request code.
    |
    *-------------------------------------------------------------------*/
    final int LOCATION_REQUEST_CODE = 1;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this);


                    } else {
                        Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }


        }
    }

    //CAN BE USED TO QUERY FOR THE PRODUCT TO MAKE THE MARKERS DYNAMIC
    boolean getDriversAroundStarted = false;
    List<Marker> markers = new ArrayList<Marker>();

    private void getDriversAround() {
        getDriversAroundStarted = true;
        final DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
        //WE NEED TO KNOW WHAT IS THEIR DISTANCE AWAY TO TELL THE CUSTOMER

        String theCurrentDriver_s_here = driverLocation.getKey();
        if (theCurrentDriver_s_here == null) {
            return;
        }


        getwhereveravailabledriverislocation = FirebaseDatabase.getInstance().getReference().child("driverAvailable").child(theCurrentDriver_s_here).child("l");
        if (getwhereveravailabledriverislocation != null) {
            getwhereveravailabledriverislocationListener = getwhereveravailabledriverislocation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && !customerId.equals("")) {
                     //   List<Object> map = (List<Object>) dataSnapshot.getValue();

                        Map<String, Object> map = new HashMap<String, Object>(); //Object is containing String
                        map = (Map<String, Object>) dataSnapshot.getValue(Users.class);


                        Map<String, String> newMap = (Map) map;
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getValue() != null) {
                                if (entry.getValue() instanceof String) {

                                try {
                                    newMap.put(entry.getKey(), (String) entry.getValue().toString());
                                } catch (ClassCastException e) {
                                    System.out.println("ERROR: " + entry.getKey() + " -> " + entry.getValue() +
                                            " not added, as " + entry.getValue() + " is not a String");
                                }
                            }}
                        double locationLat = 0;
                        double locationLng = 0;
                        if (newMap.get(0) != null) {
                            locationLat = Double.parseDouble(map.get(0).toString());
                        }
                        if (newMap.get(1) != null) {
                            locationLng = Double.parseDouble(map.get(1).toString());
                        }

                        if (DriverLocationPoint != null) {
                            DriverLocationPoint = new LatLng(locationLat, locationLng);
                            Location loc1 = new Location("");
                            if (loc1 != null) {
                                if (pickupLocation != null) {
                                    loc1.setLatitude(pickupLocation.latitude);
                                    loc1.setLongitude(pickupLocation.longitude);
                                }
                            }
                            Location loc2 = new Location("");
                            if (loc2 != null) {
                                if (DriverLocationPoint != null) {
                                    loc2.setLatitude(DriverLocationPoint.latitude);
                                    loc2.setLongitude(DriverLocationPoint.longitude);
                                }
                            }
                            if (loc1 != null) {
                                distance = loc1.distanceTo(loc2);
                            }
                            if (distance < 100) {
                                if (mRequest != null) {
                                    mRequest.setText("Driver's Here");
                                } else {
                                    mRequest.setText("Driver Found: " + distance);
                                }
                            }


                        }
                    }
                }}

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        GeoFire geoFire = new GeoFire(driverLocation);

        if (driveLocation != null) {
            double latitude = driveLocation.getLatitude();
            double longitude = driveLocation.getLongitude();
            if (geoFire != null) {
                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), 999999999);


                if (geoQuery != null) {
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        // IS KEY FROM DRIVERS AVAILABLE WHAT IS PASSED IN AS STRING KEY?

                        //YEAH...SO KEY IS THE ID OF THE DRIVER(S) WHO IS ON THE MAP, ONCE HE IS IN THE REGION OR CIRCULAR REGION, HIS INFORMATION IS CAUGHT
                        // WE CAN CALCULATE THE DISTANCE OF THE KEY, HOW TO DO THIS IS BY CALCULATING DISTANCE THROUGH MATRIX OR DISTANCE IN DATABASE


                        @Override
                        public void onKeyEntered(final String key, GeoLocation location) {

                            for (Marker markerIt : markers) {
                                if (markerIt.getTag().equals(key))

                                    return;
                            }


                            //GEOQUERY (GOELOCATION)  LISTENS TO THE LOCATION OF KEY

                            //GET THE PICTURE OF TRADER ON MAP
                            LatLng driverLocation = new LatLng(location.latitude, location.longitude);


                            //ONE BIG FEATURE TO ADD NOW!
                            //DISTANCE AND PICTURE MARKING
                            DatabaseReference NameoftheDriversontheMap = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
                            if (NameoftheDriversontheMap != null) {
                                NameoftheDriversontheMapListener = NameoftheDriversontheMap.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                            if (dataSnapshot.child(key).child("name") != null) {
                                                myTradersName = dataSnapshot.child("name").getValue(Users.class).toString();
                                            } else {
                                                myTradersName = "No Trader Name";
                                            }


                                            if (dataSnapshot.child(key).child("image") != null) {
                                                myTradersPic = dataSnapshot.getValue(Users.class).toString();
                                                Glide.with(getApplication()).load(dataSnapshot.child("image").getValue(Users.class).toString()).into(mDriverProfileImage);
                                                if (mDriverProfileImage != null) {
                                                    mydriverbitmap = ((BitmapDrawable) mDriverProfileImage.getDrawable()).getBitmap();
                                                }


                                            }


                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            // WE PUT THE IMAGE OF THE PERSON INTO THE BITMAP AND WE PLACE IT ON THE MARKER TO SHOW THE FACE OOF THE IMAGE
                            if (mMap != null) {
                                Marker mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation).title(myTradersName).icon(BitmapDescriptorFactory.fromBitmap(mydriverbitmap)));
                                mDriverMarker.setTag(key);


                                markers.add(mDriverMarker);
                            }
                        }

                        // IF THE DRIVER OR TRADER LEAVES THE DEFINED RADIUS GIVEN THERE IS AN OUT PUT OF
                        @Override
                        public void onKeyExited(String key) {
                            for (Marker markerIt : markers) {
                                if (markerIt.getTag().equals(key)) {
                                    markerIt.remove();
                                }
                            }
                        }

                        // IF THE OBJECT MOVES IT TELLS THE AREA THAT THE OBJECT IS
                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {
                            for (Marker markerIt : markers) {
                                if (markerIt.getTag().equals(key)) {

                                    if (location != null) {
                                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onGeoQueryReady() {
                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }


    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        drawer.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        drawer.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void setupDrawer() {
        if (toggle != null) {
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                @Override
                public void onDrawerClosed(View drawerView) {


                    super.onDrawerClosed(drawerView);
                    invalidateOptionsMenu();


                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();

                    drawer.setVisibility(View.GONE);
                   //  drawer.closeDrawer(View.VISIBLE);
                }
            };
            toggle.setDrawerIndicatorEnabled(true);
           if (drawer != null) {
               drawer.setDrawerListener(toggle);
           }}


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activitycustomermapdrawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_ViewStore) {

            {
                customerId = "";
                if (FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId) != null) {
                    Intent intent = new Intent(CustomerMapActivity.this, com.simcoder.bimbo.WorkActivities.HomeActivity.class);

                    intent.putExtra("rolefromcustomermapactivitytohomeactivity", role);
                    intent.putExtra("fromcustomermapactivitytohomeactivity", customerId);

                    startActivity(intent);
                    finish();

                } else {
                    Intent intent = new Intent(CustomerMapActivity.this, com.simcoder.bimbo.WorkActivities.MainActivity.class);
                    intent.putExtra("traderoruser", customerId);
                    intent.putExtra("traderoruser", role);
                    startActivity(intent);
                    finish();

                }
            }
        } else if (id == R.id.nav_SearchforTraders) {
        } else if (id == R.id.nav_searchforproducts) {

            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String cusomerId = "";
                cusomerId = user.getUid();
                Intent intent = new Intent(CustomerMapActivity.this, SearchProductsActivity.class);
                intent.putExtra("fromcustomermapactivitytosearchproductactivity", cusomerId);
                intent.putExtra("rolefromcustomermapactivtytosearchproductactivity", role);
                startActivity(intent);


            }
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            if (mGoogleSignInClient != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener(CustomerMapActivity.this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }
            Intent intent = new Intent(CustomerMapActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_history) {

            {
                Intent intent = new Intent(CustomerMapActivity.this, HistoryActivity.class);
                // WE PASS THE CUSTOMER OR DRIVER CODE TO THE HISTORY ACTIVITY TO SEE ALL THE HISTORY ACTIVITES
                intent.putExtra("customerOrDriver", "Drivers");
                startActivity(intent);
            }
        } else if (id == R.id.nav_viewprofilehome) {

            {
                Paper.book().destroy();

                Intent intent = new Intent(CustomerMapActivity.this, com.simcoder.bimbo.WorkActivities.CustomerProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        } else if (id == R.id.nav_paymenthome) {
            if (drawer != null) {


            }

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                   if (drawer != null) {

                       drawer.closeDrawer(GravityCompat.START);
                   }

        }return true;
    }
            @Override
            public void onPointerCaptureChanged ( boolean hasCapture){

            }
        }
