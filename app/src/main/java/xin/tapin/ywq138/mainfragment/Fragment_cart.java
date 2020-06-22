package xin.tapin.ywq138.mainfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import xin.tapin.ywq138.Adapter.CartAdapter;
import xin.tapin.ywq138.DB.MySQLite;
import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.R;
import xin.tapin.ywq138.bean.ShopItem;
import xin.tapin.ywq138.pay.AuthResult;
import xin.tapin.ywq138.pay.OrderInfoUtil2_0;
import xin.tapin.ywq138.pay.PayResult;

import static xin.tapin.ywq138.pay.PayDemoActivity.APPID;
import static xin.tapin.ywq138.pay.PayDemoActivity.RSA2_PRIVATE;
import static xin.tapin.ywq138.pay.PayDemoActivity.RSA_PRIVATE;

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
    private List<ShopItem> checkBuy;
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static final String APPID = "2016102400748818";

    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static final String PID = "";

    /**
     * 用于支付宝账户登录授权业务的入参 target_id。
     */
    public static final String TARGET_ID = "";

    /**
     *  pkcs8 格式的商户私钥。
     *
     * 	如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 	使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * 	RSA2_PRIVATE。
     *
     * 	建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 	工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDGHXTOWG72Pp9gM9y3vJYZJrhSRRNvFtYvz5OWSydWjEI3DSn3xY59PHm7EDM9le0cShGyaxSF3C5mSyPeLdXRqhSb6wp7RcY53YVxtjchL74sDN0kxOHCGGnfj6qmdC2Lxwtsv+GhJiMv05ILfWtE2zT7qIb6+p1mn5BU0CS+lh7cHJEKhBA/0yxqFQSoewSUkeyVuotVuCWHrLxXEO3QLI7J2IQE10LQwiVh8S3la5MSJLshB7fGkcp48KPj9sH2RoMpTdfTjnz+Xiwm8lAEzg36k/3S+3C+zpLS6IUUJ+QQ68vMQwaCNiFO8vpy1VeBwESoUm4wGL6Jddq6FE25AgMBAAECggEBALYPZ8IgnogIcGn1/wWSdtVSMdzYwc06zUZOmbBqDBKK/mac4E5j7FfGdY+kiZI07xsqLi0qHkgNWU6ECsgokvAEPsAyNQWnz0xp+DHRY8RJnZfZkngxvc2ipdjeq9IfvaNQHX2or+5pn7hZ00Jf1W4HjB8gdjq6iF9Aj2CBjuLcb+YZi0p8uBCL07ooa6t8tZq9Rc1IdD+ei581Re7TaoUYVyFqt0cvpvFGFmbfXieOa1s89ZwPrY5HA6OoF44b71lt5UkGPC2e2mhe+k9hVZGyFcZ4oS64sQAGIPEeIMXFxWBUwKDUq2EmTa0ZqWnFQWh28VH3sUFOM0cSUVchCekCgYEA6rmi3PqtQUXblD1DekZpjjzeAnh097cxE/9soFumWOe7HNeqMAYZN4Kxemb6sBgoz6Jgmx7vC5+qb/2yPX6X6lNhFkpZOzcf3zmo820e2JOUmgo8yzaQcQy98BcgWalnB+7WfYXPywvsow1n44+VNfdSNt2toMgigQ5oG9sGo2cCgYEA2BJZzO/De5CPUx51jHDXhW0fU4c7JcPWJHUKyvBMpJCyjBSAXOEerZL0koshC2vVVKYhcuZvpqkKFOdhrwweDDGyKMnbd+CkaZcz9vmnyGVZfPKmuG7TcnXR5FMIXzlzsnWS9aaaWrsMCs1gLly7PfQMd934sChH5rpW1GHI8d8CgYEAvfZ0zyB0/SF7PrKCHMBFhx7NkfRIvX3d01BhajoiG0uXjBUiH/GfkORNnEEhW0iCJuBEDOZxEdgrSK1qBgihC7xlE3QiWSEYw0DX8gH6984gcrYcU14acdOdGGSvAsFtp+bsYfPconhJEAC2dl1qpZ0+RTcM4NV3zziImxqcebsCgYBwbPv0ujyIqqsooIyhSoWZkzHdkFuiRsfBqHS5K1d1uSRt5qzzpt8DiZdgOKw0+SLDLL3yvxwRJ5trTQlyv2dTCPieaImdUnG5z0bmlvhKORHbBZbiGChFQjC4EMwmYApnLO4Oi2V9GB4n8Ly+4tk6XyWtqP2hCxR+ZS6Qy2B3GQKBgQCitEphwVZgpge98Pc03k23XUEL08SqhHQu4cS3VXZraEQR38qStuYwYZ9rOXtnFgzeP7W89bAlON4J3lfe4eFp+AswiCmghpTTApBzNyd8wSNIx3+LmPwG/Ztk58onBROn794LvDOhm+OsTjqBmGUqLWUPsgXBEAYLQMMmmqSw0g==";
    public static final String RSA_PRIVATE = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDGHXTOWG72Pp9gM9y3vJYZJrhSRRNvFtYvz5OWSydWjEI3DSn3xY59PHm7EDM9le0cShGyaxSF3C5mSyPeLdXRqhSb6wp7RcY53YVxtjchL74sDN0kxOHCGGnfj6qmdC2Lxwtsv+GhJiMv05ILfWtE2zT7qIb6+p1mn5BU0CS+lh7cHJEKhBA/0yxqFQSoewSUkeyVuotVuCWHrLxXEO3QLI7J2IQE10LQwiVh8S3la5MSJLshB7fGkcp48KPj9sH2RoMpTdfTjnz+Xiwm8lAEzg36k/3S+3C+zpLS6IUUJ+QQ68vMQwaCNiFO8vpy1VeBwESoUm4wGL6Jddq6FE25AgMBAAECggEBALYPZ8IgnogIcGn1/wWSdtVSMdzYwc06zUZOmbBqDBKK/mac4E5j7FfGdY+kiZI07xsqLi0qHkgNWU6ECsgokvAEPsAyNQWnz0xp+DHRY8RJnZfZkngxvc2ipdjeq9IfvaNQHX2or+5pn7hZ00Jf1W4HjB8gdjq6iF9Aj2CBjuLcb+YZi0p8uBCL07ooa6t8tZq9Rc1IdD+ei581Re7TaoUYVyFqt0cvpvFGFmbfXieOa1s89ZwPrY5HA6OoF44b71lt5UkGPC2e2mhe+k9hVZGyFcZ4oS64sQAGIPEeIMXFxWBUwKDUq2EmTa0ZqWnFQWh28VH3sUFOM0cSUVchCekCgYEA6rmi3PqtQUXblD1DekZpjjzeAnh097cxE/9soFumWOe7HNeqMAYZN4Kxemb6sBgoz6Jgmx7vC5+qb/2yPX6X6lNhFkpZOzcf3zmo820e2JOUmgo8yzaQcQy98BcgWalnB+7WfYXPywvsow1n44+VNfdSNt2toMgigQ5oG9sGo2cCgYEA2BJZzO/De5CPUx51jHDXhW0fU4c7JcPWJHUKyvBMpJCyjBSAXOEerZL0koshC2vVVKYhcuZvpqkKFOdhrwweDDGyKMnbd+CkaZcz9vmnyGVZfPKmuG7TcnXR5FMIXzlzsnWS9aaaWrsMCs1gLly7PfQMd934sChH5rpW1GHI8d8CgYEAvfZ0zyB0/SF7PrKCHMBFhx7NkfRIvX3d01BhajoiG0uXjBUiH/GfkORNnEEhW0iCJuBEDOZxEdgrSK1qBgihC7xlE3QiWSEYw0DX8gH6984gcrYcU14acdOdGGSvAsFtp+bsYfPconhJEAC2dl1qpZ0+RTcM4NV3zziImxqcebsCgYBwbPv0ujyIqqsooIyhSoWZkzHdkFuiRsfBqHS5K1d1uSRt5qzzpt8DiZdgOKw0+SLDLL3yvxwRJ5trTQlyv2dTCPieaImdUnG5z0bmlvhKORHbBZbiGChFQjC4EMwmYApnLO4Oi2V9GB4n8Ly+4tk6XyWtqP2hCxR+ZS6Qy2B3GQKBgQCitEphwVZgpge98Pc03k23XUEL08SqhHQu4cS3VXZraEQR38qStuYwYZ9rOXtnFgzeP7W89bAlON4J3lfe4eFp+AswiCmghpTTApBzNyd8wSNIx3+LmPwG/Ztk58onBROn794LvDOhm+OsTjqBmGUqLWUPsgXBEAYLQMMmmqSw0g==";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private static final int ACTION_EDIT = 0;

    private static final int ACTION_COMPLETE = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    Map<String, String> result = (Map<String, String>) msg.obj;
                    PayResult payResult = new PayResult(result);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功

                    if (TextUtils.equals(resultStatus, "9000")) {
                        showToast("支付成功！请查看订单");
                        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                        for (ShopItem shopItem:
                             checkBuy) {
                            writableDatabase.execSQL("delete from cartinfo where itemID=?", new Object[]{shopItem.getItemID()});
//                            Log.i("TAG", "handleMessage: " + shopItem.toString());
                        }
                        writableDatabase.close();
                        showData();
                    } else {
                        //刷新数据---显示界面
                        showData();
                        showToast("支付失败！！！请重新支付");
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        showAlert(PayDemoActivity.this, getString(R.string.pay_failed) + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
//                        showAlert(PayDemoActivity.this, "getString(R.string.auth_success) "+ authResult);
                    } else {
                        // 其他状态值则为授权失败
//                        showAlert(PayDemoActivity.this, "getString(R.string.auth_failed) "+ authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
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
    private void showToast(String s) {
        Toast.makeText(mainActivity, s, Toast.LENGTH_SHORT).show();
    }
    private void initData() {
        initListener();
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        tvShopcartEdit.setTag(ACTION_EDIT);
        tvShopcartEdit.setText("编辑");
        llCheckAll.setVisibility(View.VISIBLE);
        showData();
    }
    public void payV2(View v) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            showAlert(this, getString(R.string.error_missing_appid_rsa_private));
            return;
        }

        /*
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo 的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,adapter.getTotal());
//        Log.i("TAG", "payV2: " + adapter.getTotal());
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mainActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    private void initListener() {
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ShopItem> data = adapter.getData();
                checkBuy = new ArrayList<>();
                for (ShopItem shopItem:
                     data) {
                    if(shopItem.isSelected()){
                        checkBuy.add(shopItem);
                    }
                }
                if(checkBuy.size() == 0){
                    Toast.makeText(mainActivity, "请选择商品", Toast.LENGTH_SHORT).show();
                }else {
                    payV2(v);
                }

                }


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

