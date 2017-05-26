package com.booksharer.view;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.Book;
import com.bumptech.glide.Glide;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
//
//    private static final String TAG = "BookAdapter";
//
//    private Context mContext;
//
//    private List<Book> mBookList;
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        CardView cardView;
//        ImageView bookImage;
//        TextView bookName;
//
//        public ViewHolder(View view) {
//            super(view);
//            cardView = (CardView) view;
//            bookImage = (ImageView) view.findViewById(R.id.book_image);
//            bookName = (TextView) view.findViewById(R.id.book_name);
//        }
//    }
//
//    public BookAdapter(List<Book> bookList) {
//        mBookList = bookList;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (mContext == null) {
//            mContext = parent.getContext();
//        }
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_book, parent, false);
//        final ViewHolder holder = new ViewHolder(view);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Book book = mBookList.get(position);
//                Intent intent = new Intent(mContext, BookCommunityActivity.class);
//                intent.putExtra(BookCommunityActivity.FRUIT_NAME, book.getName());
//                intent.putExtra(BookCommunityActivity.FRUIT_IMAGE_ID, book.getImageId());
//                mContext.startActivity(intent);
//            }
//        });
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Book book = mBookList.get(position);
//        holder.bookName.setText(book.getName());
//        Glide.with(mContext).load(book.getImageId()).into(holder.bookImage);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mBookList.size();
//    }

}

