package top.omooo.zhihudaily.bean;

/**
 * Created by Omooo on 2018/1/19.
 */

public class RecycleItemInfo {
    private String title;
    private String imageUrl;

    public RecycleItemInfo(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
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
