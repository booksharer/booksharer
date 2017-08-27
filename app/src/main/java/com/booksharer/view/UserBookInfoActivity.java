package com.booksharer.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.booksharer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserBookInfoActivity extends AppCompatActivity {

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private Button addBook,deleteBook;

    private static final String TAG = "UserBookInfoActivity";

//    private int[] icon = { R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus,
//            R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus,
//            R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus,
//            R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus, R.drawable.main_tab_item_mine_focus,
//            R.drawable.main_tab_item_mine_focus };
//    private String[] iconName = { "通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声",
//            "设置", "语音", "天气", "浏览器", "视频" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_info);

        checkPreferences();
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
//        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.list_item_user_book, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

        addBook = (Button) findViewById(R.id.addUserBook);
        deleteBook = (Button) findViewById(R.id.deleteUserBook);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserBookInfoActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkPreferences() {
        SharedPreferences mSP= getSharedPreferences("userBookRemember",Activity.MODE_PRIVATE);
        if(mSP != null){
            String bookInfoId = mSP.getString("lastBookInfoId", "");
            String userId = mSP.getString("userId", "");
            String count = mSP.getString("count","");
            Set<String> bookInfoIds = mSP.getStringSet("bookInfoIds",null);
            Log.d(TAG,bookInfoId);
            Log.d(TAG,userId);
            Log.d(TAG,count);
            Log.d(TAG,bookInfoIds.toString());
        }
    }

//    public List<Map<String, Object>> getData(){
//        //cion和iconName的长度是相同的，这里任选其一都可以
//        for(int i=0;i<icon.length;i++){
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("image", icon[i]);
//            map.put("text", iconName[i]);
//            data_list.add(map);
//        }
//
//        return data_list;
//    }
}
