package com.booksharer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.booksharer.R;
import com.booksharer.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{




    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        populateAutoComplete();




    }

    private void populateAutoComplete() {
        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences("search_history", 0);
        String history = sp.getString("history", "暂时没有搜索记录");

        // 用逗号分割内容返回数组
        String[] history_arr = history.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history_arr);
        mUserNameView.setAdapter(adapter);
    }

    private void initView() {
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.user_name);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid userName
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!isUseerNameValid(userName)) {
            mUserNameView.setError(getString(R.string.error_invalid_user_name));
            focusView = mUserNameView;
            cancel = true;
        }else {
            recordUserName(userName);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userName", mUserNameView.getText().toString());
            map.put("password", mPasswordView.getText().toString());
            map.put("action", "login");
            HttpUtil.sendOkHttpPost("", map, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                     mPasswordView.requestFocus();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("test", "responseData "+responseData);
                    finish();
                }
            });
//            mAuthTask = new UserLoginTask(userName, password);
//            mAuthTask.execute((Void) null);
        }
    }

    private void recordUserName(String userName) {
        SharedPreferences sharePreferences = getSharedPreferences("search_history", 0);
        String history = sharePreferences.getString("history", "暂时没有搜索记录");
        // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
        StringBuilder builder = new StringBuilder(history);
        builder.append(userName + ",");

        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
        if (!history.contains(userName)) {
            SharedPreferences.Editor myeditor = sharePreferences.edit();
            myeditor.putString("history", builder.toString());
            myeditor.commit();
        }

    }

    private boolean isUseerNameValid(String userName) {
        //TODO: Replace this with your own logic
        return userName.matches("[a-zA-Z][a-zA-Z0-9]{3,15}");
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mUserName;
//        private final String mPassword;
//
//        UserLoginTask(String userName, String password) {
//            mUserName = userName;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

