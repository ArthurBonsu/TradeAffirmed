package  com.simcoder.bimbo.Model;

public class CustomerLocationAndInfo
{
    private String name, phone, image;
    public CustomerLocationAndInfo() {
    }
    //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public CustomerLocationAndInfo(String name, String phone,String image ) {




        this.name = name;
        this.phone = phone;
        this.image = image;

        // PARAMETERS PREPARATIONS
        // Product //Driversworking //Driversavailable must be included


    }







    // DEVELOPING ALL GETTERS AND SETTERS

    public String getname(){
        return  name;
    }
    public void setname(String name) {
        this.name = name;
    }

    public String getphone(){
        return  phone;
    }
    public void setphone(String phone) {
        this.phone = phone;
    }


    public String getimage(){
        return  image;
    }
    public void setimage(String image) {
        this.image=image;
    }






}
