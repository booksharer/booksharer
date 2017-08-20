package com.booksharer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.Utility;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class AddCommunityActivity extends AppCompatActivity {
    // UI references.
    private Button mNextButton;
    private Button mAddCommunityButton;
    private EditText mCommunityName;
    private EditText mCommunityDesc;
    private TextView mCommunityOwnerName;
    private TextView mCommunityPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_community);
        initView();
    }

    private void initView() {
        mNextButton = (Button) findViewById(R.id.next);
        Log.d("test","debug");
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.step2);
                mAddCommunityButton = (Button) findViewById(R.id.add_community_button);
                mAddCommunityButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCommunity();
                    }
                });
                mCommunityName = (EditText) findViewById(R.id.community_name);
                mCommunityDesc = (EditText) findViewById(R.id.community_desc);
                mCommunityOwnerName = (TextView) findViewById(R.id.community_owner_name);
                mCommunityOwnerName.setText("somebody");
                //TODO
                // mCommunityOwnerName.setText(MyApplication.getUser().getUserName());
                mCommunityPosition = (TextView) findViewById(R.id.community_position);
                mCommunityPosition.setText(MyApplication.getArea());

            }
        });

    }

    private void addCommunity() {
        HashMap<String, String> map = new HashMap<>();
        map.put("communityName", mCommunityName.getText().toString());
        map.put("communityDesc", mCommunityDesc.getText().toString());
        map.put("communityPosition", MyApplication.getPosition());
        MyApplication.setUrl_api("/community/add", map);
        HttpUtil.sendOkHttpRequest(MyApplication.getUrl_api(), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (Utility.handleAddCommunityResponse(responseData)) {
                    finish();
                }
            }
        });
    }

}