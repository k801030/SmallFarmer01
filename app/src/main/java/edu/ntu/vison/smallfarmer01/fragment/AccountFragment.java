package edu.ntu.vison.smallfarmer01.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.SignInActivity;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.model.UserData;
import edu.ntu.vison.smallfarmer01.service.push_notification.NotificationCountBadge;
import edu.ntu.vison.smallfarmer01.service.UserService;
import edu.ntu.vison.smallfarmer01.view.MyRoundedImageView;

/**
 * Created by Vison on 2015/9/16.
 */
public class AccountFragment extends Fragment {
    static final String TAG = "AccountFragment";

    UserService mUserService;
    ApiService mApiService;

    ImageView mUserImg;
    TextView mUserName;

    public AccountFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // getActivity() can return null if it is called before onAttach of the respective fragment.
        mUserService = new UserService(getActivity());
        mApiService = new ApiService(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button logOutButton = (Button) view.findViewById(R.id.log_out_button);

        mUserImg = (ImageView) view.findViewById(R.id.user_profile_img);
        mUserName = (TextView) view.findViewById(R.id.user_name);

        logOutButton.setOnClickListener(new OnClickLogOutListener());

        // make request
        mApiService.getUserProfile(mUserService.getUserId(), mUserService.getAccessToken(), new ApiService.GetUserProfileCallback() {
            @Override
            public void onSuccess(UserData user) {
                Log.d(TAG, user.getUserName());
                Log.d(TAG, user.getUserImgUrl());
                if (user.getUserImgUrl() != "") {
                    UrlImageViewHelper.setUrlDrawable(mUserImg, user.getUserImgUrl());
                }
                mUserName.setText(user.getUserName());
            }

            @Override
            public void onError(int statusCode) {

            }
        });

        return view;
    }


    private void goToSignInPage() {
        Intent intent = new Intent(this.getActivity(), SignInActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }


    /* Listener */

    public class OnClickLogOutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mUserService.logOut(new UserService.UserLogOutCallback() {
                @Override
                public void onSuccess() {
                    NotificationCountBadge.with(getActivity()).reset();
                    goToSignInPage();
                }

                @Override
                public void onError() {

                }
            });
        }
    }

}
