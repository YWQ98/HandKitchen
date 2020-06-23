package xin.tapin.ywq138.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存本地xml文件--已换SQLite  数据都保存本地SQLite
 */
public class CacheUtils {
    public static String getString(Context context,String key){
        SharedPreferences ywq = context.getSharedPreferences("ywq", Context.MODE_PRIVATE);
        return ywq.getString(key,"");
    }
    public static void saveString(Context context,String key,String value){
        SharedPreferences ywq = context.getSharedPreferences("ywq", Context.MODE_PRIVATE);
        ywq.edit().putString(key,value).apply();
    }
}
