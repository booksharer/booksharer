package com.booksharer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;
import com.booksharer.entity.BookCommunityLab;
import com.booksharer.service.LocationService;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;
import com.booksharer.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Response;


public class HomeFragment extends Fragment {
    private TextView city;
    private ViewPager adViewPager;
    private LinearLayout pagerLayout;
    private List<View> pageViews;
    private ImageView[] imageViews;
    private AdPageAdapter adapter;
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private Button mFindBookBtn;

    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private HomeAdapter mHomeAdapter;
    View view;

    private IntentFilter mIntentFilter;

    private LocalReceiver mLocalReceiver;

    private LocalBroadcastManager mLocalBroadcastManager;
    int loadCount;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test","HomeFragment");
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initViewPager();
        city = (TextView) view.findViewById(R.id.city);
        city.setText(MyApplication.getArea());
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.booksharer.LOCAL_BROADCAST");
        mLocalReceiver = new LocalReceiver();
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, mIntentFilter);

        mFindBookBtn = (Button) view.findViewById(R.id.find_book_btn);
        mFindBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindBookAcitivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.locate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getActivity(), LocationService.class);
                getActivity().startService(startIntent); // 启动服务
            }
        });

        mRecyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view_book_community);
        updateUI();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
    }

    private void updateUI() {
        final BookCommunityLab bookCommunityLab = BookCommunityLab.get(getActivity());
        if (mHomeAdapter == null) {
            Log.d("test", "创建被装饰者类实例");
            //创建被装饰者类实例
            mHomeAdapter = new HomeAdapter(getActivity());
            mAdapter = new LoadMoreAdapterWrapper(mHomeAdapter, new OnLoad() {
                @Override
                public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                    //此处模拟做网络操作，2s延迟，将拉取的数据更新到adpter中
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadCount = 3;
//                            Log.d("test", "触发");
//                            List<BookCommunity> mBookCommunities = new ArrayList<>();
//                            for (int i = 0; i < 3; i++) {
//                                BookCommunity bookCommunity = new BookCommunity();
//                                bookCommunity.setId(i);
//                                bookCommunity.setCommunityName("书圈 #" + i + loadCount * 10);
//                                bookCommunity.setCommunityPeopleNum(i * 5 + 2);
//                                bookCommunity.setCommunityLogo("url");
//                                mBookCommunities.add(bookCommunity);
//                            }
//                            bookCommunityLab.appendBookCommunities(mBookCommunities);
                            final List<BookCommunity> bookCommunities = bookCommunityLab.getBookCommunities();
                            //数据的处理最终还是交给被装饰的adapter来处理
                            mHomeAdapter.updateData(bookCommunities);
                            callback.onSuccess();
                            //模拟加载到没有更多数据的情况，触发onFailure
                            if (loadCount++ == 3) {
                                callback.onFailure();
                            }
                        }
                    }, 500);
                }
            });

        } else {
            Log.d("test", "此时数据为\n"+bookCommunityLab.toString());
            mAdapter.updateData(bookCommunityLab.getBookCommunities());
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
        adViewPager.setLayoutParams(new LayoutParams(dm.widthPixels/2, dm.widthPixels * 2 / 3));

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
        img1.setBackgroundResource(R.drawable.aa);
        pageViews.add(img1);

        ImageView img2 = new ImageView(getActivity());
        img2.setBackgroundResource(R.drawable.bb);
        pageViews.add(img2);

        ImageView img3 = new ImageView(getContext());
        img3.setBackgroundResource(R.drawable.cc);
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

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("test", "收到定位广播");
            TextView city = (TextView) view.findViewById(R.id.city);
            city.setText(MyApplication.getArea());
            HashMap<String, String> map = new HashMap<>();
            map.put("currentPosition", MyApplication.getPosition());
            map.put("pageIndex", "1");
            map.put("pageSize", "5");
            MyApplication.setUrl_api("community/findNear", map);
            HttpUtil.sendOkHttpRequest(MyApplication.getUrl_api(), new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("test", responseData);
                    if (Utility.handleFindNearCommunityResponse(responseData)) {
                        List<BookCommunity> bookCommunities =  BookCommunityLab.get(MyApplication.getContext()).getBookCommunities();
                        for ( BookCommunity bookCommunity:
                             bookCommunities) {
                            final String logo = bookCommunity.getCommunityLogo();
                            OkHttpUtil.downloadImage(logo);
                        }
                        //显示
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                            }
                        });

                    } else {
                        Log.d("NetWork","网络故障");
//                        Toast.makeText(view.getContext(), "网络故障", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
}
