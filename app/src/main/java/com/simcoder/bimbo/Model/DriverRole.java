package  com.simcoder.bimbo.Model;

public class DriverRole
{
    private String  role;
    public DriverRole() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public DriverRole(String role) {


        // PARAMETERS PREPARATIONS
        // Product //Driversworking //Driversavailable must be included
 this.role=role;
    }



    public String getrole(){
        return  role;
    }
    public void setrole(String role) {
        this.role =role;
    }

}
