package  com.simcoder.bimbo.Model;

public class DriverLocation
{
    private String zero;
    private  String one ;

    public DriverLocation() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public DriverLocation(String zero, String one ) {



        this.zero = zero;
        this.one = one;


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

}
