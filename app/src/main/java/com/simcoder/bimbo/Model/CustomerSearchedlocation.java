package  com.simcoder.bimbo.Model;

public class CustomerSearchedlocation
{
    private String zero;
    private  String one ;
    private  String destination;
    private  String destinationLat;
    private  String destinationLng;
    public CustomerSearchedlocation() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public    CustomerSearchedlocation(String zero, String one,  String destination, String destinationLat, String destinationLng)
     {



        this.zero = zero;
        this.one = one;

          this.destination =destination;
          this.destinationLat= destinationLat;
          this.destinationLng = destinationLng;

        //       delivery,products, amount, quantity,shippingcost, trader, Users, price, [deliveryID, distance, mode, number];
        //MORE GET INSTANTIATION

    }







    // DEVELOPING ALL GETTERS AND SETTERS
    public String getZero(){
        return  zero;
    }
    public void setuid(String zero) {
        this.zero = zero;
    }

    public String getOne(){
        return  one;
    }
    public void setname(String one) {
        this.one = one;
    }
    public String getdestination(){
        return  destination;
    }
    public void setdestination(String destination) {
        this.destination=destination;
    }



    public String getdestinationLat(){
        return destinationLat;
    }
    public void setdestinationLat(String destinationLat) {
        this.destinationLat =destinationLat;
    }
    public String getdestinationLng(){
        return destinationLng;
    }
    public void setdestinationLng(String  destinationLng) {
        this.destinationLng=destinationLng;
    }



}
