package edu.ntu.vison.smallfarmer01.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import edu.ntu.vison.smallfarmer01.api.ApiService;

/**
 * Created by Vison on 2015/9/14.
 */
public class UserService {
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    static final String SHARED_PREF_KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    static final String SHARED_PREF_KEY_USER_ID = "USER_ID";
    static final String SHARED_PREF_KEY_REG_ID = "REG_ID";
    ApiService mApiService;
    Context mContext;

    static final String UNKNOWN = "未知的錯誤";
    static final String BLANK_PASSWORD = "尚未輸入密碼";
    static final String BLANK_EMAIL = "尚未輸入電子郵件";
    static final String EMAIL_FORMAT_ERROR = "信箱格式錯誤";
    static final String PASSWORD_AT_LEAST_8 = "密碼請輸入至少8字元";
    public static final String NOT_YET_REGISTER = "尚未註冊";
    public static final String WRONG = "帳號密碼錯誤";
    @StringDef({UNKNOWN, BLANK_EMAIL, BLANK_PASSWORD, EMAIL_FORMAT_ERROR, PASSWORD_AT_LEAST_8, NOT_YET_REGISTER, WRONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AccountError{};

    public UserService(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();

        mApiService = new ApiService(context);
        mContext = context;
    }

    private void saveLoginInfo(String userId, String accessToken){
        mEditor.putString(SHARED_PREF_KEY_USER_ID, userId);
        mEditor.putString(SHARED_PREF_KEY_ACCESS_TOKEN, accessToken);
        mEditor.commit();
    }

    public void signInWithFacebook(Activity activity, CallbackManager callbackManager, final UserSignInCallback callback) {
        // get facebook token
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // send user identity data to server
                String fbToken = loginResult.getAccessToken().getToken();
                // do not need facebook token anymore
                LoginManager.getInstance().logOut();

                String regId = mSharedPreferences.getString(SHARED_PREF_KEY_REG_ID, null);
                mApiService.signInWithFacebookToken(fbToken, regId, new ApiService.SignInCallback() {
                    @Override
                    public void onSuccess(String userId, String accessToken) {
                        saveLoginInfo(userId, accessToken);
                        callback.onSuccess();

                    }

                    @Override
                    public void onError(int statusCode) {
                        // TODO: need to sign up on website
                        callback.onError(NOT_YET_REGISTER);
                    }
                });
            }

            @Override
            public void onCancel() {
                AlertDialog test = new TestErrorAlert("FbLogin onCancel", "").create();
                test.show();
            }

            @Override
            public void onError(FacebookException e) {
                AlertDialog test = new TestErrorAlert("FbLogin onError", e.toString()).create();
                test.show();
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));

    }

    public void signIn(String email, String password, TextValidator textValidator, final UserSignInCallback callback) {
        if (textValidator.checkEmail(email) && textValidator.checkPassword(password)) {

            String regId = mSharedPreferences.getString(SHARED_PREF_KEY_REG_ID, null);

            mApiService.signIn(email, password, regId, new ApiService.SignInCallback() {
                @Override
                public void onSuccess(String userId, String accessToken) {
                    saveLoginInfo(userId, accessToken);
                    callback.onSuccess();
                }

                @Override
                public void onError(int statusCode) {
                    callback.onError(WRONG);
                }
            });

        } else {
            // do not pass the validator
            String error = UNKNOWN;
            if (!textValidator.checkNotBlank(email)) {
                error = BLANK_EMAIL;
            } else if (!textValidator.checkNotBlank(password)) {
                error = BLANK_PASSWORD;
            } else if (!textValidator.checkEmail(email)) {
                error = EMAIL_FORMAT_ERROR;
            } else if (!textValidator.checkPassword(password)) {
                error = PASSWORD_AT_LEAST_8;
            }

            callback.onError(error);
        }
    }


    public void logOut(final UserLogOutCallback callback) {
        int mobileOS = 0;
        mApiService.logOut(getUserId(), getAccessToken(), mobileOS,
                mSharedPreferences.getString(SHARED_PREF_KEY_REG_ID, null), new ApiService.LogOutCallback() {
            @Override
            public void onSuccess() {
                mEditor.remove(SHARED_PREF_KEY_USER_ID);
                mEditor.remove(SHARED_PREF_KEY_ACCESS_TOKEN);
                mEditor.commit();
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });

    }

    public boolean isLogin() {
        if (mSharedPreferences.getString(SHARED_PREF_KEY_ACCESS_TOKEN, null) == null) {
            return false;
        } else {
            // TODO: try to connect to server. if success, return true
            return true;
        }

    }

    public String getUserId() {
        return mSharedPreferences.getString(SHARED_PREF_KEY_USER_ID, null);
    }

    public String getAccessToken() {
        return mSharedPreferences.getString(SHARED_PREF_KEY_ACCESS_TOKEN, null);
    }

    public interface UserSignInCallback {
        void onSuccess();
        void onError(@AccountError String error);
    }

    public interface UserLogOutCallback {
        void onSuccess();
        void onError();
    }

    private class TestErrorAlert extends AlertDialog.Builder {
        public TestErrorAlert(String tag, String errorMessage) {
            super(mContext);
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
