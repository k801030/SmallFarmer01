package edu.ntu.vison.smallfarmer01.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.Bill;
import edu.ntu.vison.smallfarmer01.model.OrderItem;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/17.
 */
public class BillFragment extends Fragment {
    UserService mUserService;
    ApiService mApiService;
    Spinner mSpinner;
    ListView mOrderList;

    public BillFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // getActivity() can return null if it is called before onAttach of the respective fragment.
        mUserService = new UserService(getActivity());
        mApiService = new ApiService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mOrderList = (ListView) view.findViewById(R.id.order_list);

        // set bill spinner list
        BillAdapter billAdapter = new BillAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        billAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        billAdapter.loadBillData();
        mSpinner.setAdapter(billAdapter);
        mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        // set orders showed
        OrderAdapter orderAdapter = new OrderAdapter();
        mOrderList.setAdapter(orderAdapter);


        return view;
    }


    /**
     * for Spinner
     */
    private class BillAdapter extends ArrayAdapter<String> {
        Bill[] mBills;

        public BillAdapter(Context context, int resource) {
            super(context, resource);
            mBills = new Bill[0];
        }

        public void loadBillData() {
            mApiService.getBillList(mUserService.getUserId(), mUserService.getAccessToken(), new ApiService.GetBillListCallback() {
                @Override
                public void onSuccess(Bill[] bills) {
                    mBills = bills;
                    notifyDataSetChanged();
                }

                @Override
                public void onError() {

                }
            });
        }

        @Override
        public int getCount() {
            return mBills.length;
        }

        @Override
        public String getItem(int i) {
            return mBills[i].getBeginAt();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public String getBillId(int i) {
            return mBills[i].getId();
        }

    }

    /**
     * For ListView
     */
    private class OrderAdapter extends BaseAdapter {

        OrderItem[] mOrders;

        public OrderAdapter() {
            mOrders = new OrderItem[0];
        }

        public void loadBillData(String billId) {
            mApiService.getBillById(mUserService.getUserId(), mUserService.getAccessToken(), billId, new ApiService.GetBillByIdCallback() {
                @Override
                public void onSuccess(OrderItem[] orders) {
                    mOrders = orders;
                    notifyDataSetChanged();
                }

                @Override
                public void onError() {

                }
            });
        }

        @Override
        public int getCount() {
            return mOrders.length;
        }

        @Override
        public Object getItem(int i) {
            return mOrders[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(BillFragment.this.getActivity()).inflate(R.layout.fragment_bills_item, viewGroup, false);
            }

            TextView orderIdText = (TextView) view.findViewById(R.id.order_id);
            TextView productNameText = (TextView) view.findViewById(R.id.product_name);
            TextView quantityText = (TextView) view.findViewById(R.id.quantity);
            TextView receiverNameText = (TextView) view.findViewById(R.id.receiver_name);
            RoundedImageView productImage = (RoundedImageView) view.findViewById(R.id.product_image);
            TextView orderPriceText = (TextView) view.findViewById(R.id.order_price);
            TextView shipmentPriceText = (TextView) view.findViewById(R.id.shipment_price);

            final OrderItem item = mOrders[i];
            orderIdText.setText(item.getId().toString());
            productNameText.setText(item.getProductName());
            quantityText.setText(item.getQuantity().toString());
            receiverNameText.setText(item.getReceiverName());
            UrlImageViewHelper.setUrlDrawable(productImage, item.getProductUrl());
            productImage.setCornerRadius(productImage.getWidth() / 2);
            orderPriceText.setText(Integer.toString(item.getOrderPrice()));
            shipmentPriceText.setText(Integer.toString(item.getShipmentPrice()));

            return view;
        }
    }


    /* Listener */
    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String billId = ((BillAdapter)adapterView.getAdapter()).getBillId(i);
            ((OrderAdapter)mOrderList.getAdapter()).loadBillData(billId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
