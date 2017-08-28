package com.booksharer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import com.booksharer.entity.BookInfo;
import com.booksharer.entity.BookInfoLab;
import com.booksharer.util.MyApplication;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class FindBookResultFragment extends android.app.Fragment {

    private RecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private FindBookResultAdapter mFindBookResultAdapter;
    private ViewPager adViewPager;
    private LinearLayout pagerLayout;
    private List<View> pageViews;
//    private AdPageAdapter adapter;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    View view;


    int loadCount;

    private  static final String TAG = "FINDBOOKRESULTFRAGMENT";

    private OnFragmentInteractionListener mListener;

    public FindBookResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"test");
        view = inflater.inflate(R.layout.fragment_find_book_result, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_find_book_result);
//        initViewPager();
        updateUI();
        return view;
    }
//
//    private void initViewPager() {
//        //从布局文件中获取ViewPager父容器
//        pagerLayout = (LinearLayout) view.findViewById(R.id.view_pager_content);
//        //创建ViewPager
//        adViewPager = new ViewPager(MyApplication.getContext());
//
//
//        //获取屏幕像素相关信息
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        //根据屏幕信息设置ViewPager广告容器的宽高      原高度dm.heightPixels * 2 / 5
//        adViewPager.setLayoutParams(new ViewGroup.LayoutParams(dm.widthPixels, dm.widthPixels * 9 / 16));
//
//        //将ViewPager容器设置到布局文件父容器中
//        pagerLayout.addView(adViewPager);
//
//        initPageAdapter();
//
//        adViewPager.setAdapter(adapter);
//        adViewPager.addOnPageChangeListener(new FindBookResultFragment.AdPageChangeListener());
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    viewHandler.sendEmptyMessage(atomicInteger.get());
////                    atomicOption();
//                }
//            }
//        }).start();
//    }

//    private void atomicOption() {
//        atomicInteger.incrementAndGet();
//        if (atomicInteger.get() > imageViews.length - 1) {
//            atomicInteger.getAndAdd(-5);
//        }
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//
//        }
//    }

    /*
    * 每隔固定时间切换广告栏图片
    */
//    private final Handler viewHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            adViewPager.setCurrentItem(msg.what);
//            super.handleMessage(msg);
//        }
//
//    };
//
//    private void initPageAdapter() {
//        pageViews = new ArrayList<>();
//
//        ImageView img1 = new ImageView(getActivity());
//        img1.setBackgroundResource(R.drawable.ad01);
//        pageViews.add(img1);
//
//        ImageView img2 = new ImageView(getActivity());
//        img2.setBackgroundResource(R.drawable.ad03);
//        pageViews.add(img2);
//
//        ImageView img3 = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            img3 = new ImageView(getContext());
//        }
//        img3.setBackgroundResource(R.drawable.ad02);
//        pageViews.add(img3);
//
//        adapter = new FindBookResultFragment.AdPageAdapter(pageViews);
//    }

    private void updateUI() {
        Log.d(TAG, "test");
        final BookInfoLab bookInfoLab = BookInfoLab.get(getActivity());
        if (mFindBookResultAdapter == null) {
            Log.d("test", "创建被装饰者类实例");
            //创建被装饰者类实例
            mFindBookResultAdapter = new FindBookResultAdapter(getActivity());
            mAdapter = new LoadMoreAdapterWrapper(mFindBookResultAdapter, new OnLoad() {
                @Override
                public void load(int pagePosition, int pageSize, final ILoadCallback callback) {
                    //此处模拟做网络操作，2s延迟，将拉取的数据更新到adpter中
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "触发");
//                            List<BookInfo> mbookResults = new ArrayList<>();
//                            for (int i = 0; i < 3; i++) {
//                                BookInfo bookInfo = new BookInfo();
//                                bookInfo.setId(String.valueOf(i));
//                                bookInfo.setBookName("书圈 #" + i + loadCount * 10);
//                                bookInfo.setAuthor("test" + i);
//                                bookInfo.setBookPic("url");
//                                mbookResults.add(bookInfo);
//                            }
//                            bookInfoLab.appendmBookResults(mbookResults);


                            final List<BookInfo> bookResults = bookInfoLab.getmBookResults();
                            Log.d(TAG,bookResults.toString());
                            //数据的处理最终还是交给被装饰的adapter来处理
                            mFindBookResultAdapter.updateData(bookResults);
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
            Log.d("test", "notify");
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

//    private final class AdPageAdapter extends PagerAdapter {
//        private List<View> views = null;
//
//        /**
//         * 初始化数据源, 即View数组
//         */
//        public AdPageAdapter(List<View> views) {
//            this.views = views;
//        }
//
//        /**
//         * 从ViewPager中删除集合中对应索引的View对象
//         */
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(views.get(position));
//        }
//
//        /**
//         * 获取ViewPager的个数
//         */
//        @Override
//        public int getCount() {
//            return views.size();
//        }
//
//        /**
//         * 从View集合中获取对应索引的元素, 并添加到ViewPager中
//         */
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(views.get(position), 0);
//            return views.get(position);
//        }
//
//        /**
//         * 是否将显示的ViewPager页面与instantiateItem返回的对象进行关联
//         * 这个方法是必须实现的
//         */
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class AdPageChangeListener implements ViewPager.OnPageChangeListener {
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
        }
    }
}
