package com.wevalue.ui.world.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.SortModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.adapter.ShiftCityAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.sortview.CityAdapter;
import com.wevalue.view.sortview.view.EditTextWithDel;
import com.wevalue.view.sortview.view.PinyinComparator;
import com.wevalue.view.sortview.view.PinyinUtils;
import com.wevalue.view.sortview.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ShiftCityActivity extends Activity implements WZHttpListener {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private ShiftCityAdapter adapter;
    private EditTextWithDel mEtCityName;
    private List<SortModel> SourceDateList;
    private List<String> cityNameList;
    private String[] zhimu = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private List<String> hotCityList;
    private TextView tv_head_title;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initViews();
    }

    private void initViews() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEtCityName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        String userlikeCity = SharedPreferencesUtil.getUserLikeCity(this);
        if (TextUtils.isEmpty(userlikeCity)) {
            userlikeCity = SharedPreferencesUtil.getLocationCity(this);
        }
        tv_head_title.setText("当前城市:" + userlikeCity);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        queryData();
    }

    private void setAdapter() {
        SourceDateList = filledData(cityNameList);
        Collections.sort(SourceDateList, new PinyinComparator());
        if (adapter == null) {
            adapter = new ShiftCityAdapter(this, SourceDateList);
            //ListView的点击事件
            sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LogUtils.e("position", position + "");
                    tv_head_title.setText("当前城市:" + SourceDateList.get(position).getName());
                    SharedPreferencesUtil.setUserLikeCity(ShiftCityActivity.this, cityNameList.get(position));
                    finish();
                }
            });

        }
//        sortListView.addHeaderView(initHeadView());
        sortListView.setAdapter(adapter);
        initDatas();
        initEvents();
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
        //根据输入框输入值的改变来过滤搜索
        mEtCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    private View initHeadView() {
        View headView = getLayoutInflater().inflate(R.layout.headview, null);
        Button btn_city_name = (Button) headView.findViewById(R.id.btn_city_name);
        btn_city_name.setText(SharedPreferencesUtil.getLocationCity(this).replace("市", ""));
        GridView mGvCity = (GridView) headView.findViewById(R.id.gv_hot_city);
        CityAdapter adapter = new CityAdapter(getApplicationContext(), R.layout.gridview_item, hotCityList);
        mGvCity.setAdapter(adapter);
        mGvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_head_title.setText("当前城市:" + hotCityList.get(position));
                SharedPreferencesUtil.setUserLikeCity(ShiftCityActivity.this, hotCityList.get(position));
                finish();
            }
        });
        btn_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_head_title.setText("当前城市:" + SharedPreferencesUtil.getLocationCity(ShiftCityActivity.this).replace("市", ""));
                SharedPreferencesUtil.setUserLikeCity(ShiftCityActivity.this, SharedPreferencesUtil.getLocationCity(ShiftCityActivity.this));
                finish();
            }
        });
        return headView;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = SourceDateList;
        } else {
            mSortList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    private List<SortModel> filledData(List<String> date) {
        List<SortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i));
            String pinyin = PinyinUtils.getPingYin(date.get(i));
            if (date.get(i).equals("重庆")) {
                //多音字自定义拼音
                pinyin = "C";
            }
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        paserJson(content);
        SharedPreferencesUtil.setHotCity(this, content);
    }

    @Override
    public void onFailure(String content) {

    }

    private void queryData() {
        String hoteCity = SharedPreferencesUtil.getHotCity(this);
        if (hoteCity.isEmpty()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", RequestPath.CODE);
            NetworkRequest.getRequest(RequestPath.GET_GETHOTCITY, map, this);
        } else {
            paserJson(hoteCity);
        }
    }

    private void paserJson(String content) {
        try {
            JSONObject obj = new JSONObject(content);
            if (obj.getString("result").equals("1")) {
                cityNameList = new ArrayList<>();
                JSONArray cityArray;
                for (int i = 0; i < zhimu.length; i++) {
                    cityArray = obj.getJSONArray(zhimu[i]);
                    if (cityArray != null && cityArray.length() > 0) {
                        for (int j = 0; j < cityArray.length(); j++) {
                            JSONObject city = cityArray.getJSONObject(j);
                            cityNameList.add(city.getString("cityname"));
                        }
                    }
                }
                hotCityList = new ArrayList<>();
                cityArray = obj.getJSONArray("hotcity");
                for (int i = 0; i < cityArray.length(); i++) {
                    JSONObject city = cityArray.getJSONObject(i);
                    hotCityList.add(city.getString("cityname"));
                }
            } else {
                ShowUtil.showToast(getApplicationContext(), obj.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapter();
    }
}
