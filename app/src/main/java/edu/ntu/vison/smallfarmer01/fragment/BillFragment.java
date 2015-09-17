package edu.ntu.vison.smallfarmer01.fragment;

import android.content.Context;
import android.os.Bundle;

import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/17.
 */
public class BillFragment {
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
}
