package edu.ntu.vison.smallfarmer01.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.MainActivity;
import edu.ntu.vison.smallfarmer01.activity.SignInActivity;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.OrderItem;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/11.
 */
public class OrdersFragment extends Fragment {
    OrdersAdapter mOrdersAdapter;
    ListView mOrderList;
    static UserService mUserService;


    public static OrdersFragment newInstance(Context context) {
        mUserService = new UserService(context);

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

    private void goToSignInPage() {
        Intent intent = new Intent(this.getActivity(), SignInActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    /* Adapter */

    private class OrdersAdapter extends BaseAdapter {
        private ApiService mApiService;
        public ArrayList<OrderItem> mOrderItems = new ArrayList<OrderItem>();;

        public OrdersAdapter(ApiService apiService) {
            this.mApiService = apiService;
        }

        public void loadOrdersData() {
            // TODO: load data via api
            mApiService.getOrders(mUserService.getUserId(), mUserService.getAccessToken(), false, new ApiService.GetOrdersCallback() {
                @Override
                public void onSuccess(ArrayList<OrderItem> orderItems) {
                    mOrderItems = orderItems;
                    notifyDataSetChanged();
                }

                @Override
                public void onError(int statusCode) {
                    if (statusCode == 401) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrdersFragment.this.getActivity());
                        builder.setMessage("登入逾時，請重新登入。");
                        builder.setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        goToSignInPage();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });


            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mOrderItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mOrderItems.get(i);
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

            TextView orderIdText = (TextView) view.findViewById(R.id.order_id);
            TextView productNameText = (TextView) view.findViewById(R.id.product_name);
            TextView quantityText = (TextView) view.findViewById(R.id.quantity);

            OrderItem item = mOrderItems.get(i);
            orderIdText.setText(item.getId().toString());
            productNameText.setText(item.getProductName());
            quantityText.setText(item.getQuantity().toString());

            return view;
        }
    }
}
