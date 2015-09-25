package edu.ntu.vison.smallfarmer01.service.push_notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

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
    private static final String NOTI_TITLE = "title";
    private static final String NOTI_MESSAGE = "message";

    public MyGcmListenerService() {

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
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.com_facebook_button_icon)
                .setContentTitle(data.getString(NOTI_TITLE))
                .setContentText(data.getString(NOTI_MESSAGE))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
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
