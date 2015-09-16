package edu.ntu.vison.smallfarmer01.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Vison on 2015/9/11.
 */
public class OrderItem {
    Order order;
    Product product;

    class Order {
        int id;
        int quantity;
    }

    class Product {
        String name;

    }

    String product_cover;

    public Integer getId() {
        return order.id;
    }

    public Integer getQuantity() {
        return order.quantity;
    }

    public String getProductName() {
        return product.name;
    }

    public String getProductUrl() {
        return product_cover;
    }

}
