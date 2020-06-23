package xin.tapin.ywq138.bean;

import java.util.List;

public class ShopOrder {
    private Integer id;//数据库id
    private String name;//姓名
    private String phoneNumber;//电话
    private String address;//地址
    private List<ShopItem> shopItems;//已购买的商品
    private Double total;//总价

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ShopOrder(String name, String phoneNumber, String address, List<ShopItem> shopItems, Double total) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.shopItems = shopItems;
        this.total = total;
    }
}
