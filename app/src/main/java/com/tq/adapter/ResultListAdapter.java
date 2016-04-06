package com.tq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tq.R;
import com.tq.model.City;

import java.util.List;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class ResultListAdapter extends BaseAdapter {
    private Context mContext;
    private List<City>mCities;

    public ResultListAdapter(Context mContext, List<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
    }
    public void changeData(List<City>list){
        if(mCities==null){
            mCities=list;
        }else{
            mCities.clear();
            mCities.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCities==null?0:mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities==null?null:mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResultViewHolder holder;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_search_result_listview,parent,false);
            holder=new ResultViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.tv_item_result_listview_name);
            convertView.setTag(holder);
        }else{
            holder= (ResultViewHolder) convertView.getTag();
        }
        holder.name.setText(mCities.get(position).getName());
        return convertView;
    }

    public static class ResultViewHolder{
        TextView name;
    }
}
