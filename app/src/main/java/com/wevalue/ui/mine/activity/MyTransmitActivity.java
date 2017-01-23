//package com.wevalue.ui.me.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
//import com.wevalue.R;
//import com.wevalue.base.BaseActivity;
//import com.wevalue.model.NoteBean;
//import com.wevalue.net.Interfacerequest.NoteRequestBase;
//import com.wevalue.net.requestbase.WZHttpListener;
//import com.wevalue.ui.influence.PopuInterface;
//import com.wevalue.ui.me.adapter.MyForwardMsgAdapter;
//import com.wevalue.ui.details.activity.NoteDetailsActivity;
//import com.wevalue.utils.LogUtils;
//import com.wevalue.utils.PopuUtil;
//import com.wevalue.utils.ShowUtil;
//import com.wevalue.view.NoScrollListview;
//
//import java.util.List;
//
///**
// * 我的转发界面
// */
//public class MyTransmitActivity extends BaseActivity implements WZHttpListener, View.OnClickListener {
//
//    LinearLayout ll_pgb;
//    private TextView tv_friends;
//    WZHttpListener wzHttpListener;
//    static String notezone = "-1";
//    private ImageView iv_back;
//    private TextView tv_head_title;
//
//    private List<NoteBean.NoteEntity> mNoteList;
//    private MyForwardMsgAdapter mAdapter;
//    private static NoteRequestBase mNoteRequestBase;
//    private static int pageindex = 1;
//    private PullToRefreshScrollView prsv_ScrollView;
//    private NoScrollListview mNoScrollListview;
//    //定义转发状态的常量
//    private static final int isself = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_forward_msg);
//        initView();
//        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(this);
//        wzHttpListener = this;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        ll_pgb.setVisibility(View.VISIBLE);
//        mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "2", isself, this);
//    }
//
//    private void initView() {
//        ll_pgb = (LinearLayout) findViewById(R.id.ll_pgb);
//        tv_friends = (TextView) findViewById(R.id.tv_friends);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_back.setOnClickListener(this);
//        tv_friends.setOnClickListener(this);
//        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
//        mNoScrollListview = (NoScrollListview) findViewById(R.id.mNoScrollListview);
//        prsv_ScrollView = (PullToRefreshScrollView) findViewById(R.id.prsv_ScrollView);
//        mNoScrollListview.setFocusable(false);
//        mNoScrollListview.setOnItemClickListener(news AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = news Intent(MyTransmitActivity.this, NoteDetailsActivity.class);
//                intent.putExtra("noteId", mNoteList.get(i).getNoteid());
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
//                mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "2", isself, MyTransmitActivity.this);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
////                上拉
//                pageindex++;
//                mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "2", isself, MyTransmitActivity.this);
//            }
//
//        });
//
//    }
//
//
//    @Override
//    public void onSuccess(String content, String isUrl) {
//        LogUtils.e("  信息列表 =" + content);
//        NoteBean noteBean = news Gson().fromJson(content, NoteBean.class);
//        prsv_ScrollView.onRefreshComplete();
//        ll_pgb.setVisibility(View.GONE);
//        if (noteBean.getResult().equals("1")) {
//            if (pageindex > 1) {
//                if (mNoteList != null && mNoteList.size() > 0 && noteBean.data.size() != 0) {
//                    mNoteList.addAll(noteBean.data);
//                    mAdapter.setmDatas(mNoteList);
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    ShowUtil.showToast(this, "没有更多数据了");
//                }
//            } else if (pageindex == 1) {
//                if (mAdapter != null && mNoteList != null && mNoteList.size() > 0) {
//                    mNoteList = noteBean.getData();
//                    mAdapter.setmDatas(mNoteList);
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    mNoteList = noteBean.getData();
//                    mAdapter = news MyForwardMsgAdapter(mNoteList, this);
//                    mAdapter.notifyDataSetChanged();
//                    mNoScrollListview.setAdapter(mAdapter);
//                }
//            }
//        } else {
//
//        }
//    }
//
//    @Override
//    public void onFailure(String content) {
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//            case R.id.tv_friends:
//                final String s = tv_friends.getText().toString();
//                PopuUtil.initMyZhuanFa(this, tv_friends, news PopuInterface() {
//                    @Override
//                    public void onSelectComplect(String content) {
//                        switch (content) {
//                            case "全部":
//                                if (!content.equals(s)) {
//                                    notezone = "-1";
//                                    ll_pgb.setVisibility(View.VISIBLE);
//                                    mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "2", isself, MyTransmitActivity.this);
//                                }
//                                break;
//                            case "影响力":
//                                if (!content.equals(s)) {
//                                    notezone = "2";
//                                    ll_pgb.setVisibility(View.VISIBLE);
//                                    mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "2", isself, MyTransmitActivity.this);
//                                }
//                                break;
//                            case "朋友们":
//                                if (!content.equals(s)) {
//                                    notezone = "1";
//                                    ll_pgb.setVisibility(View.VISIBLE);
//                                    mNoteRequestBase.getNoteListData(String.valueOf(pageindex), notezone, "2", isself, MyTransmitActivity.this);
//                                }
//                                break;
//                        }
//                    }
//                });
////                String s = tv_friends.getText().toString();
////                try {
////                    int i = s.indexOf("(");
////                    s = s.substring(0, i);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                switch (s) {
////                    case "影响力":
////                        notezone = "2";
////                        break;
////                    case "朋友们":
////                        notezone = "1";
////                        break;
////                    case "全部":
////                        notezone = "-1";
////                        break;
////                }
////                prompt_box = getLayoutInflater().inflate(R.layout.popu_mysend, null);
////                TextView tv_world = (TextView) prompt_box.findViewById(R.id.tv_world);
////                tv_world.setText("影响力");
////                TextView tv_pym = (TextView) prompt_box.findViewById(R.id.tv_pym);
////                TextView tv_all = (TextView) prompt_box.findViewById(R.id.tv_all);
////                switch (notezone) {
////                    case "-1":
////                        tv_all.setTextColor(Color.RED);
////                        tv_pym.setTextColor(Color.BLACK);
////                        tv_world.setTextColor(Color.BLACK);
////                        break;
////                    case "0":
////                        tv_world.setTextColor(Color.RED);
////                        tv_pym.setTextColor(Color.BLACK);
////                        tv_all.setTextColor(Color.BLACK);
////                        break;
////                    case "1":
////                        tv_pym.setTextColor(Color.RED);
////                        tv_world.setTextColor(Color.BLACK);
////                        tv_all.setTextColor(Color.BLACK);
////                        break;
////                }
//
//        }
//    }
//}
