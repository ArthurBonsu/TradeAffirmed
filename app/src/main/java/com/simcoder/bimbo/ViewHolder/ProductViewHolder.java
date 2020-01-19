package  com.simcoder.bimbo.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import  com.simcoder.bimbo.Interface.ItemClickListner;
import  com.simcoder.bimbo.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice, tradername;
    public ImageView Productimage;
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);

      //  Productimage = (ImageView) itemView.findViewById(R.id.product_imagehere);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        tradername = (TextView)itemView.findViewById(R.id.thetrader);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    public  void setTrader(String trader){
        tradername = (TextView)itemView.findViewById(R.id.thetrader);
        tradername.setText(trader);
    }


    public void setImage(final Context ctx, final String image) {
        final ImageView Productimage = (ImageView) itemView.findViewById(R.id.product_imagehere);

        Picasso.get().load(image).resize(400,0).networkPolicy(NetworkPolicy.OFFLINE).into(Productimage, new Callback() {




            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(image).resize(100,0).into(Productimage);
            }



        });


    }
    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }



}
