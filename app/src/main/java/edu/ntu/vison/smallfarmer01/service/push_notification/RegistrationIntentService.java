package edu.ntu.vison.smallfarmer01.service.push_notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.api.ApiService;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/19.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    private static final int ANDROID_DEVICE = 0;
    static final String SHARED_PREF_KEY_REG_ID = "REG_ID";

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    public RegistrationIntentService() {
        super(TAG);
        // the base Context in the ContextWrapper has not been set yet
        // DO NOT call context here
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String regToken = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + regToken);

            // TODO: implement sendRegistration api

            UserService userService = new UserService(this);

            new ApiService(this).sendRegistrationToServer(userService.getUserId(), userService.getAccessToken(), ANDROID_DEVICE, getOldRegToken(), regToken);
            updateRegToken(regToken);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    /**
     * handle regToken
     * when regToken is updated, remove it from app server
     */
    public String getOldRegToken() {
        return mSharedPreferences.getString(SHARED_PREF_KEY_REG_ID, null);
    }

    private void updateRegToken(String regToken) {
        mEditor.putString(SHARED_PREF_KEY_REG_ID, regToken);
        mEditor.commit();
    }
}
