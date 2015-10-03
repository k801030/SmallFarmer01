package edu.ntu.vison.smallfarmer01.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.reflect.Type;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.model.OrderItem;

/**
 * Created by Vison on 2015/9/30.
 */
public class BillDetailActivity extends AppCompatActivity {

    static final String TAG = "BILL_DETAIL_ACTIVITY";
    static final int HOME_OPTION = 16908332;
    ListView mOrderList;
    OrderAdapter mOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        mOrderList = (ListView) findViewById(R.id.order_list);
        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // set homeAsUp color
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // get extra data
        Intent i = getIntent();
        Gson gson = new Gson();
        String json = i.getStringExtra("orders");
        Type orderClass = new TypeToken<OrderItem[]>(){}.getType();
        OrderItem[] orderItems = gson.fromJson(json, orderClass);

        mOrderAdapter = new OrderAdapter();
        mOrderList.setAdapter(mOrderAdapter);
        mOrderAdapter.setOrders(orderItems);
        mOrderAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case HOME_OPTION:
                Log.d(TAG, "press home button");
               // Home as up button is to navigate to Home-Activity not previous activity
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * For ListView
     */
    private class OrderAdapter extends BaseAdapter {

        OrderItem[] orders;

        public OrderAdapter() {
            orders = new OrderItem[0];
        }

        public void setOrders(OrderItem[] orders) {
            this.orders = orders;
        }


        @Override
        public int getCount() {
            return orders.length;
        }

        @Override
        public Object getItem(int i) {
            return orders[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(BillDetailActivity.this).inflate(R.layout.activity_bills_item, viewGroup, false);
            }

            TextView orderIdText = (TextView) view.findViewById(R.id.order_id);
            TextView productNameText = (TextView) view.findViewById(R.id.product_name);
            TextView quantityText = (TextView) view.findViewById(R.id.quantity);
            TextView receiverNameText = (TextView) view.findViewById(R.id.receiver_info);
            RoundedImageView productImage = (RoundedImageView) view.findViewById(R.id.product_image);
            TextView receivedMoneyText = (TextView) view.findViewById(R.id.received_money);

            final OrderItem item = orders[i];
            orderIdText.setText(item.getId().toString());
            productNameText.setText(item.getProductName());
            quantityText.setText(item.getQuantity().toString());
            receiverNameText.setText(item.getReceiverName());
            UrlImageViewHelper.setUrlDrawable(productImage, item.getProductUrl());
            productImage.setCornerRadius(productImage.getWidth() / 2);
            // It's RECEIVED MONEY: 0.9*price
            long receivedMoney = Math.round(item.getOrderPrice() * 0.9);
            receivedMoneyText.setText(Long.toString(receivedMoney));

            return view;
        }
    }
}
