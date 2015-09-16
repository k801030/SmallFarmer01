package edu.ntu.vison.smallfarmer01.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import edu.ntu.vison.smallfarmer01.R;
import edu.ntu.vison.smallfarmer01.fragment.AccountFragment;
import edu.ntu.vison.smallfarmer01.fragment.OrdersFragment;
import edu.ntu.vison.smallfarmer01.service.UserService;

/**
 * Created by Vison on 2015/9/12.
 */
public class MainActivity extends AppCompatActivity {
    UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserService = new UserService(this);

        // TODO: check login status
        if (!mUserService.isLogin()) {
            goToSignUpPage();
        }

        // init viewPager
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        // init tabs
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

    private void goToSignUpPage() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
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
                    return OrdersFragment.newInstance(MainActivity.this);
                case 1:
                    return OrdersFragment.newInstance(MainActivity.this);
                case 2:
                    return AccountFragment.newInstance(MainActivity.this);
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
