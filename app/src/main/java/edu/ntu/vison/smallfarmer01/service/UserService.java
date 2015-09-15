package edu.ntu.vison.smallfarmer01.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.xml.validation.Validator;

import edu.ntu.vison.smallfarmer01.api.ApiService;

/**
 * Created by Vison on 2015/9/14.
 */
public class UserService {
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    static final String SHARED_PREF_KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    ApiService mApiService;

    public UserService(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();

        mApiService = new ApiService(context);
    }

    private void saveLoginInfo(String accessToken){
        mEditor.putString(SHARED_PREF_KEY_ACCESS_TOKEN, accessToken);
        mEditor.commit();
    }

    public void SignIn(String email, String password, TextValidator textValidator, UserSignInCallback callback) {
        if (textValidator.checkEmail(email) && textValidator.checkPassword(password)) {
            String accessToken = "accesstoken";
            saveLoginInfo(accessToken);
            callback.onSuccess();
        } else {
            callback.onError();
        }
    }

    public void SignUp(String firstName, String lastName, String email, String password, TextValidator textValidator, UserSignUpCallback callback) {
        if (textValidator.checkEmail(email) && textValidator.checkPassword(password)) {
            callback.onSuccess();
        } else {
            callback.onError();
        }
    }

    public void LogOut(UserLogOutCallback callback) {
        mEditor.clear();
        mEditor.commit();
    }

    public boolean isLogin() {
        if (mSharedPreferences.getString(SHARED_PREF_KEY_ACCESS_TOKEN, null) == null) {
            return false;
        } else {
            // TODO: try to connect to server. if success, return true
            return true;
        }

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
