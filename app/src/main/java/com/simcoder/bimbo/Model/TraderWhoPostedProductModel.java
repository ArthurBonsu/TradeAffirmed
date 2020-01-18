package  com.simcoder.bimbo.Model;

public class TraderWhoPostedProductModel
{
    private String name,  image, tid;


    public TraderWhoPostedProductModel() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public TraderWhoPostedProductModel( String name, String image, String  tid) {




        this.name = name;


        //NEW GETTERS AND SETTERS
        this.image = image;
         this.tid = tid;

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


    public String getimage(){
        return  image;
    }
    public void setimage(String image) {
        this.image =image;
    }




    public String gettid(){
        return  tid;
    }
    public void settid(String tid) {
        this.tid =tid;
    }



}
