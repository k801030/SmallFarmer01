package edu.ntu.vison.smallfarmer01.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

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

    public UserService(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();

        mApiService = new ApiService(context);
    }

    private void saveLoginInfo(String userId, String accessToken){
        mEditor.putString(SHARED_PREF_KEY_USER_ID, userId);
        mEditor.putString(SHARED_PREF_KEY_ACCESS_TOKEN, accessToken);
        mEditor.commit();
    }

    public void signInWithFacebook(Activity actvity, CallbackManager callbackManager, final UserSignInCallback callback) {
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
                        callback.onError();
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        LoginManager.getInstance().logInWithReadPermissions(actvity, Arrays.asList("public_profile"));

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
                    callback.onError();
                }
            });

        } else {
            callback.onError();
        }
    }

    public void signUp(String firstName, String lastName, String email, String password,
                       TextValidator textValidator, UserSignUpCallback callback) {
        if (textValidator.checkEmail(email) && textValidator.checkPassword(password)) {
            // TODO: get accessToken
            saveLoginInfo("userid","accesstoken");
            callback.onSuccess();
        } else {
            callback.onError();
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
        void onError();
    }

    public interface UserSignUpCallback {
        void onSuccess();
        void onError();
    }

    public interface UserLogOutCallback {
        void onSuccess();
        void onError();
    }
}
