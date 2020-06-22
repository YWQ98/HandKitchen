/*
package xin.tapin.ywq138.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import xin.tapin.ywq138.MainActivity;
import xin.tapin.ywq138.bean.ShopItem;


public class CartStorage {
    private static final String JSON_CART = "json_cart";
    public static  CartStorage cartStorage;
    private final Context context;
    private List<ShopItem> shopItemList;
    private static final String JSON_NUMBER = "json_number";

    private CartStorage(Context context){
        this.context = context;
        shopItemList = new ArrayList<>();

    }
    public void listToSparseArray(){
        List<ShopItem> goodsBeans = getAllData();
        for (int i = 0; i < goodsBeans.size(); i++) {
            ShopItem goodsBean = goodsBeans.get(i);
            shopItemList.add(goodsBean);
//            sparseArray.put(goodsBean.getItemID(),goodsBean);
        }
    }

    public List<ShopItem> getAllData() {
        List<ShopItem> goodsBeans = new ArrayList<>();
        String json = CacheUtils.getString(context,JSON_CART);
        if(!TextUtils.isEmpty(json)){
            goodsBeans = new Gson().fromJson(json, new TypeToken<List<ShopItem>>(){}.getType());
        }
        return goodsBeans;
    }

    public static CartStorage getInstance(MainActivity mainActivity){
        if(cartStorage == null){
            cartStorage = new CartStorage(mainActivity);
        }
        return cartStorage;
    }
    public void addData(ShopItem goodsBean){
        goodsBean.setSelected(true);
//        ShopItem temp = sparseArray.get(goodsBean.getItemID());
        ShopItem temp = null;
        ShopItem temp2 = null;
        for (int i = 0; i <shopItemList.size() ; i++) {
            if(shopItemList.get(i).getItemID() != goodsBean.getItemID()){
                temp = goodsBean;
                break;
            }else if(shopItemList.get(i).getItemID() == goodsBean.getItemID()){
                temp2 = goodsBean;
            }
        }
        if(temp != null){
            shopItemList.add(temp);
        }
        if(temp2 !=null){
            temp2.setNumber(temp2.getNumber() + 1);
        }
        save();
    }
    public void deleteData(ShopItem goodsBean){
        for (int i = 0; i < shopItemList.size(); i++) {
            if (shopItemList.get(i).getItemID() == goodsBean.getItemID()){
                shopItemList.remove(i);
                break;
            }
        }
        save();
    }


    public void updateData(ShopItem goodsBean){
        sparseArray.put(goodsBean.getItemID(),goodsBean);
        save();
    }


    private void save() {
        List<ShopItem> goodsBeans = sparseToList();
        String json = new Gson().toJson(goodsBeans);
        CacheUtils.saveString(context,JSON_CART,json);
    }

    public void saveData(List<ShopItem> goodsBeans){//保存没购买的商品
        String json = new Gson().toJson(goodsBeans);
        CacheUtils.saveString(context,JSON_CART,json);

        sparseArray = new SparseArray<>(100);//更新内存中的数据
        for (int i = 0; i < goodsBeans.size(); i++) {
            ShopItem goodsBean = goodsBeans.get(i);
            sparseArray.put(goodsBean.getItemID(),goodsBean);
        }
    }



    private List<ShopItem> sparseToList() {
        List<ShopItem> goodsBeans = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            ShopItem goodsBean = sparseArray.valueAt(i);
            goodsBeans.add(goodsBean);
        }
        return goodsBeans;
    }

    public ShopItem getGoodsBean(ShopItem goodsBean){
        return sparseArray.get(goodsBean.getItemID());
    }

//    public void saveCheck(List<ShopItem> checkData) {
//        String json = new Gson().toJson(checkData);
//        Log.i("TAG", "saveCheck: "+json);
//        CacheUtils.saveString(context,JSON_CHECK,json);
//    }
//
//    public List<ShopItem> getAllCheckData() {
//        List<ShopItem> goodsBeans = new ArrayList<>();
//        String json = CacheUtils.getString(context,JSON_CHECK);
//        if(!TextUtils.isEmpty(json)){
//            goodsBeans = new Gson().fromJson(json, new TypeToken<List<ShopItem>>(){}.getType());
//        }
//        return goodsBeans;
//    }
}
*/
