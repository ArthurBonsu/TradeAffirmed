package com.simcoder.bimbo;

public class ViewTypes {
    private  int myviewType;

    public ViewTypes() {
        super();
    }

    public ViewTypes(int ourviewType) {
   this.myviewType = myviewType;

    }

    public  int getViewType(){
        return  myviewType;


    }
       public  int setViewType(int myviewType){
        this.myviewType = myviewType;
        return  myviewType;


       }
}
