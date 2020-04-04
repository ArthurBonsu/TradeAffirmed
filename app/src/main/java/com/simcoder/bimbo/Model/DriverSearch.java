package  com.simcoder.bimbo.Model;

import com.simcoder.bimbo.instagram.Models.Like;

import java.util.List;

public class DriverSearch
{
    private String  phone;


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

    String   comment;
    String     uid;
    String likes;
    String date;

    String time;

    String gnumber;

    String commentkey;


    public DriverSearch() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public DriverSearch( String phone ) {




        this.phone = phone;




    }

    public DriverSearch(String comment, String uid, List<Like> likes, String date, String time, String tid, String name, String number, String subject, String likeid, String pid, String commentkey, String photoid, String orderkey,
                   String reply, String replyid, String traderimage, String tradername, String pname, String pimage, String image
    ) {
        this.comment = comment;
        this.uid = uid;

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




    public String getphone(){
        return  phone;
    }
    public void setphone(String phone) {
        this.phone = phone;
    }



    }


