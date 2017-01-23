package com.wevalue.ui.we.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.SiteMessageModel;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.we.adapter.MessageDetailsAdapter;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import java.util.HashMap;
import java.util.List;

public class MeassageActivity extends BaseActivity implements WZHttpListener, View.OnClickListener {
    String messageType;
    private ListView ll_message;
    MessageDetailsAdapter messageDetailsAdapter;
    SiteMessageModel messageModel;
    List<SiteMessageModel.DataBean> dataBeanList;
    private TextView tv_back;
    private ImageView iv_back;
    private TextView tv_head_title;
    private TextView tv_nickname;
    private TextView tv_send_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meassage);
        initView();
    }

    private void initView() {
        messageType = getIntent().getStringExtra("messageType");
        ll_message = (ListView) findViewById(R.id.ll_message);
        HashMap map = new HashMap();
        if (SharedPreferencesUtil.getUid(this).isEmpty()) {
            ShowUtil.showToast(this, "请您先登陆录微值！");
        }
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("wepagedata", "1");
        map.put("messtype", messageType);
        map.put("pagenum", "10");
        map.put("pageindex", "1");
        NetworkRequest.getRequest(RequestPath.GET_USERSITEMESS, map, this);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.GONE);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        switch (messageType) {
            case "1":
                tv_head_title.setText("好友申请");
                break;
            case "2":
                tv_head_title.setText("新的粉丝");
                break;
            case "3":
                tv_head_title.setText("打赏");
                break;
            case "4":
                tv_head_title.setText("转发");
                break;
            case "5":
                tv_head_title.setText("赞");
                break;
            case "6":
                tv_head_title.setText("评论");
                break;
            case "7":
                tv_head_title.setText("系统通知");
                break;
        }
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
        tv_send_note.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        Gson gson = new Gson();
        messageModel = gson.fromJson(content, SiteMessageModel.class);
        if (messageModel.getResult() == 1) {
            dataBeanList = messageModel.getSitemesslist();
            messageDetailsAdapter = new MessageDetailsAdapter(this, dataBeanList, messageType);
            ll_message.setAdapter(messageDetailsAdapter);
//            ll_message.setOnItemClickListener(news AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = null;
//                    if (dataBeanList.get(position).getMesstype() == 7) {
//                        //消息界面
////                        intent = news Intent(MeassageActivity.this, UserDetailsActivity.class);
//                    } else {
//                        intent = news Intent(MeassageActivity.this, UserDetailsActivity.class);
//                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
////                        SharedPreferencesUtil.setDetailUserid(MeassageActivity.this, dataBeanList.get(position).getFromuserid());
//                    }
//                    startActivity(intent);
//                }
//            });
        }
    }

    @Override
    public void onFailure(String content) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
