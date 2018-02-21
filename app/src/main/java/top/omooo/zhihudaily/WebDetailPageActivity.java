package top.omooo.zhihudaily;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

/**
 * Created by Omooo on 2018/2/10.
 */

public class WebDetailPageActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private WebView mWebView;
    private String webUrl = "http://daily.zhihu.com/story/";
    private Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_page_web);

        initView();
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.write));

    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar_detail_web);

        mFrameLayout = findViewById(R.id.web_frame);
        mWebView = new WebView(this);
        WebSettings settings = mWebView.getSettings();
        //让WebView支持DOM storage API
        settings.setDomStorageEnabled(true);
        //解决图片加载
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        //设置WebView缓存模式，默认断网情况下不缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //断网情况下加载本地缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //让WebView支持缩放
        settings.setSupportZoom(true);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        mFrameLayout.addView(mWebView);
        Intent intent = getIntent();
        String url = webUrl + intent.getStringExtra("articleId");
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
