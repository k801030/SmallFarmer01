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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.SignInActivity;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.OrderItem;
import edu.ntu.vison.smallfarmer01.service.NotificationCountBadge;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/11.
 */
public class OrdersFragment extends Fragment {
    OrdersAdapter mOrdersAdapter;
    TextView mNotCalledButton;
    TextView mCalledButton;
    ListView mOrderList;
    Switcher<TextView> mSwitcher;
    static UserService mUserService;
    static ApiService mApiService;
    static Context mContext;


    public static OrdersFragment newInstance(Context context) {
        mUserService = new UserService(context);
        mApiService = new ApiService(context);
        mContext = context;
        Bundle args = new Bundle();

        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // set adapter
        mOrderList = (ListView) view.findViewById(R.id.order_list);
        mOrdersAdapter = new OrdersAdapter(new ApiService(this.getActivity()));
        mOrderList.setAdapter(mOrdersAdapter);

        // set switcher
        mNotCalledButton = (TextView) view.findViewById(R.id.not_called);
        mCalledButton = (TextView) view.findViewById(R.id.called);


        // load data from server
        mSwitcher = new Switcher<>(mNotCalledButton, mCalledButton);
        mSwitcher.setSelect(mNotCalledButton); // default is A

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

        public void loadOrdersData(String isCalled) {

            mOrderItems.clear(); // clear first
            mApiService.getOrders(mUserService.getUserId(), mUserService.getAccessToken(), isCalled, new ApiService.GetOrdersCallback() {
                @Override
                public void onSuccess(ArrayList<OrderItem> orderItems) {
                    mOrderItems = orderItems;
                    myNotifyDataSetChanged();
                }

                @Override
                public void onError(int statusCode) {
                    if (statusCode == 401) {
                        AlertDialog alert = new  ErrorLoadAlert().create();
                        alert.show();
                    }
                }
            });
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

        public void removeItem(int i) {
            mOrderItems.remove(i);
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(OrdersFragment.this.getActivity()).inflate(R.layout.fragment_orders_item, viewGroup, false);
            }

            TextView orderIdText = (TextView) view.findViewById(R.id.order_id);
            TextView productNameText = (TextView) view.findViewById(R.id.product_name);
            TextView quantityText = (TextView) view.findViewById(R.id.quantity);
            TextView receiverNameText = (TextView) view.findViewById(R.id.receiver_name);
            TextView receiverAddressText = (TextView) view.findViewById(R.id.receiver_address);
            ImageView productImage = (ImageView) view.findViewById(R.id.product_image);
            Button confirmButton = (Button) view.findViewById(R.id.confirm_button);


            final OrderItem item = mOrderItems.get(i);
            orderIdText.setText(item.getId().toString());
            productNameText.setText(item.getProductName());
            quantityText.setText(item.getQuantity().toString());
            UrlImageViewHelper.setUrlDrawable(productImage, item.getProductUrl());
            receiverNameText.setText(item.getReceiverName());
            receiverAddressText.setText(item.getReceiverAddress());

            if (!mSwitcher.isLeftSelected()) {
                confirmButton.setVisibility(View.INVISIBLE);
            } else {
                confirmButton.setVisibility(View.VISIBLE);
            }
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alert = new ConfirmOrderAlert(i, item.getId().toString()).create();
                    alert.show();
                }
            });

            return view;
        }
    }

    private class ErrorLoadAlert extends AlertDialog.Builder {
        public ErrorLoadAlert() {
            super(mContext);
            this.setMessage("登入逾時，請重新登入。");
            this.setPositiveButton("確定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            goToSignInPage();
                        }
                    });
        }
    }

    private class ConfirmOrderAlert extends AlertDialog.Builder {
        public ConfirmOrderAlert(final int index, final String orderId) {
            super(mContext);


            this.setMessage("出貨編號:" + orderId);
            this.setCancelable(true);
            this.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            this.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    mApiService.confirmOrder(mUserService.getUserId(), mUserService.getAccessToken(), orderId, new ApiService.ConfirmOrderCallback() {
                        @Override
                        public void onSuccess() {
                            mOrdersAdapter.removeItem(index);
                            myNotifyDataSetChanged();
                            dialogInterface.dismiss();
                        }

                        @Override
                        public void onError(int statusCode) {
                            AlertDialog alert = new ErrorLoadAlert().create();
                            alert.show();
                        }
                    });
                }
            });
        }
    }


    private void myNotifyDataSetChanged() {
        int badgeCount = mOrdersAdapter.getCount();
        NotificationCountBadge.with(getActivity()).setCount(badgeCount);
        mOrdersAdapter.notifyDataSetChanged();
    }

    private class Switcher<T extends TextView> {
        T A, B;
        T selected, unselected;
        public Switcher(final T A, final T B) {
            this.A = A;
            this.B = B;
            this.A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelect(A);
                }
            });
            this.B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelect(B);
                }
            });
        }

        private void setSelect(T selected) {
            this.selected = selected;
            if (A.equals(selected)) {
                unselected = B;
                mOrdersAdapter.loadOrdersData("false");
            } else {
                unselected = A;
                mOrdersAdapter.loadOrdersData("true");
            }
            setView();
        }


        private void setView() {
            selected.setBackgroundColor(getResources().getColor(R.color.main_bg_color));
            selected.setTextColor(getResources().getColor(R.color.main_text_color));
            unselected.setBackgroundColor(getResources().getColor(R.color.default_bg_color));
            unselected.setTextColor(getResources().getColor(R.color.main_bg_color));
        }

        public boolean isLeftSelected() {
            return A.equals(selected);
        }
    }
}
