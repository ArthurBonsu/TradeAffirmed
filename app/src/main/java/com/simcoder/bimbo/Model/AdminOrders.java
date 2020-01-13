package  com.simcoder.bimbo.Model;

public class AdminOrders
{
    private String name, phone, address, city, state, date, time, totalAmount, uid, delivery,products, amount, quantity,shippingcost, trader, Users, price,deliveryID, distance, mode, number,desc;
   private String count, likeID, Orders, orderID, Review, reviewID, condition,  deliverID,  traderID;
   private  String  Customers,Category, pid,coverimage,customerRequest, g, l, customerId, customerRideId,destination,destinationLat;
   private String  destinationLng, followers, followersID,customer,  driver, location, from, lat,lng,to, predictDistance, rating, timestamp;
   private String triptime, image, job,jobcategory, jobcategoryid, password, quote, quoteid, role, Drivers, Ads,AdID;
   private String  driverFoundID, descriptions,field,followesID, followesname, history;
   private  String customerPaid,driverPaidOut, operations;
   private  String product, productID, productName, reviewBy, text, residences, service,setinformations, baseprice;
   private  String     subCategory, subcategoryName, SupplementaryCategory,SupplementaryCategoryID, FinalCategory, FinalCategoryID,FinalSubCategoryName, Product, title,categoryID,category, Likes, driversAvailable, driversWorking, tid;
    public AdminOrders() {
    }
 //careful of deliverID and deliveryID
    /// DRIVERS AVAILABLE // DRIVERS WORKING
    public AdminOrders(String uid, String name, String phone, String address, String city, String state, String date, String time, String totalAmount,String delivery, String products, String amount, String quantity, String shippingcost, String trader, String Users, String  price,
                     String   deliveryID, String distance, String mode, String number, String count,String likeID,String Orders,String orderID,String Review,String reviewID,String condition,String  deliverID, String traderID,
                                 String  Customers, String Category,String pid, String coverimage, String customerRequest,String g, String l,String customerId,String customerRideId, String destination, String destinationLat,
                                String  destinationLng, String followers, String followersID, String customer, String  driver, String location, String from,String lat, String lng, String to, String predictDistance,String rating,String timestamp,
                                String triptime,String image,String job,String jobcategory, String jobcategoryid,String password,String quote,String quoteid,String role, String Drivers, String Ads, String AdID,
                                String  driverFoundID, String descriptions, String field, String followesID, String followesname, String history,
                               String customerPaid, String driverPaidOut, String operations,
                                String product,String productID, String productName,String reviewBy,String text,String residences, String service, String setinformations,String baseprice,
                       String subCategory,String subcategoryName,String SupplementaryCategory,String SupplementaryCategoryID, String FinalCategory, String FinalCategoryID,String FinalSubCategoryName,String Product,String title,String categoryID, String category, String Likes, String desc,
                      String driversAvailable,String  driversWorking, String tid ) {



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
        this.driversAvailable = driversAvailable;
        this.driversWorking = driversWorking;

           // PARAMETERS PREPARATIONS
            // Product //Driversworking //Driversavailable must be included
         this.count = count;this.likeID = likeID; this.Orders = Orders; this.orderID = orderID;this.Review = Review; this.reviewID =reviewID; this.condition =condition; this.deliverID =deliverID; this.traderID =traderID;
                this.Customers =Customers; this.Category = Category; this.pid = pid; this.coverimage =coverimage;this.customerRequest =customerRequest;this.g= g; this.l=l;this.customerId=customerId; this.customerRideId =customerRideId; this.destination =destination;this.destinationLat =destinationLat;this.destinationLng= destinationLng;
                this.destinationLng =destinationLng;  this.followers =followers;this.followersID =followersID; this.customer = customer; this.driver =driver; this.location =  location; this.from = from; this.lat= lat; this.lng = lng; this.to= to; this.predictDistance = predictDistance;this.rating =rating;this.timestamp = timestamp;
                 this.triptime = triptime;  this.image = image;  this.job = job; this.jobcategory = jobcategory;this.jobcategoryid = jobcategoryid;this.password = password; this.quote = quote;this.quoteid = quoteid; this.role=role; this.Drivers = Drivers; this.Ads =Ads; this.AdID= AdID;
                this.driverFoundID = driverFoundID; this.descriptions= descriptions; this.field=field; this.followesID=followesID; this.followesname =followesname; this.history= history;
                this.customerPaid= customerPaid; this.driverPaidOut= driverPaidOut; this.operations= operations;
                this.product = product; this.productID= productID; this.productName = productName;this.reviewBy =reviewBy;this.text =text; this.residences= residences;this.service =service;this.setinformations= setinformations;this.baseprice =baseprice;this.desc = desc;


        //       delivery,products, amount, quantity,shippingcost, trader, Users, price, [deliveryID, distance, mode, number];
       //MORE GET INSTANTIATION
        this.subCategory = subCategory; this.subcategoryName = subcategoryName; this.SupplementaryCategory = SupplementaryCategory; this.SupplementaryCategoryID =SupplementaryCategoryID; this.FinalCategory = FinalCategory; this.FinalCategoryID= FinalCategoryID; this.FinalSubCategoryName = FinalSubCategoryName;this.Product = Product; this.title =title; this.categoryID = categoryID; this.category = category; this.Likes = Likes; this.tid= tid;

    }







// DEVELOPING ALL GETTERS AND SETTERS
   public String getuid(){
     return  uid;
    }
    public void setuid(String uid) {
        this.uid = uid;
    }

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

    public String getaddress(){
        return  address;
    }
    public void setaddress(String address) {
        this.address = address;
    }

    public String getcity(){
        return  city;
    }
    public void setcity(String city) {
        this.city =city;
    }

    public String getstate(){
        return  state;
    }
    public void setstate(String state) {
        this.state =state;
    }

    public String getdate(){
        return  date;
    }
    public void setdate(String date) {
        this.date=date;
    }
    public String gettime(){
        return  time;
    }
    public void settime(String time) {
        this.time =time;
    }
    public String gettotalAmount(){
        return  totalAmount;
    }
    public void settotalAmount(String totalAmount) {
        this.totalAmount=totalAmount;
    }
    public String getdelivery(){
        return  delivery;
    }
    public void setdelivery(String delivery) {
        this.delivery =delivery;
    }

    public String getproducts(){
        return  products;
    }
    public void setproducts(String products) {
        this.products =products;
    }
    public String getamount(){
        return  amount;
    }
    public void setamount(String amount) {
        this.amount =amount;
    }


    public String getquantity(){
        return  quantity;
    }
    public void setquantity(String quantity) {
        this.quantity =quantity;
    }
    public String getshippingcost(){
        return  shippingcost;
    }
    public void setshippingcost(String shippingcost) {
        this.shippingcost =shippingcost;
    }



    public String gettrader(){
        return  trader;
    }
    public void settrader(String trader) {
        this.trader =trader;
    }
    public String getUsers(){
        return   Users;
    }
    public void setUsers(String  Users) {
        this. Users = Users;
    }



    public String getprice(){
        return  price;
    }
    public void setprice(String price) {
        this.price =price;
    }
    public String getdeliveryID(){
        return   deliveryID;
    }
    public void setdeliveryID(String  deliveryID) {
        this.deliveryID = deliveryID;
    }



    public String getdistance(){
        return  distance;
    }
    public void setdistance(String distance) {
        this.distance =distance;
    }
    public String getmode(){
        return  mode;
    }
    public void setmode(String  mode) {
        this.mode= mode;
    }

    public String getnumber(){
        return  number;
    }
    public void setnumber(String number) {
        this.number =number;
    }
    public String getcount(){
        return  count;
    }
    public void setcount(String  count) {
        this.count= count;
    }

    public String getlikeID(){
        return  likeID;
    }
    public void setlikeID(String likeID) {
        this.likeID =likeID;
    }
    public String getOrders(){
        return  Orders;
    }
    public void setOrders(String  Orders) {
        this.Orders= Orders;
    }




    public String getorderID(){
        return  orderID;
    }
    public void setorderID(String orderID) {
        this.orderID =orderID;
    }
    public String getReview(){
        return Review;
    }
    public void setReview(String  Review) {
        this.Review=Review;
    }

    public String getcondition(){
        return  condition;
    }
    public void setcondition(String condition) {
        this.condition =condition;
    }
    public String getreviewID(){
        return  reviewID;
    }
    public void setreviewID(String  reviewID) {
        this.reviewID= reviewID;
    }



    public String getdeliverID(){
        return  deliverID;
    }
    public void setdeliverID(String deliverID) {
        this.deliverID =deliverID;
    }
    public String gettraderID(){
        return traderID;
    }
    public void settraderID(String  traderID) {
        this.traderID=traderID;
    }

    public String getCustomers(){
        return  Customers;
    }
    public void setCustomers(String Customers) {
        this.Customers =Customers;
    }
    public String getCategory(){
        return  Category;
    }
    public void setCategory(String  Category) {
        this.Category= Category;
    }




    public String getpid(){
        return  pid;
    }
    public void setpid(String pid) {
        this.pid =pid;
    }
    public String getcoverimage(){
        return coverimage;
    }
    public void setcoverimage(String  coverimage) {
        this.coverimage=coverimage;
    }

    public String getcustomerRequest(){
        return  customerRequest;
    }
    public void setcustomerRequest(String customerRequest) {
        this.customerRequest =customerRequest;
    }
    public String getg(){
        return  g;
    }
    public void setg(String  g) {
        this.g= g;
    }




    public String getl(){
        return l;
    }
    public void setl(String l) {
        this.l =l;
    }
    public String getcustomerId(){
        return customerId;
    }
    public void setcustomerId(String  customerId) {
        this.customerId=customerId;
    }

    public String getcustomerRideId(){
        return  customerRideId;
    }
    public void setcustomerRideId(String customerRideId) {
        this.customerRideId =customerRideId;
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

    public String getfollowers(){
        return  followers;
    }
    public void setfollowers(String followers) {
        this.followers =followers;
    }
    public String getfollowersID(){
        return  followersID;
    }
    public void setfollowersID(String followersID) {
        this.followersID=followersID;
    }




    public String getcustomer(){
        return customer;
    }
    public void setcustomer(String customer) {
        this.customer =customer;
    }
    public String getdriver(){
        return driver;
    }
    public void setdriver(String driver) {
        this.driver=driver;
    }

    public String getlocation(){
        return  location;
    }
    public void setlocation(String location) {
        this.followers =location;
    }
    public String getfrom(){
        return  from;
    }
    public void setfrom(String from) {
        this.from=from;
    }





    public String getlat(){
        return lat;
    }
    public void setlat(String lat) {
        this.lat =lat;
    }
    public String getlng(){
        return lng;
    }
    public void setlng(String lng) {
        this.lng=lng;
    }

    public String getto(){
        return  to;
    }
    public void setto(String location) {
        this.to =to;
    }
    public String getpredictDistance(){
        return  predictDistance;
    }
    public void setpredictDistance(String predictDistance) {
        this.predictDistance=predictDistance;
    }




    public String getrating(){
        return rating;
    }
    public void setrating(String rating) {
        this.rating =rating;
    }
    public String gettimestamp(){
        return timestamp;
    }
    public void settimestamp(String timestamp) {
        this.timestamp=timestamp;
    }

    public String gettriptime(){
        return  triptime;
    }
    public void settriptime(String triptime) {
        this.triptime =triptime;
    }
    public String getimage(){
        return  image;
    }
    public void setimage(String image) {
        this.image=image;
    }





    public String getjob(){
        return job;
    }
    public void setjob(String job) {
        this.job =job;
    }
    public String getjobcategory(){
        return jobcategory;
    }
    public void setjobcategory(String jobcategory) {
        this.jobcategory=jobcategory;
    }

    public String getjobcategoryid(){
        return  jobcategoryid;
    }
    public void setjobcategoryid(String jobcategoryid) {
        this.jobcategoryid =jobcategoryid;
    }
    public String getpassword(){
        return  password;
    }
    public void setpassword(String password) {
        this.password=password;
    }




    public String getquote(){
        return quote;
    }
    public void setquote(String quote) {
        this.quote =quote;
    }
    public String getquoteid(){
        return quoteid;
    }
    public void setquoteid(String quoteid) {
        this.quoteid=quoteid;
    }

    public String getrole(){
        return  role;
    }
    public void setrole(String role) {
        this.role =role;
    }
    public String getDrivers(){
        return  Drivers;
    }
    public void setDrivers(String Drivers) {
        this.Drivers=Drivers;
    }




    public String getAds(){
        return Ads;
    }
    public void setAds(String Ads) {
        this.Ads =Ads;
    }
    public String getAdID(){
        return AdID;
    }
    public void setAdID(String AdID) {
        this.AdID=AdID;
    }

    public String getdriverFoundID(){
        return  driverFoundID;
    }
    public void setdriverFoundID(String driverFoundID) {
        this.driverFoundID =driverFoundID;
    }
    public String getdescriptions(){
        return  descriptions;
    }
    public void setdescriptions(String descriptions) {
        this.descriptions=descriptions;
    }




    public String getfield(){
        return field;
    }
    public void setfield(String field) {
        this.field =field;
    }
    public String getfollowesID(){
        return followesID;
    }
    public void setfollowesID(String followesID) {
        this.followesID=followesID;
    }

    public String getfollowesname(){
        return  followesname;
    }
    public void setfollowesname(String followesname) {
        this.followesname =followesname;
    }
    public String gethistory(){
        return  history;
    }
    public void sethistory(String history) {
        this.history=history;
    }


    public String getcustomerPaid(){
        return  customerPaid;
    }
    public void setcustomerPaid(String customerPaid) {
        this.customerPaid =customerPaid;
    }
    public String getdriverPaidOut(){
        return  driverPaidOut;
    }
    public void setdriverPaidOut(String driverPaidOut) {
        this.driverPaidOut=driverPaidOut;
    }
    public String getoperations(){
        return  operations;
    }
    public void setoperations(String operations) {
        this.operations =operations;
    }
    public String getproduct(){
        return  product;
    }
    public void setproduct(String product) {
        this.product=product;
    }


    public String getproductID(){
        return  productID;
    }
    public void setproductID(String productID) {
        this.productID =productID;
    }
    public String getproductName(){
        return  productName;
    }
    public void setproductName(String productName) {
        this.productName=productName;
    }
    public String getreviewBy(){
        return  reviewBy;
    }
    public void setreviewBy(String reviewBy) {
        this.reviewBy =reviewBy;
    }
    public String gettext(){
        return  text;
    }
    public void settext(String text) {
        this.text=text;
    }


    public String getresidences(){
        return  residences;
    }
    public void setresidences(String residences) {
        this.residences =residences;
    }
    public String getservice(){
        return  service;
    }
    public void setservice(String service) {
        this.service=service;
    }
    public String getsetinformations(){
        return  setinformations;
    }
    public void setsetinformations(String setinformations) {
        this.setinformations =setinformations;
    }
    public String getbaseprice(){
        return  baseprice;
    }
    public void setbaseprice(String baseprice) {
        this.baseprice=baseprice;
    }



    public String getsubCategory(){
        return  subCategory;
    }
    public void setsubCategory(String subCategory) {
        this.subCategory =subCategory;
    }
    public String getsubcategoryName(){
        return  subcategoryName;
    }
    public void setsubcategoryName(String subcategoryName) {
        this.subcategoryName=subcategoryName;
    }
    public String getSupplementaryCategory(){
        return SupplementaryCategory;
    }
    public void setSupplementaryCategory(String SupplementaryCategory) {
        this.SupplementaryCategory =SupplementaryCategory;
    }
    public String getSupplementaryCategoryID(){
        return  SupplementaryCategoryID;
    }
    public void setSupplementaryCategoryID(String SupplementaryCategoryID) {
        this.SupplementaryCategoryID=SupplementaryCategoryID;
    }





    public String getFinalCategory(){
        return  FinalCategory;
    }
    public void setFinalCategory(String FinalCategory) {
        this.FinalCategory =FinalCategory;
    }
    public String getFinalCategoryID(){
        return  FinalCategoryID;
    }
    public void setFinalCategoryID(String FinalCategoryID) {
        this.FinalCategoryID=FinalCategoryID;
    }
    public String getFinalSubCategoryName(){
        return FinalSubCategoryName;
    }
    public void setFinalSubCategoryName(String FinalSubCategoryName) {
        this.FinalSubCategoryName =FinalSubCategoryName;
    }
    public String getProduct(){
        return  Product;
    }
    public void setProduct(String Product) {
        this.Product=Product;
    }



    public String gettitle(){
        return  title;
    }
    public void settitle(String title) {
        this.title =title;
    }
    public String getcategoryID(){
        return  categoryID;
    }
    public void setcategoryID(String categoryID) {
        this.categoryID=categoryID;
    }
    public String getcategory(){
        return category;
    }
    public void setcategory(String category) {
        this.category =category;
    }
    public String getLikes(){
        return  Likes;
    }
    public void setLikes(String Likes) {
        this.Likes=Likes;
    }


    public String getdriversAvailable(){
        return driversAvailable;
    }
    public void setdriversAvailable(String driversAvailable) {
        this.driversAvailable =driversAvailable;
    }
    public String getdriversWorking(){
        return  driversWorking;
    }
    public void setdriversWorking(String driversWorking) {
        this.driversWorking=driversWorking;
    }


    public String getdesc(){
        return  desc;
    }
    public void setdesc(String desc) {
        this.desc=desc;
    }


    public String gettid(){
        return  tid;
    }
    public void settid(String tid) {
        this.tid=tid;
    }

}
