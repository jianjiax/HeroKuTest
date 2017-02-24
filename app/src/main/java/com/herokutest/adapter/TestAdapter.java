package com.herokutest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.herokutest.AppApplication;
import com.herokutest.R;
import com.herokutest.model.TestModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by xufangqiang on 2017/2/24.
 */

public class TestAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<TestModel> dataList;

    private ViewHolder holder = null;

    private ImageLoader imageLoader;

    public TestAdapter(Context context,ArrayList<TestModel> dataList){
        this.context = context;
        this.dataList = dataList;
        imageLoader = ImageLoader.getInstance();
    }

    public void setData(ArrayList<TestModel> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_test, null);
            holder.image_iv = (ImageView) convertView.findViewById(R.id.image_iv);
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.desc_tv = (TextView) convertView.findViewById(R.id.desc_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TestModel testModel = dataList.get(position);
        imageLoader.displayImage(testModel.getImageHref(), holder.image_iv, AppApplication.options);
        holder.title_tv.setText(testModel.getTitle());
        holder.desc_tv.setText(testModel.getDescription());
        return convertView;
    }


    class ViewHolder {
        ImageView image_iv;
        TextView title_tv;
        TextView desc_tv;

    }
}
