package com.example.clothecommerceapp.constant;

import com.example.clothecommerceapp.model.ProductModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Constant {


    public static final String CURRENCY = "₹";

    public static final ProductModel PRODUCT1 = new ProductModel(1, "Jeans and White Top Combo", BigDecimal.valueOf(1999),
            " made of the most durable materials\nand are superbly dense to keep\nyou warm even on the coldest days ","one");
    public static final ProductModel PRODUCT2 = new ProductModel(2, "White and Black Shirt Combo", BigDecimal.valueOf(1099),
            "made of the most durable materials\nand are they are unmatched in\nsophistication and style.",
            "two");
    public static final ProductModel PRODUCT3 = new ProductModel(3, "Three shirt Combo", BigDecimal.valueOf(3099),
            "made of the most durable materials\nand are they are unmatched in\nsophistication and style.\n. It’s slim fit and has accentuated\nshoulders that give a classic style.",
            "three");

    public static final List<ProductModel> PRODUCT_LIST = new ArrayList<ProductModel>();

    static {
        PRODUCT_LIST.add(PRODUCT1);
        PRODUCT_LIST.add(PRODUCT2);
        PRODUCT_LIST.add(PRODUCT3);
    }



}
