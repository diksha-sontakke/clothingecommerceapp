package com.example.clothecommerceapp.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.interfaces.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cartProductName, cartProductPrice,cartQuantity;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        cartProductName=itemView.findViewById(R.id.cart_product_name);
        cartProductPrice=itemView.findViewById(R.id.cart_product_price);
        cartQuantity=itemView.findViewById(R.id.quantity);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.OnClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
