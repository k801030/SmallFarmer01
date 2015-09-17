package edu.ntu.vison.smallfarmer01.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import edu.ntu.vison.smallfarmer01.model.OrderItem;

/**
 * Created by Vison on 2015/9/11.
 */
public class ApiService {
    private Context mContext;
    public static String HOST = "http://register.ac-experts.com.tw/";
    public static String mField = "";

    public static String SIGN_IN_FIELD = "account_api/v1/auth/signIn";
    public static String GET_ORDERS_FIELD = "order_api/v1/index";
    public static String CONFIRM_ORDER = "order_api/v1/confirm";

    public ApiService(Context context) {
        mContext = context;

    }

    public void signIn(final String email, final String password, final SignInCallback callback){
        String url = getUrlWithField(SIGN_IN_FIELD);

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

    public void getOrders(String userId, String accessToken, String called, final GetOrdersCallback callback) {
        String url = getUrlWithField(GET_ORDERS_FIELD);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("access_token", accessToken);
            json.put("called_smallfarmer_c", called);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    String json = response.getJSONArray("orders").toString();

                    Type orderClass = new TypeToken<ArrayList<OrderItem>>(){}.getType();
                    ArrayList<OrderItem> orderItems = gson.fromJson(json, orderClass);
                    callback.onSuccess(orderItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    // unauthorized: error account or password
                    callback.onError(401);
                }

            }
        });

        queue.add(req);
    }


    public void confirmOrder(String userId, String accessToken, String orderId, final ConfirmOrderCallback callback) {
        String url = getUrlWithField(CONFIRM_ORDER);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("token", accessToken);
            json.put("order_id", orderId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    // unauthorized: error account or password
                    callback.onError(401);
                }
            }
        });

        queue.add(req);
    }

    public void getBills() {

    }

    /**
     *
     * @param field
     * @return full request url
     */
    private String getUrlWithField(String field) {
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
        void onSuccess(ArrayList<OrderItem> orderItems);
        void onError(int statusCode);
    }

    public interface ConfirmOrderCallback {
        void onSuccess();
        void onError(int statusCode);
    }

    public interface GetBillsCallback {
        void onSuccess();
        void onError();
    }
}
