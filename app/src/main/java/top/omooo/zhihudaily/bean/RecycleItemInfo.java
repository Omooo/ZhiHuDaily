package top.omooo.zhihudaily.bean;

/**
 * Created by Omooo on 2018/1/19.
 */

public class RecycleItemInfo {
    private String title;
    private String imageUrl;
    private int id;

    public RecycleItemInfo(String title, String imageUrl, int id) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
