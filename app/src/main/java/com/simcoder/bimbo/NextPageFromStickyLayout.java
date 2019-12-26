package com.simcoder.bimbo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class NextPageFromStickyLayout extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.fromstickylayout);
    }

    public NextPageFromStickyLayout() {
        super();
    }

  RecyclerView  nextpagefromstickynoterecycler = (RecyclerView)findViewById(R.id.fromstickylayoutrecycler);



    }

