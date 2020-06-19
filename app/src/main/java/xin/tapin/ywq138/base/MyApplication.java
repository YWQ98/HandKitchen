package xin.tapin.ywq138.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import xin.tapin.ywq138.bean.CookBook;

/**
 * 辅助保存所需数据
 * 配置OkHttpClient
 */
public class MyApplication extends Application {
    //新版食谱辅助参数
    private static String newClassShuPu;//当前食谱类型
    private static String upPage;//上一页
    private static String nextPage;//下一页
    private static Integer search;//搜索状态

    public static Integer getSearch() {
        return search;
    }

    public static void setSearch(Integer search) {
        MyApplication.search = search;
    }

    //以下新版食谱不可用
    private static String classShuPu;//当前食谱类型
    private static int maxPage;//最大页数
    private static int page;//当前页面
    private static String trueSearch;//搜索真实地址  已取消使用，原需要此参数数据已修改
    private static Context context;

    public static String getNewClassShuPu() {
        return newClassShuPu;
    }

    public static void setNewClassShuPu(String newClassShuPu) {
        MyApplication.newClassShuPu = newClassShuPu;
    }

    public static String getUpPage() {
        return upPage;
    }

    public static void setUpPage(String upPage) {
        MyApplication.upPage = upPage;
    }

    public static String getNextPage() {
        return nextPage;
    }

    public static void setNextPage(String nextPage) {
        MyApplication.nextPage = nextPage;
    }

    public static int getMaxPage() {
        return maxPage;
    }

    public static void setMaxPage(int maxPage) {
        MyApplication.maxPage = maxPage;
    }

    public static String getTrueSearch() {
        return trueSearch;
    }

    public static void setTrueSearch(String trueSearch) {
        MyApplication.trueSearch = trueSearch;
    }

    public static String getClassShuPu() {
        return classShuPu;
    }

    public static void setClassShuPu(String classShuPu) {
        MyApplication.classShuPu = classShuPu;
    }

    public static int getPage() {
        return page;
    }

    public static void setPage(int page) {
        MyApplication.page = page;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static Context getContext() {
        return context;
    }
}
