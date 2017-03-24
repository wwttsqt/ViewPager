package com.example.admin.viewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private int[] mImageResIds;
    private ImageView mImageView;
    private ArrayList<ImageView> mImageViews;
    private LinearLayout mLinearLayout;
    private TextView mTextView;
    private View mPointView;
    private String[] mContentDescs;
    private int mPreviousSelectedPosition;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化布局 View视图
        initViews();

        // Model数据
        initData();

        // Controller 控制器
        initAdapter();

        // 开启轮询
        new Thread() {
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 往下跳一位
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("设置当前位置: " + mViewPager.getCurrentItem());
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }

    private void initViews() {
        mLinearLayout = (LinearLayout) findViewById(R.id.ll1);
        mTextView = (TextView) findViewById(R.id.tv1);
        mViewPager = (ViewPager) findViewById(R.id.vp1);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        // 初始化要显示的数据

        // 图片资源id数组
        mImageResIds = new int[]{R.drawable.a, R.
                drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        // 文本描述
        mContentDescs = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
        };
        mImageViews = new ArrayList<>();
        for (int i = 0; i < mImageResIds.length; i++) {
            // 初始化要显示的图片对象
            mImageView = new ImageView(this);
            mImageView.setBackgroundResource(mImageResIds[i]);
            mImageViews.add(mImageView);

            // 加小白点, 指示器
            mPointView = new View(this);
            mPointView.setBackgroundResource(R.drawable.selector_bg_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(5, 5);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
            // 设置默认所有都不可用
            mPointView.setEnabled(false);
            mLinearLayout.addView(mPointView, layoutParams);
        }
    }

    private void initAdapter() {
        mLinearLayout.getChildAt(0).setEnabled(true);
        mTextView.setText(mContentDescs[0]);
        mPreviousSelectedPosition = 0;
        //设置适配器
        mViewPager.setAdapter(new MyAdapter());
        // 默认设置到中间的某个位置
        //int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        mViewPager.setCurrentItem(5000000); // 设置到某个位置
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;
        }

        // 1. 返回要显示的条目内容, 创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4

            //newPosition = position % 5
            int newPosition = position % mImageViews.size();

            ImageView imageView = mImageViews.get(newPosition);
            // a. 把View对象添加到container中
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView; // 必须重写, 否则报异常
        }

        // 2. 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 滚动时调用
    }

    @Override
    public void onPageSelected(int position) {
        // 新的条目被选中时调用
        int newPosition = position % mImageViews.size();
        //设置文本
        mTextView.setText(mContentDescs[newPosition]);

        // 把之前的禁用, 把最新的启用, 更新指示器
        mLinearLayout.getChildAt(mPreviousSelectedPosition).setEnabled(false);
        mLinearLayout.getChildAt(newPosition).setEnabled(true);

        // 记录之前的位置
        mPreviousSelectedPosition = newPosition;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // 滚动状态变化时调用
    }

}
