package edu.ntu.vison.smallfarmer01.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.BillDetailActivity;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.Bill;
import edu.ntu.vison.smallfarmer01.model.OrderItem;
import edu.ntu.vison.smallfarmer01.model.TotalBillCalculator;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/17.
 */
public class BillFragment extends Fragment {
    UserService mUserService;
    ApiService mApiService;
    Spinner mSpinner;
    ListView mOrderList;
    OrderItem[] mOrders;
    Button mShowDetailButton;

    private TextView mTotalSales;
    private TextView mCashFlow;
    private TextView mCouponFee;
    private TextView mAdminFee;
    private TextView mSalesTax;
    private TextView mTranslateFee;
    private TextView mReceivedCash;

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
        View emptyView = view.findViewById(R.id.bill_empty);
        mSpinner.setEmptyView(emptyView);


        // set bill spinner list
        BillAdapter billAdapter = new BillAdapter(getActivity(), R.layout.fragment_bills_spinner_item_with_arrow, R.id.spinner_text);
        billAdapter.setDropDownViewResource(R.layout.fragment_bills_spinner_item);
        billAdapter.loadBillData();
        mSpinner.setAdapter(billAdapter);
        mSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());


        // set total bill
        mTotalSales = (TextView) view.findViewById(R.id.total_sales);
        mCashFlow = (TextView) view.findViewById(R.id.cash_flow);
        mCouponFee = (TextView) view.findViewById(R.id.coupon_fee);
        mAdminFee = (TextView) view.findViewById(R.id.admin_fee);
        mSalesTax = (TextView) view.findViewById(R.id.sales_tax);
        mTranslateFee = (TextView) view.findViewById(R.id.translate_fee);
        mReceivedCash = (TextView) view.findViewById(R.id.received_cash);


        // set show detail
        mShowDetailButton = (Button) view.findViewById(R.id.show_detail_button);
        mShowDetailButton.setOnClickListener(new OnClickShowDetailListener());

        return view;
    }

    private void setTotalBill() {
        TotalBillCalculator billCal = new TotalBillCalculator(mOrders);
        mTotalSales.setText(billCal.getTotalSales().toString());
        mCashFlow.setText(billCal.getCashFlowFee().toString());
        mCouponFee.setText(billCal.getCouponFee().toString());
        mAdminFee.setText(billCal.getAdminFee().toString());
        mSalesTax.setText(billCal.getSalesTax().toString());
        mTranslateFee.setText(billCal.getTranslateFee().toString());
        mReceivedCash.setText(billCal.getReceivedCash().toString());
    }

    /**
     * for Spinner
     */
    private class BillAdapter extends ArrayAdapter<String> {
        Bill[] mBills;

        public BillAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
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

            String beginDate = mBills[i].getBeginAt();
            String endDate = mBills[i].getEndAt();
            return beginDate + " 至 " +endDate;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public String getBillId(int i) {
            return mBills[i].getId();
        }

    }






    /* Listener */

    public class OnClickShowDetailListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(BillFragment.this.getActivity(), BillDetailActivity.class);
            Gson gson = new Gson();
            intent.putExtra("orders", gson.toJson(mOrders));
            startActivity(intent);
        }
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String billId = ((BillAdapter)adapterView.getAdapter()).getBillId(i);
            mApiService.getBillById(mUserService.getUserId(), mUserService.getAccessToken(), billId, new ApiService.GetBillByIdCallback() {
                @Override
                public void onSuccess(OrderItem[] orders) {
                    mOrders = orders;
                    setTotalBill();
                }

                @Override
                public void onError() {

                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
