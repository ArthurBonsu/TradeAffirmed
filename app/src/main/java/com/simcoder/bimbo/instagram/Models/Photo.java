package com.simcoder.bimbo.instagram.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Photo implements Parcelable{

    private String caption;
    private String date;
    private String image;
    private String photoid;
    private String uid;
    private String name;
    String time;
    String likeid;
    String commentkey;
    String comment;
    String number;
    String pid;
    String tid;
    String price;
    private List<Like> likes;
    private List<Comment> comments;
    private List<Tags>tags;


    public Photo() {
    }
    public Photo(String caption, String date, String image, String time, String uid, String name, List<Like> likes, List<Comment> comments, String photoid, String commentkey, String likeid, String comment,String number, String tid, String pid,List<Tags>tags, String price) {
        this.caption = caption;
        this.date = date;
        this.image = image;
        this.photoid = photoid;
        this.uid = uid;
        this.name = name;
        this.likes = likes;
        this.comments = comments;
        this.commentkey = commentkey;
        this.likeid = likeid;
        this.comment = comment;
        this.number = number;
        this.tid = tid ;
        this.pid = pid;
        this.tags = tags;
        this.price = price;
    }
    public Photo(String caption, String date, String image, String time, String uid, String name, String photoid, String  number, String number1, String tid, String pid, String price) {

        this.caption = caption;
        this.date = date;
        this.image = image;
        this.photoid = photoid;
        this.uid = uid;
        this.name = name;
        this.likes = likes;
        this.comments = comments;
        this.commentkey = commentkey;
        this.likeid = likeid;
        this.comment = comment;
        this.number = number;
        this.tid = tid ;
        this.pid = pid;
        this.tags = tags;
        this.price = price;
        this.time = time;

    }
    protected Photo(Parcel in) {

        caption = in.readString();
        date = in.readString();
        image = in.readString();
        photoid = in.readString();
        uid = in.readString();
        name = in.readString();
        commentkey = in.readString();
        likeid = in.readString();
        comment = in.readString();
        number = in.readString();
        tid = in.readString() ;
        pid = in.readString();
        price = in.readString();

       /* caption = in.readString();
        date = in.readString();
        image = in.readString();
        photoid = in.readString();
        uid = in.readString();
        name = in.readString();
        likeid = in.readString();
        commentkey = in.readString();
*/
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };





    public String getCaption() {
        return caption;
    }



    public void setCaption(String caption) {
        this.caption = caption;
    }


    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }

    public String getuid() {
        return uid;
    }
    public void setuid(String uid) {
        this.uid = uid;
    }


    public String getLikeid(String likeid) {
        return likeid;
    }
    public void setLikeid(String likeid) {
        this.likeid = likeid;
    }
    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String gettime(){return  time;}

    public void settime(String time) {
        this.time = time;
    }

    public String gettid() {
        return tid;
    }

    public void settid(String tid) {
        this.tid = tid;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }



    public String getpid() {
        return pid;
    }

    public void setpid(String pid) {
        this.pid = pid;
    }


    public String getphotoid() {
        return photoid;
    }

    public void setphotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getcommentkey() {
        return commentkey;
    }

    public void getcommentkey(String commentkey) {
        this.commentkey = commentkey;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Tags> getTag() {
        return tags ;
    }

    public void setTag(List<Tags> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Photo> setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        return null;
    }


    public String getTags() {
        return name;
    }

    public void setTags(String name) {
        this.name = name;
    }








    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);


        dest.writeString(caption);
        dest.writeString(date);
        dest.writeString(image);
        dest.writeString(photoid);
        dest.writeString(uid);
        dest.writeString(name);


        dest.writeString(likeid);
        dest.writeString(comment);
        dest.writeString(number);
        dest.writeString(tid);
        dest.writeString(price);

    }
}
