package com.example.clothecommerceapp.model;

public class OrdersModel {

    private String name, city, address, phone, date, totalAmount;


    public OrdersModel() {
    }

    public OrdersModel(String name, String city, String address, String phone, String date, String totalAmount) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.date = date;
        this.totalAmount = totalAmount;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
