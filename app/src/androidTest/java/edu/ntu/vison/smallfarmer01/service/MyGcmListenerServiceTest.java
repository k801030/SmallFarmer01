package edu.ntu.vison.smallfarmer01.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.test.ActivityInstrumentationTestCase2;

import edu.ntu.vison.smallfarmer01.activity.MainActivity;
import edu.ntu.vison.smallfarmer01.service.push_notification.MyGcmListenerService;

/**
 * Created by Vison on 2015/9/19.
 */
public class MyGcmListenerServiceTest extends ActivityInstrumentationTestCase2 {
    Context mContext;
    boolean mBound;
    MyGcmListenerService mGcmService;

    public MyGcmListenerServiceTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testOnMessageReceived() throws Exception {
        Intent intent = new Intent(getActivity(), MyGcmListenerService.class);
        getActivity().startService(intent);


        // Bundle data = new Bundle();
        // data.putString(MyGcmListenerService.NOTI_TITLE, "標題");
        // data.putString(MyGcmListenerService.NOTI_MESSAGE, "今天天氣真好");
        // gcm.onMessageReceived("from", data);
    }

}