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

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.Bill;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/17.
 */
public class BillFragment extends Fragment {
    static UserService mUserService;
    static ApiService mApiService;
    static Context mContext;

    public static BillFragment newInstance(Context context) {
        mUserService = new UserService(context);
        mApiService = new ApiService(context);
        mContext = context;
        Bundle args = new Bundle();

        BillFragment fragment = new BillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        BillAdapter adapter = new BillAdapter();
        adapter.loadBillData();

        return view;
    }

    private class BillAdapter extends BaseAdapter {

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
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
