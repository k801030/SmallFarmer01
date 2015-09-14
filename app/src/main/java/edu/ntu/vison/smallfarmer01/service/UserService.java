package edu.ntu.vison.smallfarmer01.service;

/**
 * Created by Vison on 2015/9/14.
 */
public class UserService {

    public void SignIn(String email, String password, UserSignInCallback callback) {

    }

    public void SignUp(String email, String password, UserSignUpCallback callback) {

    }

    public void LogOut(UserLogOutCallback callback) {

    }

    public boolean isLogin() {
        return true;
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
