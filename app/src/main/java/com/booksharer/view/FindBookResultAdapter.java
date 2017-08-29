package com.booksharer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.BookCommunityLab;
import com.booksharer.entity.BookInfo;
import com.booksharer.entity.BookInfoLab;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;

import java.util.Locale;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class FindBookResultAdapter extends BaseAdapter<BookInfo> {
    private Context mContext;

    public FindBookResultAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_find_book_result, parent, false);
        FindBookResultAdapter.ViewHolder holder = new FindBookResultAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FindBookResultAdapter.ViewHolder) holder).bindBook(BookInfoLab.get(MyApplication.getContext()).getmBookResults().get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View mBookView;
        TextView mBookName,mBookAuthor,mBookPublisher;
        ImageView mBookPic;

        public ViewHolder(View itemView) {
            super(itemView);
            mBookView = itemView;
            mBookView.setOnClickListener(this);
            mBookName = (TextView) itemView.findViewById(R.id.book_name);
            mBookAuthor = (TextView) itemView.findViewById(R.id.author_name);
            mBookPublisher = (TextView) itemView.findViewById(R.id.publisher);
//            mBookPic = (ImageView) itemView.findViewById(R.id.book_pic);

        }

        @Override
        public void onClick(View v) {

        }

        public void bindBook(BookInfo book) {
            mBookName.setText(book.getBookName());
            mBookAuthor.setText(book.getAuthor());
            mBookPublisher.setText(book.getPublisher());
        }
    }
}
