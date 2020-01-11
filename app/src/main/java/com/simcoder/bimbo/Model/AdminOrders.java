package  com.simcoder.bimbo.Model;

public class AdminOrders
{
    private String name, phone, address, city, state, date, time, totalAmount, uid, delivery,products, amount, quantity,shippingcost, trader, Users, price,deliveryID, distance, mode, number;

    public AdminOrders() {
    }

    public AdminOrders(String uid, String name, String phone, String address, String city, String state, String date, String time, String totalAmount,String delivery, String products, String amount, String quantity, String shippingcost, String trader, String Users, String  price,
                     String   deliveryID, String distance, String mode, String number) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;

        //NEW GETTERS AND SETTERS

        this.delivery = delivery;
        this.products = products;
        this.amount = amount;
        this.quantity = quantity;
        this.shippingcost = shippingcost;
        this.trader = trader;
        this.Users = Users;
        this.price = price;

          // MORE GETTERS
        this.deliveryID = deliveryID;
        this.distance = distance;
        this.mode = mode;
        this.number = number;


        //       delivery,products, amount, quantity,shippingcost, trader, Users, price, [deliveryID, distance, mode, number];

    }

    public String getdelivery() {
        return delivery;
    }

    public void setdelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getproducts() {
        return products;
    }

    public void setproducts(String products) {
        this.products = products;
    }

    public String getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    public String getquantity() {
        return quantity;
    }

    public void setquantity(String quantity) {
        this.quantity = quantity;
    }

    public String getshippingcost() {
        return shippingcost;
    }

    public void setshippingcost(String shippingcost) {
        this.shippingcost = shippingcost;
    }

    public String gettrader() {
        return trader;
    }

    public void settrader(String trader) {
        this.trader = trader;
    }

    public String getUsers() {
        return Users;
    }

    public void setUsers(String Users) {
        this.Users = Users;
    }

    public String getprice() {
        return price;
    }

    public void setprice(String price) {
        this.price = price;
    }




    public String getdeliveryID() {
        return deliveryID;
    }

    public void setdeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getdistance() {
        return distance;
    }

    public void setdistance(String distance) {
        this.distance = distance;
    }

    public String getmode() {
        return mode;
    }

    public void setmode(String mode) {
        this.mode = mode;
    }

    public String getnumber() {
        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }



    // NEW SETTERS AND GETTERS
         public String getUid() {
             return uid;
         }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


}
