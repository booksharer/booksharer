package com.booksharer.view;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.booksharer.R;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    // 定义一个布局
    private LayoutInflater layoutInflater;
    // 定义FragmentTabHost对象
    private FragmentTabHost mTabHost;
    // 定义数组来存放Fragment界面
    private Class fragmentArray[] = {BookCommunityFragment.class,
            MineFragment.class};
    // Tab选项卡的文字
    private String mTextViewArray[] = {
            "书圈",   "我的"
    };
    // 定义数组来存放按钮图片
    private int mImageViewArray[] = {
            R.drawable.main_tab_item_home,
            R.drawable.main_tab_item_mine};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        initView();

        requestLocation();
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }


    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);

        //去分割线
        final TabWidget tabWidget = mTabHost.getTabWidget();
        //tabWidget.setStripEnabled(false);
        tabWidget.setDividerDrawable(null);


        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i])
                    .setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
//            // 设置Tab按钮的背景
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(1);
//                    .setBackgroundResource(R.drawable.main_tab_item_bg);
        }
    }  /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);

        return view;
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder curremtPosition = new StringBuilder();
            curremtPosition.append("维度").append(bdLocation.getLatitude())
                    .append("\n");
            curremtPosition.append("经度").append(bdLocation.getLongitude())
                    .append("\n");
            curremtPosition.append("定位方式");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                curremtPosition.append("GPS");
            }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                curremtPosition.append("网络");
            }else if (bdLocation.getLocType() == BDLocation.TypeCacheLocation) {
                curremtPosition.append("缓存");
            }
                Log.d("test", curremtPosition.toString()+bdLocation.getAddrStr());
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(MainActivity.this).edit();
            editor.putString("city", bdLocation.getCity())
                    .putString("position", bdLocation.getLatitude()+","+bdLocation.getLongitude())
                    .apply();
        }
    }
}
