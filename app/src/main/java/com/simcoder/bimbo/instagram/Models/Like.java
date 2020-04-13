package com.simcoder.bimbo.instagram.Models;

import java.util.List;

public class Like {


    private String uid;
    private List<Like> likes;
    private String date;
    private  String time;
    String likeid;
    String tid;
    String name;
    String number;

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
    String photid;



    public Like() {
    }

    public Like(String user_id) {
        this.uid = uid;
    }


     public  Like( String uid, List<Like> likes, String date, String time, String tid, String name, String number,  String likeid, String pid,String photoid, String orderkey,
                   String reply,String replyid,String traderimage,String tradername,String pname,String pimage,String image ){


         this.uid = uid;
         this.likes = likes;
         this.date = date;
         this.likeid = likeid;
         this.time = time;
         this.tid = tid;
         this.name = name;
         this.number = number;

         this.pid = pid;

         this.photoid = photoid;
         this.orderkey = orderkey;
         this.reply = reply;
         this.replyid = replyid;
         this.traderimage = traderimage;
         this.tradername = tradername;
         this.pname = pname;
         this.pimage = pimage;
         this.image = image;

    }




    public String getphotoid() {
        return photoid;
    }
    public void setphotoid(String photoid) {
        this.photoid = photoid;
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


    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
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

    public String getLikeid() {
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



    public String getpid() {
        return pid;
    }

    public void setpid(String pid) {
        this.pid = pid;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}


// #BuiltByGOD