package edu.ntu.vison.smallfarmer01.model;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import edu.ntu.vison.smallfarmer01.R;

/**
 * Created by Vison on 2015/9/11.
 */
public class OrderItem {
    Order order;


    Product product;
    Shipments[] shipments;
    Invoice invoice;
    Product_boxing product_boxing;

    private final static double CASH_FLOW_RATE = new Double(0.05);

    class Order {
        int id;
        int quantity;

        // for bills
        int price;
        int shipping_rates;
        int size;
        boolean problem_c;

        // for order sorting
        String called_smallfarmer_at;


    }

    class Product_boxing {
        int quantity;
    }
    class Product {
        String name;
        int unit;
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

    public Date getCallFarmerAt() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(order.called_smallfarmer_at);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public String getUnit() {
        String[] unit = new String[]{"台斤", "公斤", "顆", "包", "罐", "毫升", "公克", "盒"};
        return unit[product.unit];
    }

    public int getBoxingQuantity() {
        return product_boxing.quantity;
    }


}