package com.booksharer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import com.booksharer.R;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class AddCommunityActivity extends AppCompatActivity {
    // UI references.
    private Button takePhoto;
    private Button choosePhotoFromAlbum;
    private Button mNextButton;
    private Button mAddCommunityButton;
    private EditText mCommunityName;
    private EditText mCommunityDesc;
    private TextView mCommunityOwnerName;
    private TextView mCommunityPosition;

    private Uri imageUri;
    //拍照
    public static final int TAKE_PHOTO=1;
    private static final int REQUEST_POLICY = 1;

    private static final int RESULT_OK = -1;
    public static final int CHOOSE_PHORO=2;
    private File outputImage;

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
                takePhoto = (Button)findViewById(R.id.add_community_take_photo);
                choosePhotoFromAlbum = (Button)findViewById(R.id.add_community_choose_photo_from_album);
                ImageView picture = (ImageView)findViewById(R.id.picture);
                takePhoto.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //创建File对象，用于存储拍照后的照片
                        outputImage = new File(Environment.getExternalStorageDirectory(),"/sdcard/shuquan/output_image.jpg");
                        try{
                            if (outputImage.exists()){
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT>=24){
                            imageUri = FileProvider.getUriForFile(AddCommunityActivity.this,"com.booksharer.camera.fileprovider",outputImage);
                        }else{
                            imageUri = Uri.fromFile(outputImage);
                        }
                        //启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                    }
                });

                choosePhotoFromAlbum.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if (ContextCompat.checkSelfPermission(AddCommunityActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(AddCommunityActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else{
                            openAlbum();
                        }
                    }
                });
            }
        });
    }

    private void addCommunity() {
        HashMap<String, String> map = new HashMap<>();
        map.put("communityName", mCommunityName.getText().toString());
        map.put("communityDesc", mCommunityDesc.getText().toString());
        map.put("communityPosition", MyApplication.getPosition());
        //TODO
        //map.put("communityCreatorId",MyApplication.getUser().getId().toString());
        map.put("communityCreatorId","1");
        MyApplication.setUrl_api("/community/add");
        List<File> fileList = new ArrayList<>();
        fileList.add(outputImage);
        HttpUtil.sendOkHttpPostMultipart(MyApplication.getUrl_api(), map, "communityLogo", fileList, new okhttp3.Callback() {
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

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHORO);
    }

}