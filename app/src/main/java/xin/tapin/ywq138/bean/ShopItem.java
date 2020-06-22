package xin.tapin.ywq138.bean;



/**
 * 商品类
 */
public class ShopItem {
    private String itemID;//辅助存储购物车商品
    private int number;//购买数量
    private boolean selected;//选中购买
    private String url ;//地址
    private String imageURL ;//图片地址
    private String name ;//商品名
    private Double price ;//价格

    @Override
    public String toString() {
        return "ShopItem{" +
                "itemID='" + itemID + '\'' +
                ", number=" + number +
                ", selected=" + selected +
                ", url='" + url + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    public ShopItem(String itemID, int number, boolean selected, String url, String imageURL, String name, Double price) {
        this.itemID = itemID;
        this.number = number;
        this.selected = selected;
        this.url = url;
        this.imageURL = imageURL;
        this.name = name;
        this.price = price;
    }

    public ShopItem(String itemID, String url, String imageURL, String name, Double price) {
        this.itemID = itemID;
        this.url = url;
        this.imageURL = imageURL;
        this.name = name;
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ShopItem(String url, String imageURL, String name, Double price) {
        this.url = url;
        this.imageURL = imageURL;
        this.name = name;
        this.price = price;
    }
}
