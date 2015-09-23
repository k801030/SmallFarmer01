package edu.ntu.vison.smallfarmer01.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.zip.Inflater;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.Bill;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/17.
 */
public class BillFragment extends Fragment {
    UserService mUserService;
    ApiService mApiService;

    public BillFragment() {
        mUserService = new UserService(getActivity());
        mApiService = new ApiService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        ListView bills = (ListView) view.findViewById(R.id.bill_list);
        BillAdapter adapter = new BillAdapter();
        adapter.loadBillData();
        bills.setAdapter(adapter);

        return view;
    }

    private class BillAdapter extends BaseAdapter {
        Bill[] bills;

        public BillAdapter() {
            bills = new Bill[2];
            bills[0] = new Bill();
            bills[1] = new Bill();
        }

        private void loadBillData() {
            mApiService.getBillList(mUserService.getUserId(), mUserService.getAccessToken(), new ApiService.GetBillListCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }

        @Override
        public int getCount() {
            return bills.length;
        }

        @Override
        public Object getItem(int i) {
            return bills[i];
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
