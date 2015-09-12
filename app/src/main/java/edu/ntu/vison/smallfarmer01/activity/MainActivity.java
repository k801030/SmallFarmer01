package edu.ntu.vison.smallfarmer01.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.astuetz.PagerSlidingTabStrip;

import edu.ntu.vison.smallfarmer01.R;

/**
 * Created by Vison on 2015/9/12.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init viewPager
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        // init tabs
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"管理出貨", "管理商品", "管理帳單"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return OrdersFragment.newInstance();
                case 1:
                    return OrdersFragment.newInstance();
                case 2:
                    return OrdersFragment.newInstance();
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
