package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.PaymentDetailModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.mine.adapter.PaymentDetailAdapter;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollListview;

import java.util.HashMap;
import java.util.List;

/**
 * 收支明細的詳情頁
 */
public class PaymentDetailsActivity extends BaseActivity implements WZHttpListener {
    private ImageView iv_back;
    private TextView tv_head_title;
    private NoScrollListview mNoScrollListview;
    private PullToRefreshScrollView prsv_ScrollView;
    private PaymentDetailAdapter adapter;
    private PaymentDetailModel mPaymentDetailModel;
    private List<PaymentDetailModel.DataBean> mDataBeanList;
    private final String pagenum = "10";//分頁大小
    private int pageindex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouzhi_mingxi);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        mNoScrollListview = (NoScrollListview) findViewById(R.id.mNoScrollListview);
        prsv_ScrollView = (PullToRefreshScrollView) findViewById(R.id.prsv_ScrollView);
        mNoScrollListview.setFocusable(false);
        tv_head_title.setText("收支明细");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addData();
        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                下拉
                pageindex = 1;
                addData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex++;
                addData();
//                上拉
            }
        });
    }

    private void addData() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("pageindex", String.valueOf(pageindex));
        map.put("pagenum", pagenum);
        NetworkRequest.postRequest(RequestPath.POST_PENDIST, map, this);
    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new PaymentDetailAdapter(this, mDataBeanList);
            mNoScrollListview.setAdapter(adapter);
            mNoScrollListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(PaymentDetailsActivity.this, PaymentDetails_DetailsActivity.class);
                    intent.putExtra("orderno", mDataBeanList.get(position).getOrderno());
                    intent.putExtra("ordertype", mDataBeanList.get(position).getOrdertype());
                    intent.putExtra("money", mDataBeanList.get(position).getMoney() + "");
                    LogUtils.e("money", mDataBeanList.get(position).getMoney() + "");
                    intent.putExtra("title", mDataBeanList.get(position).getTitle());
                    intent.putExtra("addtime", mDataBeanList.get(position).getAddtime());
                    intent.putExtra("type", mDataBeanList.get(position).getType() + "");
                    intent.putExtra("ordertype", mDataBeanList.get(position).getOrdertype());
                    startActivity(intent);
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        prsv_ScrollView.onRefreshComplete();
        Gson gson = new Gson();
        mPaymentDetailModel = gson.fromJson(content, PaymentDetailModel.class);
        if (pageindex == 1) {
            if (mPaymentDetailModel.getResult() == 1) {
                mDataBeanList = mPaymentDetailModel.getData();
                setAdapter();
            } else {
            }
        } else if (pageindex > 1) {
            if (mPaymentDetailModel.getResult() == 1) {
                mDataBeanList.addAll(mPaymentDetailModel.getData());
                setAdapter();
            } else {
                pageindex--;
                ShowUtil.showToast(this, "沒有更多数据了...");
            }
        }
    }

    @Override
    public void onFailure(String content) {

    }
}