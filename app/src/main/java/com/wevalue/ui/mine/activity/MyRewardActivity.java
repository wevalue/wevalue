//package com.wevalue.ui.me.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
//import com.wevalue.R;
//import com.wevalue.base.BaseActivity;
//import com.wevalue.model.RewardBean;
//import com.wevalue.model.RewardBean.RewardEntity;
//import com.wevalue.net.Interfacerequest.NoteRequestBase;
//import com.wevalue.net.requestbase.WZHttpListener;
//import com.wevalue.ui.details.activity.NoteDetailsActivity;
//import com.wevalue.ui.world.adapter.MyRewardAdapter;
//import com.wevalue.utils.LogUtils;
//import com.wevalue.utils.PopuUtil;
//import com.wevalue.utils.ShowUtil;
//import com.wevalue.view.NoScrollListview;
//
//import java.util.List;
//
///**
// * 我的打赏界面
// */
//public class MyRewardActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {
//
//    int position;
//    String notezone = "-1";
//    private TextView tv_friends;
//    private ImageView iv_back;
//    private TextView tv_head_title;
//    private List<RewardEntity> rewardEntities;
//    private MyRewardAdapter adapter;
//    private NoteRequestBase mNoteRequestBase;
//    private int pageindex = 1;
//    private PullToRefreshScrollView prsv_ScrollView;
//    private NoScrollListview mNoScrollListview;
//    private static final int isself = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_reward);
//        initView();
//        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(this);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "3", isself, this);
//    }
//
//    private void initView() {
//        tv_friends = (TextView) findViewById(R.id.tv_friends);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
//        iv_back.setOnClickListener(this);
//        tv_friends.setOnClickListener(this);
//        prsv_ScrollView = (PullToRefreshScrollView) findViewById(R.id.prsv_ScrollView);
//        mNoScrollListview = (NoScrollListview) findViewById(R.id.mNoScrollListview);
//        mNoScrollListview.setFocusable(false);
//
//        mNoScrollListview.setOnItemClickListener(news AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = news Intent(MyRewardActivity.this, NoteDetailsActivity.class);
//                intent.putExtra("noteId", rewardEntities.get(position).getNoteid());
//                startActivity(intent);
//            }
//        });
//
//        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
//        prsv_ScrollView.setOnRefreshListener(news PullToRefreshBase.OnRefreshListener2<ScrollView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
////                下拉
//                pageindex = 1;
//                mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "3", isself, MyRewardActivity.this);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
////                上拉
//                pageindex++;
//                mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "3", isself, MyRewardActivity.this);
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_friends:
//                String s = tv_friends.getText().toString();
//                try {
//                    int i = s.indexOf("(");
//                    s = s.substring(0, i);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                switch (s) {
//                    case "世界":
//                        position = 1;
//                        notezone = "0";
//                        break;
//                    case "朋友们":
//                        position = 2;
//                        notezone = "1";
//                        break;
//                    case "全部":
//                        position = 0;
//                        notezone = "-1";
//                        break;
//                }
//                LogUtils.e(position + "");
//                PopuUtil.initpopumyinfo(this, tv_friends, position, "打赏");
//                break;
//            case R.id.iv_back:
//                finish();
//        }
//
//    }
//
//    public void switchType() {
//        mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "3", isself, this);
//    }
//    @Override
//    public void onSuccess(String content, String isUrl) {
//        RewardBean rewardBean = news Gson().fromJson(content, RewardBean.class);
//        prsv_ScrollView.onRefreshComplete();
//        if (rewardBean.getResult().equals("1")) {
//            if (pageindex > 1) {
//                if (rewardEntities != null && rewardEntities.size() > 0 && rewardBean.getData().size() != 0) {
//                    rewardEntities.addAll(rewardBean.getData());
//                    adapter.setmData(rewardEntities);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    ShowUtil.showToast(MyRewardActivity.this, "没有更多数据了");
//                }
//            } else if (pageindex == 1) {
//                if (adapter != null && rewardEntities != null && rewardEntities.size() > 0) {
//                    rewardEntities = rewardBean.getData();
//                    adapter.setmData(rewardEntities);
//                    adapter.notifyDataSetChanged();
//                } else {
//                    rewardEntities = rewardBean.getData();
//                    adapter = news MyRewardAdapter(this, rewardEntities);
//                    adapter.notifyDataSetChanged();
//
//                    mNoScrollListview.setAdapter(adapter);
//                }
//            }
//        } else {
//        }
//    }
//
//    @Override
//    public void onFailure(String content) {
//        ShowUtil.showToast(this, "网络请求错误，" + content);
//    }
//}
