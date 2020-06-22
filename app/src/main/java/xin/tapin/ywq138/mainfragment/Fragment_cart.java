package xin.tapin.ywq138.mainfragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.EnvUtils;

import java.util.ArrayList;
import java.util.List;

import xin.tapin.ywq138.Adapter.CartAdapter;
import xin.tapin.ywq138.DB.MySQLite;
import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;

/**
 * 集市Fragment
 */
public class Fragment_cart extends Fragment {
    private final MainActivity mainActivity;
    private TextView tvShopcartEdit;
    private RecyclerView recyclerview;
    private LinearLayout llCheckAll;
    private CheckBox checkboxAll;
    private TextView tvShopcartTotal;
    private Button btnCheckOut;
    private LinearLayout llDelete;
    private CheckBox cbAll;
    private Button btnDelete;
    private Button btnCollection;
    private ImageView ivEmpty;
    private TextView tvEmptyCartTobuy;
    private LinearLayout ll_empty_shopcart;
    private MySQLite dbHelper;
    private CartAdapter adapter;

    private static final int ACTION_EDIT = 0;

    private static final int ACTION_COMPLETE = 1;

    public Fragment_cart(MainActivity mainActivity) {
        this.dbHelper = new MySQLite(mainActivity,"userinfo.db", null, 1);
        this.mainActivity = mainActivity;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mainActivity,
                R.layout.fragment_cart, null);
        findViews(view);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    private void initData() {
        initListener();
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setText("编辑");
        llCheckAll.setVisibility(View.VISIBLE);
        showData();
    }
    private void initListener() {
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onlineTest();
//                f2fpay();
//                List<GoodsBean> data = CartStorage.getInstance().getAllData();
//                GoodsBean goodsBean = null;
//                for (GoodsBean g:
//                        data) {
//                    if(g.isSelected()){
//                        goodsBean = g;
//                        break;
//                    }
//                }
//                if(goodsBean == null){
//                    Toast.makeText(mContext, "请选择商品", Toast.LENGTH_SHORT).show();
//                }else{
//
//                    payV2(v);

//                    Intent intent = new Intent(mContext, PayDemoActivity.class);
//                    intent.putExtra("total_amount",adapter.getTotalPrice());
//                    intent.putExtra("checkData",checkData.substring(1,checkData.length()));
//                    startActivity(intent);

                }


//                if (hasInstalledAlipayClient(mContext)) {
//                    startAlipayClient((Activity) mContext, PAYEE);
//                } else {
//                    Toast.makeText(mContext, "未检测到支付宝！请安装支付宝app在进行尝试~！", Toast.LENGTH_SHORT).show();
//                }
//
//                try {
//                    Intent intent;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
//                        intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//                    } else {
//                        intent = new Intent("xin.tapin.ywq1712123138.Pay");
//                    }
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Toast.makeText(mContext, "结算", Toast.LENGTH_SHORT).show();
                /*
                    这边可以设置跳转到支付宝支付
                 */
//                Intent intent = new Intent(mContext, MainActivity.class);
//                startActivity(intent);
//            }
        });
        tvShopcartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) tvShopcartEdit.getTag();
                if (tag == ACTION_EDIT) {
                    showDelete();
                } else {
                    hideDelete();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteData();

                adapter.showTotalPrice();

                checkData();

                adapter.checkAll();
            }
        });
        tvEmptyCartTobuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.backHome();
            }
        });
    }
    private void checkData() {
        if (adapter != null && adapter.getItemCount() > 0) {
            tvShopcartEdit.setVisibility(View.VISIBLE);
            ll_empty_shopcart.setVisibility(View.GONE);
        } else {
            ll_empty_shopcart.setVisibility(View.VISIBLE);
            tvShopcartEdit.setVisibility(View.GONE);
        }
    }
    private void hideDelete() {
        tvShopcartEdit.setText("编辑");
        tvShopcartEdit.setTag(ACTION_EDIT);
        adapter.checkAll_none(true);
        llDelete.setVisibility(View.GONE);
        llCheckAll.setVisibility(View.VISIBLE);
        adapter.showTotalPrice();
    }
    private void showDelete() {
        tvShopcartEdit.setText("完成");
        tvShopcartEdit.setTag(ACTION_COMPLETE);
        adapter.checkAll_none(false);
        cbAll.setChecked(false);
        checkboxAll.setChecked(false);
        llDelete.setVisibility(View.VISIBLE);
        llCheckAll.setVisibility(View.GONE);
        adapter.showTotalPrice();
    }
    private void showData() {
        List<ShopItem> data = new ArrayList<>();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query("cartinfo",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String itemID = cursor.getString(cursor.getColumnIndex("itemID"));
            int number = cursor.getInt(cursor.getColumnIndex("number"));
            String selected = cursor.getString(cursor.getColumnIndex("selected"));
            boolean select = false;
            if (selected.equals("1")){
                select = true;
            }
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String imageURL = cursor.getString(cursor.getColumnIndex("imageURL"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Double price = Double.valueOf(cursor.getString(cursor.getColumnIndex("price")));
            data.add(new ShopItem(itemID,number,select,url,imageURL,name,price));
        }
        if (data != null && data.size() > 0) {
            tvShopcartEdit.setVisibility(View.VISIBLE);
            adapter = new CartAdapter(mainActivity, data,
                    tvShopcartTotal, dbHelper, checkboxAll, cbAll);
            recyclerview.setLayoutManager(new
                    LinearLayoutManager(mainActivity));
            recyclerview.setAdapter(adapter);
            ll_empty_shopcart.setVisibility(View.GONE);
        } else {
            tvShopcartEdit.setVisibility(View.GONE);
            ll_empty_shopcart.setVisibility(View.VISIBLE);
        }
    }

    private void findViews(View view) {
        tvShopcartEdit = view.findViewById(R.id.tv_shopcart_edit);
        recyclerview = view.findViewById(R.id.recyclerview);
        llCheckAll = view.findViewById(R.id.ll_check_all);
        checkboxAll =  view.findViewById(R.id.checkbox_all);
        tvShopcartTotal = view.findViewById(R.id.tv_shopcart_total);
        btnCheckOut = view.findViewById(R.id.btn_check_out);
        llDelete = view.findViewById(R.id.ll_delete);
        cbAll = view.findViewById(R.id.cb_all);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnCollection = view.findViewById(R.id.btn_collection);
        ivEmpty = view.findViewById(R.id.iv_empty);
        tvEmptyCartTobuy = view.findViewById(R.id.tv_empty_cart_tobuy);
        ll_empty_shopcart = view.findViewById(R.id.ll_empty_shopcart);
        tvEmptyCartTobuy.setClickable(true);
    }


}

