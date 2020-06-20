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
import xin.tapin.ywq138.Adapter.OldCookBookRecyclerViewAdapter;
import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.base.MyApplication;
import xin.tapin.ywq138.bean.OldCookBook;
import xin.tapin.ywq138.jsoup.MyJsoup;
import xin.tapin.ywq138.utils.Constants;

/**
 * 旧版本 厨房Fragment
 */
public class Fragment_Old_kitchen extends Fragment {
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
    private List<OldCookBook> xinShiPu;
    private MyJsoup myJsoup;
    private OldCookBookRecyclerViewAdapter oldCookBookRecyclerViewAdapter;


    public Fragment_Old_kitchen(MainActivity mainActivity) {
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

                //search比较特殊需要先根据搜索内容先获取真实的url
                OkHttpUtils
                        .get()
                        .url(Constants.BASE_URL + "/doSearch.html?q=" + query)
                        .build()
                        .execute(new StringCallback()
                        {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e("info",e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                int https = response.indexOf("https");//地址https开头
                                String doc = "";//真实url
                                if(https+100 < response.length() && https > 1){//可能搜索内容不存在报错
                                    for (int i = https; i < https+100; i++) {//拼接真实地址
                                        if(response.substring(i,i+1).equals("\"")){//结束跳出
                                            break;
                                        }
                                        doc += response.substring(i,i+1);
                                    }
                                }
//                                MyApplication.setTrueSearch(doc);//赋值（暂停使用）  直接调用rsQuery()并把url传过去即可
                                rsQuery(doc);//调用该方法实现搜索结果数据显示
                            }
                        });
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //甜品分类事件监听
        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewData(Constants.TAIN_PIN_URL,null);//调用设置RecyclerView，显示数据
                MyApplication.setPage(1);//默认第一页
                MyApplication.setClassShuPu(Constants.TAIN_PIN_URL);//赋值当前在哪个类别，以便换页
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
            }
        });
        //家常菜
        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewData(Constants.JIA_CHANG_CHANG_URL,null);//调用设置RecyclerView，显示数据
                MyApplication.setPage(1);//默认第一页
                MyApplication.setClassShuPu(Constants.JIA_CHANG_CHANG_URL);//赋值当前在哪个类别，以便换页
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
            }
        });
        //最新食谱
        newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewData(Constants.XIN_SHI_PU_URL,null);//调用设置RecyclerView，显示数据
                MyApplication.setPage(1);//默认第一页
                MyApplication.setClassShuPu(Constants.XIN_SHI_PU_URL);//赋值当前在哪个类别，以便换页
                scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
            }
        });
        //上一页
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = MyApplication.getPage()-1;//获取现页面-1
                String classShuPu = MyApplication.getClassShuPu();//获取当前类别页面
                if (page >= 1 ){//上一页大于等于1
                    MyApplication.setPage(page);//重新赋值当前页数
                    setRecyclerViewData(classShuPu,page);//调用该方法设置显示RecyclerView数据
                    scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                    recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
                }else {//提示当前就是第一页
                    Toast.makeText(mainActivity, "已经是第一页", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //下一页
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = MyApplication.getPage()+1;//获取现页面+1
                String classShuPu = MyApplication.getClassShuPu();//获取当前类别页面
                if (MyApplication.getMaxPage() < page) {
                    Toast.makeText(mainActivity, "已经是最后一页", Toast.LENGTH_SHORT).show();
//                    Log.i("TAG", "onClick: \n"+nowCookBook.get(0).getUrl()+"\n"+nextCookBook.get(0).getUrl());
                    page--;
                }else{
                    setRecyclerViewData(classShuPu,page);//调用该方法设置显示RecyclerView数据
                    scrollView.scrollTo(0,0);//获取完成重新定位到顶部
                    recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
                }
                MyApplication.setPage(page);//重新赋值当前页数
            }
        });
        //初始界面  设置默认获取食谱
        setRecyclerViewData(Constants.XIN_SHI_PU_URL,null);
        MyApplication.setPage(1);
        MyApplication.setClassShuPu(Constants.XIN_SHI_PU_URL);
        return view;
    }

    public void rsQuery(String url) {
        //获取真实地址
        url = url.replace("m.","www.");
        url = url.replace("mip/","");
//        Log.i("TAG", "run: "+url);
        setRecyclerViewData(url,null);//调用该方法设置显示RecyclerView数据
        MyApplication.setPage(1);//默认第一页
        MyApplication.setClassShuPu(url);//设置当前所在类别 换页需使用此参数
        scrollView.scrollTo(0,0);//获取完成重新定位到顶部
        recyclerView.scrollToPosition(0);//获取完成重新定位到顶部
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
                String url2 = "";
                if(url == null){
                    url2 = MyApplication.getClassShuPu();
                }else{
                    url2 = url;
                }
                try {
//                    xinShiPu = myJsoup.getShiPu(url2,page,xinShiPu);//获取数据
                    xinShiPu = myJsoup.getOldShiPu(url2,page,xinShiPu);//获取数据
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //设置RecyclerView适配器
                            oldCookBookRecyclerViewAdapter = new OldCookBookRecyclerViewAdapter(xinShiPu, mainActivity);
                            recyclerView.setAdapter(oldCookBookRecyclerViewAdapter);
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

