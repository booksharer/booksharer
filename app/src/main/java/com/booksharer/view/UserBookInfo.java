package com.booksharer.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.booksharer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBookInfo extends AppCompatActivity {

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;

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

        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
//        getData();
        //新建适配器
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.user_book_list_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
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
