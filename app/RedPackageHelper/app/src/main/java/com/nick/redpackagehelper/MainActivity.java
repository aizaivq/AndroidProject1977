package com.nick.redpackagehelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.nick.redpackagehelper.base.AboutActivity;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {
        private Fragment[] mFragments = new Fragment[2];
        private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragments[0] = SettingsFragment.instantiate(this,SettingsFragment.class.getName());
        mFragments[1] = RecordFragment.instantiate(this,RecordFragment.class.getName());
        ViewPager mVPMain = findViewById(R.id.mVPMain);
        mVPMain.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mVPMain.setOnPageChangeListener(this);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.mBNBar);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.colorPrimary);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_settings, getString(R.string.settings)).setActiveColorResource(R.color.colorPrimaryDark))
                .addItem(new BottomNavigationItem(R.drawable.ic_record, getString(R.string.record)).setActiveColorResource(R.color.colorPrimaryDark))
                .setFirstSelectedPosition(0)
                .initialise();


    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        Log.i(TAG,"onPageSelected" + i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments[i];
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }
    }


    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_about:
                AboutActivity.start(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
