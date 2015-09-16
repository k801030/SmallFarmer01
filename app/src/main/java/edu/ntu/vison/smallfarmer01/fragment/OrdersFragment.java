package edu.ntu.vison.smallfarmer01.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class OrdersFragment extends Fragment {
    OrdersAdapter mOrdersAdapter;
    ListView mOrderList;

    public static OrdersFragment newInstance() {
        Bundle args = new Bundle();

        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        mOrderList = (ListView) view.findViewById(R.id.order_list);
        mOrdersAdapter = new OrdersAdapter(new ApiService(this.getActivity()));
        mOrderList.setAdapter(mOrdersAdapter);

        mOrdersAdapter.loadOrdersData();

        return view;
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
                view = LayoutInflater.from(OrdersFragment.this.getActivity()).inflate(R.layout.fragment_orders_item, viewGroup, false);
            }

            return view;
        }
    }
}
