package edu.ntu.vison.smallfarmer01.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Vison on 2015/9/20.
 */
public class NotificationCountBadge {

    private static final String SHARED_PREF_NOTI_COUNT = "notificationCount";

    static SharedPreferences mSharedPreferences;
    static SharedPreferences.Editor mEditor;
    static ShortcutBadger mBadger;

    public static NotificationCountBadge with(Context context) {
        mBadger = ShortcutBadger.with(context);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
        return new NotificationCountBadge();
    }

    /**
     * set basic setCount from start
     */
    public void initCount() {
        int before = getCountFromPref();
        mBadger.count(before);
    }

    public void setCount(int count) {
        setCountToPref(count);
        mBadger.count(count);
    }

    public void addOne() {
        int count = getCountFromPref()+1;
        mEditor.putInt(SHARED_PREF_NOTI_COUNT, count);
        mBadger.count(count);
    }

    public void reset() {
        mEditor.putInt(SHARED_PREF_NOTI_COUNT, 0);
        mBadger.remove();
    }

    /**
     * get setCount from sharedPreferences
     * @return setCount
     */
    private int getCountFromPref() {
        return mSharedPreferences.getInt(SHARED_PREF_NOTI_COUNT, 0);
    }

    /**
     * set setCount to sharedPreferences
     * @return null
     */
    private void setCountToPref(int count) {
        mEditor.putInt(SHARED_PREF_NOTI_COUNT, count);
        mEditor.commit();
    }

}
