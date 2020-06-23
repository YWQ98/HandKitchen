package xin.tapin.ywq138.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;

/**
 * 订单详情适配器
 */
public class ShopOderDetailAdapter extends RecyclerView.Adapter<ShopOderDetailAdapter.ShopOrderDetailViewHolder> {
    private Context mContext;
    private List<ShopItem> data;

    public ShopOderDetailAdapter(Context context, final List<ShopItem> datas) {
        this.mContext = context;
        this.data = datas;
    }
    @Override
    public ShopOrderDetailViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        return new ShopOrderDetailViewHolder(View.inflate(mContext, R.layout.shoporderdetail, null));
    }
    @Override
    public void onBindViewHolder(ShopOrderDetailViewHolder holder, int
            position) {
        ShopItem shopItem = data.get(position);
        Picasso.get().load(shopItem.getImageURL()).into(holder.imageView);
        holder.tv_desc_gov.setText(shopItem.getName());
        holder.tv_price_gov.setText(String.format("￥%s", shopItem.getPrice()));
        holder.number.setText(String.format("X%d", shopItem.getNumber()));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    class ShopOrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;//商品图片
        TextView tv_desc_gov;//商品名字
        TextView tv_price_gov;//商品价格
        TextView number;//商品数量
        ShopOrderDetailViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_gov);
            tv_desc_gov = itemView.findViewById(R.id.tv_desc_gov);
            tv_price_gov = itemView.findViewById(R.id.tv_price_gov);
            number = itemView.findViewById(R.id.number);
        }
    }


}
