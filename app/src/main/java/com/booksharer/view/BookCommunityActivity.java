package com.booksharer.view;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;
import com.booksharer.util.RandomUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booksharer.R;
import com.booksharer.entity.BookCommunity;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;
import com.booksharer.util.RandomUtil;
import com.booksharer.util.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BookCommunityActivity extends AppCompatActivity implements View.OnClickListener {
    // UI references.
    private TextView mCommunityName;
    private TextView mCommunityDesc;
    private TextView mCommunityOwnerName;
    private TextView mCommunityPeopleNum;
    private ImageView picture;
    private BookCommunity bookCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_community);
        initView();
    }

    private void initView() {
        mCommunityName = (TextView) findViewById(R.id.show_community_name);
        mCommunityDesc = (TextView) findViewById(R.id.show_community_desc);
        mCommunityOwnerName = (TextView) findViewById(R.id.show_community_owner_name);
        mCommunityPeopleNum = (TextView) findViewById(R.id.show_community_people_name);
        picture = (ImageView) findViewById(R.id.show_community_logo);

        bookCommunity = MyApplication.getBookCommunity();
        mCommunityName.setText(bookCommunity.getCommunityName());
        mCommunityDesc.setText(bookCommunity.getCommunityDesc());
        mCommunityOwnerName.setText(bookCommunity.getCommunityCreatorId());
        mCommunityPeopleNum.setText(bookCommunity.getCommunityPeopleNum());
        displayImage(bookCommunity.getCommunityLogo());
        //TODO 没检查呢
    }


    @Override
    public void onClick(View v) {

    }

    private void  displayImage(String imagePath){
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }
}