package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.BookInfo;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.ImageUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;
import com.booksharer.util.RegexUtils;
import com.booksharer.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class AddBookActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText mBookNameView, mBookAuthorView, mBookPublisherView,
            mBookPublishDateView, mBookPriceView, mBookISBNView,
            mBookContentIntroView, mBookAuthorIntroView;

    private ImageView mBookPicView;

    private String mBookTag, mBookZhuangZhen,mFileName;
    private Spinner sTag,sZhuangZhen;

    private Button mAddBookButton,mAddBookPicButton;
    private View mProgressView,mAddBookView;

    private static final String TAG = "ADDBOOKACTIVITY";
    private static int RESULT_LOAD_IMAGE = 1;
    private static String old_picture_path, new_picture_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        initView();
    }

    private void initView() {
        mBookNameView = (EditText) findViewById(R.id.book_name);
        mBookAuthorView = (EditText) findViewById(R.id.author_name);
        mBookPublisherView = (EditText) findViewById(R.id.book_publisher);
        mBookPublishDateView = (EditText) findViewById(R.id.book_publish_date);
        mBookPriceView = (EditText) findViewById(R.id.book_price);
        mBookISBNView = (EditText) findViewById(R.id.book_isbn);
        mBookContentIntroView = (EditText) findViewById(R.id.book_contentIntro);
        mBookAuthorIntroView = (EditText) findViewById(R.id.book_authorIntro);

        sZhuangZhen = (Spinner) findViewById(R.id.spinner_zhuangzhen);
        sZhuangZhen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] zhuangzhen = getResources().getStringArray(R.array.zhuangzhen);
                Log.d(TAG,zhuangzhen[position] + "clicked");
                mBookZhuangZhen = zhuangzhen[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sTag = (Spinner) findViewById(R.id.spinner_tag);
        sTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] tags = getResources().getStringArray(R.array.tags);
                Log.d(TAG,tags[position] + "clicked");
                mBookTag = tags[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBookPicView = (ImageView) findViewById(R.id.book_picture);

        mAddBookPicButton = (Button) findViewById(R.id.add_book_pic_btn);
        mAddBookPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        mAddBookButton = (Button) findViewById(R.id.confirm_add_book);
        mAddBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempAddBook();
            }
        });
        mProgressView = findViewById(R.id.sign_up_progress);
        mAddBookView = findViewById(R.id.add_book_form);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            old_picture_path = cursor.getString(columnIndex);
            cursor.close();

            Bitmap mPic = ImageUtil.decodeSampledBitmapFromFilePath(old_picture_path,150,100);
//            new_picture_path = ImageUtil.compressBmpToFile(mPic);
//            Bitmap mNewPic = BitmapFactory.decodeFile(new_picture_path);
            mBookPicView.setImageBitmap(mPic);
            Log.d(TAG,old_picture_path);

        }
    }

    private void attempAddBook() {
        mBookNameView.setError(null);
        mBookAuthorView.setError(null);
        mBookPublisherView.setError(null);
        mBookISBNView.setError(null);


        String book_name = mBookNameView.getText().toString();
        String book_author = mBookAuthorView.getText().toString();
        String book_publisher = mBookPublisherView.getText().toString();
        String book_ISBN = mBookISBNView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(book_name)){
            mBookNameView.setError(getString(R.string.error_field_required));
            focusView = mBookNameView;
            cancel = true;
        }
        if(TextUtils.isEmpty(book_author)){
            mBookAuthorView.setError(getString(R.string.error_field_required));
            focusView = mBookAuthorView;
            cancel = true;
        }
        if(TextUtils.isEmpty(book_publisher)){
            mBookPublisherView.setError(getString(R.string.error_field_required));
            focusView = mBookPublisherView;
            cancel = true;
        }
        if(TextUtils.isEmpty(book_ISBN)){
            mBookISBNView.setError(getString(R.string.error_field_required));
            focusView = mBookISBNView;
            cancel = true;
        }
        if (false) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            addBook();
            //跳转
            Intent intent = new Intent(AddBookActivity.this, UserBookInfoActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mAddBookView.setVisibility(show ? View.GONE : View.VISIBLE);
            mAddBookView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddBookView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mAddBookView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.book_name:
                    String userName = mBookNameView.getText().toString();
                    // Check for a valid userName
                    if (TextUtils.isEmpty(userName)) {
                        mBookNameView.setError(getString(R.string.error_field_required));
                    } else if (!RegexUtils.checkUsername(userName)) {
                        mBookNameView.setError(getString(R.string.error_invalid_user_name));
                    } else {

                    }
                    break;
            }
        }
    }

    private void addBook() {
        //向服务器传数据
        String test01 = "\\80\\96\\20170825_173204_357.jpg";
        String test = "/sdcard/shuquan/book_pic/9b71e48a-6973-422d-a23e-901e99f69d5b.jpg";
        HashMap<String, String> map = new HashMap<>();
//        map.put("bookName", mBookNameView.getText().toString());
//        map.put("author", mBookAuthorView.getText().toString());
//        map.put("publisher", mBookPublisherView.getText().toString());
//        map.put("publishDate", mBookPublishDateView.getText().toString());
//        map.put("price", mBookPriceView.getText().toString());
//        map.put("isbn", mBookISBNView.getText().toString());
//        map.put("bookPic",new_picture_path);
//        map.put("contentIntro", mBookContentIntroView.getText().toString());
//        map.put("authorIntro", mBookAuthorIntroView.getText().toString());
//        map.put("tags", mBookTag);
//        map.put("zhuangZhen", mBookZhuangZhen);

//        map.put("bookName","数理统计");
//        map.put("author", "施雨");
//        map.put("publisher", "西安交通大学出版社");
//        map.put("publishDate", "2005-01-01");
//        map.put("price", "123元");
//        map.put("isbn", "123456789");
//        map.put("bookPic",test);
//        map.put("contentIntro", "这是一本好书");
//        map.put("authorIntro", "金龙练了，吴疆没练");
//        map.put("tags", "文学");
//        map.put("zhuangZhen", "平装");
//
//        map.put("translateAuthor","文强");
//        map.put("pageNum","5页");
//        map.put("congShu","金龙客车丛书");
//        map.put("menu","自己编吧");
//
//        map.put("originName"," ");
//        map.put("score"," ");
//
//        MyApplication.setUrl_api("book/add");

//        HttpUtil.sendOkHttpPost(MyApplication.getUrl_api(), map, new okhttp3.Callback(){
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseData = response.body().string();
//                if(Utility.handleAddBookResponse(responseData)){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d(TAG,responseData);
//
//                        }
//                    });
//                }else{
//                    Log.d(TAG,responseData);
//                    Log.d(TAG,"添加未成功");
//                }
//            }
//        });
        Log.d(TAG,old_picture_path);
        OkHttpUtil.sendMultipart(old_picture_path);
    }
}
