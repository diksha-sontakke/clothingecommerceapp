package com.example.clothecommerceapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.clothecommerceapp.R;
import com.example.clothecommerceapp.constant.Constant;
import com.example.clothecommerceapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {



    //This adapter is for ListView
    private static final String TAG = "ProductAdapter";

    private List<ProductModel> products = new ArrayList<ProductModel>();

    private final Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void updateProducts(List<ProductModel> products) {
        this.products.addAll(products);
        notifyDataSetChanged();
    }





    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position , View view, ViewGroup parent) {
        TextView tvName;
        TextView tvPrice;
        ImageView ivImage;
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.product_adapter, parent, false);
            tvName = (TextView) view.findViewById(R.id.tvProductName);
            tvPrice = (TextView) view.findViewById(R.id.tvProductPrice);
            ivImage = (ImageView) view.findViewById(R.id.ivProductImage);
            view.setTag(new ViewHolder(tvName, tvPrice, ivImage));
        } else {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            tvName = viewHolder.tvProductName;
            tvPrice = viewHolder.tvProductPrice;
            ivImage = viewHolder.ivProductImage;
        }

        final ProductModel product = (ProductModel) getItem(position);
        tvName.setText(product.getpName());

        //we have created the constant class
        //currency is th rupees sign from that class
        tvPrice.setText(Constant.CURRENCY+String.valueOf(product.getpPrice().setScale(0, BigDecimal.ROUND_HALF_UP)));
        Log.d(TAG, "Context package name: " + context.getPackageName());
        ivImage.setImageResource(context.getResources().getIdentifier(
                product.getpImageName(), "drawable", context.getPackageName()));
        return view;
    }



    private static class ViewHolder {
        public final TextView tvProductName;
        public final TextView tvProductPrice;
        public final ImageView ivProductImage;

        public ViewHolder(TextView tvProductName, TextView tvProductPrice, ImageView ivProductImage) {
            this.tvProductName = tvProductName;
            this.tvProductPrice = tvProductPrice;
            this.ivProductImage = ivProductImage;
        }
    }

}
