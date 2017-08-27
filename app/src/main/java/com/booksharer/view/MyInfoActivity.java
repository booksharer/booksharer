package com.booksharer.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.booksharer.R;
import com.booksharer.entity.User;
import com.booksharer.util.MyApplication;

public class MyInfoActivity extends AppCompatActivity {
    private TextView username,phone,nickname;
    private View mBook,mBookIn,mBookOut,mBookCircle;
    private static final String TAG = "MyInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initView();
    }

    private void initView() {
        User user = MyApplication.getUser();
        username = (TextView) findViewById(R.id.username);
        phone = (TextView) findViewById(R.id.phone);
        nickname = (TextView) findViewById(R.id.nickname);
        mBook = (View)findViewById(R.id.mybook);
        mBookIn = (View)findViewById(R.id.mybookin);
        mBookOut = (View)findViewById(R.id.mybookout);
        mBookCircle = (View)findViewById(R.id.mybookcircle);


        if(user.getUserName() != null){
            username.setText(user.getUserName());
        }else{
            username.setText("");
        }

        if(user.getPhone() != null){
            phone.setText(user.getPhone());
        }else{
            phone.setText("");
        }

        if(user.getNickName() != null){
            nickname.setText(user.getNickName());
        }else{
            nickname.setText("");
        }

        mBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click my book view");
//                Intent intent = new Intent(MyInfoActivity.this,MyBook.class);
//                startActivity(intent);
            }
        });

        mBookIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click my bookin view");
//                Intent intent = new Intent(MyInfoActivity.this,MyBookIn.class);
//                startActivity(intent);
            }
        });

        mBookOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click my bookout view");
//                Intent intent = new Intent(MyInfoActivity.this,MyBookOut.class);
//                startActivity(intent);
            }
        });

        mBookCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click my bookcircle view");
//                Intent intent = new Intent(MyInfoActivity.this,MyBookCircle.class);
//                startActivity(intent);
            }
        });
    }
}
