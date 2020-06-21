package xin.tapin.ywq138.jsoup;



import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xin.tapin.ywq138.base.MyApplication;
import xin.tapin.ywq138.bean.CookBook;
import xin.tapin.ywq138.bean.OldCookBook;
import xin.tapin.ywq138.bean.ShopItem;
import xin.tapin.ywq138.utils.Constants;

/**
 * 数据获取类
 */
public class MyJsoup {

    /**
     * 新版本 获取食谱
     * 该方法能获取该APP内所有功能所需数据
     * 获取数据不保证一直都能用（只要原网站布局或者class之类属性修改，就不能很好的获取数据了）
     * 以防不能用  测试完成有录制视频已被不时之需
     * @param url
     * @param search
     * @return
     * @throws IOException
     */
    public List<CookBook> getShiPu(String url, Integer search) throws IOException {

        List<CookBook> data = new ArrayList<>();//返回数据

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .get();
//        Elements next =  doc.getElementsByTag("a");
        Elements elements ;
        if(search != null){//搜索方式样式不一样
            elements = doc.select("div[class=\"ui_list_1\"]").select("ul").select("li");
        }else{
            elements = doc.select("div[class=\"ui_newlist_1 get_num\"]").select("ul").select("li");
        }
        for (Element element:
             elements) {
            Elements a = element.select("div[class=\"pic\"]").select("a");
            String title = a.attr("title");
            if (TextUtils.isEmpty(title)){
                title = element.select("div[class=\"detail\"]").select("h4").text();
            }
            String href = a.attr("href");

            Document doc2 = Jsoup.connect(href)//二次获取食谱的材料和做法
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .get();
            Elements recipDetail = doc2.select("div[class=\"recipDetail\"]");
            Elements recipeStepE = doc2.select("div[class=\"recipeStep\"]").select("ul").select("li");
            int i = 1;
            String recipeStep = "";
            for (Element element1:
                 recipeStepE) {
                String text = element1.select("div[class=\"recipeStep_num\"]").text();
                String replace = recipeStepE.get(i-1).child(1).text();
                recipeStep += replace.replace(text,(i++)+"、")+"\n";
//                Log.i("TAG", "getShiPu: "+step+"\n");
            }
//            Log.i("TAG", "getShiPu: "+recipeStep);
            String message = doc2.select("div[id=\"block_txt1\"]").text();//食谱详情页图片下面的一段信息
            String mainIngredient = "";
            String auxiliaryIngredient = "";
            String seasoning = "";
//            Log.i("TAG", "getShiPu: "+message);
            Elements particulars = doc2.select("fieldset[class=\"particulars\"]");//获取食材
            //新方式  适用大部分获取食材界面布局
            if(particulars.size() == 3){
                String text = particulars.get(0).text();
                mainIngredient = text.replace("主料","");
                String text2 = particulars.get(1).text();
                auxiliaryIngredient = text2.replace("辅料","");
                String text3 = particulars.get(2).text();
                seasoning = text3.replace("调料","");
            }else if(particulars.size() == 2){
                String text = particulars.get(0).text();
                mainIngredient = text.replace("主料","");
                String text2 = particulars.get(1).text();
                auxiliaryIngredient = text2.replace("辅料","");
            }else if(particulars.size() == 1){
                String text = particulars.get(0).text();
                mainIngredient = text.replace("主料","");
            }
            //新方式  适用大部分获取食材界面布局

            //旧方式 可能存在不能完美获取食谱材料
           /* for (Element element1:
                particulars) {
                String text = element1.text();
                if (text.contains("主料") || text.contains("主面团")){//主料、主面团
                    mainIngredient = text.replace("主料","");
//                    Log.i("TAG", "getShiPu: "+mainIngredient+"\n");
                }else if (text.contains("辅料") || text.contains("表面酥粒")){//辅料、表面酥粒
                    auxiliaryIngredient = text.replace("辅料","");
//                    Log.i("TAG", "getShiPu: "+auxiliaryIngredient+"\n");
                }else if (text.contains("调料") || text.contains("配料") || text.contains("奶油内馅")){//调料、配料、奶油内馅
                    seasoning = text.replace("调料","");
//                    Log.i("TAG", "getShiPu: "+seasoning+"\n");
                }
//                Log.i("TAG", "getShiPu: "+text+"\n");
            }*/
            //旧方式 可能存在不能完美获取食谱材料
            String img = recipDetail.select("div[class=\"recipe_De_imgBox\"]").select("a[class=\"J_photo\"]").select("img").attr("src");
            data.add(new CookBook(href,title,img,message,mainIngredient,auxiliaryIngredient,seasoning,recipeStep));
//            Log.i("TAG", "getShiPu: "+title+"\n"+href+"\n"+img);
        }


        Elements pageNum = doc.select("div[class=\"ui-page-inner\"]");//获取页数
        //设置默认上一页和下一页
        MyApplication.setUpPage(null);
        MyApplication.setNextPage(null);

        if(search == null){
            if(pageNum != null){//获取并设置上一页 下一页参数ui-page mt20
                Elements a = pageNum.select("a");
                if(a.text().contains("上一页")){
                    MyApplication.setUpPage(a.select("a").attr("href"));
//                Log.i("TAG", "getUpPage: "+MyApplication.getUpPage());
                }
                if (a.last().text().contains("下一页")){
                    MyApplication.setNextPage(a.select("a").last().attr("href"));
//                Log.i("TAG", "getNextPage: "+MyApplication.getNextPage());
                }
            }
        }else{//搜索布局界面不一样
            Elements select = doc.select("div[class=\"ui-page mt20\"]").select("div[class=\"ui-page-inner\"]");
            if(select != null){//获取并设置上一页 下一页参数
                Elements a = select.select("a");
                String attr = a.select("a").last().attr("href");
                String attr1 = a.select("a").attr("href");
                if(a.text().contains("上一页")){
                    MyApplication.setUpPage(attr1.substring(0,attr1.length()-1));
//                    Log.i("TAG", "getUpPage: "+MyApplication.getUpPage());
                }
                if (a.last().text().contains("下一页")){
                    MyApplication.setNextPage(attr.substring(0,attr.length()-1));
//                    Log.i("TAG", "getNextPage: "+MyApplication.getNextPage());
                }
            }
        }


        return data;
    }

    /**
     * 旧版本 获取食谱
     * 该方法能获取该APP内所有功能所需数据
     * 获取数据不保证一直都能用（只要原网站布局或者class之类属性修改，就不能很好的获取数据了）
     * 以防不能用  测试完成有录制视频已被不时之需
     * @param url
     * @param page
     * @return
     * @throws IOException
     */
    public List<OldCookBook> getOldShiPu(String url, Integer page, List<OldCookBook> nowCookBook) throws IOException {
        List<OldCookBook> data = new ArrayList<>();//返回数据
        String url2 = url;//这个url2才是获取数据的url  传入的数据可能需要换页显示
        if(page != null && page != 1){//判断是否需要换页----需换页拼接url赋值给url2
            url2 = url + "/?page="+page;
        }

        Document doc = Jsoup.connect(url2)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .get();
//        Elements next =  doc.getElementsByTag("a");
        Elements elements = null;

        if (Constants.XIN_SHI_PU_URL.equals(url)){//甜点的布局样式属性不一样
            elements =  doc.select("a[class=\"no-overflow\"]");
        }else{//默认这个格式可以通用
            elements =  doc.select("a[class=\"shipu\"]");
        }

        //获取最大页数
        Elements maxPageDIV = doc.select("div[class=\"page-num fl\"]");
//        Log.i("TAG", "getShiPu: "+maxPageDIV.text());
        String pageText = maxPageDIV.select("ul[class=\"clearfix\"]").text();
        MyApplication.setMaxPage(1);
        if(!TextUtils.isEmpty(pageText)){
            String[] s = pageText.split(" ");//获取到的是有格式的字符串
//            Log.i("TAG", "getShiPu: "+s[s.length-1]);
            int maxPage = Integer.parseInt(s[s.length-1]);
            MyApplication.setMaxPage(maxPage);
//            Log.i("TAG", "getShiPu: "+maxPage);
        }


        for (Element element:
                elements) {
            String href = element.attr("href");//食谱地址
            String title = element.attr("title");//食谱标题
            String img = element.select("img").attr("src");//食谱图片地址
            String material = "";//食谱所需材料
            String practice = "";//食谱做法
            //二次获取食谱  材料、做法
            Document doc2 = Jsoup.connect(Constants.BASE_URL+href)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                    .get();
            Elements elements2 = doc2.select("div[class=\"dl clearfix mt20\"]");
            for (Element element2:
                    elements2) {

//                Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dt cg2\"]").toString());

                if(element2.select("div[class=\"dt cg2\"]").toString().contains("材料")){
                    material = element2.select("div[class=\"dd\"]").text();
                    if (material == ""){//甜点部分div的class不一样
                        material = element2.select("div[class=\"dd food-material\"]").text();
                    }
//                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").text());
                    //                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").toString());
                }
                if(element2.select("div[class=\"dt cg2\"]").toString().contains("做法")){
                    practice = element2.select("div[class=\"dd\"]").text();
//                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").text());
//                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").toString());
                }
            }
            data.add(new OldCookBook(href,title,img,material,practice));
//            Log.i("TAG", "getXinShiPu: "+element.select("img").attr("src"));
        }


        return data;
    }

    /**
     * 获取商品   无法很好的控制页数  但是可以换页
     * 该方法能获取该APP内所有功能所需数据
     * 获取数据不保证一直都能用（只要原网站布局或者class之类属性修改，就不能很好的获取数据了）
     * 以防不能用  测试完成有录制视频已被不时之需
     * @param url
     * @return
     * @throws IOException
     */
    public List<ShopItem> getShopItem(String url) throws IOException {
        List<ShopItem> data = new ArrayList<>();//返回数据


        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .get();

        Elements gl_item =  doc.select("li[class=\"gl-item\"]");//获取每一项

        for (Element element:
             gl_item) {
            Elements p_img = element.select("div[class=\"p-img\"]");//图片
            Elements a = p_img.select("a");//图片下a标签
            String shopUrl = "https:" + a.attr("href");//商品链接  //item.jd.com/64592930772.html
            String imageURL = "https:" + a.select("img").attr("src");//图片地址  //img12.360buyimg.com/n7/jfs/t1/111716/31/2362/220510/5ea16f9cE06d5ae43/ccf221007893e1b7.jpg
            String price = element.select("div[class=\"gl-i-wrap\"]").select("strong").select("i").text();//价格
            String name = element.select("div[class=\"p-name p-name-type-2\"]").text();//商品名
            data.add(new ShopItem(shopUrl,imageURL,name,Double.parseDouble(price)));
//            Log.i("TAG", "getShopItem: "+shopUrl+"\n"+imageURL+"\n"+price+"\n"+name);
        }

        return data;
    }

    public String getShop_info(String url) throws IOException {
        String info = "";

        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
                .get();
        Elements detail_info_wrap =  doc.select("div[class=\"tab-con\"]").select("ul[class=\"parameter2 p-parameter-list\"]").select("li");//商品介绍
        for (Element element:
             detail_info_wrap) {
            info += element.text() + "\n";
        }
//        Log.i("TAG", "getShop_info: "+info);
        return info;
    }

    /**
     * 获取食谱 旧方法只能提取最新食谱
     * @return
     * @throws IOException
     *//*
    public List<CookBook> getXinShiPu() throws IOException {
        List<CookBook> data = new ArrayList<>();
        Document doc = Jsoup.connect(Constants.XIN_SHI_PU_URL).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
//        Elements next =  doc.getElementsByTag("a");
        int i = 0;
        Elements elements =  doc.select("a[class=\"no-overflow\"]");
        for (Element element:
             elements) {
            String href = element.attr("href");
            String title = element.attr("title");
            String img = element.select("img").attr("src");
            String material = "";//材料
            String practice = "";//做法
            Document doc2 = Jsoup.connect(Constants.BASE_URL+href).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
            Elements elements2 = doc2.select("div[class=\"dl clearfix mt20\"]");
            for (Element element2:
                 elements2) {

//                Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dt cg2\"]").toString());

                if(element2.select("div[class=\"dt cg2\"]").toString().contains("材料")){
                    material = element2.select("div[class=\"dd\"]").text();
//                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").text());
                    //                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").toString());
                }
                if(element2.select("div[class=\"dt cg2\"]").toString().contains("做法")){
                    practice = element2.select("div[class=\"dd\"]").text();
//                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").text());
//                    Log.i("TAG", "getXinShiPu: "+element2.select("div[class=\"dd\"]").toString());
                }
            }
            data.add(new CookBook(href,title,img,material,practice));
//            Log.i("TAG", "getXinShiPu: "+element.select("img").attr("src"));
            i++;
            if(i >= 8){
                break;
            }
        }
        return data;
    }*/


}
