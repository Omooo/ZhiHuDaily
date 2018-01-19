package top.omooo.zhihudaily.bean;

/**
 * Created by Omooo on 2018/1/19.
 */

public class CustomBean {
    private String imageUrl;
    private String title;
    private int id;


    /**
     * 通用数据类
     * @param imageUrl
     * @param title
     * @param id
     */
    public CustomBean(String imageUrl, String title, int id) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
