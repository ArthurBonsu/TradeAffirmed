package com.simcoder.bimbo.instagram.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private String uid;
    private String phone;
    private String email;
    private String name;
    private  String posts;
    private  String desc;
    private  String website;
    private  String image;
    String photoid;
    String tid;


    public User(String email, String name, String desc, String website, String image) {
        this.email = email;
        this.name = name;
        this.desc = desc;
        this.website = website;
        this.image = image;
        this.photoid = photoid;
        this.tid = tid;
       }


    public User(String email, String name, String desc, String website, String image, String uid, String phone,String posts, String photoid,String tid) {
        this.uid = uid;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.posts = posts;
        this.desc = desc;
        this.website = website;
        this.image = image;
        this.photoid = photoid;
        this.tid = tid;
    }


    public User() {
    }

    protected User(Parcel in) {
        uid = in.readString();
        phone = in.readString();
        email = in.readString();
        name = in.readString();
        posts = in.readString();
        photoid = in.readString();
        tid = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };




    public String gettid() {
        return tid;
    }

    public void settid(String tid) {
        this.tid = tid;
    }


    public String getphotoid() {
        return photoid;
    }

    public void setphotoid(String photoid) {
        this.photoid = photoid;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }

    public String getphone() {
        return phone;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + uid + '\'' +
                ", phone_number='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + name + '\'' +
                ", posts='" + posts + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(posts);
    }
}
