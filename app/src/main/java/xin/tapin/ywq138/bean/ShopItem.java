package xin.tapin.ywq138.bean;



/**
 * 商品类
 */
public class ShopItem {
    private String url ;//地址
    private String imageURL ;//图片地址
    private String name ;//商品名
    private Double price ;//价格

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
