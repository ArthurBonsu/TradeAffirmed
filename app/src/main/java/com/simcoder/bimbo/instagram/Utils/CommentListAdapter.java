package com.simcoder.bimbo.instagram.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.simcoder.bimbo.Model.Users;
import com.simcoder.bimbo.instagram.Models.Comment;
import com.simcoder.bimbo.instagram.Models.User;
import com.simcoder.bimbo.instagram.Models.UserAccountSettings;
import com.simcoder.bimbo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private static final String TAG = "CommentListAdapter";

    private LayoutInflater mLayoutInflater;
    private int layoutResource;
    private Context mContext;
    String saveCurrentDate;
    String saveCurrentTime;
    String getcommentername;
    String gethecomment;
    String thetimestampforcomment;
    String thecommentkey;
    String theuserkey;
    String commentkey;
    String thereplythatwehave;
    String commentreply;
    String thelikethatwehave;
     String likeshere;

    public CommentListAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        layoutResource = resource;;
    }

    private static class ViewHolder {
        TextView comment, username, timestamp, reply, likes;
        CircleImageView profileImage;
        ImageView like;
    }

    //if (dataSnapshot.child(postkey).hasChild(mAuth.getCurrentUser().getUid()))
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.username = (TextView) convertView.findViewById(R.id.comment_username);
            holder.timestamp = (TextView) convertView.findViewById(R.id.comment_time_posted);
            holder.reply = (TextView) convertView.findViewById(R.id.comment_reply);
            holder.like = (ImageView) convertView.findViewById(R.id.comment_like);
            holder.likes = (TextView) convertView.findViewById(R.id.comment_likes);
            holder.profileImage = (CircleImageView) convertView.findViewById(R.id.comment_profile_image);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set values
        holder.comment.setText(getItem(position).getComment());
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        if (currentDate != null) {
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            if (currentTime != null) {
                saveCurrentTime = currentTime.format(calendar.getTime());
            }}


                //set timestamp difference
                String timestampDifference = getTimestampDifference(getItem(position));
                if (!timestampDifference.equals("0")) {
                    holder.timestamp.setText(timestampDifference + " d");
                } else {
                    holder.timestamp.setText(thetimestampforcomment);
                }

        //set username and profile image.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference      photosreferece = FirebaseDatabase.getInstance().getReference().child("Photos");
         String referencekey = photosreferece.getKey();
        Query query = reference
                .child("Photos")
                .orderByChild("photoid")
                .equalTo(getItem(position).getphotoid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                         thecommentkey = singleSnapshot.child("Comments").getKey();
                          theuserkey = singleSnapshot.child("Comments").child("Users").getKey();
                     commentkey = singleSnapshot.child("Comments").child("Users").child("comments").getKey();
                     commentreply = singleSnapshot.child("Comments").child("Users").child("comments").child("replies").getKey();
                    likeshere = singleSnapshot.child("Comments").child("Users").child("comments").child("likes").getKey();

                    getcommentername = singleSnapshot.child("Comments").child(thecommentkey).child("Users").child(theuserkey).child("name").getValue(Comment.class).getname();
                    gethecomment =   singleSnapshot.child("Comments").child(thecommentkey).child("Users").child(theuserkey).child("comments").child(commentkey).child("comment").getValue(Comment.class).getname();
                    thetimestampforcomment = singleSnapshot.child("Comments").child(thecommentkey).child("Users").child(theuserkey).child("comments").child(commentkey).child("time").getValue(Comment.class).gettime();
                    thereplythatwehave = singleSnapshot.child("Comments").child(thecommentkey).child("Users").child(theuserkey).child("comments").child(commentkey).child("replies").child(commentreply).child("number").getValue(Comment.class).getnumber();
                    thelikethatwehave = singleSnapshot.child("Comments").child(thecommentkey).child("Users").child(theuserkey).child("comments").child(commentkey).child("likes").child(likeshere).child("number").getValue(Comment.class).getnumber();

                    holder.username.setText(getcommentername);
                    holder.comment.setText(gethecomment);
                    holder.timestamp.setText(thetimestampforcomment);
                    holder.reply.setText(thereplythatwehave);
                    holder.likes.setText(thelikethatwehave);

                    ImageLoader imageLoader = ImageLoader.getInstance();

                    imageLoader.displayImage(
                            singleSnapshot.child("Comments").child(thecommentkey).child("Users").child(theuserkey).child("image").getValue(UserAccountSettings.class).getimage(),
                            holder.profileImage);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });

        // set Invisible for first Comment.
        try {
            holder.like.setVisibility(View.GONE);
            holder.likes.setVisibility(View.GONE);
            holder.reply.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            Log.e(TAG, "getView: NullPointerException: " + e.getMessage() );
        }
        return convertView;
    }

    /**
     * Returns a string representing the number of days ago the post was made
     *
     * @return
     */
    private String getTimestampDifference(Comment comment) {
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));//google 'android list of timezones'
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;
        final String photoTimestamp = comment.getdate();
        try {
            timestamp = sdf.parse(photoTimestamp);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
        } catch (ParseException e) {
            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage());
            difference = "0";
        }
        return difference;
    }
}
