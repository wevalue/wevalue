package com.wevalue.ui.world.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.ChannelBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.adapter.MyWishGridAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuhua on 2016/8/23.
 */
public class MyWishActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {

    private ImageView iv_back;
    private TextView tv_head_title;
    private EditText ed_wish;
    private TextView tv_xuyuan;
    private GridView gv_wish;
    private List<String> data;
    private MyWishGridAdapter adapter;
    //用户填写的心愿单内容
    String userlike;
    private List<ChannelBean.Channel> userChannelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);
        initView();
        getMyWish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(MyWishActivity.this, StatisticsConsts.event_makeWishes);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        ed_wish = (EditText) findViewById(R.id.ed_wish);
        tv_xuyuan = (TextView) findViewById(R.id.tv_xuyuan);
        gv_wish = (GridView) findViewById(R.id.gv_wish);

        ed_wish.getText().toString().trim();
        ed_wish.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 5) {
                    ShowUtil.showToast(MyWishActivity.this, "五个字以内");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        iv_back.setOnClickListener(this);
        tv_xuyuan.setOnClickListener(this);
        tv_head_title.setText("心愿单");
        userChannelList = new ArrayList<>();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_xuyuan:
                userlike = ed_wish.getText().toString();
                if (userlike.isEmpty()) {
                    ShowUtil.showToast(this, "许愿内容不能为空！");
                    return;
                } else if (data != null && data.contains(userlike)) {
                    ShowUtil.showToast(this, "该许过该心愿了！");
                    return;
                }
                if (!TextUtils.isEmpty(SharedPreferencesUtil.getAllChannel(MyWishActivity.this)) && SharedPreferencesUtil.getAllChannel(MyWishActivity.this).contains(userlike)) {
                    ShowUtil.showToast(MyWishActivity.this, "已经存在该分类了哦！");
                    return;
                }
                HashMap<String, Object> map = new HashMap();
                map.put("code", RequestPath.CODE);
                map.put("userid", SharedPreferencesUtil.getUid(this));
                LogUtils.e(SharedPreferencesUtil.getUid(this));
                map.put("userlike", userlike);
                NetworkRequest.postRequest(RequestPath.POST_WISH, map, this);
                break;
            case R.id.ed_wish:
                break;
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            //发送我的心愿单
            case RequestPath.POST_WISH:
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        ShowUtil.showToast(this, obj.getString("message"));
                        MobclickAgent.onEvent(MyWishActivity.this, StatisticsConsts.event_wishesOk);
                        finish();
                    } else {
                        LogUtils.e(obj.getString("message"));
                        ShowUtil.showToast(this, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.GET_WISH:
                //获取用户的心愿单json串
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        data = new ArrayList<>();
                        JSONArray dataArray;
                        dataArray = obj.getJSONArray("data");
                        if (dataArray != null && dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsondata = dataArray.getJSONObject(i);
                                data.add(jsondata.getString("userlike"));
                            }
                            //绑定我的心愿单适配器
                            adapter = new MyWishGridAdapter(this, data);
                            adapter.notifyDataSetChanged();
                            gv_wish.setAdapter(adapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.e(content);
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(this, content);
    }

    //获取我的心愿单
    private void getMyWish() {
        HashMap<String, String> map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.getRequest(RequestPath.GET_WISH, map, this);
    }
}
