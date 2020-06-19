package xin.tapin.ywq138.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;

/**
 * 集市适配器
 */
public class ShopRecyclerViewAdapter extends RecyclerView.Adapter <ShopRecyclerViewAdapter.CookBookViewHolder>{
    private List<ShopItem> data;
    private Context context;
    public ShopRecyclerViewAdapter(List<ShopItem> data, Context context) {
        this.data = data;
        this.context = context;
    }
    class CookBookViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        ImageView imageView;
        public CookBookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    @NonNull
    @Override
    public CookBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem,parent,false);
        return new CookBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CookBookViewHolder holder, int position) {
        final ShopItem ShopItem = data.get(position);
        holder.textView.setText(String.format("￥%s", ShopItem.getPrice()));
        holder.textView2.setText(ShopItem.getName());
        Glide.with(context)
                .load("https:"+ShopItem.getImageURL())
                .into(holder.imageView);
        //点击监听弹窗显示
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
//                        .setView(getDialogView(ShopItem))
                        .setTitle("商品"+ShopItem.getName())
                        .setPositiveButton("关闭",null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
