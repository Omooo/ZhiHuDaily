package top.omooo.zhihudaily.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import top.omooo.zhihudaily.bean.CustomBean;

/**
 * Created by Omooo on 2018/1/19.
 */

public class CutJson {
    private JSONObject mJSONObject;
    private JSONArray mJSONArray;
    private static final int TYPE_BANNER = 0;   //解析轮播图数据标识
    private static final int TYPE_RECYCLE_ITEM = 1; //解析RecycleView的Item内容标识

    public List<CustomBean> getDataFromJson(String json, int type) {
        List<CustomBean> customBeans = new ArrayList<>();
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customBeans;
    }
}
