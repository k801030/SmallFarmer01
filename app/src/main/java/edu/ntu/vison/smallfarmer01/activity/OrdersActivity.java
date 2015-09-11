package edu.ntu.vison.smallfarmer01.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.Order;

/**
 * Created by Vison on 2015/9/11.
 */
public class OrdersActivity extends AppCompatActivity {
    OrdersAdapter mOrdersAdapter;
    ListView mOrderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        mOrdersAdapter = new OrdersAdapter(new ApiService());
        mOrderList = (ListView) findViewById(R.id.orderList);
        mOrderList.setAdapter(mOrdersAdapter);

        mOrdersAdapter.loadOrdersData();
    }

    private class OrdersAdapter extends BaseAdapter {
        private ApiService mApiService;
        public ArrayList<Order> orders = new ArrayList<Order>();;

        public OrdersAdapter(ApiService apiService) {
            this.mApiService = apiService;
        }

        public void loadOrdersData() {
            // TODO: load data via api
            orders.add(new Order());
            orders.add(new Order());
            orders.add(new Order());

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int i) {
            return orders.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(OrdersActivity.this).inflate(R.layout.activity_orders_item, viewGroup, false);
            }

            return view;
        }
    }
}
