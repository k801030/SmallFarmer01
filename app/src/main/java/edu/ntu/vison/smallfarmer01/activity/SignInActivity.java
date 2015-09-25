package edu.ntu.vison.smallfarmer01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.service.TextValidator;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/14.
 */
public class SignInActivity extends AppCompatActivity {
    UserService mUserService;
    EditText mEmailText;
    EditText mPasswordText;
    Button mSignInButton;
    Button mFBLoginButton;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mUserService = new UserService(this);


        mEmailText = (EditText) findViewById(R.id.email_text);
        mPasswordText = (EditText) findViewById(R.id.password_text);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickSignInListener());

        mUserService = new UserService(this);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mPasswordText = (EditText) findViewById(R.id.password_text);

        // fb button
        mFBLoginButton = (Button) findViewById(R.id.fb_login_button);
        mFBLoginButton.setOnClickListener(new OnClickFbSignInListener());
        mCallbackManager = CallbackManager.Factory.create();

        // TODO: remove below after finishing test.
        tempFillUpAccount();
    }

    private void tempFillUpAccount() {
        mEmailText.setText("sueming0908@gmail.com");
        mPasswordText.setText("suesue0908");
    }


    private void goToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /* Listener */
    public class OnClickFbSignInListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            mUserService.signInWithFacebook(SignInActivity.this, mCallbackManager, new UserService.UserSignInCallback() {
                @Override
                public void onSuccess() {
                    goToMainPage();
                }

                @Override
                public void onError() {
                    // TODO: need to sign up on website

                }
            });
         }
    }

    public class OnClickSignInListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String email = mEmailText.getText().toString();
            String password = mPasswordText.getText().toString();

            mUserService.signIn(email, password, new TextValidator(), new UserService.UserSignInCallback() {
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
