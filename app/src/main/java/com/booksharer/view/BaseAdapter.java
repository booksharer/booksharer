package com.booksharer.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/5/27.
 */

/**
 * 与数据源相关的字段和方法封装在父类中
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {
    protected List<T> dataSet = new ArrayList<>();


    public void updateData(List dataSet) {
        this.dataSet.clear();
        appendData(dataSet);
    }

    public void appendData(List dataSet) {
        if (dataSet != null && !dataSet.isEmpty()) {
            this.dataSet.addAll(dataSet);
            Log.d("test", " "+dataSet.size());
            notifyDataSetChanged();
        }
    }

    public List<T> getDataSet() {
        return dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

