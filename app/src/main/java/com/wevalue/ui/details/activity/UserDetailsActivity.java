package com.wevalue.ui.details.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.Transformation;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.adapter.NoteListAdapter;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NoteBean;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.utils.ImageUitls;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.view.NoScrollListview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 该类是用户详情页的界面
 */
public class UserDetailsActivity extends BaseActivity implements View.OnClickListener, WZHttpListener, FriendManagerInterface, PopClickInterface{
    private LinearLayout layout_head;//title背景
    private LinearLayout tab_layout,tab_layout_top;//发布 转发 朋友 切换布局
    private PullToRefreshScrollView pull_scrollView;//头像大背景
    private ImageView iv_head_bg;//头像大背景
    private ImageView iv_user_img;//头像
    private ImageView iv_user_v;//大V标志
    private TextView tv_nickname;//昵称
    private TextView tv_dengji;//等级
    private TextView tv_jianjie;//简介


    private TextView tv_1;//加关注
    private TextView tv_2;//加好友
    private TextView tv_wz_hao;//微值号

    private TextView tv_fensi;//粉丝
    private TextView tv_guanzhu;//关注

    private TextView tv_title;//标题
    private ImageView iv_back;//返回
    private ImageView iv_more;//更多按钮
    /**因为滑到顶部要浮停在顶部 所以在顶部也加了一个同样的布局**/
    private TextView tv_fabu;//发布
    private TextView tv_zhuangfa;//转发
    private TextView tv_haoyoukejian;//好友可见
    private ImageView cursor;// 动画图片
    private TextView tv_fabu_top;//发布
    private TextView tv_zhuangfa_top;//转发
    private TextView tv_haoyoukejian_top;//好友可见
    private ImageView cursor_top;// 动画图片

    /**帖子列表 与 适配器**/
    private NoScrollListview ls_notelist; //
    private NoteListAdapter mAdapter;

    /**全局变量**/
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int screenW;//获取控件的宽度
    private NoteBean.UserBean userData;//用户详情实体类对象

    String isFriend = "";//该用户是否为好友关系
    String isFocus = "";//该用户是否为关注的人
    private String userremark;//给好友设置的备注
    private String isBlack;//0 没有拉黑 1 已拉黑
    private String userv;// 0 or 1  是否是大V

    String detailuserid = "";//用户id
    private boolean isFirstLoad = true;
    private int headViewHeight;
    private int tabLayoutScrollTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userxiangqings);
        initView();
        infoView();
        InitImageView();
        initPullToRefreshScrollView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad) {
            detailuserid = getIntent().getStringExtra("detailuserid");
            getUserInfo();
            isFirstLoad = false;
        }

    }

    private void initView() {
        tab_layout = (LinearLayout) findViewById(R.id.tab_layout);
        tab_layout_top = (LinearLayout) findViewById(R.id.tab_layout_top);
        layout_head = (LinearLayout) findViewById(R.id.layout_head);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_dengji = (TextView) findViewById(R.id.tv_dengji);
        iv_more = (ImageView) findViewById(R.id.iv_more);
        iv_more.setVisibility(View.INVISIBLE);
        ls_notelist = (NoScrollListview) findViewById(R.id.ls_notelist);
        ls_notelist.setFocusable(false);
        mAdapter = new NoteListAdapter(this);
        ls_notelist.setAdapter(mAdapter);
        tab_layout.post(new Runnable() {
            @Override
            public void run() {
                // 获取悬浮控件到顶部的距离
                tabLayoutScrollTop = tab_layout.getTop();
            }
        });
        layout_head.post(new Runnable() {
            @Override
            public void run() {
                // 获取悬浮控件到顶部的距离
                headViewHeight = layout_head.getHeight();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initPullToRefreshScrollView(){
        pull_scrollView = (PullToRefreshScrollView) findViewById(R.id.pull_scrollView);
        pull_scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        pull_scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex = 1;
                getUserInfo();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pageindex++;
                getUserInfo();
            }
        });
        pull_scrollView.setFocusable(false);
        pull_scrollView.setOnScrollListener(new PullToRefreshScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                Log.e("onScroll","scrollY = "+ scrollY);
                //如果当前的 scrollY 大于 view 到顶部的距离 则显示 此view
                if (scrollY >= tabLayoutScrollTop-headViewHeight){
                    tab_layout_top.setVisibility(View.VISIBLE);
                    layout_head.setBackgroundResource(R.color.gray_f5);
                    tv_title.setTextColor(getColor(R.color.black_40));
                }else {
                    tab_layout_top.setVisibility(View.GONE);
                    layout_head.setBackgroundResource(R.color.transparent);
                    tv_title.setTextColor(getColor(R.color.transparent));
                }
            }
        });
    }
    private void infoView() {
        iv_head_bg = (ImageView) findViewById(R.id.iv_head_bg);
        iv_user_v = (ImageView) findViewById(R.id.iv_user_v);
        iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
        iv_user_img.setOnClickListener(this);
        tv_guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
        tv_wz_hao = (TextView) findViewById(R.id.tv_wz_hao);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_fensi = (TextView) findViewById(R.id.tv_fensi);
        tv_haoyoukejian_top = (TextView) findViewById(R.id.tv_haoyoukejian_top);
        tv_haoyoukejian = (TextView) findViewById(R.id.tv_haoyoukejian);
        tv_zhuangfa_top = (TextView) findViewById(R.id.tv_zhuangfa_top);
        tv_zhuangfa = (TextView) findViewById(R.id.tv_zhuangfa);
        tv_fabu_top = (TextView) findViewById(R.id.tv_fabu_top);
        tv_fabu = (TextView) findViewById(R.id.tv_fabu);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        cursor = (ImageView) findViewById(R.id.cursor);
        cursor_top = (ImageView) findViewById(R.id.cursor_top);
        tv_guanzhu.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_fabu.setOnClickListener(this);
        tv_fabu_top.setOnClickListener(this);
        tv_zhuangfa.setOnClickListener(this);
        tv_zhuangfa_top.setOnClickListener(this);
        tv_haoyoukejian.setOnClickListener(this);
        tv_haoyoukejian_top.setOnClickListener(this);

        tv_jianjie.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_fensi.setOnClickListener(this);
        iv_back.setOnClickListener(this);


    }

    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        bmpW = (BitmapFactory.decodeResource(getResources(), R.mipmap.myreleaseline).getWidth());// 获取图片宽度
        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
        cursor_top.setImageMatrix(matrix);// 设置动画初始位置
    }

    private void lineAnimation(int index) {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
        currIndex = index;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
        cursor_top.startAnimation(animation);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_more:
                addBlackFriend();
                break;
            case R.id.iv_user_img:
                if (userData == null) {
                    // TODO: 2016/12/12 如果没有获取用户数据直接跳出方法体
                    return;
                }
                String[] url = {userData.getUserface()};
                intent = new Intent(UserDetailsActivity.this, ImgShowActivity.class);
                intent.putExtra("index", 0);
                intent.putExtra("imgUrl", url);
                startActivity(intent);
                break;
//            case R.id.ll_guanzhu:
//                if (TextUtils.isEmpty(detailuserid)) {
//                    // TODO: 2016/12/12 用户id为空  直接退出方法体
//                    return;
//                }
//                intent = new Intent(UserDetailsActivity.this, UserGuanZhuActivity.class);
//                intent.putExtra("detailuserid", detailuserid);
//                startActivity(intent);
//                break;
            case R.id.iv_back:
                finish();
                break;
//            case R.id.ll_fensi:
//                if (TextUtils.isEmpty(detailuserid)) {
//                    // TODO: 2016/12/12 用户id为空  直接退出方法体
//                    return;
//                }
//                Intent intent1 = new Intent(UserDetailsActivity.this, UserFenSiActivity.class);
//                intent1.putExtra("detailuserid", detailuserid);
//                startActivity(intent1);
//                break;
            case R.id.tv_fabu:
                currIndex = 0;
                pageindex = 1;
                getUserInfo();
                lineAnimation(currIndex);
                setFontColor(currIndex);
                break;
            case R.id.tv_zhuangfa:
                currIndex = 1;
                pageindex = 1;
                getUserInfo();
                lineAnimation(currIndex);
                setFontColor(currIndex);
                break;
            case R.id.tv_haoyoukejian:
                currIndex = 2;
                pageindex = 1;
                getUserInfo();
                lineAnimation(currIndex);
                setFontColor(currIndex);
                break;
            case R.id.tv_fabu_top:
                currIndex = 0;
                pageindex = 1;
                getUserInfo();
                lineAnimation(currIndex);
                setFontColor(currIndex);
                break;
            case R.id.tv_zhuangfa_top:
                currIndex = 1;
                pageindex = 1;
                getUserInfo();
                lineAnimation(currIndex);
                setFontColor(currIndex);
                break;
            case R.id.tv_haoyoukejian_top:
                currIndex = 2;
                pageindex = 1;
                getUserInfo();
                lineAnimation(currIndex);
                setFontColor(currIndex);
                break;
            case R.id.tv_2:
                if (userData == null) {
                    // TODO: 2016/12/19 如果没有获取用户数据直接跳出方法体
                    return;
                }
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    String conten = tv_2.getText().toString();
                    if (conten.equals("+好友")) {
                        conten = "加好友";
                    }
                    if (conten.equals("申请中")) {
                        return;
                    }
                    PopuUtil.initManageFriend(this, conten, this);
                }
                break;
            case R.id.tv_1:
                if (userData == null) {
                    // TODO: 2016/12/19 如果没有获取用户数据直接跳出方法体
                    return;
                }
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(UserDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    String content = tv_1.getText().toString().trim();
                    if (content.equals("设置备注")) {
                        PopuUtil.initSetRemark(UserDetailsActivity.this, UserDetailsActivity.this);
                    } else {
                        if (content.equals("+关注")) {
                            content = "加关注";
                        }
                        PopuUtil.initManageFriend(this, content, this);
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        if (loadingDialog.isShowing())loadingDialog.dismiss();
        pull_scrollView.onRefreshComplete();
        JSONObject object = null;
        String message = "";
        String result = "";
        switch (isUrl) {
            case RequestPath.GET_USERDETAILS:
                Gson gson = new Gson();
                NoteBean noteBean = gson.fromJson(content, NoteBean.class);
                if (pageindex == 1) {
                    if (null != noteBean && noteBean.getResult().equals("1") && 0 != noteBean.getUser().size()) {
                        // TODO: 2016/12/15   判断是否存在用户
                        userData = noteBean.getUser().get(0);
                        isFriend = userData.getIsfriend();
                        isFocus = userData.getIsfocus();
                        userremark = userData.getUserremark();
                        isBlack = userData.getIsblack();
                        userv =  userData.getUserv();
                        iv_more.setVisibility(View.VISIBLE);//操作好友
                        iv_more.setOnClickListener(this);
                        fillData();
                        setListData(noteBean.getData());
                        if (!TextUtils.isEmpty(noteBean.getMessage())) {
                            ShowUtil.showToast(WeValueApplication.applicationContext, noteBean.getMessage());
                            return;
                        }
                    } else
                        Toast.makeText(this, noteBean.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    setListData(noteBean.getData());
                }

                break;
            case RequestPath.POST_SETFRIENDREMARK:
                try {
                    object = new JSONObject(content);
                    message = object.getString("message");
                    result = object.getString("result");
                    if (result.equals("1")) {
                        getUserInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
            case RequestPath.POST_DELBLACKFRIEND:
                try {//取消拉黑好友
                    object = new JSONObject(content);
                    result = object.getString("result");
                    message = object.getString("message");
                    LogUtils.e("POST_DELBLACKFRIEND", result + "   " + message);
                    if (result.equals("1")) {
                        isBlack = "0";
                        ShowUtil.showToast(UserDetailsActivity.this, message);
                        pageindex = 1;
                        getUserInfo();
                    } else {
                        ShowUtil.showToast(UserDetailsActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_BLACKFRIEND:
                try {
                    //拉黑好友
                    object = new JSONObject(content);
                    result = object.getString("result");
                    message = object.getString("message");
                    if (result.equals("1")) {
                        isBlack = "1";
                        ShowUtil.showToast(UserDetailsActivity.this, message);
                        pageindex = 1;
                        getUserInfo();
                    } else {
                        ShowUtil.showToast(UserDetailsActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                //默认是好友请求
                try {
                    object = new JSONObject(content);
                    message = object.getString("message");
                    result = object.getString("result");
                    if (result.equals("1")) {
                        getUserInfo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setListData(List<NoteBean.NoteEntity> data) {

        if (data == null || data.isEmpty()) {
            //如果是第一次加载
            if (pageindex == 1&&mAdapter!=null) {
                mAdapter.clear();
                mAdapter.setmDatas(new ArrayList<NoteBean.NoteEntity>());
                mAdapter.notifyDataSetChanged();
                return;
            }

        }
        if (mAdapter != null) {
            if (pageindex == 1){
                mAdapter.clear();
                mAdapter.setmDatas(data);
                mAdapter.notifyDataSetChanged();
            }else {
                mAdapter.setmDatas(data);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(this, content);
        pull_scrollView.onRefreshComplete();
        if (loadingDialog.isShowing())loadingDialog.dismiss();
    }



    //添加备注popuwindow 退出后的点击事件
    @Override
    public void onClickOk(String content) {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("frienduserid", detailuserid);
        map.put("userremark", content);
        NetworkRequest.postRequest(RequestPath.POST_SETFRIENDREMARK, map, this);
    }

   private void setUserRemark(){
       tv_nickname.setText(userData.getUsernickname());
       //如果是好友且设置了备注则 名字显示备注
       if (isFriend.equals("1")){
           String userRemark =  userData.getUserremark();
           if (!TextUtils.isEmpty(userRemark)||!"".equals(userRemark)){
               tv_nickname.setText(userRemark);
           }
       }
   }

    /**
     * Title: MyViewPagerAdapter<br>
     * Description: TODO Viewpager适配器<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    /*
    * 填充数据
    * */
    private void fillData() {
        tv_guanzhu.setText("关注  " + userData.getFocusnum());
        tv_fensi.setText("粉丝  " + userData.getFansnum());
        if ("1".equals(userv))
            iv_user_v.setVisibility(View.VISIBLE);
        else  iv_user_v.setVisibility(View.INVISIBLE);
        ImageUitls.setHead(userData.getUserface(), iv_user_img);
        Transformation<Bitmap> trans = new BlurTransformation(WeValueApplication.applicationContext,24);
        ImageUitls.setImg(userData.getUserface(), iv_head_bg,trans);
        tv_jianjie.setText(userData.getUserinfo());
        setUserRemark();
        tv_dengji.setText(userData.getUserlevel());
        tv_wz_hao.setText("微值号：" + userData.getUsernumber());
        LogUtils.e("isFriend", isFriend);
        if (isFriend.equals("1")) {
            //该用户是自己的好友
            tv_1.setVisibility(View.VISIBLE);
            tv_2.setVisibility(View.VISIBLE);
            tv_1.setText("设置备注");
            tv_2.setText("解除好友");

        } else {
            if (isFocus.equals("1")) {   //该用户是自己关注的人
                tv_1.setVisibility(View.VISIBLE);
                tv_2.setVisibility(View.VISIBLE);
                tv_1.setText("取消关注");
                if (!isFriend.equals("3")) {
                    tv_2.setText("加好友");
                } else {
                    tv_2.setText("申请中");
                }
            } else {
                //该用户是自己的粉丝或者陌生人
                tv_1.setVisibility(View.VISIBLE);
                tv_2.setVisibility(View.VISIBLE);
                tv_1.setText("加关注");
                if (!isFriend.equals("3")) {
                    tv_2.setText("加好友");
                } else {
                    tv_2.setText("申请中");
                }
            }
        }
        if (userData.getUserid().equals(SharedPreferencesUtil.getUid(this))) {
            tv_1.setVisibility(View.GONE);
            tv_2.setVisibility(View.GONE);
        }
    }

    //管理好友关系的方法的重写，关注、取消的具体实现
    @Override
    public void manageFriend(String content) {
        switch (content) {
            case "加好友":
                FriendManageBase.AddFriend(this, userData.getUserid(), this);
                break;
            case "解除好友":
                FriendManageBase.DelFriend(this, userData.getUserid(), this);
                break;
            case "加关注":
                FriendManageBase.AddFocus(this, userData.getUserid(), this);
                break;
            case "取消关注":
                FriendManageBase.DelFocus(this, userData.getUserid(), this);
                break;
        }
    }

    @Override
    public void manageFriend(String content, String userid) {

    }

    /*获取用户详情的方法*/
    private void getUserInfo() {
        loadingDialog.show();
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("detailuserid", detailuserid);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("pagenum", "10");
        map.put("detaildata", (currIndex + 1) + ""); // 1 发布 2 转发 3 好友可见
        map.put("pageindex", pageindex + "");
        NetworkRequest.getRequest(RequestPath.GET_USERDETAILS, map, this);
    }

    private int pageindex = 1;

    /*改变字体颜色*/
    private void setFontColor(int position) {

        switch (position) {
            case 0:
                tv_fabu.setTextColor(getResources().getColor(R.color.blue));
                tv_fabu_top.setTextColor(getResources().getColor(R.color.blue));
                tv_zhuangfa.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_zhuangfa_top.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_haoyoukejian.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_haoyoukejian_top.setTextColor(getResources().getColor(R.color.but_text_color));
                break;
            case 1:
                tv_fabu.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_fabu_top.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_zhuangfa.setTextColor(getResources().getColor(R.color.blue));
                tv_zhuangfa_top.setTextColor(getResources().getColor(R.color.blue));
                tv_haoyoukejian.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_haoyoukejian_top.setTextColor(getResources().getColor(R.color.but_text_color));
                break;
            case 2:
                tv_fabu.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_fabu_top.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_zhuangfa.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_zhuangfa_top.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_haoyoukejian.setTextColor(getResources().getColor(R.color.blue));
                tv_haoyoukejian_top.setTextColor(getResources().getColor(R.color.blue));
                break;
        }
    }

    /*拉黑好友界面*/
    private void addBlackFriend() {
        if (TextUtils.isEmpty(detailuserid) || TextUtils.isEmpty(isBlack)) {
            ShowUtil.showToast(UserDetailsActivity.this, "网络状态不佳，请稍后重试。");
            return;
        }
        if (isBlack.equals("1")) {
            //用户被拉黑
            new ActionSheetDialog(this)
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("解除黑名单", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            HashMap map = new HashMap();
                            map.put("code", RequestPath.CODE);
                            map.put("userid", SharedPreferencesUtil.getUid(UserDetailsActivity.this));
                            map.put("blackuserid", detailuserid);
                            NetworkRequest.postRequest(RequestPath.POST_DELBLACKFRIEND, map, UserDetailsActivity.this);
                        }
                    }).show();
        } else {
            //用户没有被拉黑
            new ActionSheetDialog(this)
                    .builder()
                    .setTitle("说好一起到白头，他却偷偷焗了油。或许不是好友、不再关注、不再评论、" +
                            "不再看到他、也不让他再看到你，才能让你们真正相忘于江湖，毕竟，曾经有过美好的记忆。")
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("加入黑名单", ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            HashMap map = new HashMap();
                            map.put("code", RequestPath.CODE);
                            map.put("userid", SharedPreferencesUtil.getUid(UserDetailsActivity.this));
                            map.put("blackuserid", detailuserid);
                            NetworkRequest.postRequest(RequestPath.POST_BLACKFRIEND, map, UserDetailsActivity.this);
                        }
                    }).show();
        }
    }
}
