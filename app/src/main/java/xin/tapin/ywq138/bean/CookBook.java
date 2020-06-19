package xin.tapin.ywq138.bean;


import xin.tapin.ywq138.utils.Constants;

/**
 * 食谱类
 */
public class CookBook {
    private String url ;//食谱地址，直接获取
    private String title ;//直接获取
    private String imgUrl ;//直接获取
    private String message ;//食谱详情页图片下面的一段信息
    private String mainIngredient ;//主料
    private String auxiliaryIngredient ;//辅料
    private String seasoning ;//调料
    private String recipeStep ;//步骤

    public String getRecipeStep() {
        return recipeStep;
    }

    public void setRecipeStep(String recipeStep) {
        this.recipeStep = recipeStep;
    }

    public CookBook(String url, String title, String imgUrl, String message, String mainIngredient, String auxiliaryIngredient, String seasoning, String recipeStep) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.message = message;
        this.mainIngredient = mainIngredient;
        this.auxiliaryIngredient = auxiliaryIngredient;
        this.seasoning = seasoning;
        this.recipeStep = recipeStep;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public String getAuxiliaryIngredient() {
        return auxiliaryIngredient;
    }

    public void setAuxiliaryIngredient(String auxiliaryIngredient) {
        this.auxiliaryIngredient = auxiliaryIngredient;
    }

    public String getSeasoning() {
        return seasoning;
    }

    public void setSeasoning(String seasoning) {
        this.seasoning = seasoning;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public CookBook(String url, String title, String imgUrl) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
    }
}
