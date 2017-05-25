package com.booksharer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.baidu.location.LocationClient;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;
import com.booksharer.entity.BookCommunityLab;
import com.booksharer.service.LocationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class HomwFragment extends Fragment {

    private ViewPager adViewPager;
    private LinearLayout pagerLayout;
    private List<View> pageViews;
    private ImageView[] imageViews;
    private AdPageAdapter adapter;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    View view;

    private IntentFilter mIntentFilter;

    private LocalReceiver mLocalReceiver;

    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViewPager();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.booksharer.LOCAL_BROADCAST");
        mLocalReceiver = new LocalReceiver();
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, mIntentFilter);

        view.findViewById(R.id.locate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(getActivity(), LocationService.class);
                getActivity().startService(startIntent); // 启动服务
            }
        });

        mRecyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view_book_community);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
    }

    private void updateUI() {
        BookCommunityLab bookCommunityLab = BookCommunityLab.get(getActivity());
        List<BookCommunity> bookCommunities = bookCommunityLab.getBookCommunities();
        mHomeAdapter = new HomeAdapter(bookCommunities);
        mRecyclerView.setAdapter(mHomeAdapter);
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

                    viewHandler.sendEmptyMessage(atomicInteger.get());
                    atomicOption();

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
            ImageView imageView = new ImageView(getActivity());
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

    private class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            TextView city = (TextView) view.findViewById(R.id.city);
            city.setText(preferences.getString("city", "定位失败"));
            Toast.makeText(context,preferences.getString("city", "定位失败"), Toast.LENGTH_SHORT).show();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("position", preferences.getString("position", "0.0, 0.0"));
//            HttpUtil.sendOkHttpPost("", map, new okhttp3.Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    Toast.makeText(view.getContext(),"网络故障", Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Toast.makeText(view.getContext(),"圈子结果", Toast.LENGTH_SHORT).show();
//                    String responseData = response.body().string();
//                }
//            });
        }
    }
}
