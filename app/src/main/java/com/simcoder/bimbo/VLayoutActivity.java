package com.simcoder.bimbo;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.simcoder.bimbo.VirtualLayoutManager.LayoutParams;

// import com.simcoder.bimbo.VirtualLayoutManager.LayoutParams;


// import com.simcoder.bimbo.VirtualLayoutManager.LayoutParams;
import com.simcoder.bimbo.RangeGridLayoutHelper.GridRangeStyle;
// import com.simcoder.bimbo.VirtualLayoutManager.LayoutParams;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author villadora
 */
public class VLayoutActivity extends Activity {
    //VLAYOUT IS THE MAIN ACTIVITY
//VLAYOUT IS THE MAIN ACTIVITY THAT HAS ALL THE DIVERSE ACTIVITIES
    private static final boolean BANNER_LAYOUT = true;

    private static final boolean FIX_LAYOUT = true;

    private static final boolean LINEAR_LAYOUT = true;

    private static final boolean SINGLE_LAYOUT = true;

    private static final boolean FLOAT_LAYOUT = true;

    private static final boolean ONEN_LAYOUT = true;

    private static final boolean COLUMN_LAYOUT = true;

    private static final boolean GRID_LAYOUT = true;

    private static final boolean STICKY_LAYOUT = true;
    public    static  final  int VIEWTYPE_GROUP = 0;
    public    static  final  int VIEWTYPE_PERSON = 1;
    public    static  final  int RESULT_CODE = 1000;

    private static final boolean STAGGER_LAYOUT = true;


    private RecyclerView mRecyclerView;
    private RecyclerView albumpicrecycler;
    private DatabaseReference mViewpagerDatabase;
    private  DatabaseReference myEcommerceDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseGetPic;
    String churchkey;
    String currentid;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query mQueryGetPic;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String title;
    String image;
    String desc;
    String traderkey;
      public ArrayList<ViewPagerObject> AdbannerList;
    public ArrayList<EcommerceGrid> EccomerceList;



      //THIS IS FOR VIEWPAGER
    String viepagertitle;
    String viewpagerimage;
    String viewpagerdesc;
    private ArrayList viewpagerHistory = new ArrayList<ViewPagerObject>();

    private ArrayList<ViewPagerObject> getDataSetHistory() {
        return viewpagerHistory;
    }
    private TextView mFirstText;
    private TextView mLastText;
    ImageView adbannerslothere;
    private TextView mCountText;
    ViewPager myadviewpager;
    private TextView mTotalOffsetText;

    private Runnable trigger;


    // THIS IS FOR GRID LAYOUT
      String  traderimage;
    String tradername;
    String productimage;
    String productname;
    String productprice;
    String likenumber;
    String likeid;
    String category;


   // public ArrayList<EcommerceGrid > EcommerceList;


    private ArrayList<EcommerceGrid> getEcommerceListHistory() {
        return EccomerceList;
    }
                 // GRID VIEW PRODUCT
    ImageView myvlayoutproductimage;
    ImageView  myvlayouttraderimage;
    ImageView numberoflikesimage;
    RecyclerView ecommercegridrecyclervie;

    // STICKY LAYOUT THAT WE HAVE
    public  List<String>  storecategorylist = new ArrayList<>();
    ImageView mySTICKYlayoutproductimage;
    ImageView  mySTICKYlayouttraderimage;
    ImageView mystickynumberoflikesimage;
    RecyclerView stickylayourrecycler;

           // FOOTER LAYOUT THAT WE HAVE
    RecyclerView footerrecycler;
    ImageView ecommercehome;
    ImageView  ecommercesearched;
    ImageView ecommercemessages;
    ImageView  ecommercecart;
    ImageView ecommerceprofile;

    // STAGGERED STAGLAYOUT PARAMETERS

    ImageView stagtradersimageonscreen;
    ImageView  stagproductimageonscreeen;
    ImageView  stagnumberoflikesimage;
    RecyclerView mystaggeredgridrecyler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //SWIPE REFRESH LAYOUT, THERE IS A SWIPE SCREEN

        //   mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        ;
        //  mFirstText = (TextView) findViewById(R.id.first);
        //  mLastText = (TextView) findViewById(R.id.last);
        //  mCountText = (TextView) findViewById(R.id.count);
        //   mTotalOffsetText = (TextView) findViewById(R.id.total_offset);


        myadviewpager = findViewById(R.id.pager);
        // VIEWPAGER STUFF HERE

        adbannerslothere = (ImageView) findViewById(R.id.viewpagerimages);

        // GRID VIEW PRODUCT
        myvlayoutproductimage = (ImageView) findViewById(R.id.productimageonscreeen);
        myvlayouttraderimage = (ImageView) findViewById(R.id.tradersimageonscreen);
        numberoflikesimage = (ImageView) findViewById(R.id.numberoflikesimage);
        ecommercegridrecyclervie = (RecyclerView) findViewById(R.id.ecommercegridrecyclerview);

        // STICY LAYOUT HERE
        mySTICKYlayoutproductimage = (ImageView) findViewById(R.id.stickyproductimageonscreeen);
        mySTICKYlayouttraderimage = (ImageView) findViewById(R.id.stickytradersimageonscreen);
        mystickynumberoflikesimage = (ImageView) findViewById(R.id.stickynumberoflikesimage);
        stickylayourrecycler = (RecyclerView) findViewById(R.id.fromstickylayoutrecycler);

        //FOOTER LAYOUT
        footerrecycler = (RecyclerView) findViewById(R.id.myfooterrecyler);
        ecommercehome = (ImageView) findViewById(R.id.ecommercehome);
        ecommercesearched = (ImageView) findViewById(R.id.ecommercesearched);
        ecommercemessages = (ImageView) findViewById(R.id.ecommercemessages);
        ecommercecart = (ImageView) findViewById(R.id.ecommercecart);
        ecommerceprofile = (ImageView) findViewById(R.id.ecommerceprofile);

        // STAGGERED LAYOUT GRID
        stagtradersimageonscreen = (ImageView) findViewById(R.id.stagtradersimageonscreen);
        stagproductimageonscreeen = (ImageView) findViewById(R.id.stagproductimageonscreeen);
        stagnumberoflikesimage = (ImageView) findViewById(R.id.stagnumberoflikesimage);
        mystaggeredgridrecyler = (RecyclerView) findViewById(R.id.mystaggeredgridrecyler);

        // HOW TO PULL UP
        // THIS IS THE RECYCLE SCREEN
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logintent = new Intent(VLayoutActivity.this, DriverLoginActivity.class);
                    logintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logintent);


                }
            }
        };
        //DATANASE MUST BE POPULATED WITH IMAGES OF ADS FOR VIEW PAGER


        mViewpagerDatabase = FirebaseDatabase.getInstance().getReference().child("AdS");

        DatabaseReference addDatabase = FirebaseDatabase.getInstance().getReference().child("ADS");
        //RIDEKEY
        mViewpagerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //RIDEKEY ==RIDE_ID
                    //BUT WHERE DID HE PASS IT TO HISTORY SINGLE ACTIVITY
                    // THIS IS WHERE RIDE ID SRARTED // WE STARTED POPULATING THE RIDE ID INTO ALL ACTIVITIES HERE
                    String addvertId = dataSnapshot.getKey();

                    // I have to add goods prices here Double Agreed Prices
                    //WE WILL CLEAR OFF PRICE CALCULATIONS

                    if (dataSnapshot.child("title").getValue() != null) {
                        viepagertitle = dataSnapshot.child("title").getValue().toString();
                    }

                    if (dataSnapshot.child("image").getValue() != null) {
                        viewpagerimage = dataSnapshot.child("image").getValue().toString();
                    }

                    if (dataSnapshot.child("desc").getValue() != null) {
                        viewpagerdesc = dataSnapshot.child("desc").getValue().toString();
                    }


                    // HERE WE GRAB ALL THE OBJECTS SO THAT THEY CAN ALL BE SHOWN ON THE VIEW HOLDERS
                    //VERY STRONGLY IT IS LEFT - RIGHT, FROM HERE TO VIEW HOLDERS
                    //we timestamp the ride


                    ViewPagerObject obj = new ViewPagerObject(viepagertitle, viewpagerimage, viewpagerdesc);
                    if (AdbannerList != null) {
                        AdbannerList.add(obj);

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        mDatabaseGetPic = FirebaseDatabase.getInstance().getReference().child("PERSONALPICS");

        mViewpagerDatabase.keepSynced(true);
        mDatabaseGetPic.keepSynced(true);

        churchkey = getIntent().getExtras().getString("churchkey");
        currentid = mAuth.getCurrentUser().getUid();
        mQueryGetPic = mDatabaseGetPic.orderByChild("uid").equalTo(currentid);
        // HOW TO PULL UP
        // THIS IS THE RECYCLE SCREEN
        //    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);


        // FOR GRID VIEW E COMMERCE

        myEcommerceDatabase = FirebaseDatabase.getInstance().getReference().child("Product");


        //RIDEKEY
        myEcommerceDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //RIDEKEY ==RIDE_ID
                    //BUT WHERE DID HE PASS IT TO HISTORY SINGLE ACTIVITY
                    // THIS IS WHERE RIDE ID SRARTED // WE STARTED POPULATING THE RIDE ID INTO ALL ACTIVITIES HERE
                    String productId = dataSnapshot.getKey();


                    // I have to add goods prices here Double Agreed Prices
                    //WE WILL CLEAR OFF PRICE CALCULATIONS

                    if (dataSnapshot.child("traderID").getValue() != null) {
                        traderkey = dataSnapshot.child(productId).child("traderID").getValue().toString();
                    }


                    if (dataSnapshot.child("traderImage").getValue() != null) {
                        traderimage = dataSnapshot.child(productId).child(traderkey).child("traderImage").getValue().toString();
                    }

                    if (dataSnapshot.child("traderName").getValue() != null) {
                        tradername = dataSnapshot.child(productId).child(traderkey).child("traderName").getValue().toString();
                    }

                    if (dataSnapshot.child("productImage").getValue() != null) {
                        productimage = dataSnapshot.child(productId).child("productImage").getValue().toString();
                    }

                    if (dataSnapshot.child("productName").getValue() != null) {
                        productname = dataSnapshot.child(productId).child("productName").getValue().toString();
                    }
                    if (dataSnapshot.child("price").getValue() != null) {
                        productprice = dataSnapshot.child(productId).child("price").getValue().toString();
                    }

                    if (dataSnapshot.child("traderID").getValue() != null) {
                        traderkey = dataSnapshot.child(productId).child("traderID").getValue().toString();
                    }

                    if (dataSnapshot.child("LikeID").getValue() != null) {
                        likeid = dataSnapshot.child(productId).child("LikeID").getValue().toString();
                    }

                    if (dataSnapshot.child("LikeNumber").getValue() != null) {
                        likenumber = dataSnapshot.child(productId).child("LikeID").child("LikeNumber").getValue().toString();
                    }

                    if (dataSnapshot.child("LikeNumber").getValue() != null) {
                        category = dataSnapshot.child(productId).child("categoryID").getValue().toString();
                    }

                    // HERE WE GRAB ALL THE OBJECTS SO THAT THEY CAN ALL BE SHOWN ON THE VIEW HOLDERS
                    //VERY STRONGLY IT IS LEFT - RIGHT, FROM HERE TO VIEW HOLDERS
                    //we timestamp the ride


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        EcommerceGrid ecommerceGrid = new EcommerceGrid(traderimage, tradername, productimage, productname, productprice, likenumber, category);
        if (EccomerceList != null) {
            EccomerceList.add(ecommerceGrid);
        }
        mDatabaseGetPic = FirebaseDatabase.getInstance().getReference().child("PERSONALPICS");
                if (mViewpagerDatabase != null) {
                    mViewpagerDatabase.keepSynced(true);
                }
                if (mDatabaseGetPic != null) {
                    mDatabaseGetPic.keepSynced(true);
                }
        churchkey = getIntent().getExtras().getString("churchkey");
        currentid = mAuth.getCurrentUser().getUid();


        // STICKY LAYOUT STUFF


        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        layoutManager.setPerformanceMonitor(new PerformanceMonitor() {

            long start;
            long end;

            @Override
            public void recordStart(String phase, View view) {
                start = System.currentTimeMillis();
            }

            @Override
            public void recordEnd(String phase, View view) {
                end = System.currentTimeMillis();
                Log.d("VLayoutActivity", view.getClass().getName() + " " + (end - start));
            }
        });
        // recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        //    @Override
        //      public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

        //        }

        //  @Override
        //   public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        //     mFirstText.setText("First: " + layoutManager.findFirstVisibleItemPosition());
        //     mLastText.setText("Existing: " + MainViewHolder.existing + " Created: " + MainViewHolder.createdTimes);
        //     mCountText.setText("Count: " + recyclerView.getChildCount());
        //     mTotalOffsetText.setText("Total Offset: " + layoutManager.getOffsetToStart());
        //  }
        // });

        recyclerView.setLayoutManager(layoutManager);

        // layoutManager.setReverseLayout(true);

        final RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((VirtualLayoutManager.LayoutParams) view.getLayoutParams()).getViewPosition();
                outRect.set(4, 4, 4, 4);
            }
        };

// HERE WE JUST SET UP THE BASIS WITH THE VLAYOUT BACKGROUND
// THIS VLAYOUT SERVES AS THE TEMPLATE UPON WHICH EVERYTHING IS BUILT
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        recyclerView.setRecycledViewPool(viewPool);

        // recyclerView.addItemDecoration(itemDecoration);

        viewPool.setMaxRecycledViews(0, 20);

        layoutManager.setRecycleOffset(300);

        // viewLifeCycleListener should be used with setRecycleOffset()
        layoutManager.setViewLifeCycleListener(new ViewLifeCycleListener() {
            @Override
            public void onAppearing(View view) {
//                Log.e("ViewLifeCycleTest", "onAppearing: " + view);
            }

            @Override
            public void onDisappearing(View view) {
//                Log.e("ViewLifeCycleTest", "onDisappearing: " + view);
            }

            @Override
            public void onAppeared(View view) {
//                Log.e("ViewLifeCycleTest", "onAppeared: " + view);
            }

            @Override
            public void onDisappeared(View view) {
//                Log.e("ViewLifeCycleTest", "onDisappeared: " + view);
            }
        });

        layoutManager.setLayoutManagerCanScrollListener(new LayoutManagerCanScrollListener() {
            @Override
            public boolean canScrollVertically() {
                Log.i("vlayout", "canScrollVertically: ");
                return true;
            }

            @Override
            public boolean canScrollHorizontally() {
                Log.i("vlayout", "canScrollHorizontally: ");
                return true;
            }
        });

        // DELEGATE LAYOUT HELPS TO KEEP OR THE LAYOUT HERE
        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);

        recyclerView.setAdapter(delegateAdapter);

        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        if (BANNER_LAYOUT) {
            // FOR THIS NEW ADAPTER LAYOUT THIS IS A NEW ADAPTER LAYOUT THAT IS PUT UNTO THE INTERNET
            if (AdbannerList != null) {
            adapters.add(new SubAdapter(this, new LinearLayoutHelper(), 1) {

                @Override
                public void onViewRecycled(MainViewHolder holder) {

                    if (holder.itemView instanceof ViewPager) {
                        //HOW TO REFERENCE THE HOLDERS
                        ((ViewPager) holder.itemView).setAdapter(null);
                        // I HAVE TO KEEP THE VIEWPAGER SOMEWHERE
                    }
                }

                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // YOU CAN USE THIS TO GET MULTIPLE PICURES AND IMAGES UP THERE
                    if (viewType == 1)
                        return new MainViewHolder(
                                // HERE WE GET ALL THE VIEWS FROM THE VIEWPAGER LAYOUT PAGE, VIEWS SUCH AS TEXTVIEW, VIEWPAGER ETC.
                                LayoutInflater.from(VLayoutActivity.this).inflate(R.layout.fragment_view_pager, parent, false));

                    return super.onCreateViewHolder(parent, viewType);
                }

                @Override
                public int getItemViewType(int position) {
                    return 1;
                }

                @Override
                protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {

                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    if (holder.itemView instanceof ViewPager) {
                        //HOW DO WE POPULATE IT
                        myadviewpager = (ViewPager) holder.itemView;

                        myadviewpager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200) {
                            @Override
                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {

                            }
                        });

                        holder.itemView.setLayoutParams(
                                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) {
                                    @Override
                                    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                        return null;
                                    }

                                    @Override
                                    public void onBindViewHolder(MainViewHolder holder, int position) {

                                    }
                                });


                        holder.bannertext.setText(AdbannerList.get(position).getTitle());
                        holder.adbannerslot.setImageResource(Integer.parseInt(AdbannerList.get(position).getImage()));

                        Glide.with(getApplication()).load(AdbannerList.get(position).getImage()).into(adbannerslothere);


                        // from position to get adapter
                        if (myadviewpager != null) {
                            myadviewpager.setAdapter(new PagerAdapter(this, viewPool));
                        }

                    }
                }

                public int getItemCount() {


                    return AdbannerList.size();
                }
            });

        }
        }

        //{
        //    GridLayoutHelper helper = new GridLayoutHelper(10);
        //    helper.setAspectRatio(4f);
        //    helper.setGap(10);
        //    adapters.add(new SubAdapter(this, helper, 80));
        //}

        if (FLOAT_LAYOUT) {
            FloatLayoutHelper layoutHelper = new FloatLayoutHelper();
            layoutHelper.setAlignType(FixLayoutHelper.BOTTOM_RIGHT);
            layoutHelper.setDefaultLocation(100, 400);
            VirtualLayoutManager.LayoutParams layoutParams = new LayoutParams(150, 150) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            };
            if ( adapters != null) {
                adapters.add(new FloatSubAdapter(this, layoutHelper, 1, layoutParams));
            }}
        // MY LINEARR LAYOUT FOR HORIZONTAL
        if (LINEAR_LAYOUT) {
            LinearLayoutHelper layoutHelper1 = new LinearLayoutHelper();
            layoutHelper1.setBgColor(Color.YELLOW);
            layoutHelper1.setAspectRatio(2.0f);
            layoutHelper1.setMargin(10, 10, 10, 10);
            layoutHelper1.setPadding(10, 10, 10, 10);
            LinearLayoutHelper layoutHelper2 = new LinearLayoutHelper();
            layoutHelper2.setAspectRatio(4.0f);
            layoutHelper2.setDividerHeight(10);
            layoutHelper2.setMargin(10, 0, 10, 10);
            layoutHelper2.setPadding(10, 0, 10, 10);
            layoutHelper2.setBgColor(0xFFF5A623);
            final Handler mainHandler = new Handler(Looper.getMainLooper());
            if (adapters != null) {
                adapters.add(new SubAdapter(this, layoutHelper1, 1) {
                    @Override
                    public void onBindViewHolder(final MainViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        final SubAdapter subAdapter = this;
                        //mainHandler.postDelayed(new Runnable() {
                        //    @Override
                        //    public void run() {
                        //        //delegateAdapter.removeAdapter(subAdapter);
                        //        //notifyItemRemoved(1);
                        //        holder.itemView.setVisibility(View.GONE);
                        //        notifyItemChanged(1);
                        //        layoutManager.runAdjustLayout();
                        //    }
                        //}, 2000L);
                    }
                });
            }
            if (adapters != null) {
                adapters.add(new SubAdapter(this, layoutHelper2, 6) {

                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        if (position % 2 == 0) {
                            VirtualLayoutManager.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                                @Override
                                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    return null;
                                }

                                @Override
                                public void onBindViewHolder(MainViewHolder holder, int position) {

                                }
                            };
                            layoutParams.mAspectRatio = 5;
                            holder.itemView.setLayoutParams(layoutParams);
                        }
                    }
                });
            }
        }
        {
            RangeGridLayoutHelper ourlayoutHelper = new RangeGridLayoutHelper(4);
            ourlayoutHelper.setBgColor(Color.GREEN);
            ourlayoutHelper.setWeights(new float[]{20f, 26.665f});
            ourlayoutHelper.setPadding(15, 15, 15, 15);
            ourlayoutHelper.setMargin(15, 50, 15, 150);
            ourlayoutHelper.setHGap(10);
            ourlayoutHelper.setVGap(10);
            GridRangeStyle rangeStyle = new GridRangeStyle();
            rangeStyle.setBgColor(Color.RED);
            rangeStyle.setSpanCount(2);
            rangeStyle.setWeights(new float[]{46.665f});
            rangeStyle.setPadding(15, 15, 15, 15);
            rangeStyle.setMargin(15, 15, 15, 15);
            rangeStyle.setHGap(5);
            rangeStyle.setVGap(5);
            ourlayoutHelper.addRangeStyle(0, 7, rangeStyle);

            GridRangeStyle rangeStyle1 = new GridRangeStyle();
            rangeStyle1.setBgColor(Color.YELLOW);
            rangeStyle1.setSpanCount(2);
            rangeStyle1.setWeights(new float[]{46.665f});
            rangeStyle1.setPadding(15, 15, 15, 15);
            rangeStyle1.setMargin(15, 15, 15, 15);
            rangeStyle1.setHGap(5);
            rangeStyle1.setVGap(5);
            ourlayoutHelper.addRangeStyle(8, 15, rangeStyle1);

            GridRangeStyle rangeStyle2 = new GridRangeStyle();
            rangeStyle2.setBgColor(Color.CYAN);
            rangeStyle2.setSpanCount(2);
            rangeStyle2.setWeights(new float[]{46.665f});
            rangeStyle2.setPadding(15, 15, 15, 15);
            rangeStyle2.setMargin(15, 15, 15, 15);
            rangeStyle2.setHGap(5);
            rangeStyle2.setVGap(5);
            ourlayoutHelper.addRangeStyle(16, 22, rangeStyle2);
            GridRangeStyle rangeStyle3 = new GridRangeStyle();
            rangeStyle3.setBgColor(Color.DKGRAY);
            rangeStyle3.setSpanCount(1);
            rangeStyle3.setWeights(new float[]{46.665f});
            rangeStyle3.setPadding(15, 15, 15, 15);
            rangeStyle3.setMargin(15, 15, 15, 15);
            rangeStyle3.setHGap(5);
            rangeStyle3.setVGap(5);
            rangeStyle2.addChildRangeStyle(0, 2, rangeStyle3);
            GridRangeStyle rangeStyle4 = new GridRangeStyle();
            rangeStyle4.setBgColor(Color.BLUE);
            rangeStyle4.setSpanCount(2);
            rangeStyle4.setWeights(new float[]{46.665f});
            rangeStyle4.setPadding(15, 15, 15, 15);
            rangeStyle4.setMargin(15, 15, 15, 15);
            rangeStyle4.setHGap(5);
            rangeStyle4.setVGap(5);
            rangeStyle2.addChildRangeStyle(3, 6, rangeStyle4);

            GridRangeStyle rangeStyle5 = new GridRangeStyle();
            rangeStyle5.setBgColor(Color.RED);
            rangeStyle5.setSpanCount(2);
            rangeStyle5.setPadding(15, 15, 15, 15);
            rangeStyle5.setMargin(15, 15, 15, 15);
            rangeStyle5.setHGap(5);
            rangeStyle5.setVGap(5);
            ourlayoutHelper.addRangeStyle(23, 30, rangeStyle5);
            GridRangeStyle rangeStyle6 = new GridRangeStyle();
            rangeStyle6.setBgColor(Color.MAGENTA);
            rangeStyle6.setSpanCount(2);
            rangeStyle6.setPadding(15, 15, 15, 15);
            rangeStyle6.setMargin(15, 15, 15, 15);
            rangeStyle6.setHGap(5);
            rangeStyle6.setVGap(5);
            rangeStyle5.addChildRangeStyle(0, 7, rangeStyle6);

            if (adapters != null) {
                adapters.add(new RangeGridLayoutAdapter(this, ourlayoutHelper, 23));
            }
        }
        {
            SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
            singleLayoutHelper.setBgColor(Color.BLUE);
            singleLayoutHelper.setMargin(0, 30, 0, 200);
            adapters.add(new SingleLayoutAdapter(this, singleLayoutHelper, 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            }));
        }

        if (STICKY_LAYOUT) {
            final StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
            //layoutHelper.setOffset(100);
            stickyLayoutHelper.setAspectRatio(4);

               if (adapters != null) {
                   adapters.add(new StickyLayoutAdapter(this, stickyLayoutHelper, 1, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100) {

                       Context context = null;


                       @Override
                       public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                           // IT WIIL POPULATE EVERYTHING, SORT IT OUT, PRODUCE THE NEW RESULT,
                           // CHECK WHETHER CATEGORIES HAVE ALREADY BEEN REPEATED AND THEN SET THE VIEWTYPES

                           Common categoryarraylist = new Common();
                           Common.sortList(EccomerceList);
                           categoryarraylist.populatealphabetandproductlist(EccomerceList);
                           categoryarraylist.getcategorywithequalnames(storecategorylist);
                           viewType = categoryarraylist.viewType;


                           if (viewType == Common.CATEGORY) {


                               ViewGroup group = (ViewGroup) getLayoutInflater().inflate(R.layout.headerlayout, parent, false);
                               MainViewHolder mainViewHolder = new MainViewHolder(group);


                               // PASS IN THE LIST TO ANOTHER ACTIVITY


                               return mainViewHolder;

                           } else if (viewType == Common.ECOMMERCEVIEW) {


                               ViewGroup group = (ViewGroup) getLayoutInflater().inflate(R.layout.fromstickylayout, parent, false);
                               MainViewHolder mainViewHolder = new MainViewHolder(group);


                               return mainViewHolder;
                           } else {
                               ViewGroup group = (ViewGroup) getLayoutInflater().inflate(R.layout.stickynoterecycler, parent, false);
                               MainViewHolder mainViewHolder = new MainViewHolder(group);


                               return mainViewHolder;

                           }


                       }
                       // I HAVE TO FIX GETITEMVIEWTYPE


                       public int getItemCount() {
                           return EccomerceList.size();
                       }


                       @Override
                       public void onBindViewHolder(MainViewHolder holder, int position) {
                           // only vertical


                           ecommercegridrecyclervie = (RecyclerView) holder.itemView;


                           LayoutParams lp = (LayoutParams) holder.itemView.getLayoutParams();

                           if (storecategorylist.get(position) == EccomerceList.get(position).getCategory()) {
                               //GRABS THE BANNER TEXT
                               holder.categoryheaderbanner.setText(storecategorylist.get(position).toString());

                               // GRABBING TO ECOMMERCE VIEW HOLDER

                               holder.stickytraderimage.setImageResource(Integer.parseInt(EccomerceList.get(position).getTraderimage()));
                               holder.stickyproductimageview.setImageResource(Integer.parseInt(EccomerceList.get(position).getProductimage()));
                               holder.stickynumberoflikesimage.setImageResource(R.drawable.foollow_heart);

                               holder.stagcategoryhere.setText(EccomerceList.get(position).getCategory());
                               holder.stickyproductname.setText(EccomerceList.get(position).getProductname());
                               holder.stickyproductprice.setText(EccomerceList.get(position).getProductprice());
                               holder.stickytradername.setText(EccomerceList.get(position).getTradername());
                               holder.stickynumberoflikeshere.setText(EccomerceList.get(position).getLikenumber());

                           }


                           Glide.with(getApplication()).load(Integer.parseInt(EccomerceList.get(position).getProductimage())).into(mySTICKYlayoutproductimage);
                           Glide.with(getApplication()).load(Integer.parseInt(EccomerceList.get(position).getTraderimage())).into(mySTICKYlayouttraderimage);
                           Glide.with(getApplication()).load((R.drawable.foollow_heart)).into(mystickynumberoflikesimage);


                           // from position to get adapter
                           //sTICKY ADAPTER
                           if (stickylayourrecycler != null) {
                               stickylayourrecycler.setAdapter(new StickyLayoutAdapter(context, stickyLayoutHelper, 80));

                           }
                       }


                   }));
               }

            if (adapters != null) {
                adapters.add(
                        new FooterAdapter(recyclerView, VLayoutActivity.this, new GridLayoutHelper(1), 1) {

                            Context ourcontext = null;


                            @Override
                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                LayoutInflater.from(VLayoutActivity.this).inflate(R.layout.footer, parent, false);
                                return super.onCreateViewHolder(parent, viewType);
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {

                                holder.footerecommercehome.setImageResource(R.drawable.home_icon);
                                holder.footerecommercesearched.setImageResource(R.drawable.basedonsearchhere);
                                holder.footerecommercemessages.setImageResource(R.drawable.commentsectionagain);
                                holder.footerecommercecart.setImageResource(R.drawable.cart);
                                holder.footerecommerceprofile.setImageResource(R.drawable.profile_imagehere);


                                Glide.with(getApplication()).load(R.drawable.home_icon).into(ecommercehome);
                                Glide.with(getApplication()).load(R.drawable.basedonsearchhere).into(ecommercesearched);
                                Glide.with(getApplication()).load(R.drawable.commentsectionagain).into(ecommercemessages);
                                Glide.with(getApplication()).load(R.drawable.cart).into(ecommercecart);
                                Glide.with(getApplication()).load(R.drawable.profile_imagehere).into(ecommerceprofile);


                                // from position to get adapter
                                footerrecycler.setAdapter(new FooterAdapter(footerrecycler, ourcontext, stickyLayoutHelper, 80));


                                super.onBindViewHolder(holder, position);
                            }

                            @Override
                            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
                                super.onBindViewHolderWithOffset(holder, position, offsetTotal);
                            }

                            @Override
                            public int getItemViewType(int position) {
                                return super.getItemViewType(position);
                            }

                            @Override
                            public int getItemCount() {
                                return super.getItemCount();
                            }

                            @Override
                            public void toggleFoot() {
                                super.toggleFoot();
                            }
                        });


            }
        }
        ;

        //{
        //    final StaggeredGridLayoutHelper helper = new StaggeredGridLayoutHelper(3, 10);
        //    helper.setBgColor(0xFF86345A);
        //    adapters.add(new SubAdapter(this, helper, 4) {
        //
        //        @Override
        //        public void onBindViewHolder(MainViewHolder holder, int position) {
        //            super.onBindViewHolder(holder, position);
        //            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        //            if (position % 2 == 0) {
        //                layoutParams.mAspectRatio = 1.0f;
        //            } else {
        //                layoutParams.height = 340 + position % 7 * 20;
        //            }
        //            holder.itemView.setLayoutParams(layoutParams);
        //        }
        //    });
        //}
        {

            final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3, 4);
            gridLayoutHelper.setBgColor(0xFF86345A);
            if (adapters != null) {
                adapters.add(new GridSubAdapter(this, gridLayoutHelper, 4) {
                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        VirtualLayoutManager.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                            @Override

                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {

                            }
                        };
                        holder.itemView.setLayoutParams(layoutParams);
                    }
                });


            }
        }
        {
            RangeGridLayoutHelper layoutHelper = new RangeGridLayoutHelper(4);
            layoutHelper.setBgColor(Color.GREEN);
            layoutHelper.setWeights(new float[]{20f, 26.665f});
            layoutHelper.setPadding(15, 15, 15, 15);
            layoutHelper.setMargin(15, 15, 15, 15);
            layoutHelper.setHGap(10);
            layoutHelper.setVGap(10);
            GridRangeStyle rangeStyle = new GridRangeStyle();
            rangeStyle.setBgColor(Color.RED);
            rangeStyle.setSpanCount(2);
            rangeStyle.setWeights(new float[]{46.665f});
            rangeStyle.setPadding(15, 15, 15, 15);
            rangeStyle.setMargin(15, 15, 15, 15);
            rangeStyle.setHGap(5);
            rangeStyle.setVGap(5);
            layoutHelper.addRangeStyle(4, 7, rangeStyle);
            GridRangeStyle rangeStyle1 = new GridRangeStyle();
            rangeStyle1.setBgColor(Color.YELLOW);
            rangeStyle1.setSpanCount(2);
            rangeStyle1.setWeights(new float[]{46.665f});
            rangeStyle1.setPadding(15, 15, 15, 15);
            rangeStyle1.setMargin(15, 15, 15, 15);
            rangeStyle1.setHGap(5);
            rangeStyle1.setVGap(5);
            layoutHelper.addRangeStyle(8, 11, rangeStyle1);
            adapters.add(new RangeGridLayoutAdapter(this, layoutHelper, 16));

        }

        if (SINGLE_LAYOUT) {
            SingleLayoutHelper firstsinglelayoutHelper = new SingleLayoutHelper();
            firstsinglelayoutHelper.setBgColor(Color.rgb(135, 225, 90));
            firstsinglelayoutHelper.setAspectRatio(4);
            firstsinglelayoutHelper.setMargin(10, 20, 10, 20);
            firstsinglelayoutHelper.setPadding(10, 10, 10, 10);
            if (adapters != null) {
                adapters.add(new SingleLayoutAdapter(this, firstsinglelayoutHelper, 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100) {
                    @Override
                    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {

                    }
                }));
            }
        }
        if (COLUMN_LAYOUT) {
            ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
            columnLayoutHelper.setBgColor(0xff00f0f0);
            columnLayoutHelper.setWeights(new float[]{40.0f, Float.NaN, 40});

            if (adapters != null) {
                adapters.add(new CollumnLayoutAdapter(this, columnLayoutHelper, 5) {

                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        if (position == 0) {
                            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                                @Override
                                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    return null;
                                }

                                @Override
                                public void onBindViewHolder(MainViewHolder holder, int position) {

                                }
                            };
                            layoutParams.mAspectRatio = 4;
                            holder.itemView.setLayoutParams(layoutParams);
                        } else {
                            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                                @Override
                                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    return null;
                                }

                                @Override
                                public void onBindViewHolder(MainViewHolder holder, int position) {

                                }
                            };
                            layoutParams.mAspectRatio = Float.NaN;
                            holder.itemView.setLayoutParams(layoutParams);
                        }
                    }

                });
            }
        }
        if (ONEN_LAYOUT) {
            OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
            helper.setBgColor(0xff876384);
            helper.setAspectRatio(4.0f);
            helper.setColWeights(new float[]{40f, 45f});
            helper.setMargin(10, 20, 10, 20);
            helper.setPadding(10, 10, 10, 10);
             if (adapters !=null) {
                 adapters.add(new OnePlusNLayoutAdapter(this, helper, 2));
             }}

        if (ONEN_LAYOUT) {
            OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
            helper.setBgColor(0xffef8ba3);
            helper.setAspectRatio(2.0f);
            helper.setColWeights(new float[]{40f});
            helper.setRowWeight(30f);
            helper.setMargin(10, 20, 10, 20);
            helper.setPadding(10, 10, 10, 10);
            if (adapters != null) {
                adapters.add(new OnePlusNLayoutAdapter(this, helper, 4) {
                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        VirtualLayoutManager.LayoutParams lp = (VirtualLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                        if (position == 0) {
                            lp.rightMargin = 1;
                        } else if (position == 1) {

                        } else if (position == 2) {
                            lp.topMargin = 1;
                            lp.rightMargin = 1;
                        }
                    }
                });
            }
        }
        if (ONEN_LAYOUT) {
            adapters.add(new OnePlusNLayoutAdapter(this, new OnePlusNLayoutHelper(), 0));
            OnePlusNLayoutHelper helper = new OnePlusNLayoutHelper();
            helper.setBgColor(0xff87e543);
            helper.setAspectRatio(1.8f);
            helper.setColWeights(new float[]{33.33f, 50f, 40f});
            helper.setMargin(10, 20, 10, 20);
            helper.setPadding(10, 10, 10, 10);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) {

                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            };
            adapters.add(new OnePlusNLayoutAdapter(this, helper, 3, lp) {
                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    LayoutParams lp = (LayoutParams) holder.itemView.getLayoutParams();
                    if (position == 0) {
                        lp.rightMargin = 1;
                    }
                }
            });
        }

        if (COLUMN_LAYOUT) {
                if (adapters != null) {
                    adapters.add(new CollumnLayoutAdapter(this, new ColumnLayoutHelper(), 0));
                    adapters.add(new CollumnLayoutAdapter(this, new ColumnLayoutHelper(), 4));
                } }

        if (FIX_LAYOUT) {
            FixLayoutHelper layoutHelper = new FixLayoutHelper(10, 10);
            if (adapters != null) {
                adapters.add(new FixLayoutAdapter(this, layoutHelper, 0));

                layoutHelper = new FixLayoutHelper(FixLayoutHelper.TOP_RIGHT, 20, 20);

                adapters.add(new FixLayoutAdapter(this, layoutHelper, 1) {
                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200) {
                            @Override
                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {

                            }
                        };
                        holder.itemView.setLayoutParams(layoutParams);
                    }
                });
            }
        }
        //if (STICKY_LAYOUT) {
        //    StickyLayoutHelper layoutHelper = new StickyLayoutHelper(false);
        //    adapters.add(new SubAdapter(this, layoutHelper, 0));
        //    layoutHelper = new StickyLayoutHelper(false);
        //    layoutHelper.setOffset(100);
        //    adapters.add(new SubAdapter(this, layoutHelper, 1, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100)));
        //}

        if (GRID_LAYOUT) {
            GridLayoutHelper layoutHelper = new GridLayoutHelper(2);
            layoutHelper.setMargin(7, 0, 7, 0);
            layoutHelper.setWeights(new float[]{46.665f});
            layoutHelper.setHGap(3);
            adapters.add(new GridSubAdapter(this, layoutHelper, 2));

            layoutHelper = new GridLayoutHelper(4);
            layoutHelper.setWeights(new float[]{20f, 26.665f});
            layoutHelper.setMargin(7, 0, 7, 0);
            layoutHelper.setHGap(3);
             if (adapters != null) {
                 adapters.add(new GridSubAdapter(this, layoutHelper, 8));

             }}

        if (GRID_LAYOUT) {

            if (adapters != null) {
                adapters.add(new GridSubAdapter(this, new GridLayoutHelper(4), 0));
            }
            final GridLayoutHelper mygridhelperhere = new GridLayoutHelper(4);

            mygridhelperhere.setAspectRatio(4f);
            //helper.setColWeights(new float[]{40, 20, 30, 30});
            // helper.setMargin(0, 10, 0, 10);
            mygridhelperhere.setGap(10);

            if (adapters != null) {
                adapters.add(new GridSubAdapter(this, mygridhelperhere, 80) {

                    Context context = null;


                    @Override
                    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        if (viewType == 1)
                            return new MainViewHolder(
                                    // HERE WE GET ALL THE VIEWS FROM THE VIEWPAGER LAYOUT PAGE, VIEWS SUCH AS TEXTVIEW, VIEWPAGER ETC.
                                    LayoutInflater.from(VLayoutActivity.this).inflate(R.layout.viewproductactivity, parent, false));

                        return super.onCreateViewHolder(parent, viewType);
                    }


                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {

                        //HOW DO WE POPULATE IT
                        ecommercegridrecyclervie = (RecyclerView) holder.itemView;


                        LayoutParams lp = (LayoutParams) holder.itemView.getLayoutParams();
                        lp.bottomMargin = 1;
                        lp.rightMargin = 1;
                        //    holder.itemView.setLayoutParams(
                        //          new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        if (ecommercegridrecyclervie != null) {
                            ecommercegridrecyclervie.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200) {
                                @Override
                                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                    return null;
                                }


                                @Override
                                public void onBindViewHolder(MainViewHolder holder, int position) {

                                    holder.productimageview.setImageResource(Integer.parseInt(EccomerceList.get(position).getProductimage()));
                                    holder.traderimage.setImageResource(Integer.parseInt(EccomerceList.get(position).getTraderimage()));
                                    holder.numberoflikesimage.setImageResource(R.drawable.foollow_heart);

                                    holder.productname.setText(EccomerceList.get(position).getProductname());
                                    holder.productprice.setText(EccomerceList.get(position).getProductprice());
                                    holder.tradername.setText(EccomerceList.get(position).getTradername());
                                    holder.numberoflikes.setText(EccomerceList.get(position).getLikenumber());


                                    Glide.with(getApplication()).load(Integer.parseInt(EccomerceList.get(position).getProductimage())).into(myvlayoutproductimage);
                                    Glide.with(getApplication()).load(Integer.parseInt(EccomerceList.get(position).getTraderimage())).into(myvlayouttraderimage);
                                    Glide.with(getApplication()).load((R.drawable.foollow_heart)).into(numberoflikesimage);

                                    // from position to get adapter
                                    ecommercegridrecyclervie.setAdapter(new GridSubAdapter(context, mygridhelperhere, 80));
                                }
                            });


                        }
                    }


                });
            }

            if (adapters != null) {
                adapters.add(
                        new FooterAdapter(recyclerView, VLayoutActivity.this, new GridLayoutHelper(1), 1) {

                            Context ourcontext = null;


                            @Override
                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                LayoutInflater.from(VLayoutActivity.this).inflate(R.layout.footer, parent, false);
                                return super.onCreateViewHolder(parent, viewType);
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {

                                holder.footerecommercehome.setImageResource(R.drawable.home_icon);
                                holder.footerecommercesearched.setImageResource(R.drawable.basedonsearchhere);
                                holder.footerecommercemessages.setImageResource(R.drawable.commentsectionagain);
                                holder.footerecommercecart.setImageResource(R.drawable.cart);
                                holder.footerecommerceprofile.setImageResource(R.drawable.profile_imagehere);

                                Glide.with(getApplication()).load(R.drawable.home_icon).into(ecommercehome);
                                Glide.with(getApplication()).load(R.drawable.basedonsearchhere).into(ecommercesearched);
                                Glide.with(getApplication()).load(R.drawable.commentsectionagain).into(ecommercemessages);
                                Glide.with(getApplication()).load(R.drawable.cart).into(ecommercecart);
                                Glide.with(getApplication()).load(R.drawable.profile_imagehere).into(ecommerceprofile);


                                // from position to get adapter
                                if (footerrecycler != null) {
                                    footerrecycler.setAdapter(new FooterAdapter(footerrecycler, ourcontext, mygridhelperhere, 80));


                                    super.onBindViewHolder(holder, position);
                                }

                            }

                            @Override
                            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
                                super.onBindViewHolderWithOffset(holder, position, offsetTotal);
                            }

                            @Override
                            public int getItemViewType(int position) {
                                return super.getItemViewType(position);
                            }

                            @Override
                            public int getItemCount() {
                                return super.getItemCount();
                            }

                            @Override
                            public void toggleFoot() {
                                super.toggleFoot();
                            }
                        });


            }
        }
        if (FIX_LAYOUT) {
            if (adapters != null) {
                adapters.add(new FixLayoutAdapter(this, new ScrollFixLayoutHelper(20, 20), 1) {
                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        LayoutParams layoutParams = new LayoutParams(200, 200) {
                            @Override
                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {

                            }
                        };
                        holder.itemView.setLayoutParams(layoutParams);
                    }
                });
            }
        }
        if (adapters != null) {
            if (LINEAR_LAYOUT)
                adapters.add(new SubAdapter(this, new LinearLayoutHelper(), 10));
        }
        if (GRID_LAYOUT) {
            GridLayoutHelper helper = new GridLayoutHelper(3);
            helper.setMargin(0, 10, 0, 10);
            if (adapters != null) {
                adapters.add(new GridSubAdapter(this, helper, 3));
            }}
        // IMPLEMENT STAGERRED GRID VIEW HERE
        if (STAGGER_LAYOUT) {
            // adapters.add(new SubAdapter(this, new StaggeredGridLayoutHelper(2, 0), 0));
            final StaggeredGridLayoutHelper mystaggeredgridhelper = new StaggeredGridLayoutHelper(2, 10);
            mystaggeredgridhelper.setMargin(20, 10, 10, 10);
            mystaggeredgridhelper.setPadding(10, 10, 20, 10);
            mystaggeredgridhelper.setBgColor(0xFF86345A);
            if (adapters != null) {
                adapters.add(new StaggeredGridLayoutAdapters(this, mystaggeredgridhelper, 27) {
                    Context context = null;

                    @Override
                    public LayoutHelper onCreateLayoutHelper() {
                        return super.onCreateLayoutHelper();
                    }

                    @Override
                    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        LayoutInflater.from(VLayoutActivity.this).inflate(R.layout.staggeredgridlayout, parent, false);

                        return super.onCreateViewHolder(parent, viewType);
                    }

                    @Override
                    public void onBindViewHolder(MainViewHolder holder, int position) {
                        super.onBindViewHolder(holder, position);
                        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200) {
                            @Override
                            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(MainViewHolder holder, int position) {
                                // NO TRADER IMAGE
                                holder.stagtradersimageonscreen.setImageResource(Integer.parseInt(EccomerceList.get(position).getTraderimage()));
                                holder.stagproductimageonscreeen.setImageResource(Integer.parseInt(EccomerceList.get(position).getProductimage()));
                                holder.stagnumberoflikesimage.setImageResource((R.drawable.foollow_heart));


                                holder.stagtheproductname.setText(EccomerceList.get(position).getProductname());
                                holder.stagtheproductprice.setText(EccomerceList.get(position).getProductprice());
                                holder.stagtradernamehere.setText(EccomerceList.get(position).getTradername());
                                holder.stagnumberoflikes.setText(EccomerceList.get(position).getLikenumber());
                                holder.stagcategoryhere.setText(EccomerceList.get(position).getCategory());


                                Glide.with(getApplication()).load(Integer.parseInt(EccomerceList.get(position).getProductimage())).into(stagproductimageonscreeen);
                                Glide.with(getApplication()).load(Integer.parseInt(EccomerceList.get(position).getTraderimage())).into(stagtradersimageonscreen);
                                Glide.with(getApplication()).load(R.drawable.foollow_heart).into(stagnumberoflikesimage);
                                // from position to get adapter
                                if (mystaggeredgridrecyler != null) {
                                    mystaggeredgridrecyler.setAdapter(new StaggeredGridLayoutAdapters(context, mystaggeredgridhelper, 80));


                                }
                            }
                        };
                        if (position % 2 == 0) {
                            layoutParams.mAspectRatio = 1.0f;
                        } else {
                            layoutParams.height = 340 + position % 7 * 20;
                        }
                        holder.itemView.setLayoutParams(layoutParams);
                    }


                });
            }
        }

        if (COLUMN_LAYOUT) {
            // adapters.add(new SubAdapter(this, new ColumnLayoutHelper(), 3));
        }

        if (GRID_LAYOUT) {
            // adapters.add(new SubAdapter(this, new GridLayoutHelper(4), 24));
        }
        if (adapters != null) {
            adapters.add(
                    new FooterAdapter(recyclerView, VLayoutActivity.this, new GridLayoutHelper(1), 1));
        }
                if (delegateAdapter != null) {
                    delegateAdapter.setAdapters(adapters);
                }

        final Handler mainHandler = new Handler(Looper.getMainLooper());

        trigger = new Runnable() {
            @Override
            public void run() {
                //recyclerView.scrollToPosition(22);
                //recyclerView.getAdapter().notifyDataSetChanged();
                //mainHandler.postDelayed(trigger, 1000);
                //List<DelegateAdapter.Adapter> newAdapters = new ArrayList<>();
                //newAdapters.add((new SubAdapter(VLayoutActivity.this, new ColumnLayoutHelper(), 3)));
                //newAdapters.add((new SubAdapter(VLayoutActivity.this, new GridLayoutHelper(4), 24)));
                //delegateAdapter.addAdapter(0, new SubAdapter(VLayoutActivity.this, new ColumnLayoutHelper(), 3));
                //delegateAdapter.addAdapter(1, new SubAdapter(VLayoutActivity.this, new GridLayoutHelper(4), 24));
                //delegateAdapter.notifyDataSetChanged();
            }
        };


        mainHandler.postDelayed(trigger, 1000);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 2000L);
                }
            });
            setListenerToRootView();
        }
    }

    boolean isOpened = false;

    public void setListenerToRootView() {
        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                    if (isOpened == false) {
                        //Do two things, make the view top visible and the editText smaller
                    }
                    isOpened = true;
                } else if (isOpened == true) {
                    isOpened = false;
                    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_view);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    static class FooterAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private RecyclerView mRecyclerView;

        private Context mContext;

        private LayoutHelper mLayoutHelper;

        private LayoutParams mLayoutParams;
        private int mCount = 0;

        private boolean showFooter = false;

        public FooterAdapter(RecyclerView recyclerView, Context context, LayoutHelper layoutHelper, int count) {
            this(recyclerView, context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public FooterAdapter(RecyclerView recyclerView, Context context, LayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mRecyclerView = recyclerView;
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public int getItemViewType(int position) {
            return 100;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.footer, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            LayoutParams lp = (LayoutParams) holder.itemView.getLayoutParams();
            if (showFooter) {
                lp.height = 300;
            } else {
                lp.height = 0;
            }
            holder.itemView.setLayoutParams(lp);




        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));


        }

        @Override
        public int getItemCount() {
            return mCount;
        }

        public void toggleFoot() {

            this.showFooter = !this.showFooter;
            if (mRecyclerView != null) {
                mRecyclerView.getAdapter().notifyItemChanged(205);
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.scrollToPosition(205);
                        mRecyclerView.requestLayout();
                    }
                });
            }
        }
    }

    // RecyclableViewPager

    static class PagerAdapter extends RecyclablePagerAdapter<MainViewHolder> {


        private Context context;


        public PagerAdapter(SubAdapter adapter, RecyclerView.RecycledViewPool pool) {
            super(adapter, pool);
              }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void onBindViewHolder(MainViewHolder viewHolder, int position) {
            // only vertical



        }


        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    }


    static class SubAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public SubAdapter(Context context, LinearLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public SubAdapter(Context context, LinearLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    static class GridSubAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public GridSubAdapter(Context context, GridLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public GridSubAdapter(Context context, GridLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.viewproductactivity, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {



                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    static class FixLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public FixLayoutAdapter(Context context, FixLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public FixLayoutAdapter(Context context, FixLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }




    static class FloatSubAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public FloatSubAdapter(Context context, FloatLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public FloatSubAdapter(Context context, FloatLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class RangeGridLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public RangeGridLayoutAdapter(Context context, RangeGridLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public RangeGridLayoutAdapter(Context context, RangeGridLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class SingleLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public SingleLayoutAdapter(Context context, SingleLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public SingleLayoutAdapter(Context context, SingleLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class StickyLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public StickyLayoutAdapter(Context context, StickyLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });


        }



        public StickyLayoutAdapter(Context context, StickyLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    }

    static class CollumnLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;

        public CollumnLayoutAdapter (Context context, ColumnLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public CollumnLayoutAdapter (Context context, ColumnLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class OnePlusNLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public OnePlusNLayoutAdapter(Context context, OnePlusNLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public OnePlusNLayoutAdapter (Context context, OnePlusNLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class ScrollFixLayoutAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public ScrollFixLayoutAdapter(Context context, ScrollFixLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public ScrollFixLayoutAdapter (Context context, ScrollFixLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }


    static class StaggeredGridLayoutAdapters extends DelegateAdapter.Adapter<MainViewHolder> {

        private Context mContext;

        private LayoutHelper mLayoutHelper;


        private LayoutParams mLayoutParams;
        private int mCount = 0;


        public StaggeredGridLayoutAdapters(Context context, StaggeredGridLayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300) {
                @Override
                public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return null;
                }

                @Override
                public void onBindViewHolder(MainViewHolder holder, int position) {

                }
            });
        }

        public StaggeredGridLayoutAdapters (Context context, StaggeredGridLayoutHelper layoutHelper, int count, @NonNull LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            // only vertical
            holder.itemView.setLayoutParams(
                    new LayoutParams(mLayoutParams) {
                        @Override
                        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            return null;
                        }

                        @Override
                        public void onBindViewHolder(MainViewHolder holder, int position) {

                        }
                    });
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
            ((TextView) holder.itemView.findViewById(R.id.title)).setText(Integer.toString(offsetTotal));
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }



    static class MainViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        public static volatile int existing = 0;
        public static int createdTimes = 0;


        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
        }


        public TextView bannertext;
        //    public TextView time;
        ImageView adbannerslot;
      //GRID LAYOUT
      ImageView traderimage;
        ImageView productimageview;
        ImageView numberoflikesimage;
        TextView productname;
        TextView productprice;
        TextView tradername;

        TextView category;
        TextView numberoflikes;



        // STICKYLAYOUT
        ImageView  stickytraderimage;
        ImageView    stickyproductimageview;
        ImageView   stickynumberoflikesimage;
        TextView     stickyproductname;
        TextView  stickyproductprice;
        TextView   stickytradername ;

        TextView   stickycategory;
        TextView categoryheaderbanner;
        TextView stickynumberoflikeshere;


 // FOOTER LAYOUT HERE
        ImageView footerecommercehome;
        ImageView  footerecommercesearched;
        ImageView footerecommercemessages;
        ImageView  footerecommercecart;
        ImageView footerecommerceprofile;

        // STAGGERED GRID VIEW LAYOUT STUFF

        ImageView stagtradersimageonscreen;
        ImageView stagproductimageonscreeen;
        ImageView stagnumberoflikesimage;
        TextView stagtheproductname;
        TextView stagtheproductprice;
        TextView stagtradernamehere;
        TextView stagcategoryhere;
        TextView stagnumberoflikes;


        public MainViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;

            itemView.setOnClickListener(this);

            bannertext = itemView.findViewById(R.id.viewpagertext);
            adbannerslot = itemView.findViewById(R.id.viewpagerimages);

               // DEFINING ITEMS FOR GRIDLAYOUT FILES



            traderimage = itemView.findViewById(R.id.tradersimageonscreen);
            productimageview = itemView.findViewById(R.id.productimageonscreeen);

            productname = itemView.findViewById(R.id.theproductname);
            productprice = itemView.findViewById(R.id.theproductprice);
            tradername = itemView.findViewById(R.id.tradernamehere);
            numberoflikesimage = itemView.findViewById(R.id.numberoflikesimage);
            category = itemView.findViewById(R.id.categoryhere);
             numberoflikes  = itemView.findViewById(R.id.numoflikesgotten);

                 // STICKYLAYOUT VIEW

            categoryheaderbanner = itemView.findViewById(R.id.Stickyheader);
                  // STICKYLAYOUT FROM ECOMMERCE VIEW

            stickytraderimage = itemView.findViewById(R.id.stickytradersimageonscreen);
            stickyproductimageview = itemView.findViewById(R.id.stickyproductimageonscreeen);

            stickyproductname = itemView.findViewById(R.id.stickytheproductname);
            stickyproductprice = itemView.findViewById(R.id.stickytheproductprice);
            stickytradername = itemView.findViewById(R.id.stickytradernamehere);
            stickynumberoflikesimage = itemView.findViewById(R.id.stickynumberoflikesimage);
            stickycategory = itemView.findViewById(R.id.stickycategoryhere);
            stickynumberoflikeshere = itemView.findViewById(R.id.stickynumberoflikeshere);

            // FOR FOOTER LAYOUT AND STUFF

             footerecommercehome  = itemView.findViewById(R.id.ecommercehome);
            footerecommercesearched = itemView.findViewById(R.id.ecommercesearched);
            footerecommercemessages   = itemView.findViewById(R.id.ecommercemessages);
             footerecommercecart = itemView.findViewById(R.id.ecommercecart);
             footerecommerceprofile = itemView.findViewById(R.id.ecommerceprofile);

              // DEFINING STAGGERED GRID STUFF



            stagtradersimageonscreen = itemView.findViewById(R.id.stagtradersimageonscreen);
            stagproductimageonscreeen = itemView.findViewById(R.id.stagproductimageonscreeen);

            stagtheproductname = itemView.findViewById(R.id.stagtheproductname);
            stagtheproductprice = itemView.findViewById(R.id.stagtheproductprice);
            stagtradernamehere = itemView.findViewById(R.id.stagtradernamehere);
            stagnumberoflikesimage = itemView.findViewById(R.id.stagnumberoflikesimage);
            stagcategoryhere = itemView.findViewById(R.id.stagcategoryhere);
            stagnumberoflikes = itemView.findViewById(R.id.stagnumberoflikes);

        }



        @Override
        public void onClick(View v) {
                 switch (v.getId()){
                     // VIEWPAGER BUTTONS

                     case R.id.viewpagertext:

                         break;
                     case R.id.viewpagerimages:
                    // GRIDVIEW BUTTONS
                         break;
                     case R.id.tradersimageonscreen:

                         break;
                     case R.id.productimageonscreeen:

                         break;
                     case R.id.theproductname:

                         break;
                     case R.id.theproductprice:

                         break;
                     case R.id.tradernamehere:

                         break;
                     case R.id.numberoflikesimage:

                         break;
                     case  R.id.categoryhere:

                         // FOOTER BUTTONS BUTTONS

                     case R.id.ecommercehome:

                         break;
                     case R.id.ecommercesearched:

                         break;
                     case R.id.ecommercemessages:

                         break;
                     case R.id.ecommercecart:

                         break;
                     case R.id.ecommerceprofile:

                   // STAGGERED GRID LAYOUT

                     case R.id.stagtradersimageonscreen:
                         break;

                     case R.id.stagproductimageonscreeen:
                         break;

                     case R.id.stagtheproductname:
                         break;

                     case R.id.stagtheproductprice:
                         break;


                     case R.id.stagtradernamehere:
                         break;

                     case R.id.stagnumberoflikesimage:
                         break;

                     case  R.id.stagcategoryhere:
                         break;

                     case R.id.stagnumberoflikes:
                         break;

                 }



            Intent intent = new Intent(v.getContext(), VLayoutActivity.class);
            Bundle b = new Bundle();
                        if (bannertext != null) {
                            b.putString("bannertext", bannertext.getText().toString());
                            intent.putExtras(b);
                            v.getContext().startActivity(intent);
                        }}



}

}
