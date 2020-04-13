package com.simcoder.bimbo.instagram.Models;

import java.util.List;

public class Tags {

    private  String commentkey;
    private String comment;
    private String uid;
    private List<Like> likes;
    private List<Tags> tags;
    private String date;
    private  String time;
    String likeid;
    String tid;
    String name;
    String number;
    String subject;
    String pid;
    String image;

    String photoid;
    String orderkey;
    String reply;
    String replyid;
    String traderimage;
    String tradername;

    String pname;
    String pimage;




    public Tags() {
    }

    public Tags(String comment, String uid, List<Like> likes, String date, String time, String tid, String name, String number, String subject, String likeid, String pid, String commentkey, List<Tags> tags, String image,
                String photoid,String orderkey,String reply,String replyid,String traderimage,String tradername,String pname,String pimage,String photid) {
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
        this.tags = tags;
        this.image = image;
        this.photoid = photoid;

        this.orderkey = orderkey;
        this.reply = reply;
        this.replyid =replyid;
        this.traderimage = traderimage;
        this.tradername = tradername;
        this.pimage = pimage;
        this.pname =pname;
        this.image =photoid;
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



    public String getreplyid(String replyid) {
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

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
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


    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "comment='" + comment + '\'' +
                ", user_id='" + uid + '\'' +
                ", likes=" + likes +
                ", date_created='" + date + '\'' +
                '}';
    }
}

// #BuiltByGOD