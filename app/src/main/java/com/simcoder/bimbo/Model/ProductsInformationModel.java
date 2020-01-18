package  com.simcoder.bimbo.Model;

public class ProductsInformationModel
{
    private String name,   time,   price,desc, pid, image;


    public ProductsInformationModel() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public ProductsInformationModel( String name,String time, String  price, String pid, String image) {




        this.name = name;
        this.time = time;

        //NEW GETTERS AND SETTERS
          this.price = price;
             this.pid = pid;
             this.image = image;
        // MORE GETTERS

        // PARAMETERS PREPARATIONS
        // Product //Driversworking //Driversavailable must be included




        //       delivery,products, amount, quantity,shippingcost, trader, Users, price, [deliveryID, distance, mode, number];
        //MORE GET INSTANTIATION


    }


   // DEVELOPING ALL GETTERS AND SETTERS

    public String getname(){
        return  name;
    }
    public void setname(String name) {
        this.name = name;
    }


     public String gettime(){
        return  time;
    }
    public void settime(String time) {
        this.time =time;
    }




    public String getprice(){
        return  price;
    }
    public void setprice(String price) {
        this.price =price;
    }



    public String getpid(){
        return  pid;
    }
    public void setpid(String pid) {
        this.pid =pid;
    }

    public String getdesc(){
        return  desc;
    }
    public void setdesc(String desc) {
        this.desc=desc;
    }


}
