package edu.ntu.vison.smallfarmer01.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Vison on 2015/9/11.
 */
public class ApiService {
    private Context mContext;
    public static String HOST = "http://register.ac-experts.com.tw/";
    public static String mField = "";

    public ApiService(Context context) {
        mContext = context;

    }

    public void signIn(final String email, final String password, final SignInCallback callback) throws JSONException {
        Log.d("TEST" , "LOG");
        String url = getUrlwithField("users/sign_in");

        // create json post
        final JSONObject jsonBody = new JSONObject();
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("email", email);
        jsonUser.put("password", password);
        jsonBody.put("user", jsonUser);


        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.onError();
            }
        }) {
            @Override
            public String getBodyContentType() {
                String contentType = "application/json";
                return contentType;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.toString().getBytes();
            }
        };
        // req.setShouldCache(false);
        queue.add(req);
    }

    public void logIn() {

    }

    public void getOrders() {

    }

    public void getBills() {

    }

    /**
     *
     * @param field
     * @return full request url
     */
    public String getUrlwithField(String field) {
        return HOST + field;
    }


    /* Callback */

    interface SignInCallback {
        void onSuccess();
        void onError();
    }
    interface LogInCallback {
        void onSuccess();
        void onError();
    }

    interface GetOrdersCallback {
        void onSuccess();
        void onError();
    }

    interface GetBillsCallback {
        void onSuccess();
        void onError();
    }
}
