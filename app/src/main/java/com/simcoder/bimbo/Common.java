package com.simcoder.bimbo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Common {
       int i=0;
    int viewType;
    String thefirstpositioncategorytoo;
    String thefirstpositionproductnametoo;
     String traderimage;
     String tradername;
     String productimage;
     String productname;
     String productprice;
     String likenumber;
     String cagegory;


     public static final int  CATEGORY =0;
     public  static final  int ECOMMERCEVIEW= 1;

      // THIS WILL BE THE LIST PICKERS
      public  List<String>  newactivitylistwithfirstchar = new ArrayList<>();
    public  List<String>  storecategorylist = new ArrayList<>();
      List<String> forallproducts = new ArrayList<>();
      List<String> forallcategoris = new ArrayList<>();
    public   ArrayList<EcommerceGrid> Ecommercestore = new ArrayList<EcommerceGrid>();


    EcommerceGrid productdetails = new EcommerceGrid(traderimage, tradername, productimage, productname, productprice, likenumber, cagegory);

     public static ArrayList<EcommerceGrid> sortList(ArrayList<EcommerceGrid> ecommerceGrids){



          // WE CAN SORT ALL THE MEMBERS OF THE ARRAYS USING THIS FUNCTION

          Collections.sort(ecommerceGrids, new Comparator<EcommerceGrid>() {
               @Override
               public int compare(EcommerceGrid o1, EcommerceGrid o2) {
                    return o1.getCategory().compareTo(o2.getCategory());
               }
          });
          return ecommerceGrids; }



           // THE SPONTANEOUS GETTERS CAN BE GOTEN HERE

    public String getThefirstpositionCategory(String thefirstpositioncategory){return thefirstpositioncategorytoo;}
    public String getThefirstpositionProductName(String thefirstpositionproductname){return thefirstpositionproductnametoo;}
    public EcommerceGrid getTheProductDetailsInACase(EcommerceGrid theproductdetailsinacase){return productdetails;}
     public  ArrayList<EcommerceGrid> getTheArrayOfProductItself( ArrayList<EcommerceGrid> theArrayoftheproductthemselves){return Ecommercestore;  }


     // WE CAN GET THE LISTERS THAT ARE REMAINING AFTER MANIPULATIONS OF THEM ALL

     public String getThefirstProductName(String thefirstpositioncategory){return thefirstpositioncategorytoo;}
    public String getTheSecondPersonProductName(String thefirstpositionproductname){return thefirstpositionproductnametoo;}
    public String getThefirstPersonProductCategory(String thefirstpositionproductname){return thefirstpositionproductnametoo;}
    public String getTheSecondPersonProductCategory(String thefirstpositionproductname){return thefirstpositionproductnametoo;}
    public  List<String> getcategorywithequalnames  ( List<String> equalcategories  ){ return forallproducts;  }
    public  List<String> getproductwithequalnames  (List<String> equalproduct){ return forallcategoris;  }
    public  List<String> getcategorieswithoutqualnames  (List<String> unequalcategories ){ return forallcategoris;  }
    public  List<String> getproductwithoutequalnames  (List<String> equalproduct ){ return forallproducts;  }




    // WE CAN POPULATE THE CATEGORIES INTO AN ALPHABETIC STRUCTURE OR WE CAN JUST PUT THEME IN AN ARRAY AND USE THEM AS HEADER STICKERS

         public  EcommerceGrid populatealphabetandproductlist(ArrayList<EcommerceGrid> ecommercefullproductlist){


      ArrayList<EcommerceGrid> Ecommercefullproductlist = new ArrayList<EcommerceGrid>();

    EcommerceGrid firstposition = new EcommerceGrid(traderimage, tradername, productimage, productname, productprice,  likenumber,  cagegory);
                ArrayList<ViewTypes> myviewtype = new ArrayList<>();

                ViewTypes theviewtype = new ViewTypes(Common.CATEGORY);



             String categorylist =            firstposition.setCategory(String.valueOf(Ecommercestore.get(0).getCategory()));
             String productnamegetting =            firstposition.setProductname(String.valueOf(Ecommercestore.get(0).getProductname().charAt(0)));

          String mygroupings = firstposition.setCategory(String.valueOf(Ecommercestore.get(0).getCategory().charAt(0)));
          viewType = theviewtype.setViewType(Common.CATEGORY);
          if (viewType == Common.CATEGORY) {

              // WE STORE THE CATEGORY NAMES HERE ALTHOUGH CONCATENATED

              newactivitylistwithfirstchar.add(String.valueOf(Ecommercestore.get(0).getCategory().charAt(0)));

                     //WE KEEP ALL CATEGORY LISTING HERE
              storecategorylist.add(categorylist);
              // WE KEEP THE WHOLE PRODUCT LISTING HERE

              Ecommercefullproductlist.add(firstposition);


            return firstposition;
          }

           // WE CAN JUST PULL UP THE CATEGORY RIGHT FROM HERE

             getThefirstpositionCategory(mygroupings);

                            // WE CAN JUST PULL UP THE PRODUCT NAME RIGHT FROM HERE

             getThefirstpositionProductName(productnamegetting);

                 // WE CAN JUST PULL UP THE OBJECT OF ECOMMERCESTORE ITSELF RIGHT FROM HERE TO READ INTO THE DETAILS

             getTheProductDetailsInACase(firstposition);

                   // WE CAN JUST PULL UP THE ARRAYS ITSELF RIGHT FROM HERE BEFORE WE MANIPULATE IT

             getTheArrayOfProductItself(Ecommercefullproductlist);


             // WE COULD HAVE USED THE  ALPHABETICAL BUT WE WILL USE THE CATEGORICAL TO GROUP ALL THE CATEGORIES THAT WE HAVE HERE
         // WE LOOK THROUGH THE OBJECT LIST

         for (i = 0; i < Ecommercefullproductlist.size() - 1; i++) {

             ArrayList<EcommerceGrid> productgrouplist = new ArrayList<EcommerceGrid>();
             List<String> keeperofproductnames = new ArrayList<>();
             List<String> keeperofcategories = new ArrayList<>();

             // WE WILL PICK THE NAME OF THE PRODUCT
             String product1 = productdetails.setProductname(String.valueOf(Ecommercestore.get(i).getProductimage()));
             String product2 = productdetails.setProductname(String.valueOf(Ecommercestore.get(i + 1).getProductimage()));
             String category1 = productdetails.setCategory(String.valueOf(Ecommercestore.get(i).getCategory()));
             String category2 = productdetails.setCategory(String.valueOf(Ecommercestore.get(i + 1).getCategory()));

             getThefirstProductName(product1);
             getTheSecondPersonProductName(product2);
             getThefirstPersonProductCategory(category1);
             getTheSecondPersonProductCategory(category2);

             if (category1 == category2) {

                 // WE THEN SET THE VIEW TYPE
                      //OR IF CATEGORIES ARE THE SAME, YOU HAVE TO ADD ONLY ONE TO THE CATEGORY INDEX TO BE BOUNDED


                  // WE WILL JUST ADD ONLY ONE OF THE CATEGORIES HERE

                 keeperofproductnames.add(product1);
                 keeperofproductnames.add(product2);
                 viewType = theviewtype.setViewType(Common.ECOMMERCEVIEW);

                 keeperofcategories.add(category1);
                    // WE ONLY SEE OE CATEGORY HERE
                 getcategorywithequalnames  ( keeperofcategories  );
                 getproductwithequalnames  (keeperofproductnames);
                 // WE WILL ADD THE CATEGORIES HERE
                 viewType = theviewtype.setViewType(Common.CATEGORY);
                 Ecommercefullproductlist.add(productdetails);
              return productdetails;
             }

             // WE DO ACCEPT THE WHOLE PRODUCT IN ARRAY HERE WITH A REGULAR VIEW AN WE ACCEPT THE

             else {
                 productgrouplist = new ArrayList<EcommerceGrid>();

                 product1 = productdetails.setProductname(String.valueOf(Ecommercestore.get(i).getProductimage()));
                 product2 = productdetails.setProductname(String.valueOf(Ecommercestore.get(i + 1).getProductimage()));

                 // SET THE VIEWTYPE TO COMMERCEVIEW

                 viewType = theviewtype.setViewType(Common.ECOMMERCEVIEW);

                        //ADD ALL THE PRODUCTS, THE FIRST AND THE SECOND

                 keeperofproductnames.add(product1);
                 keeperofproductnames.add(product2);

                  //GET PRODUCT WITH EQUAL NAMES
                 getproductwithoutequalnames  (  keeperofproductnames );

                   // SET THE VIEWTYPE TO CATEGORY
                 viewType = theviewtype.setViewType(Common.CATEGORY);
                 String product1category = productdetails.setCategory(String.valueOf(Ecommercestore.get(i).getCategory()));
                 String product2category = productdetails.setCategory(String.valueOf(Ecommercestore.get(i + 1).getCategory()));

                      // ADD THE TWO CATEGORIES TO THE LIST

                 keeperofcategories.add(product1category);
                 keeperofcategories.add(product2category);
                 // YOU CAN PULL OUT LIST OF CATEGORIES GAINED SO FAR

                 getcategorieswithoutqualnames  (keeperofcategories);

                 // ADD ALL THE PRODUCT DETAILS HERE
                 Ecommercefullproductlist.add(productdetails);

                 //WE ACCEPT CATEGORY GROUP ALSO HERE

                return  productdetails;
             }
         }
             viewType = theviewtype.setViewType(Common.ECOMMERCEVIEW);
             Ecommercefullproductlist.add(productdetails);
             return productdetails;
     }



        // WE CAN THE POSITION OF AN OBJECT BY TYPING IN THE NAME OR CLICKING ON THE NAME

 public  static int  findPositionwithName(String name, ArrayList<EcommerceGrid> ecommercefullproductlist) {
     int c;
     for (c = 0; c < ecommercefullproductlist.size(); c++) {

         if (ecommercefullproductlist.get(c).getProductname().equals(name)) {

             return c;

         }

     }  return -1;}

     // YOU CAN SEARCH FOR CATEGORIES HERE BY TYPING IN CATEGORY NAME OR CLICKING ON THE NAME

    public  static int  findPositionwithCategory(String name, ArrayList<EcommerceGrid> ecommercefullproductlist) {
        int c;
        for (c = 0; c < ecommercefullproductlist.size(); c++) {

            if (ecommercefullproductlist.get(c).getCategory().equals(name)) {

                return c;

            }

            // IF IT IS NOT THERE

        }  return -1;}

        // WE GENERATE THE ALPHABET OF FOR ALL THE CHARACTERS IN ASCII CODE

    public  static ArrayList<String>generatealphabetsforcategories (){

     ArrayList<String> result = new ArrayList<>();
     int c;
        for (c=65; c<=90;  c++) {
               char c1 =(char)1;
               result.add(String.valueOf(c1));


        }
        return result;

}}




