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

    ApiService mApiService;

    public ApiServiceTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mApiService = new ApiService(getActivity());
    }


    public void testSignInSuccess() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        mApiService.signIn("r03725043@ntu.edu.tw", "000000000", new ApiService.SignInCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                Assert.assertNotNull(userId);
                Assert.assertNotNull(accessToken);
                signal.countDown();
            }

            @Override
            public void onError(int statusCode) {
                Assert.assertNull(statusCode);
                signal.countDown();
            }
        });

        signal.await();

    }

    public void testSignInFailure() throws Exception {
        final CountDownLatch signal = new CountDownLatch(1);

        mApiService.signIn("r03725043@ntu.edu.tw", "123", new ApiService.SignInCallback() {
            @Override
            public void onSuccess(String userId, String accessToken) {
                Assert.assertNull(userId);
                Assert.assertNull(accessToken);
                signal.countDown();
            }

            @Override
            public void onError(int statusCode) {
                Assert.assertEquals(401, statusCode);
                signal.countDown();
            }
        });

        signal.await();

    }
}