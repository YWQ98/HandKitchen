package xin.tapin.ywq138.mainfragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.Banner;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.List;

import okhttp3.Call;
import xin.tapin.ywq138.Adapter.CookBookRecyclerViewAdapter;
import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.base.MyApplication;
import xin.tapin.ywq138.bean.CookBook;
import xin.tapin.ywq138.jsoup.MyJsoup;
import xin.tapin.ywq138.utils.Constants;

/**
 * 厨房Fragment
 */
public class Fragment_kitchen extends Fragment {
    private final MainActivity mainActivity;
    private SearchView searchView;//搜索框
    private LinearLayout ranking;//甜品
    private LinearLayout classify;//家常菜
    private LinearLayout newest;//最新
    private Banner banner;
    private RecyclerView recyclerView;
    private Button button;//上一页
    private Button button2;//下一页
    private ScrollView scrollView;
    private List<CookBook> xinShiPu;
    private MyJsoup myJsoup;
    private CookBookRecyclerViewAdapter cookBookRecyclerViewAdapter;


    public Fragment_kitchen(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);

        myJsoup = new MyJsoup();

        initView(view);
        //搜索框监听事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setRecyclerViewData(Constants.NEW_SEARCH_URL+"/"+query,1);//调用设置RecyclerView，显示数据
                MyApplication.setSearch(1);
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //早餐分类事件监听
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewData(Constants.NEW_BREAKFAST_URL,null);//调用设置RecyclerView，显示数据
                MyApplication.setSearch(null);
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
            }
        });
        //甜品分类事件监听
        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewData(Constants.NEW_SWEET_URL,null);//调用设置RecyclerView，显示数据
                MyApplication.setSearch(null);
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
            }
        });
        //下午茶分类事件监听
        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewData(Constants.NEW_AFTERNOON_TEA_URL,null);//调用设置RecyclerView，显示数据
                MyApplication.setSearch(null);
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
            }
        });
        //上一页
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nextPage = MyApplication.getUpPage();
                if (nextPage != null){
                    setRecyclerViewData(nextPage,MyApplication.getSearch());
                    scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                    recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
                }else{
                    Toast.makeText(mainActivity, "已经是第一页了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //下一页
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nextPage = MyApplication.getNextPage();
                if (nextPage != null){
                    setRecyclerViewData(nextPage,MyApplication.getSearch());
                    scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                    recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
                }else{
                    Toast.makeText(mainActivity, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //初始界面  设置默认获取食谱
        MyApplication.setSearch(null);
        setRecyclerViewData(Constants.NEW_WEEK_URL,null);
        return view;
    }



    /**
     * 根据url获取数据并设置RecyclerView
     * @param url
     * @param page
     */
    private void setRecyclerViewData(final String url, final Integer page){
        //获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    xinShiPu = myJsoup.getShiPu(url,page);//获取数据
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置RecyclerView适配器
                            cookBookRecyclerViewAdapter = new CookBookRecyclerViewAdapter(xinShiPu, mainActivity);
                            recyclerView.setAdapter(cookBookRecyclerViewAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(mainActivity,DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 初始化View
     * @param view
     */
    private void initView(View view) {
        searchView = view.findViewById(R.id.searchView);
        ranking = view.findViewById(R.id.ranking);
        classify = view.findViewById(R.id.classify);
        newest = view.findViewById(R.id.newest);
        banner = view.findViewById(R.id.banner);
        recyclerView = view.findViewById(R.id.recyclerView);
        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        scrollView = view.findViewById(R.id.scrollView);
    }


}

