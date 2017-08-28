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

public class AddCommunityActivity extends AppCompatActivity implements View.OnClickListener {
    // UI references.
    private Button takePhoto;
    private Button choosePhotoFromAlbum;
    private Button mNextButton;
    private Button mAddCommunityButton;
    private EditText mCommunityName;
    private EditText mCommunityDesc;
    private TextView mCommunityOwnerName;
    private TextView mCommunityPosition;
    private ImageView picture;
    private Uri imageUri;
    //拍照
    public static final int TAKE_PHOTO=1;
    private static final int REQUEST_POLICY = 1;

    private static final int RESULT_OK = -1;
    public static final int CHOOSE_PHORO=2;
    private File outputImage;
    private String newfile;

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
                mCommunityOwnerName.setText(MyApplication.getUser().getUserName());
                mCommunityPosition = (TextView) findViewById(R.id.community_position);
                mCommunityPosition.setText(MyApplication.getArea());
                takePhoto = (Button)findViewById(R.id.add_community_take_photo);
                choosePhotoFromAlbum = (Button)findViewById(R.id.add_community_choose_photo_from_album);
                picture = (ImageView)findViewById(R.id.community_choose_photo);
                takePhoto.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //创建File对象，用于存储拍照后的照片
                        newfile = RandomUtil.getRandomFileName();
                        outputImage = new File("/sdcard/shuquan/"+newfile+".jpg");
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
        BookCommunity bookCommunity = new BookCommunity();
        bookCommunity.setCommunityCreatorId(MyApplication.getUser().getId());
        bookCommunity.setCommunityName(mCommunityName.getText().toString());
        bookCommunity.setCommunityDesc(mCommunityDesc.getText().toString());
        bookCommunity.setCommunityPosition(MyApplication.getPosition());
        bookCommunity.setCommunityLogo("/sdcard/shuquan/"+newfile+".jpg");

        OkHttpUtil.sendMultipartBookCommunity(bookCommunity);

    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHORO);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            case CHOOSE_PHORO:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT >=19){
                        //4.4以上系统使用这个方法处理照片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下系统使用这个方法处理照片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri = data.getData();
        Log.d("intent.getData :",""+uri);
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d("getDocumentId(uri) :",""+docId);
            Log.d("uri.getAuthority() :",""+uri.getAuthority());
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }
            else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }

        }
        else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
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