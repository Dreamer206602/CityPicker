package com.tq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class HotCityGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<String>mCities;

    public HotCityGridAdapter(Context mContext) {
        this.mContext = mContext;
        mCities=new ArrayList<>();
        mCities.add("北京");
        mCities.add("上海");
        mCities.add("广州");
        mCities.add("深圳");
        mCities.add("杭州");
        mCities.add("南京");
        mCities.add("天津");
        mCities.add("武汉");
        mCities.add("重庆");
    }

    @Override
    public int getCount() {
        return mCities==null?0:mCities.size();
    }

    @Override
    public String getItem(int position) {
        return mCities==null?null:mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotCityViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_hot_city_gridview,parent,false);
            holder=new HotCityViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.tv_hot_city_name);
            convertView.setTag(holder);
        }else{
            holder= (HotCityViewHolder) convertView.getTag();
        }
        holder.name.setText(mCities.get(position));
        return convertView;
    }

    public static class HotCityViewHolder{
        TextView name;
    }
}
