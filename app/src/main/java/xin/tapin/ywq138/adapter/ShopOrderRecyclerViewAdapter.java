package xin.tapin.ywq138.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;
import xin.tapin.ywq138.bean.ShopOrder;
import xin.tapin.ywq138.db.MySQLite;

/**
 * 订单适配器
 */
public class ShopOrderRecyclerViewAdapter extends RecyclerView.Adapter <ShopOrderRecyclerViewAdapter.CookBookViewHolder>{
    private final TextView textView2;
    private final LinearLayout linearLayout;
    private List<ShopOrder> data;
    private Context context;
    private MySQLite dbHelper;
    public ShopOrderRecyclerViewAdapter(List<ShopOrder> data, Context context, TextView textView2, LinearLayout linearLayout) {
        this.dbHelper = new MySQLite(context,"userinfo.db", null, 1);
        this.data = data;
        this.context = context;
        this.textView2 = textView2;
        this.linearLayout = linearLayout;
    }
    class CookBookViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        public CookBookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }
    }
    @NonNull
    @Override
    public CookBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoporderitem,parent,false);
        return new CookBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CookBookViewHolder holder, int position) {
        final ShopOrder shopOrder = data.get(position);
        holder.textView.setText(String.format("%d", shopOrder.getId()));
        holder.textView2.setText(String.format("￥%s", shopOrder.getTotal()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setView(getDialogView(shopOrder))
                        .setPositiveButton("关闭",null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 弹窗显示订单详情
     * @param shopOrder
     * @return
     */
    public View getDialogView (ShopOrder shopOrder){
        View view = View.inflate(this.context,R.layout.dialog_order_detail,null);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        TextView textView4 = view.findViewById(R.id.textView4);
        TextView textView5 = view.findViewById(R.id.textView5);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        textView.setText(String.format("%d", shopOrder.getId()));
        textView2.setText(shopOrder.getName());
        textView3.setText(shopOrder.getPhoneNumber());
        textView4.setText(shopOrder.getAddress());
        textView5.setText(String.format("￥%s", shopOrder.getTotal()));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<ShopItem> data = new ArrayList<>();

        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        String Query = "Select * from shop_order_detail where  shop_order_id=?";
        Cursor cursor = writableDatabase.rawQuery(Query, new String[]{shopOrder.getId()+""});
        while (cursor.moveToNext()){
            String itemID = cursor.getString(cursor.getColumnIndex("itemID"));
            int number = cursor.getInt(cursor.getColumnIndex("number"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String imageURL = cursor.getString(cursor.getColumnIndex("imageURL"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = Double.valueOf(cursor.getString(cursor.getColumnIndex("price")));
            data.add(new ShopItem(itemID,number,true,url,imageURL,name,price));
        }
        cursor.close();
        writableDatabase.close();

        recyclerView.setAdapter(new ShopOderDetailAdapter(context,data));
        return view;
    }
}
