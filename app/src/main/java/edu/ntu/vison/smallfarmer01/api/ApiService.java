package edu.ntu.vison.smallfarmer01.api;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringDef;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import edu.ntu.vison.smallfarmer01.model.Bill;
import edu.ntu.vison.smallfarmer01.model.OrderItem;
import edu.ntu.vison.smallfarmer01.model.UserData;

/**
 * Created by Vison on 2015/9/11.
 */
public class ApiService {
    private static final String TAG = "ApiService";

    private static final String HOST = "https://www.smallfarmer01.com/";

    @StringDef({SIGN_IN_FIELD, GET_ORDERS_FIELD, CONFIRM_ORDER, UPDATED_REG_ID, LOG_OUT, GET_BILL_LIST, GET_BILL, FB_SIGN_IN_FIELD, GET_USER_PROFILE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UrlField{};

    private static final String SIGN_IN_FIELD = "account_api/v1/auth/signIn";
    private static final String GET_ORDERS_FIELD = "order_api/v1/index";
    private static final String CONFIRM_ORDER = "order_api/v1/confirm";
    private static final String UPDATED_REG_ID = "user_device_api/v1/update";
    private static final String LOG_OUT = "user_api/v1/logout";
    private static final String GET_BILL_LIST = "bill_api/v1/index";
    private static final String GET_BILL = "bill_api/v1/show";
    private static final String FB_SIGN_IN_FIELD = "account_api/v1/auth/facebookSignIn";
    private static final String GET_USER_PROFILE = "user_api/v1/show";

    private Context mContext;

    public ApiService(Context context) {
        mContext = context;

    }

    public void signInWithFacebookToken(String fbToken, String regId, final SignInCallback callback) {
        String url = getUrlWithField(FB_SIGN_IN_FIELD);

        final JSONObject json = new JSONObject();
        try {
            json.put("fb_token", fbToken);
            json.put("registration_id", regId);
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
                AlertDialog test = new TestErrorAlert("ApiService onError", error.toString()).create();
                test.show();
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    // unauthorized: error account or password
                    callback.onError(401);
                }
            }
        });

        queue.add(req);
    }

    public void signIn(final String email, final String password, final String regId, final SignInCallback callback){
        String url = getUrlWithField(SIGN_IN_FIELD);

        // create json post
        final JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            json.put("registration_id", regId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, json.toString());
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

    public void getUserProfile(String userId, String accessToken, final GetUserProfileCallback callback) {
        String url = getUrlWithField(GET_USER_PROFILE);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("access_token", accessToken);
            json.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                String json = response.toString();
                Type userClass = new TypeToken<UserData>(){}.getType();
                UserData user = gson.fromJson(json, userClass);
                callback.onSuccess(user);

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
            json.put("access_token", accessToken);
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



    /**
     *
     *
     * @param userId id got from app server
     * @param accessToken user token got from app server
     * @param mobileOS if it's an android device, set 0; else if it's an iPhone set 1
     * @param oldRegId if updating a registrationId, set old one; else set null
     * @param newRegId registrationId to send
     */
    public void sendRegistrationToServer(String userId, String accessToken, int mobileOS, String oldRegId, String newRegId) {
        String url = getUrlWithField(UPDATED_REG_ID);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("access_token", accessToken);
            json.put("mobile_os", mobileOS);
            if (oldRegId != newRegId) { // set when not null
                json.put("old_registration_id", oldRegId);
            }
            json.put("new_registration_id", newRegId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "successful");
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Log.d(TAG, "error");

            }
        });

        queue.add(req);

    }

    public void logOut(String userId, String accessToken, int mobileOS, String oldRegId, final LogOutCallback callback) {
        String url = getUrlWithField(LOG_OUT);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("access_token", accessToken);
            json.put("mobile_os", mobileOS);
            json.put("registration_id", oldRegId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "successful");
                Log.d(TAG, response.toString());
                callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
                Log.d(TAG, "error");
                callback.onError();
            }
        });

        queue.add(req);

    }


    public void getBillList(final String userId, final String accessToken, final GetBillListCallback callback) {
        String url = getUrlWithField(GET_BILL_LIST);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("access_token", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create request
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String json = response.getString("bills").toString();
                    Gson gson = new Gson();
                    Type typeClass = new TypeToken<Bill[]>(){}.getType();

                    Bill[] bills = gson.fromJson(json, typeClass);
                    callback.onSuccess(bills);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError();
                }

                // callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError();
            }
        });

        // make request
        queue.add(req);
    }

    public void getBillById(String userId, String accessToken, String billId, final GetBillByIdCallback callback) {
        String url = getUrlWithField(GET_BILL);
        final JSONObject json = new JSONObject();
        try {
            json.put("id", userId);
            json.put("access_token", accessToken);
            json.put("bill_id", billId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create request
        RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String json = response.getString("orders").toString();
                    Gson gson = new Gson();
                    Type typeClass = new TypeToken<OrderItem[]>(){}.getType();

                    OrderItem[] orders = gson.fromJson(json, typeClass);
                    callback.onSuccess(orders);
                } catch (JSONException e) {
                    e.printStackTrace();

                }


                // callback.onSuccess();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError();
            }
        });

        // make request
        queue.add(req);
    }

    /**
     *
     * @param field
     * @return full request url
     */
    private String getUrlWithField(@UrlField String field) {
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

    public interface GetBillListCallback {
        void onSuccess(Bill[] bills);
        void onError();
    }

    public interface GetBillByIdCallback {
        void onSuccess(OrderItem[] orders);
        void onError();
    }

    public interface LogOutCallback {
        void onSuccess();
        void onError();
    }

    public interface GetUserProfileCallback {
        void onSuccess(UserData user);
        void onError(int statusCode);
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
