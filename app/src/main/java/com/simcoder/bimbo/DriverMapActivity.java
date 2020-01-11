package com.simcoder.bimbo;

import android.Manifest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.WorkActivities.CartActivity;
import com.simcoder.bimbo.WorkActivities.HomeActivity;
import com.simcoder.bimbo.WorkActivities.SearchProductsActivity;
import com.simcoder.bimbo.WorkActivities.SettinsActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;


public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    String driverId;
    Button mLogout;
    Button mSettings;
    Button mRideStatus;
    Button mHistory;
    String role;
    ImageButton mydrivernavigations;

    private Switch mWorkingSwitch;

    private int status = 0;
    // HERE WE CAN ASSIGN CUSTOMER ID AND DETAILS TO CUSTOMER ID
    private String customerId = "", destination;
    // THERE IS A PATHWAY CALLED LATLNG
    private LatLng destinationLatLng, pickupLatLng;
    private float rideDistance;

    private Boolean isLoggingOut = false;

    private SupportMapFragment mapFragment;

    private LinearLayout mCustomerInfo;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private ImageView mCustomerProfileImage;
    // BUYER NAME, PHONE AND CUSTOMER
    private TextView mCustomerName, mCustomerPhone, mCustomerDestination;

    //HOW IS IT ABLE TO GET THE DETAILS OF THE USER?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_driver_map);
        mSettings = (Button) findViewById(R.id.settings);
        mLogout = (Button) findViewById(R.id.logout);
        mRideStatus = (Button) findViewById(R.id.rideStatus);
        mHistory = (Button) findViewById(R.id.historys);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        polylines = new ArrayList<>();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

        }
        mCustomerInfo = findViewById(R.id.customerInfo);
        FirebaseAuth.getInstance();
        mCustomerProfileImage = findViewById(R.id.customerProfileImage);

        mCustomerName = findViewById(R.id.customerName);
        mCustomerPhone = findViewById(R.id.customerPhone);
        mCustomerDestination = findViewById(R.id.customerDestination);
        mydrivernavigations = findViewById(R.id.mydrivernavigation);
        //HE CAN SWITHCH FROM NON WORK TO WORK
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(DriverMapActivity.this, gso);

        mWorkingSwitch = findViewById(R.id.workingSwitch);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            driverId = "";
            driverId = user.getUid();
        }
        Intent intent = new Intent(DriverMapActivity.this, com.simcoder.bimbo.WorkActivities.HomeActivity.class);

        intent.putExtra("ecommerceuserkey", driverId);

        if (mWorkingSwitch != null) {
            mWorkingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        connectDriver();
                    } else {
                        disconnectDriver();
                    }
                }
            });

        }
        if (mRideStatus != null) {
            mRideStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (status) {
                        case 1:
                            status = 2;
                            erasePolylines();
                            if (destinationLatLng != null) {
                                if (destinationLatLng.latitude != 0.0 && destinationLatLng.longitude != 0.0) {
                                    getRouteToMarker(destinationLatLng);
                                }
                            }
                            mRideStatus.setText("drive completed");

                            break;
                        case 2:
                            // RIDE CAN BE RECORDED HERE
                            recordRide();
                            endRide();
                            break;
                    }
                }
            });
        }
        /*
       mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(DriverMapActivity.this,
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

*/
        if (mLogout != null) {
            mLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isLoggingOut = true;

                    disconnectDriver();

                    FirebaseAuth.getInstance().signOut();
                    if (mGoogleApiClient != null) {
                        mGoogleSignInClient.signOut().addOnCompleteListener(DriverMapActivity.this,
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                    Intent intent = new Intent(DriverMapActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                    return;
                }
            });

        }
        if (mSettings != null) {
            mSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DriverMapActivity.this, DriverSettingsActivity.class);
                    startActivity(intent);
                    return;
                }
            });

        }
        DatabaseReference myrolereference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("role");
        myrolereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    role = dataSnapshot.getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        mydrivernavigations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId) != null) {


                    Intent intent = new Intent(DriverMapActivity.this, com.simcoder.bimbo.WorkActivities.HomeActivity.class);
                    intent.putExtra("Trader", role);
                    startActivity(intent);
                    finish();
                    return;
                } else {
                    Intent intent = new Intent(DriverMapActivity.this, com.simcoder.bimbo.WorkActivities.MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        });


        if (mHistory != null) {
            mHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DriverMapActivity.this, HistoryActivity.class);
                    // WE PASS THE CUSTOMER OR DRIVER CODE TO THE HISTORY ACTIVITY TO SEE ALL THE HISTORY ACTIVITES
                    intent.putExtra("customerOrDriver", "Drivers");
                    startActivity(intent);
                    return;
                }
            });
            getAssignedCustomer();
        }
    }


    private void getAssignedCustomer() {
        FirebaseUser driver = FirebaseAuth.getInstance().getCurrentUser();
        if (driver != null) {
            String driverId = "";
            driverId = driver.getUid();
            // HERE WE GET THE CUSTOMER RIDE ID AND WE ESTABLISH FOR THAT CUSTOMER
            DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("customerRequest").child("customerRideId");
            assignedCustomerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        status = 1;
                        customerId = dataSnapshot.getValue().toString();
                        getAssignedCustomerPickupLocation();
                        getAssignedCustomerDestination();
                        getAssignedCustomerInfo();
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

    Marker pickupMarker;
    private DatabaseReference assignedCustomerPickupLocationRef;
    private ValueEventListener assignedCustomerPickupLocationRefListener;

    private void getAssignedCustomerPickupLocation() {
        //WE GOT CUSTOMER LOCATION FROM HERE

        assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("l");
        assignedCustomerPickupLocationRefListener = assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !customerId.equals("")) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    if (pickupLatLng != null) {
                        pickupLatLng = new LatLng(locationLat, locationLng);
                        pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLatLng).title("pickup location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));
                        getRouteToMarker(pickupLatLng);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
// This is the routing code

    private void getRouteToMarker(LatLng pickupLatLng) {

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(latitude, longitude), pickupLatLng)
                    .build();
            routing.execute();

        }
    }

    // WE CAN DETECT CUSTOMER DESTINATION HEREFR
    //CUSTOMER MUST SET SOME DESTINATION PRIOR
    private void getAssignedCustomerDestination() {
        FirebaseUser driver = FirebaseAuth.getInstance().getCurrentUser();
        if (driver != null) {
            String driverId = "";
            driverId = driver.getUid();
            DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverId).child("customerRequest");
            assignedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        if (map.get("destination") != null) {
                            destination = map.get("destination").toString();
                            mCustomerDestination.setText("Destination: " + destination);
                        } else {
                            mCustomerDestination.setText("Destination: --");
                        }

                        Double destinationLat = 0.0;
                        Double destinationLng = 0.0;
                        if (map.get("destinationLat") != null) {
                            destinationLat = Double.valueOf(map.get("destinationLat").toString());
                        }
                        if (map.get("destinationLng") != null) {
                            destinationLng = Double.valueOf(map.get("destinationLng").toString());
                            destinationLatLng = new LatLng(destinationLat, destinationLng);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    // GET THE ASSIGNED CUSTOMER INFORMATION
    private void getAssignedCustomerInfo() {
        mCustomerInfo.setVisibility(View.VISIBLE);
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        mCustomerName.setText(map.get("name").toString());
                    }
                    if (map.get("phone") != null) {
                        mCustomerPhone.setText(map.get("phone").toString());
                    }
                    if (map.get("image") != null) {
                        Glide.with(getApplication()).load(map.get("image").toString()).into(mCustomerProfileImage);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    // HERE I  CAN END THE DRIVER CAN END THE RIDE
    private void endRide() {
        mRideStatus.setText("picked customer");
        erasePolylines();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = "";


            userId = user.getUid();

            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId).child("customerRequest");
            driverRef.removeValue();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
            GeoFire geoFire = new GeoFire(ref);
            geoFire.removeLocation(customerId);
            customerId = "";
            rideDistance = 0;

            if (pickupMarker != null) {
                pickupMarker.remove();
            }
            if (assignedCustomerPickupLocationRefListener != null) {
                assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
            }
            if (mCustomerInfo != null) {
                mCustomerInfo.setVisibility(View.GONE);
            }
            if (mCustomerName != null) {
                mCustomerName.setText("");
            }
            if (mCustomerPhone != null) {
                mCustomerPhone.setText("");
            }
            if (mCustomerDestination != null) {
                mCustomerDestination.setText("Destination: --");
            }
            if (mCustomerProfileImage != null) {
                mCustomerProfileImage.setImageResource(R.mipmap.ic_default_user);
            }
        }
    }

    private void recordRide() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = "";


            userId = user.getUid();
            DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId).child("history");
            DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(customerId).child("history");
            DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("history");
            String requestId = historyRef.push().getKey();
            driverRef.child(requestId).setValue(true);
            customerRef.child(requestId).setValue(true);

            //SAME MUST BE DONE FOR HISTORYREF OVERHERE
            // RIDE ID MUST BE STORED
            if (destinationLatLng != null) {
                HashMap map = new HashMap();
                map.put("driver", userId);
                map.put("customer", customerId);
                map.put("rating", 0);
                map.put("timestamp", getCurrentTimestamp());
                map.put("destination", destination);
                if (pickupLatLng != null) {
                    map.put("location/from/lat", pickupLatLng.latitude);
                    map.put("location/from/lng", pickupLatLng.longitude);
                    map.put("location/to/lat", destinationLatLng.latitude);
                    map.put("location/to/lng", destinationLatLng.longitude);
                    map.put("distance", rideDistance);
                    historyRef.child(requestId).updateChildren(map);
                    // PUT THIS INTO THE DRIVER CATALOG
                    HashMap drivemap = new HashMap();
                    map.put("driver", userId);
                    map.put("customer", customerId);
                    map.put("rating", 0);
                    map.put("timestamp", getCurrentTimestamp());
                    map.put("destination", destination);
                    map.put("location/from/lat", pickupLatLng.latitude);
                    map.put("location/from/lng", pickupLatLng.longitude);
                    map.put("location/to/lat", destinationLatLng.latitude);
                    map.put("location/to/lng", destinationLatLng.longitude);
                    map.put("distance", rideDistance);
                    driverRef.child(requestId).updateChildren(drivemap);

                    HashMap drivemapers = new HashMap();
                    map.put("driver", userId);
                    map.put("customer", customerId);
                    map.put("rating", 0);
                    map.put("timestamp", getCurrentTimestamp());
                    map.put("destination", destination);
                    map.put("location/from/lat", pickupLatLng.latitude);
                    map.put("location/from/lng", pickupLatLng.longitude);
                    map.put("location/to/lat", destinationLatLng.latitude);
                    map.put("location/to/lng", destinationLatLng.longitude);
                    map.put("distance", rideDistance);
                    customerRef.child(requestId).updateChildren(drivemapers);

                }
            }
        }
    }

    // GET CURRENTTIMESTAMP
    private Long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //FINDS LOCATION OF THE SYSTEM
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }        //PROVIDES THE LOCATION

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (getApplicationContext() != null) {
            //
            if (!customerId.equals("")) {
                //WE CALCULATE RIDE DISTANCE USING THIS PROTOCOL
                if (mLastLocation != null) {
                    rideDistance += mLastLocation.distanceTo(location) / 1000;
                }
            }
        }
        mLastLocation = location;

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = "";


                userId = user.getUid();

                DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("driversAvailable");
                DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("driversWorking");
                GeoFire geoFireAvailable = new GeoFire(refAvailable);
                GeoFire geoFireWorking = new GeoFire(refWorking);

                switch (customerId) {
                    case "":
                        if (geoFireWorking != null) {
                            geoFireWorking.removeLocation(userId);
                        }

                        if (geoFireAvailable != null) {
                            geoFireAvailable.setLocation(userId, new GeoLocation(latitude, longitude));
                        }
                        break;

                    default:
                        if (geoFireAvailable != null) {
                            geoFireAvailable.removeLocation(userId);
                        }
                        if (geoFireWorking != null) {
                            geoFireWorking.setLocation(userId, new GeoLocation(latitude, longitude));
                        }
                        break;
                }
            }
        }
    }

    //THIS CHECKS FOR INTERNET CONNECTION AND ENSURES TH
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mLocationRequest != null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    // THIS API WILL HELP US TO
    private void connectDriver() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DriverMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
        if (mLocationRequest != null) {
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    private void disconnectDriver() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = "";


                userId = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

                GeoFire geoFire = new GeoFire(ref);
                geoFire.removeLocation(userId);
            }
        }
    }

    final int LOCATION_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    // IS THAT ALL , I HAVE TO FIND OUT THE DETAILS OF THE THINGS MISSING HERE
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            if (mMap != null) {
                Polyline polyline = mMap.addPolyline(polyOptions);

                if (polylines != null) {
                    polylines.add(polyline);

                    Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onRoutingCancelled() {
    }

    private void erasePolylines() {
        if (polylines != null) {
            for (Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activitytradermapdrawer, menu);
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



    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            if (!role.equals("Trader"))
            {
                Intent intent = new Intent(DriverMapActivity.this, CartActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_search)
        {
            if (!role.equals("Trader"))
            {
                Intent intent = new Intent(DriverMapActivity.this, SearchProductsActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.nav_categories)
        {

        }
        else if (id == R.id.nav_settings)
        {
            if (!role.equals("Trader"))
            { FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String cusomerId = "";
                cusomerId = user.getUid();
                Intent intent = new Intent(DriverMapActivity.this, SettinsActivity.class);
                intent.putExtra("traderorcustomer", driverId);
                intent.putExtra("role", role);
                startActivity(intent);
            }

            else {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String cusomerId = "";
                cusomerId = user.getUid();
                Intent intent = new Intent(DriverMapActivity.this, SettinsActivity.class);
                intent.putExtra("traderorcustomer", driverId);
                intent.putExtra("role", role);
                startActivity(intent);

            }
        }
        else if (id == R.id.nav_logout)
        {
            if (!role.equals("Trader"))
            {
                Paper.book().destroy();

                Intent intent = new Intent(DriverMapActivity.this, com.simcoder.bimbo.WorkActivities.MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}