package edu.ntu.vison.smallfarmer01.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/14.
 */
public class SignInActivity extends AppCompatActivity {
    UserService mUserService;
    Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mUserService = new UserService();

        mSignInButton = (Button) findViewById(R.id.signInButton);
        mSignInButton.setOnClickListener(new OnClickSignInListener());

    }

    public class OnClickSignInListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String email = "";
            String password = "";

            mUserService.SignIn(email, password, new UserService.UserSignInCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
