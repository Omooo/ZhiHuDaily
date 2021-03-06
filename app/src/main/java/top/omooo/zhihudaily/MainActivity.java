package top.omooo.zhihudaily;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import top.omooo.zhihudaily.adapter.RecycleAdapter;
import top.omooo.zhihudaily.bean.CustomBean;
import top.omooo.zhihudaily.bean.RecycleItemInfo;
import top.omooo.zhihudaily.ui.FrescoImageLoader;
import top.omooo.zhihudaily.utils.AutoLinearLayoutManager;
import top.omooo.zhihudaily.utils.CutJson;
import top.omooo.zhihudaily.utils.OkHttpUtils;
import top.omooo.zhihudaily.utils.OnNetResultListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<String> list_path = new ArrayList<>();
    private ArrayList<String> list_title = new ArrayList<>();
    private ArrayList<Integer> list_id = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private List<RecycleItemInfo> mItemInfos = new ArrayList<>();
    private List<CustomBean> mCustomBeans = new ArrayList<>();
    private NavigationView mNavigationView;
    //获取Item内容以及轮播图内容的Url
    private static final String URL = "https://news-at.zhihu.com/api/4/news/latest";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //处理Item的点击事件
                switch (item.getItemId()) {
                    case R.id.item1:
                        Toast.makeText(MainActivity.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.write));
//        mToolbar.inflateMenu(R.menu.setting_menu);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        //Item的点击事件要放在 setSupportActionBar
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_setting_mode:
                        Toast.makeText(MainActivity.this, "切换模式", Toast.LENGTH_SHORT).show();
                        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                        getDelegate().setLocalNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO
                                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                        // 同样需要调用recreate方法使之生效
                        recreate();
                        break;
                    case R.id.item_setting_all:
                        Toast.makeText(MainActivity.this, "设置选项", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }

        });
        //添加轮播图
//        addBanner();
        //添加内容
//        addContent();
        getDataFromUrl();

        mNavigationView = findViewById(R.id.navigation_view);
    }

    public void getDataFromUrl() {
        OkHttpUtils.getInstance().startGet(URL, new OnNetResultListener() {
            @Override
            public void onSuccessListener(String result) {
                Log.i(TAG, result);
                CutJson cutJson = new CutJson();
                //给banner添加数据
                mCustomBeans = cutJson.getDataFromJson(result, 0);
                //测试
                Log.i(TAG, mCustomBeans.get(0).getImageUrl() + mCustomBeans.get(0).getTitle());
                for (int i = 0; i < mCustomBeans.size(); i++) {
                    list_path.add(mCustomBeans.get(i).getImageUrl());
                    list_title.add(mCustomBeans.get(i).getTitle());
                    list_id.add(mCustomBeans.get(i).getId());
                }
                addBanner(list_path, list_title,list_id);

                //给RecycleView添加数据
                mCustomBeans = cutJson.getDataFromJson(result, 1);
                for (int i = 0; i < mCustomBeans.size(); i++) {
                    mItemInfos.add(new RecycleItemInfo(mCustomBeans.get(i).getTitle(), mCustomBeans.get(i).getImageUrl(), mCustomBeans.get(i).getId()));
                }
                addContent(mItemInfos);
            }

            @Override
            public void onFailureListener(String result) {
                Toast.makeText(MainActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 轮播图
     * @param urlList   图片链接
     * @param titleList 标题
     */
    private void addBanner(ArrayList<String> urlList, ArrayList<String> titleList, final ArrayList<Integer> idList) {
        Banner banner = findViewById(R.id.banner);
        banner.setImageLoader(new FrescoImageLoader());

        banner.setImages(urlList)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        int articleId = idList.get(position);
                        Intent intent = new Intent(MainActivity.this, WebDetailPageActivity.class);
                        intent.putExtra("articleId", articleId + "");
                        startActivity(intent);
                    }
                })
                .setBannerTitles(titleList)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setBannerAnimation(Transformer.DepthPage)
                .isAutoPlay(true)
                .setIndicatorGravity(BannerConfig.CENTER)
                .setDelayTime(4000)
                .start();
    }

    /**
     * RecycleView每个Item的数据
     * @param itemInfos
     */
    private void addContent(final List<RecycleItemInfo> itemInfos) {
        mRecyclerView = findViewById(R.id.recycleview);

        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setLayoutManager(new AutoLinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        for (int i = 0; i < 10; i++) {
//            mItemInfos.add(new RecycleItemInfo(" i = " + i, "http://p3.so.qhmsg.com/bdr/_240_/t01ded66f320dd27558.jpg"));
//        }
        RecycleAdapter adapter = new RecycleAdapter(itemInfos, this);
        adapter.setOnItemClick(new RecycleAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void OnItemClick(int itemPosition) {
                int articleId = itemInfos.get(itemPosition).getId();
                Intent intent = new Intent(MainActivity.this, WebDetailPageActivity.class);
                intent.putExtra("articleId", articleId + "");
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }
    //ToolBar添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }
}
