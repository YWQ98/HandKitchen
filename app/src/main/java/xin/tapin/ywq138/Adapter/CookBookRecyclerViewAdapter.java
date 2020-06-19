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
import xin.tapin.ywq138.bean.CookBook;

/**
 * 食谱适配器
 */
public class CookBookRecyclerViewAdapter extends RecyclerView.Adapter <CookBookRecyclerViewAdapter.CookBookViewHolder>{
    private List<CookBook> data;
    private Context context;
    public CookBookRecyclerViewAdapter(List<CookBook> data, Context context) {
        this.data = data;
        this.context = context;
    }
    class CookBookViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public CookBookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    @NonNull
    @Override
    public CookBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cookbookitem,parent,false);
        return new CookBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CookBookViewHolder holder, int position) {
        final CookBook cookBook = data.get(position);
        holder.textView.setText(cookBook.getTitle());
        Glide.with(context)
                .load("https:"+cookBook.getImgUrl())
                .into(holder.imageView);
        //点击监听弹窗显示该菜的所需的材料和做法
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setView(getDialogView(cookBook))
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
     * @param cookBook
     * @return
     */
    public View getDialogView (CookBook cookBook){
        View view = View.inflate(this.context,R.layout.dialog_item_zuofa,null);
        TextView textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        textView.setText(cookBook.getTitle());
        textView2.setText(cookBook.getMaterial());
        //数据可能有部分有格式可以优化（换行显示，看起来清楚点）  不适用全部
        textView3.setText(cookBook.getPractice().replace("；","。\n"));
        Glide.with(context)
                // //ali.xinshipu.cn/20140226/original/1393380931309.jpg@288w_216h_99q_1e_1c.jpg
                //默认获取到就有http:后面的//
                .load("https:"+cookBook.getImgUrl())
                .into(imageView);
        return view;
    }
}
