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
    String likeid;

    String number;
    String subject;
    String pid;

    String orderkey;
    String reply;
    String replyid;
    String traderimage;
    String tradername;

    String pname;
    String pimage;

    String photid;

    public User(String email, String name, String desc, String website, String image) {
        this.email = email;
        this.name = name;
        this.desc = desc;
        this.website = website;
        this.image = image;
        this.photoid = photoid;
        this.tid = tid;
       }


    public User(String email, String name, String desc, String website, String image, String uid, String phone,String posts, String photoid,
            String tid,String likeid,String number,String subject, String pid,String orderkey,String reply, String replyid,String traderimage,String tradername,String pname,
            String pimage) {
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
        this.likeid = likeid;
                this.number = number;
                this.subject = subject;
                this.pid = pid;
                this.orderkey = orderkey;
                this.reply = reply;
                this.replyid= replyid;
                this.traderimage = traderimage;
                this.tradername = tradername;
                this.pname = pname;
                this.pimage = pimage;
                this.photoid = photoid;

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

        photoid = in.readString();
        tid = in.readString();
        likeid = in.readString();
        number = in.readString();
        subject = in.readString();
        pid = in.readString();
        orderkey = in.readString();

        reply = in.readString();
        replyid = in.readString();
        traderimage = in.readString();
        tradername = in.readString();
        pname = in.readString();
        pimage = in.readString();


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










    public String getlikeid() {
        return likeid;
    }

    public void setlikeid(String likeid) {
        this.likeid = likeid;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }

    public String getsubject() {
        return subject;
    }

    public void setsubject(String subject) {
        this.subject = subject;
    }

    public String getpid() {
        return pid;
    }

    public void setpid(String pid) {
        this.pid = pid;
    }

    public String getorderkey() {
        return orderkey;
    }

    public void setorderkey(String orderkey) {
        this.orderkey = orderkey;
    }



    public String getreply() {
        return reply;
    }

    public void setreply(String reply) {
        this.reply = reply;
    }

    public String getreplyid() {
        return replyid;
    }

    public void setreplyid(String replyid) {
        this.replyid = replyid;
    }

    public String gettraderimage() {
        return traderimage;
    }

    public void settraderimage(String traderimage) {
        this.traderimage = traderimage;
    }

    public String gettradername() {
        return tradername;
    }

    public void settradername(String tradername) {
        this.tradername = tradername;
    }

    public String getpname() {
        return pname;
    }

    public void setpname(String pname) {
        this.pname = pname;
    }


    public String getpimage() {
        return pimage;
    }

    public void setpimage(String pimage) {
        this.pimage = pimage;
    }


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


        dest.writeString(photoid);
        dest.writeString(tid);
        dest.writeString(likeid);
        dest.writeString(number);
        dest.writeString(subject);
        dest.writeString(pid);
        dest.writeString(orderkey);
        dest.writeString(reply);
        dest.writeString(replyid);
        dest.writeString(traderimage);
        dest.writeString(tradername);
        dest.writeString(pname);
        dest.writeString(pimage);




    }
}


// #BuiltByGOD