package com.simcoder.bimbo;
import com.simcoder.*;
import com.simcoder.bimbo.*;
import  com.simcoder.bimbo.historyRecyclerView.*;


public class EcommerceGrid {



    String  traderimage;
    String tradername;
    String productimage;
    String productnamein;
    String productprice;
    String likenumber;
    String mycategory;



    public EcommerceGrid( String  traderimage, String tradername, String productimage, String productnamer, String productprice,String likenumber, String ourcategory ){
        this.traderimage = traderimage;
        this.tradername = tradername;
        this.productimage = productimage;
        this.productnamein = productnamer;
        this.productprice = productprice;
        this.likenumber = likenumber;
        this.mycategory = ourcategory;

    }

    public String getTraderimage(){return traderimage;}
    public String setTraderimage(String traderimage) {
        this.traderimage = traderimage;
        return  traderimage;
    }

    public String getTradername(){return tradername;}
    public String  setTradername(String  tradername ) {
        this.tradername = tradername;
        return tradername;
    }
    public String getProductimage(){return productimage;}
    public String setProductimage(String productimage) {
        this.productimage = productimage;
        return productimage;
    }
    public String getProductname(){return productnamein;}

    public String setProductname(String productnamed) {
        this.productnamein = productnamed;
        return productnamed;
    }
    public String getProductprice(){return productprice;}
    public String setProductprice(String productprice) {
        this.productprice = productprice;
    return  productprice;}
    public String getLikenumber(){return likenumber;}
    public String  setLikenumber(String likenumber) {
        this.likenumber = likenumber;
        return likenumber;
    }

    public String getCategory(){return mycategory;}
     public  String setCategory(String mycategory) {
         this.mycategory = mycategory;
    return mycategory; }
}

/**
 * Created by manel on 10/10/2017.
 */
