package xin.tapin.ywq138.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import xin.tapin.ywq138.db.MySQLite;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.CookBook;

/**
 * 收藏夹适配器
 */
public class CollectRecyclerViewAdapter extends RecyclerView.Adapter <CollectRecyclerViewAdapter.CookBookViewHolder>{
    private final TextView textView2;
    private final LinearLayout linearLayout;
    private List<CookBook> data;
    private Context context;
    private MySQLite dbHelper;
    public CollectRecyclerViewAdapter(List<CookBook> data, Context context, TextView textView2, LinearLayout linearLayout) {
        this.dbHelper = new MySQLite(context,"userinfo.db", null, 1);
        this.data = data;
        this.context = context;
        this.textView2 = textView2;
        this.linearLayout = linearLayout;
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
        Picasso.get().load(cookBook.getImgUrl()).into(holder.imageView);
        //点击监听弹窗显示该菜的所需的材料和做法
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setView(getDialogView(cookBook))
                        .setPositiveButton("关闭",null)
                        .setNegativeButton("取消收藏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                data.remove(position);
                                
                                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                                writableDatabase.execSQL("delete from collect_cookbook where url=?", new Object[]{cookBook.getUrl()});
                                writableDatabase.close();
                                if(data.size() == 0){
                                    textView2.setVisibility(View.VISIBLE);
                                    linearLayout.setVisibility(View.GONE);
                                }
                                Toast.makeText(context, "已成功删除收藏", Toast.LENGTH_SHORT).show();
                                notifyItemRemoved(position);
                            }
                        })
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
