package xin.tapin.ywq138.bean;


import xin.tapin.ywq138.utils.Constants;

/**
 * 旧版本食谱类
 */
public class OldCookBook {
    private String url ;//食谱地址，直接获取
    private String title ;//直接获取
    private String imgUrl ;//直接获取
    private String material;//材料，需要再次进入该类的url中获取数据
    private String practice;//做法，需要再次进入该类的url中获取数据

    public OldCookBook(String url, String title, String imgUrl, String material, String practice) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.material = material;
        this.practice = practice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = Constants.BASE_URL+url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public OldCookBook(String url, String title, String imgUrl) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
    }
}
