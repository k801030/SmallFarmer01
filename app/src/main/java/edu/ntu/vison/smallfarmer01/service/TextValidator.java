package edu.ntu.vison.smallfarmer01.service;

import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vison on 2015/9/15.
 */
public class TextValidator {

    private Context mContext;


    public boolean checkEmail(String email) {
        Pattern p = Pattern.compile("(^[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})$");
        Matcher m = p.matcher(email);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPassword(String password) {
        Pattern p = Pattern.compile("^(.){8,}$");
        Matcher m = p.matcher(password);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkNotBlank(String string) {
        if (string != "") {
            return true;
        } else {
            return false;
        }
    }


}
