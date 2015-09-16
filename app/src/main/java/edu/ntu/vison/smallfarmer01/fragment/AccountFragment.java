package edu.ntu.vison.smallfarmer01.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.MainActivity;
import edu.ntu.vison.smallfarmer01.activity.SignUpActivity;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/16.
 */
public class AccountFragment extends Fragment {
    static UserService sUserService;



    public static AccountFragment newInstance(Context context) {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);

        sUserService = new UserService(context);
        return fragment;
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

    /* Listener */

    public class OnClickLogOutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            sUserService.logOut(new UserService.UserLogOutCallback() {
                @Override
                public void onSuccess() {
                    goToSignUpPage();
                }

                @Override
                public void onError() {

                }
            });
        }
    }

}
