package com.simcoder.bimbo.instagram.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.simcoder.bimbo.instagram.Utils.StringManipulation;

public class UserAccountSettings implements Parcelable {

    private String desc;
    private String name;
    private String number; // followers and followings

    private String  posts;
    private String image;

    private String website;
    private String uid;

    public UserAccountSettings(String desc, String name, String number,
                               String  posts, String image, String website,
                               String uid) {
        this.desc = desc;
        this.name = name;
        this.number = number;
        this.posts = posts;
        this.image = image;
        this.website = website;
        this.uid = uid;
        this.posts = posts;


    }

    public UserAccountSettings() {
    }
    public UserAccountSettings(String desc,   String image,String name,String website,
                               String uid  ) {
    }




    protected UserAccountSettings(Parcel in) {


        desc = in.readString();
        name = in.readString();
        number = in.readString();
        posts = in.readString();

        image = in.readString();
        website = in.readString();
        uid = in.readString();
        posts = in.readString();

    }

    public static final Creator<UserAccountSettings> CREATOR = new Creator<UserAccountSettings>() {
        @Override
        public UserAccountSettings createFromParcel(Parcel in) {
            return new UserAccountSettings(in);
        }

        @Override
        public UserAccountSettings[] newArray(int size) {
            return new UserAccountSettings[size];
        }
    };





    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }


    public String getdesc() {
        return desc;
    }

    public void setdesc(String desc) {
        this.desc = desc;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getFollowers() {
        return number;
    }

    public void setFollowers(String number) {
        this.number = number;
    }

    public String getFollowing() {
        return number;
    }

    public void setFollowing(String following) {
        this.number = number;
    }

    public String  getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }




    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "description='" + desc + '\'' +
                ", display_name='" + name + '\'' +
                ", followers=" + number +
                ", following=" + number +
                ", posts=" + posts +
                ", profile_photo='" + image + '\'' +
                ", username='" + name + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desc);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(number);
        dest.writeString(posts);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(website);
        dest.writeString(uid);
    }
}

