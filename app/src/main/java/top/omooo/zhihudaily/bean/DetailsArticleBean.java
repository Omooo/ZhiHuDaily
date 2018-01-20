package top.omooo.zhihudaily.bean;

/**
 * Created by Omooo on 2018/1/20.
 */

/**
 * 文章详情页数据类
 */
public class DetailsArticleBean {
    private String articleBody;
    private String imageSource;
    private String title;
    private String imageUrl;

    public DetailsArticleBean(String articleBody, String imageSource, String title, String imageUrl) {
        this.articleBody = articleBody;
        this.imageSource = imageSource;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
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
