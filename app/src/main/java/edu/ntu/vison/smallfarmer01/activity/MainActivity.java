package edu.ntu.vison.smallfarmer01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.fragment.AccountFragment;
import edu.ntu.vison.smallfarmer01.fragment.BillFragment;
import edu.ntu.vison.smallfarmer01.fragment.OrdersFragment;
import edu.ntu.vison.smallfarmer01.lib.MyPagerSlidingTabStrip;
import edu.ntu.vison.smallfarmer01.service.push_notification.RegistrationIntentService;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/12.
 */
public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 900;
    private static final String TAG = "MainActivity";

    UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserService = new UserService(this);

        // TODO: check login status
        if (!mUserService.isLogin()) {
            goToSignInPage();
        }

        // init toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // init viewPager
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        // init tabs
        MyPagerSlidingTabStrip tabs = (MyPagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        registerGCM();
    }

    private void registerGCM() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    private void goToSignUpPage() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignInPage() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /* Adapter */

    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"管理出貨", "管理帳單", "帳號設定"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new OrdersFragment();
                case 1:
                    return new BillFragment();
                case 2:
                    return new AccountFragment();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return TITLES[i];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
