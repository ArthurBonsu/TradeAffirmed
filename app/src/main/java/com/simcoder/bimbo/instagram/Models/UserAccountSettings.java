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

    String likeid;
    String tid;


    String subject;
    String pid;
    String photoid;
    String orderkey;
    String reply;
    String replyid;
    String traderimage;
    String tradername;

    String pname;
    String pimage;

    String commentkey;
    String comment;

    public UserAccountSettings(String desc, String name, String number,
                               String  posts, String website,
                               String uid,String photoid, String orderkey,
                               String reply,String replyid,String traderimage,String tradername,String pname,String pimage,String image,String commentkey, String comment, String tid, String likeid) {
        this.desc = desc;
        this.name = name;
        this.number = number;
        this.posts = posts;
        this.image = image;
        this.website = website;
        this.uid = uid;
        this.posts = posts;
        this.number = number;
        this.subject = subject;
        this.pid = pid;
        this.commentkey = commentkey;
        this.photoid = photoid;

        this.orderkey = orderkey;
        this.reply = reply;
        this.replyid = replyid;
        this.traderimage = traderimage;
        this.pname = pname;
        this.image = image;
        this.photoid = photoid;
        this.pimage = pimage;
        this.tradername = tradername;
        this.commentkey = commentkey;
        this.comment = comment;


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




        number = in.readString();
        subject = in.readString();
        pid = in.readString();
        commentkey = in.readString();

        photoid = in.readString();
        orderkey = in.readString();
        reply = in.readString();
        replyid = in.readString();

        traderimage = in.readString();
        photoid = in.readString();
        pimage = in.readString();
        tradername = in.readString();

        commentkey = in.readString();
        comment = in.readString();



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



    public String getcomment() {
        return comment;
    }


    public void setcomment(String comment) {
        this.comment = comment;
    }
    public String gettradername() {
        return tradername;
    }


    public void settradername(String tradername) {
        this.tradername = tradername;
    }
    public String getorderkey() {
        return orderkey;
    }


    public String getreply() {
        return reply;
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


    public String getpname() {
        return pname;
    }

    public void setpname(String pname) {
        this.pname = pname;
    }





    public void setreply(String reply) {
        this.reply = reply;
    }

    public void setorderkey(String orderkey) {
        this.orderkey = orderkey;
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

    public void setcommentkey(String commentkey) {
        this.commentkey = commentkey;
    }





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

        dest.writeString(number);
        dest.writeString(subject);
        dest.writeString(pid);
        dest.writeString(commentkey);
        dest.writeString(photoid);
        dest.writeString(orderkey);
        dest.writeString(reply);
        dest.writeString(replyid);
        dest.writeString(traderimage);

        dest.writeString(photoid);
        dest.writeString(pimage);
        dest.writeString(pid);
        dest.writeString(tradername);
        dest.writeString(commentkey);
        dest.writeString(comment);




    }
}


// #BuiltByGOD