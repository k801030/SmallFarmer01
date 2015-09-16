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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.HttpStatus;
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

    public static String SIGN_IN_FIELD = "account_api/v1/auth/signIn";
    public static String GET_ORDERS_FIELD = "order_api/v1/index";
    public static String CONFIRM_ORDER = "order/api_v1/confirm";

    public ApiService(Context context) {
        mContext = context;

    }

    public void signIn(final String email, final String password, final SignInCallback callback){
        Log.d("TEST" , "LOG");
        String url = getUrlwithField(SIGN_IN_FIELD);

        // create json post
        final JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String id = response.getString("id");
                    String token = response.getString("token");
                    callback.onSuccess(id, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    // unauthorized: error account or password
                    callback.onError(401);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        // req.setShouldCache(false);
        queue.add(req);
    }

    public void SignUp() {

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

    public interface SignInCallback {
        void onSuccess(String userId, String accessToken);
        void onError(int statusCode);
    }
    public interface SignUpCallback {
        void onSuccess();
        void onError();
    }

    public interface GetOrdersCallback {
        void onSuccess();
        void onError();
    }

    public interface GetBillsCallback {
        void onSuccess();
        void onError();
    }
}
