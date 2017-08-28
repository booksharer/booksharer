package com.booksharer.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.booksharer.R;

import static android.R.attr.fragment;

public class FindBookResultAcitivity extends AppCompatActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private FindBookResultFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_book_result_acitivity);

        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        fragment = new FindBookResultFragment();

        View view = findViewById(R.id.result_container);
        transaction.add(R.id.result_container,fragment);

        transaction.commit();
    }
}
