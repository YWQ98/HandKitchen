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
import com.squareup.picasso.Picasso;

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
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;
        ImageView imageView;
        public CookBookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
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
        Picasso.get().load(cookBook.getImgUrl()).into(holder.imageView);
        /*Glide.with(context)
                .load("https:"+cookBook.getImgUrl())
                .into(holder.imageView);*/
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
        TextView textView4 = view.findViewById(R.id.textView4);
        TextView textView5 = view.findViewById(R.id.textView5);
        TextView textView6 = view.findViewById(R.id.textView6);
        textView.setText(cookBook.getTitle());
        textView2.setText(cookBook.getMessage());
        textView3.setText(cookBook.getMainIngredient());
        textView4.setText(cookBook.getAuxiliaryIngredient());
        textView5.setText(cookBook.getSeasoning());
        textView6.setText(cookBook.getRecipeStep());

        Picasso.get().load(cookBook.getImgUrl()).into(imageView);

        return view;
    }
}
