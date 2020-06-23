package xin.tapin.ywq138.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Iterator;
import java.util.List;

import xin.tapin.ywq138.db.MySQLite;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;
import xin.tapin.ywq138.view.AddSubView;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MySQLite mySQLite;
    private Context mContext;

    public List<ShopItem> getData() {
        return data;
    }


    private List<ShopItem> data;
    private TextView tvShopcartTotal;
    private CheckBox checkboxAll;
    private CheckBox cb_all;
    public CartAdapter(Context context, final List<ShopItem> datas,
                       TextView tvShopcartTotal, MySQLite mySQLite, CheckBox
                                   checkboxAll, CheckBox cb_all) {
        this.mContext = context;
        this.data = datas;
        this.tvShopcartTotal = tvShopcartTotal;
        this.mySQLite= mySQLite;
        this.checkboxAll = checkboxAll;
        this.cb_all = cb_all;

            showTotalPrice();
        checkboxAll.setChecked(checkboxAll.isChecked());
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setSelected(datas.get(i).isSelected());
        }
        showTotalPrice();
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                ShopItem goodsBean = datas.get(position);
                goodsBean.setSelected(!goodsBean.isSelected());
                notifyItemChanged(position);

                SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
                String Query  = "update cartinfo set selected=? where  itemID=?";
                writableDatabase.execSQL(Query,new Object[]{goodsBean.isSelected(),goodsBean.getItemID()});
//                Log.i("TAG", "onItemClickListener: " + goodsBean.getNumber());
//              cursor.close();
                writableDatabase.close();

//                cartStorage.updateData(goodsBean);
                checkAll();
                showTotalPrice();
            }
        });
        checkboxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = getCheckboxAll().isChecked();
                checkAll_none(checked);
                showTotalPrice();
            }
        });
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = getCb_all().isChecked();
                checkAll_none(checked);
                showTotalPrice();
            }
        });
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_shop_cart, null));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int
            position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(data.get(position));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void checkAll_none(boolean checked) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(checked);
//                cartStorage.updateData(data.get(i));
                checkboxAll.setChecked(checked);
                notifyItemChanged(i);
            }
        } else {
            checkboxAll.setChecked(false);
        }
    }
    public void deleteData() {
        if (data != null && data.size() > 0) {
            for (Iterator iterator = data.iterator();
                 iterator.hasNext(); ) {
                ShopItem cart = (ShopItem) iterator.next();
                if (cart.isSelected()) {
                    int position = data.indexOf(cart);
//                    cartStorage.deleteData(cart);
                    iterator.remove();

                    SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
                    writableDatabase.execSQL("delete from cartinfo where itemID=?", new Object[]{cart.getItemID()});
                    writableDatabase.close();

                    notifyItemRemoved(position);
                }
            }
        }
    }
    public void checkAll() {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).isSelected()) {
                    checkboxAll.setChecked(false);
                    cb_all.setChecked(false);
                    return;
                } else {
                    checkboxAll.setChecked(true);
                    cb_all.setChecked(true);
                }
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbGov;
        private ImageView ivGov;
        private TextView tvDescGov;
        private TextView tvPriceGov;
        private AddSubView addSubView;
        ViewHolder(View itemView) {
            super(itemView);
            cbGov = itemView.findViewById(R.id.cb_gov);
            ivGov = itemView.findViewById(R.id.iv_gov);
            tvDescGov = itemView.findViewById(R.id.tv_desc_gov);
            tvPriceGov = itemView.findViewById(R.id.tv_price_gov);
            addSubView = itemView.findViewById(R.id.AddSubView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(v,getLayoutPosition());

                    }
                }
            });
        }
        public void setData(final ShopItem goodsBean) {
            cbGov.setChecked(goodsBean.isSelected());
            Glide.with(mContext)
                    .load(goodsBean.getImageURL())
                    .into(ivGov);
            tvDescGov.setText(goodsBean.getName());
            tvPriceGov.setText(String.format("￥%s", goodsBean.getPrice()));
            addSubView.setValue(goodsBean.getNumber());
            addSubView.setOnNumberChangeListener(new AddSubView.OnNumberChangeListener() {
                     @Override
                     public void addNumber(View view, int value) {
                         goodsBean.setNumber(value);

                         SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
                         String Query  = "update cartinfo set number=? where  itemID=?";
                         writableDatabase.execSQL(Query,new Object[]{value,goodsBean.getItemID()});
//                         Log.i("TAG", "onItemClickListener: " + goodsBean.getNumber());
//                         cursor.close();
                         writableDatabase.close();

//                         cartStorage.updateData(goodsBean);
                         showTotalPrice();
                     }
                     @Override
                     public void subNumber(View view, int value) {
                         goodsBean.setNumber(value);

                         SQLiteDatabase writableDatabase = mySQLite.getWritableDatabase();
                         String Query  = "update cartinfo set number=? where  itemID=?";
                         writableDatabase.execSQL(Query,new Object[]{value,goodsBean.getItemID()});
//                         Log.i("TAG", "onItemClickListener: " + goodsBean.getNumber());
//                         cursor.close();
                         writableDatabase.close();

//                         cartStorage.updateData(goodsBean);
                         showTotalPrice();
                     }
                 });
        }
    }
    public void showTotalPrice() {
        tvShopcartTotal.setText(String.format("%.2f", getTotalPrice()));
    }
    public double getTotalPrice() {//结账使用
        double total = 0;
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                ShopItem goodsBean = data.get(i);
                if (goodsBean.isSelected())
                    total +=  goodsBean.getPrice() *
                                    Double.parseDouble(goodsBean.getNumber() + "");
            }
        }
        return total;
    }
    public Double getTotal(){
        Double s = Double.valueOf(tvShopcartTotal.getText().toString());
        return s;
    }
    private OnItemClickListener onItemClickListener;
    interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public CheckBox getCb_all() {
        return cb_all;
    }
    public void setCb_all(CheckBox cb_all) {
        this.cb_all = cb_all;
    }
    public CheckBox getCheckboxAll() {
        return checkboxAll;
    }
    public void setCheckboxAll(CheckBox checkboxAll) {
        this.checkboxAll = checkboxAll;
    }


}
