package edu.ntu.vison.smallfarmer01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.service.TextValidator;
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

        mUserService = new UserService(this);

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickSignInListener());

    }


    private void goToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /* Listener */
    public class OnClickSignInListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String email = "";
            String password = "";

            mUserService.SignIn(email, password, new TextValidator(),new UserService.UserSignInCallback() {
                @Override
                public void onSuccess() {
                    goToMainPage();
                }

                @Override
                public void onError() {

                }
            });
        }
    }
}
