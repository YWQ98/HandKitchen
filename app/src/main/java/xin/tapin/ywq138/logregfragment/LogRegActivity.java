package xin.tapin.ywq138.logregfragment;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import xin.tapin.ywq138.R;

/**
 * 登录注册FragmentActivity
 */
public class LogRegActivity extends FragmentActivity {
    private TabLayout loglayout;
    private ViewPager reglayout;


    private List<String> tabs;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        loglayout = findViewById(R.id.logLayout);
        reglayout = findViewById(R.id.regLayout);

        initData();
        initViewPager();
        initTabLayout();
    }

    /**
     * 初始化TabLayout
     * 关联tabLayout和ViewPager,两者的选择和滑动状态会相互影响
     */
    private void initTabLayout() {

        loglayout.setupWithViewPager(reglayout);
        for (int i = 0; i < tabs.size(); i++) {
            TabLayout.Tab tab = loglayout.getTabAt(i);
            TextView tv = new TextView(this);
            tv.setText(tabs.get(i));
            tv.setTextColor(Color.BLACK);
            tab.setCustomView(tv);
        }
    }

    /**
     * 初始化ViewPager设置适配器
     */
    private void initViewPager() {
        reglayout.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    /**
    * 初始化数据将Fragment添加到数据中以便切换
    */
    private void initData() {
        tabs = new ArrayList<>();
        tabs.add("登录");
        tabs.add("注册");

        fragments = new ArrayList<>();

        fragments.add(new Fragment_log(this));
        fragments.add(new Fragment_reg(this));


    }

    /**
     * 设置Fragment适配器
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
