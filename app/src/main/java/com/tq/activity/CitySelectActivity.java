package com.tq.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tq.R;
import com.tq.adapter.CityListAdapter;
import com.tq.adapter.ResultListAdapter;
import com.tq.db.DBManager;
import com.tq.model.City;
import com.tq.model.LocateState;
import com.tq.utils.StringUtils;
import com.tq.utils.ToastUtils;
import com.tq.widget.SideLetterBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CitySelectActivity extends AppCompatActivity implements View.OnClickListener {

    //ImageView
    @Bind(R.id.back)
    ImageView backBtn;
    @Bind(R.id.iv_search_clear)
    ImageView clearBtn;

    //TextView
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.tv_letter_overlay)
    TextView overlay;

    @Bind(R.id.et_search)
    EditText searchBox;

    //ListView
    @Bind(R.id.listview_all_city)
    ListView mListView;
    @Bind(R.id.listview_search_result)
    ListView mResultListView;


    @Bind(R.id.empty_view)
    LinearLayout emptyView;

    @Bind(R.id.side_letter_bar)
    SideLetterBar mLetterBar;

    //private ViewGroup emptyView;
    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City>mAllCities;
    private DBManager dbManager;
    private AMapLocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        
        initData();
        initView();
        initLocation();
    }

    private void initLocation() {

        mLocationClient=new AMapLocationClient(this);
        AMapLocationClientOption option=new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode()==0){
                        String city=aMapLocation.getCity();
                        String district=aMapLocation.getDistrict();
                        String location= StringUtils.extractLocation(city,district);
                        mCityAdapter.updateLocateState(LocateState.SUCCESS,location);
                    }else{
                        //定位失败
                        mCityAdapter.updateLocateState(LocateState.FAILED,null);
                    }
                }
            }
        });
        mLocationClient.startLocation();

    }

    private void initView() {

        mListView.setAdapter(mCityAdapter);

        //右侧的栏
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangeListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position=mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    String keyword=s.toString();
                if(TextUtils.isEmpty(keyword)){
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                }else{
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City>result=dbManager.searchCity(keyword);
                    if(result==null||result.size()==0){
                        emptyView.setVisibility(View.VISIBLE);
                    }else{
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }

                }

            }
        });
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    private void initData() {
        dbManager=new DBManager(this);
        dbManager.copyDBFile();
        mAllCities=dbManager.getAllCities();
        mCityAdapter=new CityListAdapter(this,mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                    back(name);
            }

            @Override
            public void onLocateClick() {
                Log.e("OnLocateClick","重新定位....");
                mCityAdapter.updateLocateState(LocateState.LOCATING,null);
                mLocationClient.startAssistantLocation();
            }
        });
        mResultAdapter=new ResultListAdapter(this,null);

    }
    private void back(String city){
        ToastUtils.showToast(this,"点击的城市："+city);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }
}
