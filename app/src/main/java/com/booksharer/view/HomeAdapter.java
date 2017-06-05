package com.booksharer.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;

import java.util.List;

/**
 * Created by DELL on 2017/5/21.
 */
public class HomeAdapter extends BaseAdapter<BookCommunity>{

    private Context mContext;

    public HomeAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_community, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindBookCommunity(getDataSet().get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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
            communityName.setText(community.getCommunityName());
//        communityLogo.setImageResource(community.getCommunityLogo());
            communityPeopleNum.setText(community.getCommunityPeopleNum().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(),
                    communityName.getText() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
