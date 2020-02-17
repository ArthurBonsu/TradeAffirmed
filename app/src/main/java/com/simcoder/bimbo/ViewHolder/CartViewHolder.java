package  com.simcoder.bimbo.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import  com.simcoder.bimbo.Interface.ItemClickListner;
import  com.simcoder.bimbo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView carttheproductname;
    public TextView carttheproductprice;
    public TextView        carttradernamehere;
    public TextView      cartdescriptionhere;
    public TextView     cartquantity;
    public ImageView  cartimageonscreen;
    public ImageView       cartproductimageonscreeen;
    public ImageView numberoflikesimage;
    public ItemClickListner listner;


    public CartViewHolder(View itemView)
    {
        super(itemView);

        //  Productimage = (ImageView) itemView.findViewById(R.id.product_imagehere);
        carttheproductname = (TextView) itemView.findViewById(R.id.carttheproductname);
        carttheproductprice = (TextView) itemView.findViewById(R.id.carttheproductprice);
        carttradernamehere = (TextView) itemView.findViewById(R.id.carttradernamehere);
        cartdescriptionhere = (TextView)itemView.findViewById(R.id.cartdescriptionhere);
        cartquantity = (TextView) itemView.findViewById(R.id.cartquantity);

               //cartimage referst to the trader of the product
        cartimageonscreen = (ImageView) itemView.findViewById(R.id.cartimageonscreen);
        cartproductimageonscreeen = (ImageView) itemView.findViewById(R.id.cartproductimageonscreeen);
        numberoflikesimage = (ImageView)itemView.findViewById(R.id.numberoflikesimage);

   //    cartimageonscreen
        // cartproductimageonscreeen
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

       public  void setcartproductname(String cartproductname){
           carttheproductname = (TextView)itemView.findViewById(R.id.carttheproductname);
           carttheproductname.setText(cartproductname);
    }

    public  void setproductprice(String price){
        carttheproductprice = (TextView)itemView.findViewById(R.id.carttheproductprice);
        carttheproductprice.setText(price);
    }

    public  void settradername(String tradername){
        carttradernamehere = (TextView)itemView.findViewById(R.id.carttradernamehere);
        carttradernamehere.setText(tradername);
    }



    public  void setcartdescriptionhere(String cartdescription){
        cartdescriptionhere  = (TextView)itemView.findViewById(R.id.cartdescriptionhere);
        cartdescriptionhere.setText(cartdescription);
    }



    public  void setcartquantity(String quantity){
        cartquantity = (TextView)itemView.findViewById(R.id.cartquantity);
        cartquantity.setText(quantity);
    }






    public void setImage(final Context ctx, final String image) {
        final ImageView cartproductimageonscreeen = (ImageView) itemView.findViewById(R.id.cartproductimageonscreeen);

        Picasso.get().load(image).resize(400,0).networkPolicy(NetworkPolicy.OFFLINE).into(cartproductimageonscreeen, new Callback() {




            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).resize(100,0).into(cartproductimageonscreeen);
            }



        });


    }

    public void setTraderImage(final Context ctx, final String image) {
        final ImageView cartimageonscreen = (ImageView) itemView.findViewById(R.id.cartimageonscreen);

        Picasso.get().load(image).resize(400,0).networkPolicy(NetworkPolicy.OFFLINE).into(cartimageonscreen, new Callback() {




            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).resize(100,0).into(cartimageonscreen);
            }



        });


    }

    public void setNumberofimage(final Context ctx, final String image) {
        final ImageView numberoflikesimage = (ImageView) itemView.findViewById(R.id.numberoflikesimage);

        Picasso.get().load(image).resize(400,0).networkPolicy(NetworkPolicy.OFFLINE).into(numberoflikesimage, new Callback() {




            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).resize(100,0).into(numberoflikesimage);
            }



        });


    }


    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }



}


