package com.booksharer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;

import java.util.List;

/**
 * Created by DELL on 2017/5/21.
 */
public class BookCommunityAdapter extends RecyclerView.Adapter<BookCommunityAdapter.ViewHolder> {

    private List<BookCommunity> mBookCommunityList;

    public BookCommunityAdapter(List<BookCommunity> bookCommunityList) {
        mBookCommunityList = bookCommunityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_community, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BookCommunity circle = mBookCommunityList.get(position);
        holder.circleName.setText(circle.getCommunityName());
    }

    @Override
    public int getItemCount() {
        return mBookCommunityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView circleName;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
