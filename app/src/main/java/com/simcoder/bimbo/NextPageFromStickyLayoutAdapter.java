package com.simcoder.bimbo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simcoder.bimbo.historyRecyclerView.HistoryObject;
import com.simcoder.bimbo.historyRecyclerView.HistoryViewHolders;

import java.util.List;

public class  NextPageFromStickyLayoutAdapter extends RecyclerView.Adapter<NextViewHolder> {

    private List<HistoryObject> itemList;
    private Context context;

    public NextPageFromStickyLayoutAdapter(List<HistoryObject> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public NextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        NextViewHolder rcv = new NextViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull NextViewHolder nextViewHolder, int i) {
        nextViewHolder.rideId.setText(itemList.get(i).getRideId());
        if(itemList.get(i).getTime()!=null){
            nextViewHolder.time.setText(itemList.get(i).getTime());
    }


    //THIS IS WHERE WE SEE IT COME TO THE SCEEN AFTER THE TRIP. THIS IS THE PLACE WHERE WE SEE MANY HISTORIES
    //WE CLICK ON ONE AND IT TAKES US TO HISTORY SINGLE TO SEE FURTHEER DETAIS ABOUT THE TRIP, BUT WHERE IS THE HISTORY OBJECT PULLING

    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}