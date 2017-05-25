package com.booksharer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.C;
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
        BookCommunity community = mBookCommunityList.get(position);
        holder.bindBookCommunity(community);


    }

    @Override
    public int getItemCount() {
        return mBookCommunityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private BookCommunity mBookCommunity;
        View communityView;
        TextView communityName;
        ImageView communityLogo;
        TextView communityPeopleNum;

        public ViewHolder(View itemView) {
            super(itemView);
            communityView = itemView;
            communityView.setOnClickListener(this);
            communityName = (TextView) itemView.findViewById(R.id.community_name);
            communityPeopleNum = (TextView) itemView.findViewById(R.id.community_people_num);
            communityLogo = (ImageView) itemView.findViewById(R.id.community_logo);
        }

        public void bindBookCommunity(BookCommunity community) {
            mBookCommunity = community;
            communityName.setText(community.getCommunityName());
//        communityLogo.setImageResource(community.getCommunityLogo());
            communityPeopleNum.setText(community.getCommunityPeopleNum().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),
                    mBookCommunity.getCommunityName() + " clicked!", Toast.LENGTH_SHORT)
                    .show();

        }
    }
}
