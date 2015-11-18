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
    Invoice invoice;

    private final static float CASH_FLOW_RATE = new Float(0.05);

    class Order {
        int id;
        int quantity;

        // for bills
        int price;
        int shipping_rates;
        int size;
        boolean problem_c;
    }

    class Product {
        String name;

    }


    class Invoice {
        int payment_method;
    }

    String product_cover;

    class Shipments {
        Receiver_address receiver_address = new Receiver_address();
        Shipment shipment = new Shipment();
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

    class Shipment {
        int quantity;
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
        if (getReturnProblem())
            return 0;
        else
            return order.price;
    }

    public int getShipmentFee() {
        if (getReturnProblem())
            return 0;
        else
            return order.shipping_rates;
    }

    public int getCashFlowFee() {
        if (getReturnProblem())
            return 0;
        else {
            if (getPaymentMethod() == 1 || getPaymentMethod() == 2) {
                double sum = getOrderPrice() + getShipmentFee();
                double fee = sum * CASH_FLOW_RATE;
                return (int) Math.ceil(fee);
            } else {
                return 0;
            }
        }
    }

    public int getReceivedMoney() {
        return getOrderPrice() - getCashFlowFee();
    }

    // for shipping
    public int getShipmentQuantity() {
        return shipments[0].shipment.quantity;
    }

    public String getOrderSize() {
        String[] size = new String[]{"60公分以下", "61公分~90公分", "91公分~120公分"};

        return size[order.size];
    }

    private int getPaymentMethod() {
        return invoice.payment_method;
    }


    public boolean getReturnProblem() {
        return order.problem_c;
    }
}