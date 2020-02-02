package com.simcoder.bimbo.instagram.Models;

import java.util.List;

public class Comment {

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



    public Comment() {
    }

    public Comment(String comment, String uid, List<Like> likes, String date, String time, String tid, String name, String number, String subject, String likeid, String pid, String commentkey) {
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


    public String getuid(String uid) {
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
