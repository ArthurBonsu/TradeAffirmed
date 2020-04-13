package com.simcoder.bimbo.instagram.Models;

import java.util.List;

public class UserSettings {
    private User user;
    private UserAccountSettings settings;
   //private User user;
    //    private UserAccountSettings settings;
    //
   private  String commentkey;
    private String comment;
    private String uid;
    private List<Like> likes;
    private String date;
    private  String time;
    String likeid;

    String tid;
    String name;
    String number;
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
    String image;



    public UserSettings(User user, UserAccountSettings settings) {
        this.user = user;
        this.settings = settings;
    }


    public UserSettings(User user, UserAccountSettings settings,String pid, String commentkey, String photoid, String orderkey,
                        String reply,String replyid,String traderimage,String tradername,String pname,String pimage,String image) {
        this.user = user;
        this.settings = settings;

        this.comment = comment;
        this.uid = uid;
        this.likes = likes;
        this.date = date;
        this.likeid = likeid;
        this.time = time;
        this.tid = tid;
        this.name = name;
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




    }

    public UserSettings() {
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

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getLikeid(String likeid) {
        return likeid;
    }
    public void setLikeid(String likeid) {
        this.likeid = likeid;
    }


    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String gettime() {
        return time;
    }

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

    public String getphotoid() {
        return photoid;
    }

    public void setphotoid(String photoid) {
        this.photoid = photoid;
    }



    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public UserAccountSettings getSettings() {
        return settings;
    }
    public void setSettings(UserAccountSettings settings) {
        this.settings = settings;
    }
    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                ", settings=" + settings +
                '}';
    }
}


// #BuiltByGOD