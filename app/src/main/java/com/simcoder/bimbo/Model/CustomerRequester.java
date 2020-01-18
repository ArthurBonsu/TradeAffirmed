package  com.simcoder.bimbo.Model;

public class CustomerRequester
{
    private String  customerId;

    public CustomerRequester() {

    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public CustomerRequester(String customerId ) {




        //NEW GETTERS AND SETTERS

        // PARAMETERS PREPARATIONS
        // Product //Driversworking //Driversavailable must be included
        this.customerId=customerId;


        //       delivery,products, amount, quantity,shippingcost, trader, Users, price, [deliveryID, distance, mode, number];
        //MORE GET INSTANTIATION


    }






    public String getCustomerId(){
        return  customerId;
    }
    public void settitle(String customerId) {
        this.customerId =customerId;
    }

}
