package com.simcoder.bimbo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pulls out the main contents from the activity mean
        setContentView(R.layout.navigationactivity_main);

        //HERE WE ARE GOING TO BUILD THE
    }

    //This is the main activity of the app
    // But it only has the buttons, what is the buttons for?

    // So here from the Nav bar, we can push out the animation from the content
    // They did not add the Nav bar here

    //侧边栏样式一  Android原生风格
    public void style1(View view) {
        startActivity(new Intent(MainNavigationActivity.this, Style1Activity.class));
    }


}
