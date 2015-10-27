package edu.ntu.vison.smallfarmer01.model;

import java.util.HashMap;
import java.util.Set;

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

    private HashMap<String, Integer> sales; // product name mapping to sales quantity

    private OrderItem[] mOrders;
    public TotalBillCalculator(OrderItem[] orders) {
        sales = new HashMap<>();
        mOrders = orders;
        initBills();
        initSales();
    }

    private void initSales() {
        for (int i=0; i<mOrders.length; i++) {
            OrderItem o = mOrders[i];
            Integer currentQuantity = sales.get(o.getProductName());
            if (currentQuantity == null) {
                sales.put(o.getProductName(), o.getQuantity());
            } else {
                sales.put(o.getProductName(), o.getQuantity() + currentQuantity);
            }
        }
    }

    private void initBills() {
        setTotalSales();
        setCashFlowFee();
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
            totalSales += order.getOrderPrice();
        }
    }

    private void setCashFlowFee() {
        OrderItem order;
        cashFlow = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            double p = (order.getOrderPrice() + order.getShipmentPrice()) * CASH_FLOW_RATE;
            cashFlow += Math.round(p);
        }
    }

    private void setCouponFee() {
        OrderItem order;
        couponFee = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            double p = (order.getOrderPrice() + order.getShipmentPrice()) * COUPON_RATE;
            couponFee += Math.round(p);
        }
    }

    private void setAdminFee() {
        OrderItem order;
        adminFee = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            double p = (order.getOrderPrice() + order.getShipmentPrice()) * ADMINISTRATION_RATE;
            adminFee += Math.round(p);
        }
    }

    private void setSalesTax() {
        OrderItem order;
        salesTax = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            double p = (order.getOrderPrice() + order.getShipmentPrice()) * SALES_TAX_RATE;
            salesTax += Math.ceil(p);
        }
    }

    /**
     * MUST be set after other cash
     */
    private void setReceivedCash() {
        receivedCash = getTotalSales() - getCashFlowFee() - getCouponFee() - getAdminFee() - getTranslateFee() - getSalesTax();
    }


    // Getter


    /**
     * @return key set
     */
    public Set getSalesSet() {
        return sales.keySet();
    }

    public Integer getItemQuantity(String productName) {
        return sales.get(productName);
    }

    /**
     * @return 總銷售金額
     */
    public Integer getTotalSales() {
        return totalSales;
    }

    /**
     * 總金流支出
     * @return
     */
    public Integer getCashFlowFee() {
        return cashFlow;
    }

    /**
     *
     * @return 總回饋金支出
     */
    public Integer getCouponFee() {
        return couponFee;
    }

    /**
     * @return 總行政費用支出
     */
    public Integer getAdminFee() {
        return adminFee;
    }

    /**
     * @return 撥款手續費
     */
    public static Integer getTranslateFee() {
        return TRANSLATE_FEE;
    }

    /**
     * @return 營業稅
     */
    public Integer getSalesTax() {
        return salesTax;
    }

    /**
     * @return 應收金額
     */
    public Integer getReceivedCash() {
        return receivedCash;
    }




}
