package com.tq.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tq.R;
import com.tq.widget.SideLetterBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CitySelectActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.iv_search_clear)
    ImageView ivSearchClear;
    @Bind(R.id.listview_all_city)
    ListView listviewAllCity;
    @Bind(R.id.tv_letter_overlay)
    TextView tvLetterOverlay;
    @Bind(R.id.listview_search_result)
    ListView listviewSearchResult;
    @Bind(R.id.empty_view)
    LinearLayout emptyView;
    @Bind(R.id.side_letter_bar)
    SideLetterBar sideLetterBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
    }
}
