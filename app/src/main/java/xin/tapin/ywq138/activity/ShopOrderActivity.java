package xin.tapin.ywq138.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xin.tapin.ywq138.R;
import xin.tapin.ywq138.adapter.ShopOrderRecyclerViewAdapter;
import xin.tapin.ywq138.bean.ShopOrder;
import xin.tapin.ywq138.db.MySQLite;

/**
 * 订单Activity
 */
public class ShopOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private TextView textView2;
    private LinearLayout linearLayout;
    private MySQLite mySQLite ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);
        this.mySQLite = new MySQLite(this,"userinfo.db", null, 1);
        initView();
        initData();
        //退出监听
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<ShopOrder> data = new ArrayList<>();
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        Cursor cursor = writableDatabase.query("shop_order", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            Double total = cursor.getDouble(cursor.getColumnIndex("total"));
            data.add(new ShopOrder(id,name,phoneNumber,address,null,total));
        }
        writableDatabase.close();
        //判断是否有数据  界面提示用的  R.layout.activity_collect布局
        if(data.size() == 0){
            textView2.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }else{
            textView2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ShopOrderRecyclerViewAdapter(data,this,textView2,linearLayout));
        }
    }
    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        linearLayout = findViewById(R.id.linearLayout);
    }
}
