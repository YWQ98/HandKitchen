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

import xin.tapin.ywq138.Adapter.CollectRecyclerViewAdapter;
import xin.tapin.ywq138.DB.MySQLite;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.CookBook;

/**
 * 收藏夹Activity
 */
public class CollectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private TextView textView2;
    private LinearLayout linearLayout;
    private MySQLite mySQLite ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        this.mySQLite = new MySQLite(this,"userinfo.db", null, 1);
        initView();
        initData();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        List<CookBook> data = new ArrayList<>();
        SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
        Cursor cursor = writableDatabase.query("collect_cookbook", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
            String message = cursor.getString(cursor.getColumnIndex("message"));
            String mainIngredient = cursor.getString(cursor.getColumnIndex("mainIngredient"));
            String auxiliaryIngredient = cursor.getString(cursor.getColumnIndex("auxiliaryIngredient"));
            String seasoning = cursor.getString(cursor.getColumnIndex("seasoning"));
            String recipeStep = cursor.getString(cursor.getColumnIndex("recipeStep"));
            data.add(new CookBook(url,title,imgUrl,message,mainIngredient,auxiliaryIngredient,seasoning,recipeStep));
        }
        writableDatabase.close();
        if(data.size() == 0){
            textView2.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }else{
            textView2.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new CollectRecyclerViewAdapter(data,this,textView2,linearLayout));
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        linearLayout = findViewById(R.id.linearLayout);
    }
}
