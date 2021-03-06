package com.booksharer.view;

/**
 * Created by DELL on 2017/5/27.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;
import com.booksharer.entity.BookCommunityLab;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;
import com.booksharer.util.Utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 在这个装饰器中，只做与加载更多相关操作。
 **/
public class LoadMoreAdapterWrapper extends BaseAdapter<BookCommunity> {
    private BaseAdapter mAdapter;
    private static final int mPageSize = 5;
    private int mPagePosition = 0;
    private static boolean hasMoreData = true;
    private OnLoad mOnLoad;


    public LoadMoreAdapterWrapper(BaseAdapter adapter, OnLoad onLoad) {
        mAdapter = adapter;
        mOnLoad = onLoad;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.list_item_no_more) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new NoMoreItemVH(view);
        } else if (viewType == R.layout.list_item_loading) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new LoadingItemVH(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingItemVH) {
            requestData(mPagePosition, mPageSize);
        } else if (holder instanceof NoMoreItemVH) {

        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    private void requestData(int pagePosition, final int pageSize) {
        Log.d("test", "加载页");
        //补全请求地址
//        String requestUrl = String.format("currentPosition=%s&pageIndex=%d&pageSize=#d", MyApplication.getPosition(), pagePosition, pageSize);
        if (MyApplication.getPosition() == null){
            return;
        }
        Log.d("test", "现在有数据"+BookCommunityLab.get(MyApplication.getContext()).getCapacity() );
        Log.d("pagePosition",BookCommunityLab.get(MyApplication.getContext()).getCapacity() +" "+pagePosition );
        HashMap<String, String> map = new HashMap<>();
        map.put("currentPosition", MyApplication.getPosition());
        int pageIndex = BookCommunityLab.get(MyApplication.getContext()).getCapacity()/pageSize + 1;
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(pageSize) );
        Log.d("test", map.toString());
        MyApplication.setUrl_api("/community/findNear", map);
        HttpUtil.sendOkHttpRequest(MyApplication.getUrl_api(), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("test", responseData);
                if (Utility.handleFindNearCommunityResponse(responseData)) {
//                    List<BookCommunity> bookCommunities =  BookCommunityLab.get(MyApplication.getContext()).getBookCommunities();
                    List<BookCommunity> bookCommunities =  MyApplication.getBookCommunities();
                    for ( BookCommunity bookCommunity:
                            bookCommunities) {
                        final String logo = bookCommunity.getCommunityLogo();
                        OkHttpUtil.downloadImage(logo);
                        if (bookCommunities.size() < pageSize)hasMoreData = false;
                        else hasMoreData = true;
                    }
                } else {
                    hasMoreData = false;
                    Log.d("NetWork","网络故障");
//                        Toast.makeText(view.getContext(), "网络故障", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //网络请求,如果是异步请求，则在成功之后的回调中添加数据，并且调用notifyDataSetChanged方法，hasMoreData为true
//        如果没有数据了，则hasMoreData为false，然后通知变化，更新recylerview

        if (mOnLoad != null) {
            mOnLoad.load(pagePosition, pageSize, new ILoadCallback() {
                @Override
                public void onSuccess() {
                    Log.d("test","添加数据");
                    BookCommunityLab.get(MyApplication.getContext()).appendBookCommunities(MyApplication.getBookCommunities());
                    notifyDataSetChanged();
                    mPagePosition = (mPagePosition + 1) * mPageSize;
                    Log.d("添加后", String.valueOf(hasMoreData));
                }

                @Override
                public void onFailure() {
                    Log.d("添加后", "什么情况");
//                    hasMoreData = false;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("test position getItem", position +" "+getItemCount());
        if (position == getItemCount() - 1) {
            Log.d("test", String.valueOf(hasMoreData));
            if (hasMoreData) {
                return R.layout.list_item_loading;
            } else {
                return R.layout.list_item_no_more;
            }
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    static class LoadingItemVH extends RecyclerView.ViewHolder {

        public LoadingItemVH(View itemView) {
            super(itemView);
        }

    }

    static class NoMoreItemVH extends RecyclerView.ViewHolder {

        public NoMoreItemVH(View itemView) {
            super(itemView);
        }
    }

}


