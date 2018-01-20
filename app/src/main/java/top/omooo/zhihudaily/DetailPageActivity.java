package top.omooo.zhihudaily;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import top.omooo.zhihudaily.bean.DetailsArticleBean;
import top.omooo.zhihudaily.utils.CutJson;
import top.omooo.zhihudaily.utils.OkHttpUtils;
import top.omooo.zhihudaily.utils.OnNetResultListener;

/**
 * Created by Omooo on 2018/1/19.
 */

public class DetailPageActivity extends AppCompatActivity implements View.OnClickListener{
    private android.support.v7.widget.Toolbar mToolbar;
    private ImageView mImageBack, mImageShare, mImageFav;
    private Button mButtonComments, mButtonPrise;

    private SimpleDraweeView mImageContent;
    private TextView mTextContent;
    private static final String DETAILS_ARTICLE_URL = "https://news-at.zhihu.com/api/4/news/";

    private OkHttpUtils mOkHttpUtils;
    private List<DetailsArticleBean> mDetailsArticleBeans;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_page);

        initView();
        getContent();
    }

    /**
     * 添加数据
     */
    private void getContent() {
        Intent intent = getIntent();
        int articleId = Integer.parseInt(intent.getStringExtra("articleId"));
        Toast.makeText(this, "articleID :" + articleId, Toast.LENGTH_SHORT).show();
        String url = DETAILS_ARTICLE_URL + articleId;
        mOkHttpUtils = OkHttpUtils.getInstance();
        mOkHttpUtils.startGet(url, new OnNetResultListener() {
            @Override
            public void onSuccessListener(String result) {
                mDetailsArticleBeans = new ArrayList<>();
                mDetailsArticleBeans = new CutJson().getDataFromJson(result, 2);
                mImageContent.setImageURI(Uri.parse(mDetailsArticleBeans.get(0).getImageUrl()));
                String html = mDetailsArticleBeans.get(0).getArticleBody();
                mTextContent.setText(Html.fromHtml(html));

            }

            @Override
            public void onFailureListener(String result) {

            }
        });
    }

    private void initView() {
        mToolbar = findViewById(R.id.details_page_toolbar);
        setSupportActionBar(mToolbar);
        //去掉默认显示的应用名
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mImageBack = findViewById(R.id.toolbar_backImage);
        mImageShare = findViewById(R.id.toolbar_share);
        mImageFav = findViewById(R.id.toolbar_fav);
        mButtonComments = findViewById(R.id.toolbar_comments);
        mButtonPrise = findViewById(R.id.toolbar_prise);

        mImageBack.setOnClickListener(this);
        mImageShare.setOnClickListener(this);
        mImageFav.setOnClickListener(this);
        mButtonComments.setOnClickListener(this);
        mButtonPrise.setOnClickListener(this);

        mImageContent = findViewById(R.id.details_page_imageView);
        mTextContent = findViewById(R.id.details_page_textView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_backImage:
                onBackPressed();
                break;
            case R.id.toolbar_share:
                shareArticle();
                break;
            case R.id.toolbar_fav:
                favArticle();
                break;
            case R.id.toolbar_comments:
                seeComments();
                break;
            case R.id.toolbar_prise:
                addPrise();
                break;
        }
    }

    //按下返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //分享按钮
    private void shareArticle() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://omooo.top");
        startActivity(Intent.createChooser(intent, "分享到..."));
    }

    //收藏文章
    private void favArticle() {

    }

    //查看评论
    private void seeComments() {

    }

    //点赞
    private void addPrise() {

    }
}
