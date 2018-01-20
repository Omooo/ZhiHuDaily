package top.omooo.zhihudaily.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import top.omooo.zhihudaily.bean.CustomBean;
import top.omooo.zhihudaily.bean.DetailsArticleBean;

/**
 * Created by Omooo on 2018/1/19.
 */

public class CutJson {
    private JSONObject mJSONObject;
    private JSONArray mJSONArray;
    private static final int TYPE_BANNER = 0;   //解析轮播图数据标识
    private static final int TYPE_RECYCLE_ITEM = 1; //解析RecycleView的Item内容标识
    private static final int TYPE_DETAILS_ARTICLE = 2;  //解析详情页的文章内容

    public List getDataFromJson(String json, int type) {
        List<CustomBean> customBeans = new ArrayList<>();
        List<DetailsArticleBean> detailsArticleBeans = new ArrayList<>();
        try {
            mJSONObject = new JSONObject(json);
            if (type == TYPE_BANNER) {
                mJSONArray = mJSONObject.getJSONArray("top_stories");
                for (int i = 0; i < mJSONArray.length(); i++) {
                    JSONObject object = (JSONObject) mJSONArray.get(i);
                    String imageUrl = object.getString("image");
                    String title = object.getString("title");
                    int id = object.getInt("id");
                    customBeans.add(new CustomBean(imageUrl, title, id));
                }
                return customBeans;
            } else if (type == TYPE_RECYCLE_ITEM) {
                mJSONArray = mJSONObject.getJSONArray("stories");
                for (int i = 0; i < mJSONArray.length(); i++) {
                    JSONObject object = (JSONObject) mJSONArray.get(i);
                    JSONArray array = object.getJSONArray("images");
                    String imageUrl = array.get(0).toString();
                    String title = object.getString("title");
                    int id = object.getInt("id");
                    customBeans.add(new CustomBean(imageUrl, title, id));
                }
                return customBeans;
            } else if (type == TYPE_DETAILS_ARTICLE) {
                String articleBody = mJSONObject.getString("body");
                String imageSource = mJSONObject.getString("image_source");
                String title = mJSONObject.getString("title");
                String imageUrl = mJSONObject.getString("image");
                detailsArticleBeans.add(new DetailsArticleBean(articleBody, imageSource, title, imageUrl));
                return detailsArticleBeans;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
