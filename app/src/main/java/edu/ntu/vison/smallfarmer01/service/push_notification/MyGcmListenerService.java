package edu.ntu.vison.smallfarmer01.service.push_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Timer;
import java.util.TimerTask;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.activity.MainActivity;

/**
 * Created by Vison on 2015/9/19.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    public static final String NOTI_TITLE = "title";
    public static final String NOTI_MESSAGE = "message";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String PREF_NOTIFICATION_ID = "notification_id";

    public MyGcmListenerService() {
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "oncreate");
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * Called when message is received
     *
     * @param from senderID of sender
     * @param data data bundle containing message data as key/value pairs
     *             For set of keys use data.keySet()
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        sendNotification(data);
        NotificationCountBadge.with(this).addOne();

        // wake lock
        WakeLocker wakeLocker = new WakeLocker();
        wakeLocker.acquire(this);
        wakeLocker.releaseDelayed(WakeLocker.WAKE_TIME);

    }

    private void sendNotification(Bundle data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int color = getResources().getColor(R.color.color_primary);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setColor(color)
                .setSmallIcon(R.mipmap.noti_icon)
                .setContentTitle(data.getString(NOTI_TITLE))
                .setContentText(data.getString(NOTI_MESSAGE))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(getNotificationId() /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationId() {
        int notificationId = mSharedPreferences.getInt(PREF_NOTIFICATION_ID, 0);
        notificationId++; // sequence
        mEditor.putInt(PREF_NOTIFICATION_ID, notificationId);
        mEditor.commit();

        Log.d(TAG, Integer.toString(notificationId));
        return notificationId;
    }

    public class WakeLocker {
        static final int WAKE_TIME = 2000;
        private PowerManager.WakeLock wakeLock;

        public void acquire(Context context) {
            if (wakeLock != null) wakeLock.release();

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE, "WakeLock");
            wakeLock.acquire();
        }


        public void release() {
            if (wakeLock != null) wakeLock.release(); wakeLock = null;
        }

        public void releaseDelayed(int milliSec) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    release();
                }
            }, milliSec);

        }
    }
}
