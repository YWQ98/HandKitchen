package xin.tapin.ywq138.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * 主界面Fragment适配器
 */
public class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private ArrayList<Fragment> data;

    public MainFragmentStatePagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> data){
        super(fm);
        this.context = context;
        this.data = data;
    }
    @Override
    public Fragment getItem(int position){
        return data.get(position);
    }
    @Override
    public int getCount(){
        return data.size();
    }
}
