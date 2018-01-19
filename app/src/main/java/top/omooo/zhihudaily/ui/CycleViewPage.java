package top.omooo.zhihudaily.ui;

import android.content.Context;
import android.icu.text.IDNA;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import top.omooo.zhihudaily.MainActivity;
import top.omooo.zhihudaily.R;
import top.omooo.zhihudaily.bean.BannerInfo;

/**
 * Created by Omooo on 2018/1/18.
 */

public class CycleViewPage extends FrameLayout implements ViewPager.OnPageChangeListener {

    private Context mContext;
    private static final String TAG = "CycleViewPage";
    private TextView mTitle;    //标题
    private ViewPager mViewPager;   //轮播图
    private LinearLayout mIndicatorLayout;  //指示器
    private android.os.Handler mHandler;   //计时切换
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
    private ViewPagerAdapter mAdapter;
    private ImageCycleViewListener mImageCycleViewListener;
    //数据集合
    private List<BannerInfo> infos;
    //指示器图片，被选中状态
    private int mIndicatorSelected;
    //指示器图片，未选中状态
    private int mIndicatorUnselected;
    final Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            if (mContext != null && isWheel) {
                long now = System.currentTimeMillis();
                if (now - releaseTime > delay - 500) {
                    mHandler.sendEmptyMessage(WHEEL);
                } else {
                    mHandler.sendEmptyMessage(WHEEL_WAIT);
                }
            }
        }
    };
    public CycleViewPage(@NonNull Context context) {
        this(context,null);
    }

    public CycleViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CycleViewPage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_banner, this, true);
        mViewPager = findViewById(R.id.cycle_view_pager);
        mTitle = findViewById(R.id.cycle_title);
        mIndicatorLayout = findViewById(R.id.cycle_indicator);
        mHandler=new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == WHEEL && mViews.size() > 0) {
                    if (isScrolling) {
                        int position = (mCurrentPosition + 1) % mViews.size();
                        mViewPager.setCurrentItem(position, true);
                    }
                    releaseTime = System.currentTimeMillis();
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, delay);
                    return;
                }
                if (msg.what == WHEEL_WAIT && mViews.size() > 0) {
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, delay);
                }
            }
        };
    }

    public void setIndicators(int select, int indicatorUnselected) {
        mIndicatorSelected = select;
        mIndicatorUnselected = indicatorUnselected;
    }

    public void setData(List<BannerInfo> list, ImageCycleViewListener listener) {
        setData(list, listener, 0);
    }
    public void setData(List<BannerInfo> list, ImageCycleViewListener listener,
                        int showPosition) {
        if (list == null || list.size() == 0) {
            //没有数据时隐藏整个布局
            this.setVisibility(View.GONE);
            return;
        }
        mViews.clear();
        infos = list;
        if (isCycle) {
            //添加轮播图View，数量为集合数+2
            // 将最后一个View添加进来
            mViews.add(getImageView(mContext, infos.get(infos.size() - 1).getUrl()));
            for (int i = 0; i < infos.size(); i++) {
                mViews.add(getImageView(mContext, infos.get(i).getUrl()));
            }
            // 将第一个View添加进来
            mViews.add(getImageView(mContext, infos.get(0).getUrl()));
        } else {
            //只添加对应数量的View
            for (int i = 0; i < infos.size(); i++) {
                mViews.add(getImageView(mContext, infos.get(i).getUrl()));
            }
        }
        if (mViews == null || mViews.size() == 0) {
            //没有View时隐藏整个布局
            this.setVisibility(View.GONE);
            return;
        }
        mImageCycleViewListener = listener;
        int ivSize = mViews.size();
        // 设置指示器
        mIndicators = new ImageView[ivSize];
        if (isCycle)
            mIndicators = new ImageView[ivSize - 2];
        mIndicatorLayout.removeAllViews();
        for (int i = 0; i < mIndicators.length; i++) {
            mIndicators[i] = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 0, 10, 0);
            mIndicators[i].setLayoutParams(lp);
            mIndicatorLayout.addView(mIndicators[i]);
        }
        mAdapter = new ViewPagerAdapter();
        // 默认指向第一项，下方viewPager.setCurrentItem将触发重新计算指示器指向
        setIndicator(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        if (showPosition < 0 || showPosition >= mViews.size())
            showPosition = 0;
        if (isCycle) {
            showPosition = showPosition + 1;
        }
        mViewPager.setCurrentItem(showPosition);
        setWheel(true);//设置轮播
    }

    /**
     * 设置指示器
     *
     * @param selectedPosition 默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        setText(mTitle, infos.get(selectedPosition).getTitle());
        try {
            for (int i = 0; i < mIndicators.length; i++) {
                mIndicators[i]
                        .setBackgroundResource(mIndicatorUnselected);
            }
            if (mIndicators.length > selectedPosition)
                mIndicators[selectedPosition]
                        .setBackgroundResource(mIndicatorSelected);
        } catch (Exception e) {
            Log.i(TAG, "指示器路径不正确");
        }
    }
    private View getImageView(Context context, String url) {
        return getImageView(context, url);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int max = mViews.size() - 1;
        int position1 = position;
        mCurrentPosition = position;
        if (isCycle) {
            if (position == 0) {
                //滚动到mView的1个（界面上的最后一个），将mCurrentPosition设置为max - 1
                mCurrentPosition = max - 1;
            } else if (position == max) {
                //滚动到mView的最后一个（界面上的第一个），将mCurrentPosition设置为1
                mCurrentPosition = 1;
            }
            position1 = mCurrentPosition - 1;
        }
        setIndicator(position1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (state == 0) { // viewPager滚动结束

            releaseTime = System.currentTimeMillis();
            //跳转到第mCurrentPosition个页面（没有动画效果，实际效果页面上没变化）
            mViewPager.setCurrentItem(mCurrentPosition, false);
        }
        isScrolling = false;
    }

    private class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = mViews.get(position);
            return super.instantiateItem(container, position);
        }
    }

    /**
     * 轮播控件的监听事件
     */
    public static interface ImageCycleViewListener {
        void onImageClick(BannerInfo info, int position, View imageView);
    }
    /**
     * 为textview设置文字
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text) {
        if (text != null && textView != null) textView.setText(text);
    }

    /**
     * 为textview设置文字
     *
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, int text) {
        if (textView != null) setText(textView, text + "");
    }

    /**
     * 是否循环，默认开启。必须在setData前调用
     *
     * @param isCycle 是否循环
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 是否处于循环状态
     *
     * @return
     */
    public boolean isCycle() {
        return isCycle;
    }

    /**
     * 设置是否轮播，默认轮播,轮播一定是循环的
     *
     * @param isWheel
     */
    public void setWheel(boolean isWheel) {
        this.isWheel = isWheel;
        isCycle = true;
        if (isWheel) {
            mHandler.postDelayed(mRunnable, delay);
        }
    }

    /**
     * 刷新数据，当外部视图更新后，通知刷新数据
     */
    public void refreshData() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     * 是否处于轮播状态
     *
     * @return
     */
    public boolean isWheel() {
        return isWheel;
    }

    /**
     * 设置轮播暂停时间,单位毫秒（默认4000毫秒）
     * @param delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

}
