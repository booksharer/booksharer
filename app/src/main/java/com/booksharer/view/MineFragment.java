package com.booksharer.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booksharer.R;
import com.booksharer.entity.User;
import com.booksharer.util.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MineFragment extends Fragment implements View.OnClickListener {

    private View userView;
    private TextView name;
    private View credit_card;
    private static View view;
    private static User user = MyApplication.getUser();


    private static final int REQUEST_POLICY = 1;

    private static final int RESULT_OK = -1;

    //拍照
    public static final int TAKE_PHOTO=1;
    private ImageView picture;
    private Uri imageUri;

    public static final int CHOOSE_PHORO=2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        //将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            case CHOOSE_PHORO:
                if (requestCode == RESULT_OK){
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
        if (DocumentsContract.isDocumentUri(getActivity(),uri)){
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
        Cursor cursor = getContext().getContentResolver().query(uri,null,selection,null,null);
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
            Toast.makeText(getActivity(),"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHORO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(getActivity(),"您需要设置允许书圈访问照片才行哦~",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        Button takePhoto = (Button)view.findViewById(R.id.take_photo);
        Button choosePhotoFromAlbum = (Button)view.findViewById(R.id.choose_photo_from_album);
        picture = (ImageView)view.findViewById(R.id.picture);
        takePhoto.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v){
                //创建File对象，用于存储拍照后的照片
                File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(getActivity(),"com.booksharer.camera.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        choosePhotoFromAlbum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();


        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        view.findViewById(R.id.view_policy).setOnClickListener(this);
//        view.findViewById(R.id.view_policy2).setOnClickListener(this);
//        view.findViewById(R.id.view_policy3).setOnClickListener(this);
//        view.findViewById(R.id.view_policy4).setOnClickListener(this);
//        view.findViewById(R.id.view_policy5).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
    }


    private void initView(View view) {
        user = MyApplication.getUser();
        name = (TextView) view.findViewById(R.id.name);
        if(user != null){
            String sname = user.getUserName();
            name.setText(sname);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            name.setText("请登录");
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
//        ((MainActivity) getActivity()).setCurrentTabByTag("保单");
//        switch (v.getId()) {
//            case R.id.view_policy:
//                ((MainActivity) getActivity()).setPolicyTitle(0);
//                break;
//            case R.id.view_policy2:
//                ((MainActivity) getActivity()).setPolicyTitle(1);
//                break;
//            case R.id.view_policy3:
//                ((MainActivity) getActivity()).setPolicyTitle(2);
//                break;
//            case R.id.view_policy4:
//                ((MainActivity) getActivity()).setPolicyTitle(3);
//                break;
//            case R.id.view_policy5:
//                ((MainActivity) getActivity()).setPolicyTitle(4);
//                break;
//        }
    }
}
/**
 * 查主题背景色方法
 */
//    TypedArray array = getActivity().getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorBackground,android.R.attr.textColorPrimary});
//    int bc = array.getColor(0,0xff00ff);
////int tc = array.getColor(1,0xff00ff);
//array.recycle();
//        Log.d("life",         Integer.toHexString(bc)
//        +"");