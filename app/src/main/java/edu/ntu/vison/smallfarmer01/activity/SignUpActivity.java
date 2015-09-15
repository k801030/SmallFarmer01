package edu.ntu.vison.smallfarmer01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.service.TextValidator;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/15.
 */
public class SignUpActivity extends AppCompatActivity {
    UserService mUserService;
    EditText mLastName;
    EditText mFirstName;
    EditText mEmailText;
    EditText mPasswordText;
    Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUserService = new UserService(this);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnSignUpClickListener());
    }

    private void goToMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /* Listener */

    public class OnSignUpClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String lastName = mLastName.getText().toString();
            String firstName = mFirstName.getText().toString();
            String email = mEmailText.getText().toString();
            String password = mPasswordText.getText().toString();

            mUserService.SignUp(lastName, firstName, email, password, new TextValidator(), new UserService.UserSignUpCallback() {
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
