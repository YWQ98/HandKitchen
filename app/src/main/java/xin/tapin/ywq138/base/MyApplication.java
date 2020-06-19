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
    private static String classShuPu;//当前食谱类型
    private static int maxPage;//最大页数
    private static int page;//当前页面
    private static String trueSearch;//搜索真实地址  已取消使用，原需要此参数数据已修改
    private static Context context;



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
