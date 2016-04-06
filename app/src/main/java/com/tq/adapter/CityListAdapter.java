package com.tq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tq.R;
import com.tq.model.City;
import com.tq.model.LocateState;
import com.tq.utils.PinyinUtils;
import com.tq.widget.WrapHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class CityListAdapter extends BaseAdapter {

    public static final int VIEW_TYPE_COUNT=3;
    private Context mContext;
    private LayoutInflater inflater;
    private List<City>mCities;
    private HashMap<String,Integer>letterIndexes;
    private String[]sections;
    private OnCityClickListener onCityClickListener;
    private int locateState= LocateState.LOCATING;
    private String locatedCity;

    public CityListAdapter(Context mContext, List<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.inflater=LayoutInflater.from(mContext);
        if(mCities==null){
            mCities=new ArrayList<>();
        }
        mCities.add(0,new City("定位","0"));
        mCities.add(1,new City("热门","1"));
        int size=mCities.size();
        letterIndexes=new HashMap<>();
        sections=new String[size];
        for (int i = 0; i <size ; i++) {
                //当前城市拼音的首字母
            String currentLetter= PinyinUtils.getFirstLetter(mCities.get(i).getPinyin());
            //上个首字母，如果不存在设为“”
            String previousLetter=i>=1?PinyinUtils.getFirstLetter(mCities.get(i-1).getPinyin()):"";
            if(!TextUtils.equals(currentLetter,previousLetter)){
                letterIndexes.put(currentLetter,i);
                sections[i]=currentLetter;
            }
        }
    }

    /**
     * 更新定位信息
     * @param state
     * @param city
     */
    public void updateLocateState(int state,String city){
        this.locateState=state;
        this.locatedCity=city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter){
        Integer integer=letterIndexes.get(letter);
        return integer==null?-1:integer;
    }


    @Override
    public int getCount() {
        return mCities==null?0:mCities.size();
    }

    @Override
    public  int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position<VIEW_TYPE_COUNT-1?position:VIEW_TYPE_COUNT-1;
    }


    @Override
    public Object getItem(int position) {
        return mCities==null?null:mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CityViewHolder holder;
        int viewType=getItemViewType(position);
        switch (viewType){
            case 0://定位
                convertView=inflater.inflate(R.layout.view_locate_city,parent,false);
                ViewGroup container = (ViewGroup) convertView.findViewById(R.id.layout_locate);
                TextView state = (TextView) convertView.findViewById(R.id.tv_located_city);
                switch (locateState){
                    case LocateState.LOCATING:
                        state.setText(mContext.getString(R.string.locating));
                        break;
                    case LocateState.FAILED:
                        state.setText(R.string.located_failed);
                        break;
                    case LocateState.SUCCESS:
                        state.setText(locatedCity);
                        break;
                }
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(locateState==LocateState.FAILED){
                            //重新定位
                            if(onCityClickListener!=null){
                                onCityClickListener.onLocateClick();
                            }
                        }else if(locateState==LocateState.SUCCESS){
                            //返回定位的城市
                            if(onCityClickListener!=null){
                                onCityClickListener.onCityClick(locatedCity);
                            }
                        }
                    }
                });
                break;
            case 1://热门
                convertView=inflater.inflate(R.layout.view_hot_city,parent,false);
                WrapHeightGridView gridView = (WrapHeightGridView) convertView.findViewById(R.id.gridview_hot_city);
                final HotCityGridAdapter hotCityGridAdapter=new HotCityGridAdapter(mContext);
                gridView.setAdapter(hotCityGridAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(onCityClickListener!=null){
                            onCityClickListener.onCityClick(hotCityGridAdapter.getItem(position));
                        }
                    }
                });
                break;
            case 2://所有
                if(convertView==null){
                    convertView=inflater.inflate(R.layout.item_city_listview,parent,false);
                    holder=new CityViewHolder();
                    holder.letter= (TextView) convertView.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name= (TextView) convertView.findViewById(R.id.tv_item_city_listview_name);
                    convertView.setTag(holder);
                }else{
                    holder= (CityViewHolder) convertView.getTag();
                }
                if(position>1){
                    final String city=mCities.get(position).getName();
                    holder.name.setText(city);
                    String currentLetter=PinyinUtils.getFirstLetter(mCities.get(position).getPinyin());
                    String previousLetter=position>=1?PinyinUtils.getFirstLetter(mCities.get(position-1).getPinyin()):"";
                    if(!TextUtils.equals(currentLetter,previousLetter)){
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.letter.setText(currentLetter);
                    }else{
                        holder.letter.setVisibility(View.GONE);
                    }
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onCityClickListener!=null){
                                onCityClickListener.onCityClick(city);
                            }
                        }
                    });
                }
        }

        return convertView;
    }

    public static class CityViewHolder{
        TextView letter;
        TextView name;
    }

    public interface  OnCityClickListener{
        void onCityClick(String name);
        void onLocateClick();
    }

    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }
}
