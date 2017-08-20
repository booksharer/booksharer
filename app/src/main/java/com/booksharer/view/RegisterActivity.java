package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
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
    private EditText mNicknameView;
    private View mProgressView;
    private View mSignUpFormView;
    private String phoneNum;
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

        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked") HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    phoneNum = phone;
                }
            }
        });
        //check repeat phone
        registerPage.show(RegisterActivity.this);

        ////注册回调
        SMSSDK.initSDK(RegisterActivity.this, APPKEY, APPSECRET);
        SMSSDK.registerEventHandler(eventHandler);
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

        // Store values at the time of the login attempt.

        String password = mPasswordView.getText().toString();
        String password2 = mPasswordView2.getText().toString();

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
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            register();
            //跳转
            finish();
        }
    }

    //防止内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            super.afterEvent(event, result, data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                    showProgress(true);
//                    register();
//                    //跳转
//                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    private void register() {
        //向服务器传送数据
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", mUserNameView.getText().toString());
        map.put("password", mPasswordView.getText().toString());
        map.put("phone", phoneNum);
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