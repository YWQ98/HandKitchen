package xin.tapin.ywq138.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;
import xin.tapin.ywq138.jsoup.MyJsoup;

/**
 * 集市适配器
 */
public class ShopRecyclerViewAdapter extends RecyclerView.Adapter <ShopRecyclerViewAdapter.CookBookViewHolder>{
    private List<ShopItem> data;
    private MainActivity context;
    public ShopRecyclerViewAdapter(List<ShopItem> data, MainActivity context) {
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
                .load(ShopItem.getImageURL())
                .into(holder.imageView);
        //点击监听弹窗显示
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setView(getDialogView(ShopItem))
//                        .setTitle("商品"+ShopItem.getName())
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
     * 弹窗显示菜的材料和做法
     * @param shopItem
     * @return
     */
    @SuppressLint("SetJavaScriptEnabled")
    public View getDialogView (ShopItem shopItem){
        View view = View.inflate(this.context,R.layout.dialog_item_shop,null);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        WebView webView = view.findViewById(R.id.webView);

        Picasso.get().load(shopItem.getImageURL()).into(imageView);
        textView.setText(shopItem.getName());
        textView2.setText(String.format("%s", shopItem.getPrice()));

        MyJsoup myJsoup = new MyJsoup();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String shop_info = myJsoup.getShop_info(shopItem.getUrl());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView3.setText(shop_info);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        webView.loadUrl(shopItem.getUrl());
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        webView.getSettings().setBlockNetworkImage(false);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setUseWideViewPort(true);//支持双击页面变大变小
//        webSettings.setJavaScriptEnabled(true);//设置支持JavaScript
//        //优先使用缓存
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
////                @Override
////                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        view.loadUrl(request.getUrl().toString());
////                    }
////                    return true;
////                }
//        });

        return view;
    }

}
