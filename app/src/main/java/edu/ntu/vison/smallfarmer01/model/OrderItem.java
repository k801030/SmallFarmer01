package edu.ntu.vison.smallfarmer01.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Vison on 2015/9/11.
 */
public class OrderItem {
    Order order;
    Product product;
    Shipments[] shipments;

    class Order {
        int id;
        int quantity;

        // for bills
        int price;
        int shipping_rates;
    }

    class Product {
        String name;

    }

    String product_cover;

    class Shipments {
        Receiver_address receiver_address;
    }

    class Receiver_address {
        // full name
        String last_name;
        String first_name;
        // full address
        String county;
        String district;
        String address;
    }


    // Getter

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

    public String getReceiverName() {
        String lastName = shipments[0].receiver_address.last_name;
        String firstName = shipments[0].receiver_address.first_name;
        return lastName + firstName;
    }

    public String getReceiverAddress() {
        String county = shipments[0].receiver_address.county;
        String district = shipments[0].receiver_address.district;
        String address = shipments[0].receiver_address.address;
        return county + district + address;
    }

    // for bill
    public int getOrderPrice() {
        return order.price;
    }

    public int getShipmentPrice() {
        return order.shipping_rates;
    }

}
