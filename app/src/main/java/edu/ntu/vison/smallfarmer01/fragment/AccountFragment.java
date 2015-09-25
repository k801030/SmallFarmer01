package edu.ntu.vison.smallfarmer01.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.SignInActivity;
import edu.ntu.vison.smallfarmer01.activity.SignUpActivity;
import edu.ntu.vison.smallfarmer01.service.push_notification.NotificationCountBadge;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/16.
 */
public class AccountFragment extends Fragment {
    UserService mUserService;


    public AccountFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // getActivity() can return null if it is called before onAttach of the respective fragment.
        mUserService = new UserService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button logOutButton = (Button) view.findViewById(R.id.log_out_button);
        logOutButton.setOnClickListener(new OnClickLogOutListener());
        return view;
    }


    private void goToSignUpPage() {
        Intent intent = new Intent(this.getActivity(), SignUpActivity.class);
        startActivity(intent);
        this.getActivity().finish();
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
