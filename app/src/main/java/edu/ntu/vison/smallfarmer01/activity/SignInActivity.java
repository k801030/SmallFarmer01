package edu.ntu.vison.smallfarmer01.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.service.TextValidator;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/14.
 */
public class SignInActivity extends AppCompatActivity {
    final String REGISTER_PAGE = "https://www.smallfarmer01.com/users/sign_up";

    UserService mUserService;
    EditText mEmailText;
    EditText mPasswordText;
    Button mSignInButton;
    Button mFBLoginButton;
    TextView mLink;
    CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mUserService = new UserService(this);

        // init toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

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

        mLink = (TextView) findViewById(R.id.link);
        mLink.setClickable(true);
        mLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "第一次使用，請先至<a href='" + REGISTER_PAGE + "'>網頁版</a>註冊。";
        mLink.setText(Html.fromHtml(text));

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
                    AlertDialog test = new TestErrorAlert("userService onSuccess", "").create();
                    test.show();
                    goToMainPage();
                }

                @Override
                public void onError(String error) {

                    AlertDialog test = new TestErrorAlert("userService onError", error).create();
                    test.show();

                    if (error == UserService.NOT_YET_REGISTER) {
                        AlertDialog alert = new RegisterErrorAlert().create();
                        alert.show();
                    }
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
                public void onError(String error) {
                    Toast.makeText(SignInActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private class RegisterErrorAlert extends AlertDialog.Builder {
        public RegisterErrorAlert() {
            super(SignInActivity.this);
            this.setMessage("您好，手機app目前不提供註冊功能。第一次使用，請至網頁版進行註冊，謝謝！");
            this.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            this.setNegativeButton("立即前往",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            openRegisterWeb();
                        }
                    });
        }


    }

    private void openRegisterWeb() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(REGISTER_PAGE));
        startActivity(browserIntent);
    }

    private class TestErrorAlert extends AlertDialog.Builder {
        public TestErrorAlert(String tag, String errorMessage) {
            super(SignInActivity.this);
            this.setTitle("測試回報: " + tag);
            this.setMessage(errorMessage);
            this.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }


    }
}
