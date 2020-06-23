package xin.tapin.ywq138.mainfragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.Banner;

import java.util.List;

import xin.tapin.ywq138.adapter.ShopRecyclerViewAdapter;
import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.base.MyApplication;
import xin.tapin.ywq138.bean.ShopItem;
import xin.tapin.ywq138.jsoup.MyJsoup;
import xin.tapin.ywq138.utils.Constants;

/**
 * 集市Fragment
 */
public class Fragment_shop extends Fragment {
    private final MainActivity mainActivity;
    private SearchView searchView;//搜索框
    private Banner banner;
    private RecyclerView recyclerView;
    private Button button;//上一页
    private Button button2;//下一页
    private ScrollView scrollView;
    private List<ShopItem> xinShiPu;
    private MyJsoup myJsoup;
    private ShopRecyclerViewAdapter shopRecyclerViewAdapter;


    public Fragment_shop(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        myJsoup = new MyJsoup();

        initView(view);
        //搜索框监听事件
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = Constants.SHOP_BASE_URL+"/Search?keyword="+query;
                MyApplication.setClassShuPu(url);
                MyApplication.setPage(1);
                setRecyclerViewData(url,null);
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

        //上一页
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = MyApplication.getPage()-2;//获取现页面-2
                if(page >= 1){
                    String classShuPu = MyApplication.getClassShuPu();//获取当前类别页面
                    MyApplication.setPage(page);//重新赋值当前页数
                    setRecyclerViewData(classShuPu,page);//调用该方法设置显示RecyclerView数据
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
                int page = MyApplication.getPage()+2;//获取现页面+2
                String classShuPu = MyApplication.getClassShuPu();//获取当前类别页面
                setRecyclerViewData(classShuPu,page);//调用该方法设置显示RecyclerView数据
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
                MyApplication.setPage(page);//重新赋值当前页数
            }
        });
        //初始界面  设置默认获取商品
        setRecyclerViewData(Constants.SHOP_BASE_URL+"/Search?keyword=水果",null);
        MyApplication.setPage(1);
        MyApplication.setClassShuPu(Constants.SHOP_BASE_URL+"/Search?keyword=水果");
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
                    String trueUrl=url;
                    if (page != null){
                        trueUrl = url +"&page="+page;
                    }
                    xinShiPu = myJsoup.getShopItem(trueUrl);;//获取数据
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置RecyclerView适配器
                            shopRecyclerViewAdapter = new ShopRecyclerViewAdapter(xinShiPu, mainActivity);
                            recyclerView.setAdapter(shopRecyclerViewAdapter);
                            recyclerView.addItemDecoration(new DividerItemDecoration(mainActivity,DividerItemDecoration.VERTICAL));
                            recyclerView.setLayoutManager(new GridLayoutManager(mainActivity,2));
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
        banner = view.findViewById(R.id.banner);
        recyclerView = view.findViewById(R.id.recyclerView);
        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        scrollView = view.findViewById(R.id.scrollView);
    }

}

