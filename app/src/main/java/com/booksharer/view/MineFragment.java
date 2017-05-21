package com.booksharer.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booksharer.R;

public class MineFragment extends Fragment implements View.OnClickListener {

    private View user;
    private TextView name;
    private View credit_card;
    private View view;

    private static final int REQUEST_POLICY = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        String sname = preferences.getString("name", "用户名");
        //显示用户名
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        name = (TextView)view.findViewById(R.id.name);
        //name.setText(sname);

//        user = view.findViewById(R.id.username);
//        user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
//                startActivity(intent);
//            }
//        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();


        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        view.findViewById(R.id.view_policy).setOnClickListener(this);
//        view.findViewById(R.id.view_policy2).setOnClickListener(this);
//        view.findViewById(R.id.view_policy3).setOnClickListener(this);
//        view.findViewById(R.id.view_policy4).setOnClickListener(this);
//        view.findViewById(R.id.view_policy5).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
//        ((MainActivity) getActivity()).setCurrentTabByTag("保单");
//        switch (v.getId()) {
//            case R.id.view_policy:
//                ((MainActivity) getActivity()).setPolicyTitle(0);
//                break;
//            case R.id.view_policy2:
//                ((MainActivity) getActivity()).setPolicyTitle(1);
//                break;
//            case R.id.view_policy3:
//                ((MainActivity) getActivity()).setPolicyTitle(2);
//                break;
//            case R.id.view_policy4:
//                ((MainActivity) getActivity()).setPolicyTitle(3);
//                break;
//            case R.id.view_policy5:
//                ((MainActivity) getActivity()).setPolicyTitle(4);
//                break;
//        }
    }
}
/**
 * 查主题背景色方法
 */
//    TypedArray array = getActivity().getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorBackground,android.R.attr.textColorPrimary});
//    int bc = array.getColor(0,0xff00ff);
////int tc = array.getColor(1,0xff00ff);
//array.recycle();
//        Log.d("life",         Integer.toHexString(bc)
//        +"");