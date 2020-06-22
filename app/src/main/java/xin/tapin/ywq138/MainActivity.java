package xin.tapin.ywq138;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import xin.tapin.ywq138.Adapter.MainFragmentStatePagerAdapter;
import xin.tapin.ywq138.logregfragment.LogRegActivity;
import xin.tapin.ywq138.mainfragment.Fragment_Old_kitchen;
import xin.tapin.ywq138.mainfragment.Fragment_cart;
import xin.tapin.ywq138.mainfragment.Fragment_kitchen;
import xin.tapin.ywq138.mainfragment.Fragment_my;
import xin.tapin.ywq138.mainfragment.Fragment_shop;

/**
 * 主界面FragmentActivity
 */
public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    private BottomNavigationView navigation;
    private ArrayList<Fragment> data;
    private int position = 0;
    private Fragment temp;

    /**
     * 监听navigation的选中然后切换Fragment
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.kitchen:
//                    viewPager.setCurrentItem(0);
                    position = 0;
                    break;
                case R.id.shop:
//                    viewPager.setCurrentItem(1);
                    position = 1;
                    break;
                case R.id.cart:
//                    viewPager.setCurrentItem(2);
                    position = 2;
                    break;
                case R.id.my:
//                    viewPager.setCurrentItem(3);
                    position = 3;
                    break;
            }
            Fragment baseFragment = getFragment(position);
            switchFragment(temp,baseFragment);
            return true;
        }
    };
    public Fragment getFragment(int position){
        if(data != null && data.size() > 0){
            Fragment baseFragment = data.get(position);
            return baseFragment;
        }
        return null;
    }
    public void switchFragment(Fragment fromFragment, Fragment baseFragment){
        if(temp != baseFragment){
            temp = baseFragment;
            if(baseFragment != null){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if(!baseFragment.isAdded()){
                    if(fromFragment != null){
                        fragmentTransaction.hide(fromFragment);
                    }
                    fragmentTransaction.add(R.id.frameLayout,baseFragment).commit();
                }else {
                    if(fromFragment != null){
                        fragmentTransaction.hide(fromFragment);
                    }
                    fragmentTransaction.show(baseFragment).commit();
                    baseFragment.onResume();
                }
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加Fragment
        data = new ArrayList<>();
        data.add(new Fragment_kitchen(this));//新版本
//        data.add(new Fragment_Old_kitchen(this));//旧版本
        data.add(new Fragment_shop(this));
        data.add(new Fragment_cart(this));
        data.add(new Fragment_my());
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,data.get(0)).commit();
        //添加标题栏
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //查找ViewPager并绑定适配器
//        viewPager = findViewById(R.id.viewPager);
//        viewPager.setAdapter(new MainFragmentStatePagerAdapter(getSupportFragmentManager(), this, data));
//        //ViewPager改变监听，设置navigation的选中状态
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                navigation.getMenu().getItem(position).setChecked(true);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        //navigation设置选中监听
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 创建Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Menu点击监听
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 退出账号，退出前弹窗提示是否退出
     */
    public void exit() {
        new AlertDialog.Builder(this)
                .setTitle("是否退出")
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "已成功退出账号", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, LogRegActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setPositiveButton("取消",null)
                .show();
    }

    /**
     * 发布
     * @param view
     */
    public void publish(View view) {
        new AlertDialog.Builder(this)
                .setTitle("发布")
                .setNegativeButton("退出", null)
                .setPositiveButton("取消",null)
                .show();
    }

    public void backHome() {
        switchFragment(data.get(2),data.get(1));
        navigation.setSelectedItemId(R.id.shop);
    }
}
