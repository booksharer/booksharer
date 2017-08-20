package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Response;

import com.booksharer.R;
import com.booksharer.entity.User;
import com.booksharer.util.HttpUtil;
import com.booksharer.util.MyApplication;
import com.booksharer.util.RegexUtils;
import com.booksharer.util.Utility;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by DELL on 2017/5/22.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener {

//    private static final String APPKEY = "165330232451a";
//    private static final String APPSECRET = "478eaddde32096a52c888c7cbecd669f";

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private EditText mPasswordView2;
    private EditText mNicknameView;
    private View mProgressView;
    private View mSignUpFormView;
    private static String phoneNum;
    private static final String TAG="RegisterActivity";
    int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        使用验证码获得手机号码
        Intent intent=getIntent();
        phoneNum = intent.getStringExtra("phone");
        Log.d(TAG,phoneNum);
        isPhoneAvailable(phoneNum);
        initView();
    }


    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.user_name);
        mUserNameView.setOnFocusChangeListener(this);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView2 = (EditText) findViewById(R.id.password2);
        mNicknameView = (EditText) findViewById(R.id.nickname);
        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });
        mSignUpFormView = findViewById(R.id.sign_up_form);
        mProgressView = findViewById(R.id.sign_up_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);
        mPasswordView2.setError(null);
        mNicknameView.setError(null);

        // Store values at the time of the login attempt.

        String password = mPasswordView.getText().toString();
        String password2 = mPasswordView2.getText().toString();
        String nickname = mNicknameView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!RegexUtils.checkPassword(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password_form));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password2) || !isPasswordValid(password2)) {
            mPasswordView2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView2;
            cancel = true;
        }

        if (!RegexUtils.checkPassword(password)) {
            mPasswordView2.setError(getString(R.string.error_invalid_password_form));
            focusView = mPasswordView2;
            cancel = true;
        }

        if (!password2.equals(password)) {
            mPasswordView2.setError(getString(R.string.error_different_password));
            focusView = mPasswordView2;
            cancel = true;
        }

        if(TextUtils.isEmpty(nickname)){
            mNicknameView.setError(getString(R.string.error_field_required));
            focusView = mNicknameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            register();
            //跳转
            Intent intent = new Intent(RegisterActivity.this, MineFragment.class);
            startActivity(intent);
            finish();
        }
    }

    private void register() {
        //向服务器传送数据
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", mUserNameView.getText().toString());
        map.put("password", mPasswordView.getText().toString());
        map.put("phone", phoneNum);
        map.put("nickName", mNicknameView.getText().toString());
        map.put("position", PreferenceManager.getDefaultSharedPreferences(this).getString("position", "0.0,0.0"));

        final User user = new User();
        user.setUserName(mUserNameView.getText().toString());
        user.setPassword(mPasswordView.getText().toString());
        user.setPhone(phoneNum);
        user.setNickName(mNicknameView.getText().toString());
        user.setPosition(PreferenceManager.getDefaultSharedPreferences(this).getString("position", "0.0,0.0"));
        MyApplication.setUser(user);
        MyApplication.setUrl_api("/user/register");
        HttpUtil.sendOkHttpPost(MyApplication.getUrl_api(), map, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (Utility.handleRegisterResponse(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPasswordView.getText().clear();
                            mPasswordView2.getText().clear();
                            mNicknameView.getText().clear();
                            Intent intent = new Intent(RegisterActivity.this, MineFragment.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d(TAG,"注册未成功");
//                    Toast.makeText(MyApplication.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPhoneAvailable(String phone) {
        //TODO: Replace this with your own logic
        MyApplication.setUrl_api("/user/findrepeate?phone=" + phone);
        HttpUtil.sendOkHttpRequest(MyApplication.getUrl_api(), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (!Utility.handleFindRepeateResponse(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("系统提示")//设置对话框标题
                                    .setMessage("该手机号已经注册，是否直接登录？")
                                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.putExtra("phone", phoneNum);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//响应事件
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();//在按键响应事件中显示此对话框  //设置显示的内容
                        }
                    });
                }
            }
        });
        return true;
    }

    private boolean isUserNameAvailable(String userName) {
        //TODO: Replace this with your own logic
        MyApplication.setUrl_api("/user/findrepeate?userName=" + userName);
        HttpUtil.sendOkHttpRequest(MyApplication.getUrl_api(), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (!Utility.handleFindRepeateResponse(responseData)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mUserNameView.setError(getString(R.string.error_repeated_user_name));
                            mUserNameView.requestFocus();
                        }
                    });
                }
            }
        });
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.user_name:
                    String userName = mUserNameView.getText().toString();
                    // Check for a valid userName
                    if (TextUtils.isEmpty(userName)) {
                        mUserNameView.setError(getString(R.string.error_field_required));
                    } else if (!RegexUtils.checkUsername(userName)) {
                        mUserNameView.setError(getString(R.string.error_invalid_user_name));
                    } else {
                        if(!isUserNameAvailable(userName)){
                            mUserNameView.setError(getString(R.string.error_repeated_user_name));
                            mUserNameView.requestFocus();
                        }
                    }
                    break;
            }
        }
    }
}