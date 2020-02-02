package com.simcoder.bimbo.instagram.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private String uid;
    private String phone;
    private String email;
    private String name;

    public User(String uid, String phone, String email, String name) {
        this.uid = uid;
        this.phone = phone;
        this.email = email;
        this.name = name;
    }

    public User() {
    }


    protected User(Parcel in) {
        uid = in.readString();
        phone = in.readString();
        email = in.readString();
        name = in.readString();
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

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + uid + '\'' +
                ", phone_number='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", username='" + name + '\'' +
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
    }
}
