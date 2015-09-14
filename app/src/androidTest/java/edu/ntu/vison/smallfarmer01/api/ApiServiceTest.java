package edu.ntu.vison.smallfarmer01.api;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.android.volley.Response;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

import edu.ntu.vison.smallfarmer01.activity.MainActivity;

/**
 * Created by Vison on 2015/9/14.
 */
public class ApiServiceTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public ApiServiceTest() {
        super(MainActivity.class);
    }

    public void testSignIn() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);
        ApiService apiService = new ApiService(getActivity());
        apiService.signIn("email", "password", new ApiService.SignInCallback() {
            @Override
            public void onSuccess() {
                Assert.assertTrue(true);
                signal.countDown();
            }

            @Override
            public void onError() {
                Assert.assertTrue(false);
                signal.countDown();
            }
        });

        signal.await();

    }
}