package edu.ntu.vison.smallfarmer01.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.SignInActivity;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.OrderItem;
import edu.ntu.vison.smallfarmer01.service.push_notification.NotificationCountBadge;
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
    UserService mUserService;
    ApiService mApiService;

    public OrdersFragment () {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // getActivity() can return null if it is called before onAttach of the respective fragment.
        mUserService = new UserService(getActivity());
        mApiService = new ApiService(getActivity());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        // set adapter
        mOrderList = (ListView) view.findViewById(R.id.order_list);
        mOrdersAdapter = new OrdersAdapter(new ApiService(this.getActivity()));
        mOrderList.setAdapter(mOrdersAdapter);
        View emptyView =  view.findViewById(R.id.order_empty);
        mOrderList.setEmptyView(emptyView);

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
        public ArrayList<OrderItem> mOrderItems_false = new ArrayList<OrderItem>(); // notify is false
        public ArrayList<OrderItem> mOrderItems_true = new ArrayList<OrderItem>(); // notify is true
        public ArrayList<OrderItem> mOrderItems = new ArrayList<OrderItem>(); // notify is true

        public OrdersAdapter(ApiService apiService) {
            this.mApiService = apiService;
            init();
        }

        public void init() {
            getFirstDataSet("false");
            getFirstDataSet("true");
        }

        private void getFirstDataSet(final String isCalled) {
            mApiService.getOrders(mUserService.getUserId(), mUserService.getAccessToken(), isCalled, new ApiService.GetOrdersCallback() {
                @Override
                public void onSuccess(ArrayList<OrderItem> orderItems) {
                    if (isCalled == "false") {
                        mOrderItems_false = orderItems;
                    } else {
                        mOrderItems_true = orderItems;
                    }
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

        public void loadOrdersData(final String isCalled) {

            // make updating quick
            if (isCalled == "false") {
                mOrderItems = mOrderItems_false;
            } else {
                mOrderItems = mOrderItems_true;
            }
            mOrdersAdapter.notifyDataSetChanged();

            notifyDataSetChanged();
            mApiService.getOrders(mUserService.getUserId(), mUserService.getAccessToken(), isCalled, new ApiService.GetOrdersCallback() {
                @Override
                public void onSuccess(ArrayList<OrderItem> orderItems) {
                    if (isCalled == "false") {
                        mOrderItems_false = orderItems;
                    } else {
                        mOrderItems_true = orderItems;
                    }
                    // mOrderItems = orderItems;
                    if (isCalled == "false") { // list that is not call yet
                        int badgeCount = mOrdersAdapter.getCount();
                        NotificationCountBadge.with(getActivity()).setCount(badgeCount);
                    }
                    mOrdersAdapter.notifyDataSetChanged();
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

        public void removeItem(OrderItem order) {
            mOrderItems_true.add(order);
            mOrderItems_false.remove(order);
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(OrdersFragment.this.getActivity()).inflate(R.layout.fragment_orders_item, viewGroup, false);
            }

            TextView orderIdText = (TextView) view.findViewById(R.id.order_id);
            TextView productNameText = (TextView) view.findViewById(R.id.product_name);
            TextView quantityText = (TextView) view.findViewById(R.id.quantity);
            TextView receiverInfoText = (TextView) view.findViewById(R.id.receiver_info);
            TextView shipmentDetailText = (TextView) view.findViewById(R.id.shipment_detail);
            RoundedImageView productImage = (RoundedImageView) view.findViewById(R.id.product_image);
            View confirmButton = view.findViewById(R.id.notify_shipment_button);


            final OrderItem item = mOrderItems.get(i);
            orderIdText.setText(item.getId().toString());
            productNameText.setText(item.getProductName());
            quantityText.setText(item.getQuantity().toString());

            // appending receiver information
            receiverInfoText.setText(item.getReceiverName());
            receiverInfoText.append("｜");
            receiverInfoText.append(item.getReceiverAddress());

            // appending shipment detail
            int a = item.getQuantity() / 2;
            int b = item.getQuantity() % 2;
            int j = 1;
            shipmentDetailText.setText("");
            while(j<=a) {
                String id = item.getId()+"_"+j;
                String type = "2箱" + item.getOrderSize() + "綁一起";
                String text = id +" " + type + "\n";
                Spannable span = new SpannableString(text);
                final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.light_text));
                final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                span.setSpan(fcs, 0, id.length(), 0);
                span.setSpan(bss, 0, id.length(), 0);
                shipmentDetailText.append(span);

                j++;
            }
            if (b == 1) {
                String id = item.getId()+"_"+j;
                String type = "1箱" + item.getOrderSize();
                String text = id +" " + type + "\n";
                Spannable span = new SpannableString(text);
                final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.light_text));
                final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                span.setSpan(fcs, 0, id.length(), 0);
                span.setSpan(bss, 0, id.length(), 0);
                shipmentDetailText.append(span);
            }


            UrlImageViewHelper.setUrlDrawable(productImage, item.getProductUrl());
            productImage.setCornerRadius(productImage.getWidth() / 2);

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
                    Button negButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    negButton.setTextColor(getResources().getColor(R.color.default_text_color));
                    Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    posButton.setTextColor(getResources().getColor(R.color.color_primary));
                }
            });

            return view;
        }
    }

    private class ErrorLoadAlert extends AlertDialog.Builder {
        public ErrorLoadAlert() {
            super(getActivity());
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
            super(getActivity());

            this.setTitle("通知物流");
            this.setMessage("出貨編號：" + orderId+"\n\n下午3:30前通知，物流將於隔日取貨\n下午3:30後通知，物流將於兩日後取貨");
            this.setCancelable(true);
            this.setPositiveButton("確定出貨", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                    mApiService.confirmOrder(mUserService.getUserId(), mUserService.getAccessToken(), orderId, new ApiService.ConfirmOrderCallback() {
                        @Override
                        public void onSuccess() {
                            // clear
                            OrderItem i = (OrderItem)mOrdersAdapter.getItem(index);
                            mOrdersAdapter.removeItem(i);
                            mOrdersAdapter.notifyDataSetChanged();


                            int badgeCount = mOrdersAdapter.getCount();
                            NotificationCountBadge.with(getActivity()).setCount(badgeCount);

                            mOrdersAdapter.notifyDataSetChanged();
                            dialogInterface.dismiss();
                        }

                        @Override
                        public void onError(int statusCode) {
                            AlertDialog alert = new ErrorLoadAlert().create();
                            alert.show();
                            Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            posButton.setTextColor(getResources().getColor(R.color.color_primary));
                        }
                    });
                }
            });
            this.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
        }
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
            selected.setBackgroundColor(getResources().getColor(R.color.switcher_bg_color));
            selected.setTextColor(getResources().getColor(R.color.switcher_text_color));
            unselected.setBackgroundColor(getResources().getColor(R.color.switcher_default_bg_color));
            unselected.setTextColor(getResources().getColor(R.color.switcher_default_text_color));
        }

        public boolean isLeftSelected() {
            return A.equals(selected);
        }
    }

    private void doColorSpanForFirstString(String firstString,
                                           String lastString, TextView txtSpan) {

        String changeString = (firstString != null ? firstString : "");

        String totalString = changeString + lastString;
        Spannable spanText = new SpannableString(totalString);
        spanText.setSpan(new ForegroundColorSpan(getResources()
                .getColor(R.color.light_text)), 0, changeString.length(), 0);

        txtSpan.setText(spanText);

    }
}
