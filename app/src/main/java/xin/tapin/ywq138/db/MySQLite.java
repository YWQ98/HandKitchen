package xin.tapin.ywq138.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 */
public class MySQLite extends SQLiteOpenHelper {


    /**
     * 设置默认创建数据库表
     */
    private String sql="create table if not exists userinfo(id varchar(30) primary key,name varchar(30) not null,passwd varchar(50) not null)";
    private String cart="create table if not exists cartinfo(id INTEGER primary key AUTOINCREMENT," +
            "itemID varchar(50) not null,number int not null,selected varchar(50) not null" +
            ",url varchar(100) not null,imageURL varchar(150) not null,name varchar(70) not null,price double not null)";
    private String collect_cookbook="create table if not exists collect_cookbook(id INTEGER primary key AUTOINCREMENT," +
            "url varchar(100) not null,title varchar(50) not null,imgUrl varchar(100) not null" +
            ",message varchar(200) not null,mainIngredient varchar(200) not null,auxiliaryIngredient varchar(200) not null" +
            ",seasoning varchar(200) not null,recipeStep varchar(500) not null)";

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MySQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建数据库表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(cart);
        db.execSQL(collect_cookbook);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
