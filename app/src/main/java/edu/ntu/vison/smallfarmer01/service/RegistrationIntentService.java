package edu.ntu.vison.smallfarmer01.service;

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

/**
 * Created by Vison on 2015/9/19.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: implement sendRegistration api
            new ApiService(this).sendRegistrationToServer();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
