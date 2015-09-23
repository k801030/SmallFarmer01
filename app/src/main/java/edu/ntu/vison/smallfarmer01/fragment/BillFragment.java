package edu.ntu.vison.smallfarmer01.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.zip.Inflater;

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

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        BillAdapter adapter = new BillAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter.loadBillData();
        spinner.setAdapter(adapter);

        return view;
    }



    private class BillAdapter extends ArrayAdapter<String> {
        Bill[] mBills;

        public BillAdapter(Context context, int resource) {
            super(context, resource);
            mBills = new Bill[0];
        }

        private void loadBillData() {
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

    }

    private class OrderAdapter extends BaseAdapter {

        OrderItem[] mOrders;

        public OrderAdapter() {
            mOrders = new OrderItem[0];
        }

        private void loadBillData(String billId) {
            mApiService.getBillById(mUserService.getUserId(), mUserService.getAccessToken(), billId, new ApiService.GetBillByIdCallback() {
                @Override
                public void onSuccess(OrderItem[] orders) {
                    mOrders = orders;
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

            return view;
        }
    }

}
