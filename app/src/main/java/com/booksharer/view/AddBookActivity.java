package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.BookInfo;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.RegexUtils;
import com.booksharer.util.Utility;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class AddBookActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private EditText mBookNameView, mBookAuthorView, mBookPublisherView,
            mBookPublishDateView, mBookPriceView, mBookISBNView,
            mBookContentIntroView, mBookAuthorIntroView;

    private String mBookTag, mBookZhuangZhen,mFileName;
    private Spinner sTag,sZhuangZhen;

    private Button mAddBookButton;
    private View mProgressView,mAddBookView;
    private  static final String TAG = "ADDBOOKACTIVITY";

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
        String bookName =  mBookNameView.getText().toString();
        String author = mBookAuthorView.getText().toString();
        String publisher = mBookPublisherView.getText().toString();
        String publishDate =  mBookPublishDateView.getText().toString();
        String price = mBookPriceView.getText().toString();
        String contentIntro = mBookContentIntroView.getText().toString();
        String authorIntro = mBookAuthorIntroView.getText().toString();
        String tags = mBookTag;
        String zhuangzhen = mBookZhuangZhen;
        String isbn = mBookISBNView.getText().toString();
        String filename = mFileName;

        HashMap<String, String> map = new HashMap<>();
        map.put("bookName", bookName);
        map.put("author", author);
        map.put("publisher", publisher);
        map.put("publishDate", publishDate);
        map.put("price", price);
        map.put("isbn", isbn);
        map.put("bookPic",filename);
        map.put("contentIntro", contentIntro);
        map.put("authorIntro", authorIntro);
        map.put("tags", tags);
        map.put("zhuangZhen", zhuangzhen);

//
//        final BookInfo book = new BookInfo();
//        book.setBookName(bookName);
//        book.setAuthor(author);
//        book.setPublisher(publisher);
//        book.setPublishDate(publishDate);
//        book.setPrice(price);
//        book.setContentIntro(contentIntro);
//        book.setAuthorIntro(authorIntro);
//        book.setTags(tags);
//        book.setZhuangZhen(zhuangzhen);
//        book.setIsbn(isbn);

        HttpUtil.sendOkHttpPost(MyApplication.getUrl_api(), map, new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if(Utility.handleAddBookResponse(responseData)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }else{
                    Log.d(TAG,"添加未成功");
                }
            }
        });
    }
}
