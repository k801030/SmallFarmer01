package edu.ntu.vison.smallfarmer01.service;

import android.content.Context;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

import junit.framework.TestCase;

import edu.ntu.vison.smallfarmer01.activity.MainActivity;

/**
 * Created by Vison on 2015/9/19.
 */
public class MyGcmListenerServiceTest extends ActivityInstrumentationTestCase2 {
    Context mContext;

    public MyGcmListenerServiceTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = getActivity();
    }

    public void testOnMessageReceived() throws Exception {
        MyGcmListenerService gcm = new MyGcmListenerService(mContext);

        Bundle data = new Bundle();
        data.putString("title", "標題");
        data.putString("message", "今天天氣真好");
        gcm.onMessageReceived("from", data);
    }
}