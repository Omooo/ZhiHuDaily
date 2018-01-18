package top.omooo.zhihudaily.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by Omooo on 2018/1/18.
 */

public class CycleViewPage extends FrameLayout implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private static final String TAG = "CycleViewPage";
    private TextView mTitle;    //标题
    private ViewPager mViewPager;   //轮播图
    private LinearLayout mIndicatorLayout;  //指示器
    private Handler mHandler;   //计时切换
    private int WHEEL = 100;
    private int WHEEL_WAIT = 101;
    //需要轮播的View，数量为轮播图数量加二
    private List<View> mViews = new ArrayList<>();
    //指示器圆点
    private ImageView[] mIndicators;
    //滚动框是否滚动
    private boolean isScrolling = false;
    //是否循环
    private boolean isCycle = true;
    //是否轮播
    private boolean isWheel = true;
    //默认轮播时间
    private int delay = 3000;
    //轮播当前位置
    private int mCurrentPosition = 0;
    //手指松开、页面不滚动时间，防止手指松开后短时间进行切换
    private long releaseTime = 0;
    public CycleViewPage(@NonNull Context context) {
        super(context);
    }

    public CycleViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CycleViewPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
