package com.simcoder.bimbo.instagram.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Profile.ProfileActivity;
import com.simcoder.bimbo.R;
import com.simcoder.bimbo.instagram.Utils.BottomNavigationViewHelper;
import com.simcoder.bimbo.instagram.Utils.UserListAdapter;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;

public class SearchByResidence extends AppCompatActivity{
    private static final String TAG = "SearchActivity";
    private static final int ACTIVITY_NUM = 1;

    private Context mContext = SearchByResidence.this;

    //widgets
    private EditText mSearchParam;
    private ListView mListView;

    //vars
    private List<User> mUserList;
    private UserListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearchParam = (EditText) findViewById(R.id.search);
        mListView = (ListView) findViewById(R.id.listView);
        Log.d(TAG, "onCreate: started.");

        hideSoftKeyboard();
        setupBottomNavigationView();
        initTextListener();
    }

    private void initTextListener(){
        Log.d(TAG, "initTextListener: initializing");

        mUserList = new ArrayList<>();

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              if (mSearchParam != null){
                  if(mSearchParam.getText() != null){
                String text = mSearchParam.getText().toString().toLowerCase(Locale.getDefault());
                searchForMatch(text);
            }}}
        });
    }

    private void searchForMatch(String keyword){
        Log.d(TAG, "searchForMatch: searching for a match: " + keyword);
         if (mUserList != null){
        mUserList.clear();
        //update the users list view
        if(keyword.length() ==0) {

        } }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                  if (keyword != null) {
                      Query query = reference.child("Users").child("Customers")
                              .orderByChild("name").equalTo(keyword);

            if (query != null){
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        String thecustomerkey = singleSnapshot.getKey();
                         if (singleSnapshot != null){
                                                 if (singleSnapshot.child(thecustomerkey).child("name").getValue(String.class) != null){
                        Log.d(TAG, "onDataChange: found user:" + singleSnapshot.child(thecustomerkey).child("name").getValue(String.class));
                           if (mUserList != null){
                            if (singleSnapshot.getValue(User.class) != null){
                        mUserList.add(singleSnapshot.getValue(User.class));
                        //update the users list view
                        updateUsersList();
                            }}
                }
                }}}
                @Override
                public void onCancelled(DatabaseError databaseError){

                    }
            });
        }
    }
    }}
    private void updateUsersList() {
        Log.d(TAG, "updateUsersList: updating users list");
        if (mAdapter != null) {
            mAdapter = new UserListAdapter(SearchByResidence.this, R.layout.layout_user_listitem, mUserList);
                if (mListView != null){
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       if (mUserList.get(position) != null){
                    Log.d(TAG, "onItemClick: selected user: " + mUserList.get(position).toString());

                    //navigate to profile activity
                    Intent intent = new Intent(SearchByResidence.this, ProfileActivity.class);
                   if (intent != null){
                    intent.putExtra(getString(R.string.calling_activity), getString(R.string.search_activity));
                    if (mUserList.get(position) != null){
                    intent.putExtra(getString(R.string.intent_user), mUserList.get(position));
                    startActivity(intent);

                }}}}
            });
        }
    }}

    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            if (INPUT_METHOD_SERVICE != null) {
                if (getSystemService(INPUT_METHOD_SERVICE) != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      if (imm != null){
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
    }}
    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView() {

        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
         if (mContext != null){
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
               if (bottomNavigationViewEx != null){
        Menu menu = bottomNavigationViewEx.getMenu();
            if (menu != null){
                if (ACTIVITY_NUM !=0){
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }}}
}}}


// #BuiltByGOD