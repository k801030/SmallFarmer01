package edu.ntu.vison.smallfarmer01.model;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Vison on 2015/9/30.
 */
public class TotalBillCalculator {
    private final static double CASH_FLOW_RATE = 0.05;


    private final static int TRANSLATE_FEE = 15; // 手續費

    private int totalSales; //
    private int cashFlow; // 總金流支出



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
            cashFlow += order.getCashFlowFee();
        }
    }



    /**
     * MUST be set after other cash
     */
    private void setReceivedCash() {
        OrderItem order;
        receivedCash = 0;
        for (int i=0;i<mOrders.length;i++) {
            order = mOrders[i];
            receivedCash += order.getReceivedMoney();
        }
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
    public Integer getTotalCashFlowFee() {
        return cashFlow;
    }

    /**
     *
     * @return 總回饋金支出
     */

    /**
     * @return 撥款手續費
     */
    public static Integer getTranslateFee() {
        return TRANSLATE_FEE;
    }

    /**
     * @return 營業稅
     */

    /**
     * @return 應收金額
     */
    public Integer getReceivedCash() {
        return receivedCash;
    }




}
