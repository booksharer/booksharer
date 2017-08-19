package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

import com.booksharer.R;
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

    private static final String APPKEY = "165330232451a";
    private static final String APPSECRET = "478eaddde32096a52c888c7cbecd669f";

    // UI references.
    private EditText mUserNameView;
    private EditText mPasswordView;
    private EditText mPasswordView2;
    private EditText mPhoneView;
    private Button mGetVerificationCode;
    private EditText mVerificationCode;
    private EditText mNicknameView;
    private View mProgressView;
    private View mSignUpFormView;
    int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.user_name);
        mUserNameView.setOnFocusChangeListener(this);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView2 = (EditText) findViewById(R.id.password2);
        mPhoneView = (EditText) findViewById(R.id.phone);
        mGetVerificationCode = (Button) findViewById(R.id.get_verification_code);
        mGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mPhoneView.getText().toString().trim();

                // Check for a valid phone // 1. 通过规则判断手机号
                if (TextUtils.isEmpty(phone)) {
                    mPhoneView.setError(getString(R.string.error_field_required));
                    return;
                } else if (!RegexUtils.checkPhoneNum(phone)) {
                    mPhoneView.setError(getString(R.string.error_invalid_phone));
                    return;
                } else if (!isPhoneExist(phone)) {
                    mPhoneView.setError(getString(R.string.error_repeated_phone));
                    return;
                }

                // 启动短信验证sdk
                SMSSDK.initSDK(RegisterActivity.this, APPKEY, APPSECRET);

                EventHandler eventHandler = new EventHandler() {
                    @Override
                    public void afterEvent(int event, int result, Object data) {
                        Message msg = new Message();
                        msg.arg1 = event;
                        msg.arg2 = result;
                        msg.obj = data;
                        handler.sendMessage(msg);
                    }
                };
                //注册回调监听接口
                SMSSDK.registerEventHandler(eventHandler);

                // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phone);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                mGetVerificationCode.setClickable(false);
                mGetVerificationCode.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
            }
        });
        mVerificationCode = (EditText) findViewById(R.id.verification_code);
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
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.

        String password = mPasswordView.getText().toString();
        String password2 = mPasswordView2.getText().toString();
        String phone = mPhoneView.getText().toString();
        String verificationCode = mVerificationCode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
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
        if (!TextUtils.isEmpty(password2) && !isPasswordValid(password2)) {
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
        if (!TextUtils.isEmpty(verificationCode) && !TextUtils.isEmpty(phone)) {
            mVerificationCode.setError(getString(R.string.error_field_required));
            focusView = mVerificationCode;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            SMSSDK.submitVerificationCode("86", phone, mGetVerificationCode
                    .getText().toString());
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mGetVerificationCode.setText("重新发送(" + i + ")");
                mGetVerificationCode.setClickable(false);
            } else if (msg.what == -8) {
                mGetVerificationCode.setText("获取验证码");
                mGetVerificationCode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(RegisterActivity.this, "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        // Show a progress spinner, and kick off a background task to
                        // perform the user login attempt.
                        showProgress(true);
                        register();
                        //跳转
                        finish();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(RegisterActivity.this, "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    private void register() {
        //向服务器传送数据
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", mUserNameView.getText().toString());
        map.put("password", mPasswordView.getText().toString());
        map.put("phone", mPhoneView.getText().toString());
        map.put("nickname", mNicknameView.getText().toString());
        map.put("position", PreferenceManager.getDefaultSharedPreferences(this).getString("position", "0.0,0.0"));
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
                            mPhoneView.getText().clear();
                            mVerificationCode.getText().clear();
                            mNicknameView.getText().clear();
                        }
                    });
                } else {
                    Toast.makeText(MyApplication.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private boolean isPhoneValid(String phone) {
//        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        return !TextUtils.isEmpty(phone) && phone.matches(telRegex);
//    }

    private boolean isPhoneExist(String phone) {
        final boolean[] state = {false};
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
                            mPhoneView.setError(getString(R.string.error_repeated_phone));
                            mPhoneView.getText().clear();
                            mPhoneView.requestFocus();
                        }
                    });
                }
            }
        });
        return state[0];
    }

//    private boolean isUserNameValid(String userName) {
//        //TODO: Replace this with your own logic
//        return userName.matches("[a-zA-Z][a-zA-Z0-9]{3,15}");
//    }

    private void isUserNameExist(String userName) {
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
                        isUserNameExist(userName);
                    }
                    break;
            }
        }
    }
}