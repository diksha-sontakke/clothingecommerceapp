package com.example.clothecommerceapp.model;

import java.math.BigDecimal;

public class ProductModel {


    private int pId;
    private String pName;
    private BigDecimal pPrice;
    private String pDescription;
    private String pImageName;

    public ProductModel(int pId, String pName, BigDecimal pPrice, String pDescription, String pImageName) {
        this.pId = pId;
        this.pName = pName;
        this.pPrice = pPrice;
        this.pDescription = pDescription;
        this.pImageName = pImageName;
    }
    public ProductModel(){}

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public BigDecimal getpPrice() {
        return pPrice;
    }

    public void setpPrice(BigDecimal pPrice) {
        this.pPrice = pPrice;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getpImageName() {
        return pImageName;
    }

    public void setpImageName(String pImageName) {
        this.pImageName = pImageName;
    }
}
