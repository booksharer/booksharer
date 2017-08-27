package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.booksharer.R;
import com.booksharer.entity.BookInfo;
import com.booksharer.entity.UserBook;
import com.booksharer.util.ImageUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.OkHttpUtil;
import com.booksharer.util.RegexUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AddBookActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText mBookNameView, mBookAuthorView, mBookNumView, mBookPublisherView,
            mBookPublishDateView, mBookPriceView, mBookISBNView,
            mBookContentIntroView, mBookAuthorIntroView;

    private ImageView mBookPicView;

    private String mBookTag, mBookZhuangZhen,mFileName, mBookNum;
    private Spinner sTag,sZhuangZhen;

    private Button mAddBookButton,mAddBookPicButton;
    private View mProgressView,mAddBookView;

    private static final String TAG = "ADDBOOKACTIVITY";
    private static int RESULT_LOAD_IMAGE = 1;
    private static String old_picture_path, new_picture_path;

    private LinkedList<UserBook> mUserBooks = new LinkedList<UserBook>();

    public static SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        initView();
    }

    private void initView() {

        mSharedPreferences = getSharedPreferences("userBookRemember", Activity.MODE_PRIVATE);
        mUserBooks = MyApplication.getUserBooks();

        mBookNameView = (EditText) findViewById(R.id.book_name);
        mBookAuthorView = (EditText) findViewById(R.id.author_name);
        mBookNumView = (EditText) findViewById(R.id.book_num);
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
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            new_picture_path = ImageUtil.compressBmpToFile(mPic);

            mPic = BitmapFactory.decodeFile(new_picture_path);
            mBookPicView.setImageBitmap(mPic);
            Log.d(TAG,old_picture_path);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        setPreferences();
    }

    private void attempAddBook() {
        mBookNameView.setError(null);
        mBookAuthorView.setError(null);
        mBookPublisherView.setError(null);
        mBookISBNView.setError(null);
        mBookNumView.setError(null);
        mAddBookPicButton.setError(null);
        mBookPriceView.setError(null);


        String book_name = mBookNameView.getText().toString();
        String book_author = mBookAuthorView.getText().toString();
        String book_publisher = mBookPublisherView.getText().toString();
        String book_ISBN = mBookISBNView.getText().toString();
        String book_price = mBookPriceView.getText().toString();

        String book_num = mBookNumView.getText().toString();

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
        if(TextUtils.isEmpty(book_price)){
            mBookPriceView.setError(getString(R.string.error_field_required));
            focusView = mBookPriceView;
            cancel = true;
        }
        if(TextUtils.isEmpty(book_ISBN)){
            mBookISBNView.setError(getString(R.string.error_field_required));
            focusView = mBookISBNView;
            cancel = true;
        }
        if(TextUtils.isEmpty(book_num)){
            mBookNumView.setError(getString(R.string.error_field_required));
            focusView = mAddBookPicButton;
            cancel = true;
        }
        if( old_picture_path == null || old_picture_path.isEmpty()){
            mAddBookPicButton.setError(getString(R.string.error_field_required));
            focusView = mAddBookPicButton;
            cancel = true;
        }
        if (cancel) {
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
        BookInfo mBook = new BookInfo();
        mBook.setBookName(mBookNameView.getText().toString());
        mBook.setAuthor(mBookAuthorView.getText().toString());
        mBook.setPublisher(mBookPublisherView.getText().toString());
        mBook.setPublishDate(mBookPublishDateView.getText().toString());
        mBook.setPrice(mBookPriceView.getText().toString());
        mBook.setIsbn(mBookISBNView.getText().toString());
        mBook.setContentIntro(mBookContentIntroView.getText().toString());
        mBook.setAuthorIntro( mBookAuthorIntroView.getText().toString());
        mBook.setTags(mBookTag);
        mBook.setZhuangZhen(mBookZhuangZhen);

        mBookNum = mBookNumView.getText().toString();

        Log.d(TAG,new_picture_path);
        OkHttpUtil.sendMultipartBook(new_picture_path, mBook, mBookNum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setPreferences();
//        MyApplication.setUserBooks(MyApplication.getUserBooks());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setPreferences();
    }


    private void setPreferences() {
        SharedPreferences.Editor editor = AddBookActivity.mSharedPreferences.edit();
        LinkedList<UserBook> ubs = MyApplication.getUserBooks();
        if(ubs.size() > 0){
            UserBook ub =  MyApplication.getUserBooks().getLast();
            editor.putString("lastBookInfoId",ub.getBookInfoId());
            editor.putString("userId",ub.getUserId());
            editor.putString("count",ub.getCount());

            int length = ubs.size();
            Set<String> bookInfoIds = new HashSet<String>();
            for(int i = 0; i < length; i++){
                bookInfoIds.add(ubs.get(i).getBookInfoId());
            }
            editor.putStringSet("bookInfoIds",bookInfoIds);
            editor.commit();
        }

    }
}
