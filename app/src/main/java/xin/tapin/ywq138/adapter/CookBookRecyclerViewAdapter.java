package xin.tapin.ywq138.adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * 食谱适配器
 */
public class CookBookRecyclerViewAdapter extends RecyclerView.Adapter <CookBookRecyclerViewAdapter.CookBookViewHolder>{
    private List<CookBook> data;
    private Context context;
    private MySQLite dbHelper;
    public CookBookRecyclerViewAdapter(List<CookBook> data, Context context) {
        this.dbHelper = new MySQLite(context,"userinfo.db", null, 1);
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
                        .setNegativeButton("收藏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                                String Query = "Select * from collect_cookbook where  url=?";
                                Cursor cursor = writableDatabase.rawQuery(Query, new String[]{cookBook.getUrl()});
                                if(cursor.getCount() > 0){
//                                    cursor.moveToFirst();
//                                    String url = cursor.getString(cursor.getColumnIndex("url"));
//                                    String title = cursor.getString(cursor.getColumnIndex("title"));
//                                    String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
//                                    String message = cursor.getString(cursor.getColumnIndex("message"));
//                                    String mainIngredient = cursor.getString(cursor.getColumnIndex("mainIngredient"));
//                                    String auxiliaryIngredient = cursor.getString(cursor.getColumnIndex("auxiliaryIngredient"));
//                                    String seasoning = cursor.getString(cursor.getColumnIndex("seasoning"));
//                                    String recipeStep = cursor.getString(cursor.getColumnIndex("recipeStep"));
//                                    Log.i("TAG", "onClick: "+url+"\n"+title+"\n"+imgUrl+"\n"+message+"\n"+mainIngredient+"\n"+auxiliaryIngredient+"\n"+seasoning
//                                            +"\n"+recipeStep+"\n");
                                    Toast.makeText(context, "已在收藏夹", Toast.LENGTH_SHORT).show();
                                }else {
                                    ContentValues values = new ContentValues();
                                    values.put("url", cookBook.getUrl());
                                    values.put("title", cookBook.getTitle());
                                    values.put("imgUrl", cookBook.getImgUrl());
                                    values.put("message", cookBook.getMessage());
                                    values.put("mainIngredient", cookBook.getMainIngredient());
                                    values.put("auxiliaryIngredient", cookBook.getAuxiliaryIngredient());
                                    values.put("seasoning", cookBook.getSeasoning());
                                    values.put("recipeStep", cookBook.getRecipeStep());
                                    writableDatabase.insert("collect_cookbook", null, values);
                                    Toast.makeText(context, "已成功收藏", Toast.LENGTH_SHORT).show();
                                }
                                cursor.close();
                                writableDatabase.close();
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
