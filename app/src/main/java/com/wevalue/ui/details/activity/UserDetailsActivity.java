package com.wevalue.ui.details.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NoteBean;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.fragment.UserFriendsNoteFragment;
import com.wevalue.ui.details.fragment.UserReleaseListFragment;
import com.wevalue.ui.details.fragment.UserTransmitListFragment;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.view.LazyViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 该类是用户详情页的界面
 */
public class UserDetailsActivity extends BaseActivity implements View.OnClickListener, WZHttpListener, FriendManagerInterface, PopClickInterface {
    private ImageView iv_user_img;//头像
    private TextView tv_guanzhu;//关注
    private TextView tv_jianjie;//简介
    private ImageView cursor;// 动画图片
    private TextView tv_head_title;//title
    private TextView tv_2;//解除好友
    private TextView tv_fensi;//粉丝
    private TextView tv_1;//加关注
    private TextView tv_fabu;//发布
    private TextView tv_zhuangfa;//转发
    private TextView tv_haoyoukejian;//好友可见
    private TextView tv_wz_hao;//微值号
    private View prompt_box;
    private ImageView iv_back;
    private LazyViewPager lvp_user_xq;
    private ArrayList<Fragment> fragmentList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int screenW;//获取控件的宽度
    private NoteBean.UserBean userData;//用户详情实体类对象
    private TextView tv_nickname;
    private TextView tv_dengji;
    private TextView tv_xingbie;
    private TextView tv_remarkname;
    private LinearLayout ll_fensi;//粉丝
    private LinearLayout ll_guanzhu;
    private LinearLayout ll_fensiguangzhu;
    ProgressBar pgb;
    String isFriend = "";//该用户是否为好友关系
    String isFocus = "";//该用户是否为关注的人
    private String userremark;//给好友设置的备注
    private String isBlack;//0 没有拉黑 1 已拉黑
    UserFriendsNoteFragment userFriendsNoteFragment;
    UserReleaseListFragment userReleaseListFragment;
    UserTransmitListFragment userTransmitListFragment;
    String detailuserid = "";
    private boolean isFirstLoad = true;
    private ImageView iv_more;//更多按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userxiangqing);
        initView();
        infoView();
        InitImageView();
        tv_head_title.setText("用户详情");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad) {
            detailuserid = getIntent().getStringExtra("detailuserid");
            getUserInfo();
            initFragments();
            isFirstLoad = false;
        }
    }

    private void infoView() {
        iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
        iv_user_img.setOnClickListener(this);
        tv_guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
        tv_wz_hao = (TextView) findViewById(R.id.tv_wz_hao);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_fensi = (TextView) findViewById(R.id.tv_fensi);
        tv_haoyoukejian = (TextView) findViewById(R.id.tv_haoyoukejian);
        tv_zhuangfa = (TextView) findViewById(R.id.tv_zhuangfa);
        tv_fabu = (TextView) findViewById(R.id.tv_fabu);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        ll_fensi = (LinearLayout) findViewById(R.id.ll_fensi);
        ll_guanzhu = (LinearLayout) findViewById(R.id.ll_guanzhu);
        ll_fensiguangzhu = (LinearLayout) findViewById(R.id.ll_fensiguangzhu);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        cursor = (ImageView) findViewById(R.id.cursor);
        tv_guanzhu.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_fabu.setOnClickListener(this);
        tv_zhuangfa.setOnClickListener(this);
        tv_haoyoukejian.setOnClickListener(this);
        ll_fensi.setOnClickListener(this);
        ll_guanzhu.setOnClickListener(this);
        tv_jianjie.setOnClickListener(this);
        tv_head_title.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_fensi.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        lvp_user_xq = (LazyViewPager) findViewById(R.id.lvp_user_xq);
        fragmentList = new ArrayList<>();

    }

    private void initFragments() {
        Bundle bundle = new Bundle();
        bundle.putString("detailuserid", detailuserid);
        bundle.putString("isFriend", isFriend);
        if (userFriendsNoteFragment == null) {
            userFriendsNoteFragment = new UserFriendsNoteFragment();
            userFriendsNoteFragment.setArguments(bundle);
        }
        if (userReleaseListFragment == null) {
            userReleaseListFragment = new UserReleaseListFragment();
            userReleaseListFragment.setArguments(bundle);
        }
        if (userTransmitListFragment == null) {
            userTransmitListFragment = new UserTransmitListFragment();
            userTransmitListFragment.setArguments(bundle);
        }
        fragmentList.add(userReleaseListFragment);
        fragmentList.add(userTransmitListFragment);
        fragmentList.add(userFriendsNoteFragment);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        lvp_user_xq.setAdapter(myViewPagerAdapter);
        lvp_user_xq.setOffscreenPageLimit(2);
        lvp_user_xq.setScrollble(false);
        lvp_user_xq.setCurrentItem(1);
        lineAnimation(1);
        setFontColor(1);
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
    }

    private void lineAnimation(int index) {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
        currIndex = index;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
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
            case R.id.ll_guanzhu:
                if (TextUtils.isEmpty(detailuserid)) {
                    // TODO: 2016/12/12 用户id为空  直接退出方法体
                    return;
                }
                intent = new Intent(UserDetailsActivity.this, UserGuanZhuActivity.class);
                intent.putExtra("detailuserid", detailuserid);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_fensi:
                if (TextUtils.isEmpty(detailuserid)) {
                    // TODO: 2016/12/12 用户id为空  直接退出方法体
                    return;
                }
                Intent intent1 = new Intent(UserDetailsActivity.this, UserFenSiActivity.class);
                intent1.putExtra("detailuserid", detailuserid);
                startActivity(intent1);
                break;
            case R.id.tv_fabu:
                if (lvp_user_xq == null) {
                    // TODO: 2016/12/12 直接退出方法体
                    return;
                }
                lvp_user_xq.setCurrentItem(0);
                lineAnimation(0);
                setFontColor(0);
                break;
            case R.id.tv_zhuangfa:
                if (lvp_user_xq == null) {
                    // TODO: 2016/12/12 直接退出方法体
                    return;
                }
                lvp_user_xq.setCurrentItem(1);
                lineAnimation(1);
                setFontColor(1);
                break;
            case R.id.tv_haoyoukejian:
                if (lvp_user_xq == null) {
                    // TODO: 2016/12/12 直接退出方法体
                    return;
                }
                lvp_user_xq.setCurrentItem(2);
                lineAnimation(2);
                setFontColor(2);
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
        JSONObject object = null;
        String message = "";
        String result = "";
        switch (isUrl) {
            case RequestPath.GET_USERDETAILS:
                Gson gson = new Gson();
                NoteBean noteBean = gson.fromJson(content, NoteBean.class);
                if (null != noteBean && noteBean.getResult().equals("1") && 0 != noteBean.getUser().size()) {
                    // TODO: 2016/12/15   判断是否存在用户
                    userData = noteBean.getUser().get(0);
                    isFriend = userData.getIsfriend();
                    isFocus = userData.getIsfocus();
                    userremark = userData.getUserremark();
                    isBlack = userData.getIsblack();
                    iv_more.setVisibility(View.VISIBLE);//操作好友
                    iv_more.setOnClickListener(this);
                    pgb.setVisibility(View.GONE);
                    fillData();
                } else {
                    Toast.makeText(this, noteBean.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onFailure(String content) {
        ShowUtil.showToast(this, content);
    }

    private void initView() {
        pgb = (ProgressBar) findViewById(R.id.prg);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_dengji = (TextView) findViewById(R.id.tv_dengji);
        tv_xingbie = (TextView) findViewById(R.id.tv_xingbie);
        tv_remarkname = (TextView) findViewById(R.id.tv_remarkname);
        iv_more = (ImageView) findViewById(R.id.iv_more);

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

    /**
     * Title: MyViewPagerAdapter<br>
     * Description: TODO Viewpager适配器<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */

    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }

    /*
    * 填充数据
    * */
    private void fillData() {
        if (TextUtils.isEmpty(userremark)) {
            tv_remarkname.setVisibility(View.GONE);
        } else {
            tv_remarkname.setVisibility(View.VISIBLE);
            tv_remarkname.setText(userremark);
        }
        ll_fensiguangzhu.setVisibility(View.VISIBLE);


        if (userData.getFocusnum() != null) {
            int a = Integer.valueOf(userData.getFocusnum()).intValue();
            if (a != 0) {
                if (a < 10000) {
                    tv_guanzhu.setText("关注  " + userData.getFocusnum());
                } else {
                    int y = a / 10000;
                    if (y > 1000) {
                        int q = y / 1000;
                        tv_guanzhu.setText("关注  " + q + "千万");

                    } else {
                        tv_guanzhu.setText("关注  " + y + "万");
                    }

                }
            } else {
                tv_guanzhu.setText("关注  " + userData.getFocusnum());
            }
        }


        if (userData.getFocusnum() != null) {
            int a = Integer.valueOf(userData.getFansnum()).intValue();
            if (a != 0) {
                if (a < 10000) {
                    tv_fensi.setText("粉丝  " + userData.getFansnum());
                } else {
                    int y = a / 10000;
                    if (y > 1000) {
                        int q = y / 1000;
                        tv_fensi.setText("粉丝  " + q + "千万");

                    } else {
                        tv_fensi.setText("粉丝  " + y + "万");
                    }

                }
            } else {
                tv_fensi.setText("粉丝  " + userData.getFansnum());
            }
        }
        if (null != userData) {
            Glide.with(WeValueApplication.applicationContext).
                    load(RequestPath.SERVER_PATH + userData.getUserface())
                    .placeholder(R.mipmap.default_head)
                    .into(iv_user_img);
        }
        tv_jianjie.setText(userData.getUserinfo());
        tv_xingbie.setText(userData.getUsersex());
        tv_nickname.setText(userData.getUsernickname());
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
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("detailuserid", detailuserid);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("pagenum", "10");
        map.put("detaildata", "0");
        map.put("pageindex", "1");
        NetworkRequest.getRequest(RequestPath.GET_USERDETAILS, map, this);
    }

    /*改变字体颜色*/
    private void setFontColor(int position) {
//        case R.id.tv_fabu:
//        lvp_user_xq.setCurrentItem(0);
//        lineAnimation(0);
//        break;
//        case R.id.tv_zhuangfa:
//        lvp_user_xq.setCurrentItem(1);
//        lineAnimation(1);
//        break;
//        case R.id.tv_haoyoukejian:
//        lvp_user_xq.setCurrentItem(2);
//        lineAnimation(2);
        switch (position) {
            case 0:
                tv_fabu.setTextColor(getResources().getColor(R.color.blue));
                tv_zhuangfa.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_haoyoukejian.setTextColor(getResources().getColor(R.color.but_text_color));
                break;
            case 1:
                tv_fabu.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_zhuangfa.setTextColor(getResources().getColor(R.color.blue));
                tv_haoyoukejian.setTextColor(getResources().getColor(R.color.but_text_color));
                break;
            case 2:
                tv_fabu.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_zhuangfa.setTextColor(getResources().getColor(R.color.but_text_color));
                tv_haoyoukejian.setTextColor(getResources().getColor(R.color.blue));
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
