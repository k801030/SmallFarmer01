package edu.ntu.vison.smallfarmer01.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Vison on 2015/9/30.
 */
public class TotalBillCalculator {
    private final static double CASH_FLOW_RATE = 0.03;
    private final static double COUPON_RATE = 0.03;
    private final static double ADMINISTRATION_RATE = 0.04;
    private final static double SALES_TAX_RATE = 0.0075;
    private final static int TRANSLATE_FEE = 15; // 手續費

    private int totalSales; //
    private int cashFlow; // 總金流支出
    private int couponFee; // 總行政費支出
    private int adminFee;
    private int salesTax;
    private int receivedCash; // 應收金額


    private OrderItem[] mOrders;
    public TotalBillCalculator(OrderItem[] orders) {
        mOrders = orders;
        init();
    }


    private void init() {
        setTotalSales();
        setCashFlow();
        setCouponFee();
        setAdminFee();
        setSalesTax();
        setReceivedCash();
    }


    // Setter

    private void setTotalSales() {
        OrderItem order;
        totalSales = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            totalSales += order.getOrderPrice() + order.getShipmentPrice();
        }
    }

    private void setCashFlow() {
        OrderItem order;
        cashFlow = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            cashFlow += Math.round(order.getOrderPrice() + order.getShipmentPrice());
        }
    }

    private void setCouponFee() {
        OrderItem order;
        couponFee = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            couponFee += Math.round(order.getOrderPrice() + order.getShipmentPrice());
        }
    }

    private void setAdminFee() {
        OrderItem order;
        adminFee = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            adminFee += Math.round(order.getOrderPrice() + order.getShipmentPrice());
        }
    }

    private void setSalesTax() {
        OrderItem order;
        salesTax = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            salesTax += Math.ceil(order.getOrderPrice() + order.getShipmentPrice());
        }
    }

    /**
     * MUST be set after other cash
     */
    private void setReceivedCash() {
        receivedCash = getTotalSales() - getCashFlow() - getCouponFee() - getAdminFee() - getTranslateFee() - getSalesTax();
    }


    // Getter

    /**
     * @return 總銷售金額
     */
    public int getTotalSales() {
        return totalSales;
    }

    /**
     * 總金流支出
     * @return
     */
    public int getCashFlow() {
        return cashFlow;
    }

    /**
     *
     * @return 總回饋金支出
     */
    public int getCouponFee() {
        return couponFee;
    }

    /**
     * @return 總行政費用支出
     */
    public int getAdminFee() {
        return adminFee;
    }

    /**
     * @return 撥款手續費
     */
    public static int getTranslateFee() {
        return TRANSLATE_FEE;
    }

    /**
     * @return 營業稅
     */
    public int getSalesTax() {
        return salesTax;
    }

    /**
     * @return 應收金額
     */
    public int getReceivedCash() {
        return receivedCash;
    }




}
