package com.booksharer.view;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.booksharer.R;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.RegexUtils;
import com.booksharer.util.Utility;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class FindBookAcitivity extends AppCompatActivity {

    private EditText mFindBookByName, mFindBookByIsbn;
    private Button mBookNameBtn, mBookIsbnBtn;

    private static final String TAG = "FindBookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_book_acitivity);

        mFindBookByName = (EditText) findViewById(R.id.find_by_book_name);
        mFindBookByIsbn = (EditText) findViewById(R.id.find_by_book_isbn);

        mBookNameBtn = (Button) findViewById(R.id.btn_find_name);
        mBookIsbnBtn = (Button) findViewById(R.id.btn_find_isbn);

        mBookNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempFindByName();
            }
        });

        mBookIsbnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempFindByIsbn();
            }
        });
    }

    private void attempFindByName() {
        mFindBookByName.setError(null);
        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(mFindBookByName.getText().toString())){
            mFindBookByName.setError(getString(R.string.book_name_hint));
            focusView = mFindBookByName;
            cancel = true;
        } if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            findByName();
        }

    }

    private void findByName() {
        HashMap<String, String> map = new HashMap<>();
        map.put("bookName", mFindBookByName.getText().toString());
        map.put("pageSize", "15");
        MyApplication.setUrl_api("/book/find");
        HttpUtil.sendOkHttpPost(MyApplication.getUrl_api(), map, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (Utility.handleFindBookResponse(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFindBookByName.getText().clear();
                            Intent intent = new Intent(FindBookAcitivity.this, FindBookResultAcitivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d(TAG,"查找未成功");
                }
            }
        });
    }

    private void attempFindByIsbn() {
        mFindBookByIsbn.setError(null);
        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(mFindBookByIsbn.getText().toString())){
            mFindBookByIsbn.setError(getString(R.string.book_isbn_hint));
            focusView = mFindBookByIsbn;
            cancel = true;
        } if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            findByIsbn();
        }
    }

    private void findByIsbn() {

        HashMap<String, String> map = new HashMap<>();
        map.put("isbn", mFindBookByIsbn.getText().toString());
        map.put("pageSize", "15");
        MyApplication.setUrl_api("/book/find");
        HttpUtil.sendOkHttpPost(MyApplication.getUrl_api(), map, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (Utility.handleFindBookResponse(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFindBookByIsbn.getText().clear();
                            Intent intent = new Intent(FindBookAcitivity.this, FindBookResultAcitivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Log.d(TAG,"查找未成功");
                }
            }
        });

    }
}
