package com.example.clothecommerceapp.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.interfaces.ItemClickListener;

public class ViewProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView addProductName, addProductPrice;
    private ItemClickListener itemClickListener;
    public ImageView addProductImg;





    public ViewProductsHolder(@NonNull View itemView) {
        super(itemView);

        addProductName=itemView.findViewById(R.id.prod_name);
        addProductPrice=itemView.findViewById(R.id.prod_price);
        addProductImg=itemView.findViewById(R.id.prod_img);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
