package com.booksharer.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.baidu.location.LocationClient;

import com.booksharer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class BookCommunityFragment extends Fragment implements View.OnClickListener {


    private ViewPager adViewPager;
    private LinearLayout pagerLayout;
    private List<View> pageViews;
    private ImageView[] imageViews;
    private ImageView imageView;
    private AdPageAdapter adapter;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private boolean isContinue = true;


//    private LocationClient mLocationClient = null;

    View view;

    private TextView jiudian;

    @Override
    public void onStart() {
        super.onStart();
        //initViewPager();
        Log.d("test", "HomeFragment onStart");

        // TODO Auto-generated method stub  暂时不能用
        //checkGPSIsOpen();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mLocationClient = ((LocationApplication) getActivity().getApplication()).mLocationClient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_community, container, false);
        initViewPager();
        //获取数据并显示
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        TextView city = (TextView)view.findViewById(R.id.city);
        city.setText(preferences.getString("city", "定位失败"));

        Log.d("test", "HomeFragment onCreateView");
        ImageView gps = (ImageView) view.findViewById(R.id.index_home_tip);
        gps.setOnClickListener(this);

        /*搜索TextView*/
        TextView search_area = (TextView) view.findViewById(R.id.search);
        search_area.setOnClickListener(this);

//        View flight_policy = view.findViewById(R.id.list_one);
//        flight_policy.setOnClickListener(this);
//
//        View query_policy = view.findViewById(R.id.query_policy);
//        query_policy.setOnClickListener(this);
//
//        View scenic_policy = view.findViewById(R.id.scenic_policy);
//        scenic_policy.setOnClickListener(this);
//
//        view.findViewById(R.id.flight_delay_policy).setOnClickListener(this);
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        Log.d("life", "刷新view");
//        home_choose_area.setText(prefs.getString("city_name", "选择"));
//
//        view.findViewById(R.id.addView).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.home_choose_area:
//                Intent intent = new Intent(getActivity(), ChooseAreaActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.index_home_tip:
//                mLocationClient.start();
//                if (mLocationClient != null && mLocationClient.isStarted())
//                    mLocationClient.requestLocation();
//                break;
//            case R.id.home_search_textview:
//                //intent = new Intent(getActivity(), SearchAreaActivity.class);
//                //startActivity(intent);
//                break;
//            case R.id.list_one:
//                ((MainActivity) getActivity()).setCurrentTabByTag("生活");
//                PreferenceManager
//                        .getDefaultSharedPreferences(getActivity()).edit()
//                        .putBoolean("flightDelayView", true)
//                        .apply();
//                break;
//            case R.id.query_policy:
//                ((MainActivity) getActivity()).setCurrentTabByTag("保单");
//                break;
//            case R.id.scenic_policy:
//                ((MainActivity) getActivity()).setCurrentTabByTag("生活");
//                PreferenceManager
//                        .getDefaultSharedPreferences(getActivity()).edit()
//                        .putBoolean("scenicSpotView", true)
//                        .apply();
//                break;
//            case R.id.flight_delay_policy:
//                ((MainActivity) getActivity()).setCurrentTabByTag("生活");
//                PreferenceManager
//                        .getDefaultSharedPreferences(getActivity()).edit()
//                        .putBoolean("flightDelayView", true)
//                        .apply();
//                break;
//            case R.id.addView:
//                Intent i = new Intent(getActivity(), ColligatePolicyActivity.class);
//                startActivity(i);
//                break;
//
//        }
    }


    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.d("test", "HomeFragment onStop");
    }


    private void initViewPager() {

        //从布局文件中获取ViewPager父容器
        pagerLayout = (LinearLayout) view.findViewById(R.id.view_pager_content);
        //创建ViewPager
        adViewPager = new ViewPager(getContext());

        //获取屏幕像素相关信息
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        //根据屏幕信息设置ViewPager广告容器的宽高      原高度dm.heightPixels * 2 / 5
        adViewPager.setLayoutParams(new LayoutParams(dm.widthPixels, dm.widthPixels * 9 / 16));

        //将ViewPager容器设置到布局文件父容器中
        pagerLayout.addView(adViewPager);

        initPageAdapter();

        initCirclePoint();

        adViewPager.setAdapter(adapter);
        adViewPager.addOnPageChangeListener(new AdPageChangeListener());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isContinue) {
                        viewHandler.sendEmptyMessage(atomicInteger.get());
                        atomicOption();
                    }
                }
            }
        }).start();
    }


    private void atomicOption() {
        atomicInteger.incrementAndGet();
        if (atomicInteger.get() > imageViews.length - 1) {
            atomicInteger.getAndAdd(-5);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    /*
     * 每隔固定时间切换广告栏图片
     */
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            adViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private void initPageAdapter() {
        pageViews = new ArrayList<>();

        ImageView img1 = new ImageView(getActivity());
        img1.setBackgroundResource(R.drawable.ad01);
        pageViews.add(img1);

        ImageView img2 = new ImageView(getActivity());
        img2.setBackgroundResource(R.drawable.ad03);
        pageViews.add(img2);

        ImageView img3 = new ImageView(getContext());
        img3.setBackgroundResource(R.drawable.ad02);
        pageViews.add(img3);

        adapter = new AdPageAdapter(pageViews);
    }

    private void initCirclePoint() {
        ViewGroup group = (ViewGroup) view.findViewById(R.id.viewGroup);
        imageViews = new ImageView[pageViews.size()];
        //广告栏的小圆点图标
        for (int i = 0; i < pageViews.size(); i++) {
            //创建一个ImageView, 并设置宽高. 将该对象放入到数组中
            imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
            lp.setMargins(0, 0, 10, 0);
            imageView.setLayoutParams(lp);


            imageViews[i] = imageView;

            //初始值, 默认第0个选中
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.drawable.point_focused);
            } else {
                imageViews[i]
                        .setBackgroundResource(R.drawable.point_unfocused);
            }
            //将小圆点放入到布局中
            group.addView(imageViews[i]);
        }
    }

    /**
     * ViewPager 页面改变监听器
     */
    private final class AdPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 页面滚动状态发生改变的时候触发
         */
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        /**
         * 页面滚动的时候触发
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        /**
         * 页面选中的时候触发
         */
        @Override
        public void onPageSelected(int arg0) {
            //获取当前显示的页面是哪个页面
            atomicInteger.getAndSet(arg0);
            //重新设置原点布局集合
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.point_focused);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.point_unfocused);
                }
            }
        }
    }


    private final class AdPageAdapter extends PagerAdapter {
        private List<View> views = null;

        /**
         * 初始化数据源, 即View数组
         */
        public AdPageAdapter(List<View> views) {
            this.views = views;
        }

        /**
         * 从ViewPager中删除集合中对应索引的View对象
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        /**
         * 获取ViewPager的个数
         */
        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 从View集合中获取对应索引的元素, 并添加到ViewPager中
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            return views.get(position);
        }

        /**
         * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联
         * 这个方法是必须实现的
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
