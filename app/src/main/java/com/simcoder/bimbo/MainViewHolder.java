package com.simcoder.bimbo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


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
    public MainViewHolder(View itemView) {
        super(itemView);
        createdTimes++;
        existing++;

        itemView.setOnClickListener(this);

        bannertext = itemView.findViewById(R.id.viewpagertext);
        adbannerslot = itemView.findViewById(R.id.viewpagerimages);

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), VLayoutActivity.class);
        Bundle b = new Bundle();
        b.putString("bannertext", bannertext.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
