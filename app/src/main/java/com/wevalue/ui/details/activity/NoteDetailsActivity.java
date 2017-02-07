package com.wevalue.ui.details.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NoteBean;
import com.wevalue.model.NoteInfoBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.adapter.NoteDetailsZambiaAdapter;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.we.activity.ReplyCommentActivity;
import com.wevalue.ui.world.activity.EmoTionActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.activity.Play_videoActivity;
import com.wevalue.ui.world.activity.TransmitNoteActivity;
import com.wevalue.ui.world.adapter.WorldListGridViewAdapter;
import com.wevalue.utils.Constants;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollGridView;
import com.wevalue.view.NoScrollListview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2016-08-10.
 * 帖子详情
 */
public class NoteDetailsActivity extends BaseActivity implements WZHttpListener, View.OnClickListener, PayInterface {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ShowUtil.showToast(NoteDetailsActivity.this, "分享成功!");
                    if ("1".equals(sharefree)) {            //免费分享时不调用接口
//                        ShowUtil.showToast(NoteDetailsActivity.this, "return!");
                        return;
                    }
                    bindShareSucc(shareMap);
                    break;
                case 2:
                    ShowUtil.showToast(NoteDetailsActivity.this, "分享失败!");
                    if ("1".equals(sharefree)) {
                        return;
                    }
                    if (!shareMap.get("spendtype").equals(Constants.suiyinpay)) {
                        HashMap map = new HashMap();
                        map.put("code", RequestPath.CODE);
                        map.put("userid", SharedPreferencesUtil.getUid(NoteDetailsActivity.this));
                        map.put("money", shareMap.get("money"));
                        map.put("paytype", shareMap.get("paytype"));
                        map.put("spendtype", shareMap.get("spendtype"));
                        map.put("orderno", shareMap.get("orderno"));
                        refundMoney(map);
                    }
                    break;
                case 3:
                    ShowUtil.showToast(NoteDetailsActivity.this, "取消分享!");
                    if ("1".equals(sharefree)) {
                        return;
                    }
                    if (!shareMap.get("spendtype").equals(Constants.suiyinpay)) {
                        HashMap map = new HashMap();
                        map.put("code", RequestPath.CODE);
                        map.put("userid", SharedPreferencesUtil.getUid(NoteDetailsActivity.this));
                        map.put("money", shareMap.get("money"));
                        map.put("paytype", shareMap.get("paytype"));
                        map.put("spendtype", shareMap.get("spendtype"));
                        map.put("orderno", shareMap.get("orderno"));
                        refundMoney(map);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView iv_back;
    private ImageView cursor;// 动画图片
    private ImageView iv_sharenote;
    private ImageView iv_play;
    private TextView tv_head_title;
    private TextView tv_zhuanfa_but;//转发数
    private TextView tv_pinglun_but;//评论数
    private TextView tv_qingxu_but;// 情绪数
    private TextView tv_zan_but;//点赞数
    private TextView tv_shang_but;//打赏次数
    private LinearLayout ll_getview_width;
    private LinearLayout ll_ZF_but;
    private LinearLayout ll_PL_but;
    private LinearLayout ll_QX_but;
    private LinearLayout ll_Zan_but;
    NoteBean noteBean;
    NoteBean.NoteEntity noteEntity;
    ImageView iv_user_img;//用户头像
    ImageView iv_video_img;//视频 图片
    ImageView iv_audio_img;//音频 图片
    TextView tv_audio_long;//音乐时长
    ImageView iv_dashang;//打赏图标
    String iv_video_and_audio_url;
    TextView tv_nickname;//昵称
    TextView tv_dengji;//等级
    TextView tv_day;//日期
    TextView tv_price;//单价
    TextView tv_income;//总收益
    TextView tv_read_num;//阅读数
    TextView tv_note_title;//信息内容
    TextView tv_note_content;//信息内容
    TextView tv_delete_note;//删除帖子 按钮
    TextView tv_is_reci;//热词
    TextView tv_is_yuanchuang;//是否原创
    ImageView iv_isRenzheng;//用户认证的标识
    View in_audio_video_ui;//视频音频的图片区域;
    NoScrollGridView nsgv_world_list_gridview;//图片列表;

    private PullToRefreshScrollView prsv_ScrollView;
    private NoScrollListview mNoScrollListview;
    private NoteDetailsZambiaAdapter mAdapter;
    private List<NoteInfoBean.NoteInfoEntity> mListData_ZF;
    private List<NoteInfoBean.NoteInfoEntity> mListData_PL;
    private List<NoteInfoBean.NoteInfoEntity> mListData_QX;
    private List<NoteInfoBean.NoteInfoEntity> mListData_Zan;
    private List<NoteInfoBean.NoteInfoEntity> mListData_DS;

    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int screenW;//获取控件的宽度

    private NoteRequestBase mNoteRequestBase;
    private WorldListGridViewAdapter mGirdViewAdapter;
    String notetype;

    private int isWho = 1;// 1=转发 ,2 =评论 ,3 =情绪, 4 =赞 = 5= 赏;
    private int pagerIndex = 1;
    private String noteId;
    private String notecontent;
    private String imgurl;
    private String nickname;
    private String userid;
    private String repostid;
    private String isself;
    private String noteFee;
    Drawable iszan;
    Drawable nozan;
    private TextView tv_iszan;
    private String money = "0";
    //判断该支付是那种支付行为
    private String spendtype = "0";
    private String orderno = "0";

    private String sharefree = "0";
    private View view;
    private String paynum;//用户发布帖子时的定价
    boolean isSharePay = false;
    private String isshare;   //分享至其他 0允许 1不允许

    HashMap shareMap;//分享时候的信息

    private WebView web_tuwen;
    private int isDelete = 0;//  1=自己的帖子,显示删除, 2 = 别人的帖子,显示举报
    private String repostfrom = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info_copy);
        if (getIntent() != null) {
            repostid = getIntent().getStringExtra("repostid");
            noteId = getIntent().getStringExtra("noteId");
            if (!TextUtils.isEmpty(getIntent().getStringExtra("repostfrom"))) {
                repostfrom = getIntent().getStringExtra("repostfrom");
            }
        } else {
            noteId = "1";
        }
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(this);
        initView();
        InitImageView();
        mNoteRequestBase.getNoteInfo(noteId, repostid, this);
        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREPOSTLIST, noteId, repostid, pagerIndex, this);
    }


    /**
     * Title:  InitImageView<br>
     * Description: TODO  初始化线的动画<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
        ViewTreeObserver vto2 = ll_getview_width.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_getview_width.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                screenW = ll_getview_width.getWidth();
                LogUtils.e("ww=" + screenW);
                bmpW = (BitmapFactory.decodeResource(getResources(), R.mipmap.hengxian).getWidth());// 获取图片宽度
                offset = (screenW / 5 - bmpW) / 2;// 计算偏移量
                Matrix matrix = new Matrix();
                matrix.postTranslate(offset, 0);
                cursor.setImageMatrix(matrix);// 设置动画初始位置
                LogUtils.e("ww=1111----" + screenW);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_isRenzheng = (ImageView) findViewById(R.id.iv_isRenzheng);
        tv_iszan = (TextView) findViewById(R.id.tv_iszan);
        tv_is_reci = (TextView) findViewById(R.id.tv_is_reci);
        tv_is_yuanchuang = (TextView) findViewById(R.id.tv_is_yuanchuang);
        nozan = getResources().getDrawable(R.mipmap.note_praise);
        iszan = getResources().getDrawable(R.mipmap.notedetail_hongxin);
        iszan.setBounds(0, 0, iszan.getMinimumWidth(), iszan.getMinimumHeight()); //设置边界
        nozan.setBounds(0, 0, nozan.getMinimumWidth(), nozan.getMinimumHeight()); //设置边界
        iv_sharenote = (ImageView) findViewById(R.id.iv_share_note);
        iv_sharenote.setVisibility(View.VISIBLE);
        iv_sharenote.setOnClickListener(this);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_dashang = (ImageView) findViewById(R.id.iv_dashang);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        ll_getview_width = (LinearLayout) findViewById(R.id.ll_getview_width);
        tv_head_title.setText("信息详情");
        tv_zhuanfa_but = (TextView) findViewById(R.id.tv_zhuanfa_but);
        tv_pinglun_but = (TextView) findViewById(R.id.tv_pinglun_but);
        tv_qingxu_but = (TextView) findViewById(R.id.tv_qingxu_but);
        tv_zan_but = (TextView) findViewById(R.id.tv_zan_but);
        tv_shang_but = (TextView) findViewById(R.id.tv_shang_but);
        tv_delete_note = (TextView) findViewById(R.id.tv_delete_note);

        prsv_ScrollView = (PullToRefreshScrollView) findViewById(R.id.prsv_ScrollView);
        mNoScrollListview = (NoScrollListview) findViewById(R.id.mNoScrollListview);
        nsgv_world_list_gridview = (NoScrollGridView) findViewById(R.id.nsgv_world_list_gridview);
        mNoScrollListview.setFocusable(false);


        web_tuwen = (WebView) findViewById(R.id.web_tuwen);

        ll_ZF_but = (LinearLayout) findViewById(R.id.ll_ZF_but);
        ll_PL_but = (LinearLayout) findViewById(R.id.ll_PL_but);
        ll_QX_but = (LinearLayout) findViewById(R.id.ll_QX_but);
        ll_Zan_but = (LinearLayout) findViewById(R.id.ll_Zan_but);


        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
        iv_video_img = (ImageView) findViewById(R.id.iv_video_img);
        iv_audio_img = (ImageView) findViewById(R.id.iv_audio_img);
        tv_audio_long = (TextView) findViewById(R.id.tv_audio_long);
        tv_dengji = (TextView) findViewById(R.id.tv_dengji);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_income = (TextView) findViewById(R.id.tv_income);
        tv_read_num = (TextView) findViewById(R.id.tv_read_num);
        tv_note_content = (TextView) findViewById(R.id.tv_note_content);
        tv_note_title = (TextView) findViewById(R.id.tv_note_title);
        in_audio_video_ui = findViewById(R.id.in_audio_video_ui);
        iv_video_img.setOnClickListener(this);
        iv_audio_img.setOnClickListener(this);

        iv_dashang.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_user_img.setOnClickListener(this);
        tv_delete_note.setOnClickListener(this);
        ll_ZF_but.setOnClickListener(this);
        ll_PL_but.setOnClickListener(this);
        ll_QX_but.setOnClickListener(this);
        ll_Zan_but.setOnClickListener(this);
        tv_zhuanfa_but.setOnClickListener(new MyOnClickListener(0));
        tv_pinglun_but.setOnClickListener(new MyOnClickListener(1));
        tv_qingxu_but.setOnClickListener(new MyOnClickListener(2));
        tv_zan_but.setOnClickListener(new MyOnClickListener(3));
        tv_shang_but.setOnClickListener(new MyOnClickListener(4));
        //隐藏 视频音频 图片 内容 等控件
        in_audio_video_ui.setVisibility(View.GONE);
        iv_video_img.setVisibility(View.GONE);
        iv_play.setVisibility(View.GONE);
        nsgv_world_list_gridview.setVisibility(View.GONE);
        iv_audio_img.setVisibility(View.GONE);
        tv_audio_long.setVisibility(View.GONE);


        mListData_ZF = new ArrayList<>();
        mAdapter = new NoteDetailsZambiaAdapter(this, mListData_ZF, new NoteDetailsZambiaAdapter.ReplyCommentInterface() {
            @Override
            public void replyComment(int positon) {
                Intent intent = null;
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(NoteDetailsActivity.this))) {
                    intent = new Intent(NoteDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                intent = new Intent(NoteDetailsActivity.this, ReplyCommentActivity.class);
                intent.putExtra("noteid", noteId);
                intent.putExtra("repostid", repostid);
                intent.putExtra("replyuserid", mListData_PL.get(positon).getCommuserid());
                if (mListData_PL.get(positon).getReplycommid().equals("0")) {
                    intent.putExtra("replycommid", mListData_PL.get(positon).getCommid());
                } else {
                    intent.putExtra("replycommid", mListData_PL.get(positon).getReplycommid());
                }
                startActivityForResult(intent, 2);
            }
        });
        mAdapter.notifyDataSetChanged();
        mNoScrollListview.setAdapter(mAdapter);

        prsv_ScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_ScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                下拉
                pagerIndex = 1;
                mNoteRequestBase.getNoteInfo(noteId, repostid, NoteDetailsActivity.this);
                switch (isWho) {
                    case 1:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREPOSTLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 2:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 3:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEMOODLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 4:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEZANLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 5:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREWARDLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;

                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                上拉
                pagerIndex++;
                switch (isWho) {
                    case 1:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREPOSTLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 2:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 3:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEMOODLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 4:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEZANLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                    case 5:
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREWARDLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        break;
                }
            }
        });

    }


    /**
     * Title:  MyOnClickListener<br>
     * Description: TODO  标题点击事件<br>
     *
     * @author xuzhuchao
     * @since JDK 1.7
     */

    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_zhuanfa_but:
                    tv_zhuanfa_but.setTextColor(getResources().getColor(R.color.black));
                    tv_pinglun_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_qingxu_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_zan_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_shang_but.setTextColor(getResources().getColor(R.color.font_gray));

                    lineAnimation(index);
                    isWho = 1;
                    if (mListData_ZF != null && mListData_ZF.size() > 0) {
                        mAdapter.setmDatas(mListData_ZF, 1);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex = 1;
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREPOSTLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                    }
                    break;
                case R.id.tv_pinglun_but:
                    tv_zhuanfa_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_pinglun_but.setTextColor(getResources().getColor(R.color.black));
                    tv_qingxu_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_zan_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_shang_but.setTextColor(getResources().getColor(R.color.font_gray));
                    lineAnimation(index);
                    isWho = 2;
                    if (mListData_PL != null && mListData_PL.size() > 0) {
                        mAdapter.setmDatas(mListData_PL, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex = 1;
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                    }
                    break;
                case R.id.tv_qingxu_but:
                    tv_zhuanfa_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_pinglun_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_qingxu_but.setTextColor(getResources().getColor(R.color.black));
                    tv_zan_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_shang_but.setTextColor(getResources().getColor(R.color.font_gray));
                    lineAnimation(index);
                    isWho = 3;
                    if (mListData_QX != null && mListData_QX.size() > 0) {
                        mAdapter.setmDatas(mListData_QX, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex = 1;
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEMOODLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                    }
                    break;
                case R.id.tv_zan_but:
                    tv_zhuanfa_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_pinglun_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_qingxu_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_zan_but.setTextColor(getResources().getColor(R.color.black));
                    tv_shang_but.setTextColor(getResources().getColor(R.color.font_gray));
                    lineAnimation(index);
                    isWho = 4;
//                    if (mListData_Zan != null && mListData_Zan.size() > 0) {
//                        mAdapter.setmDatas(mListData_Zan, isWho);
//                        mAdapter.notifyDataSetChanged();
//                    } else {
                    pagerIndex = 1;
                    mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEZANLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
//                    }
                    break;
                case R.id.tv_shang_but:
                    tv_zhuanfa_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_pinglun_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_qingxu_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_zan_but.setTextColor(getResources().getColor(R.color.font_gray));
                    tv_shang_but.setTextColor(getResources().getColor(R.color.black));
                    lineAnimation(index);
                    isWho = 5;
                    if (mListData_DS != null && mListData_DS.size() > 0) {
                        mAdapter.setmDatas(mListData_DS, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex = 1;
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREWARDLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                    }
                    break;
                default:
                    break;
            }
        }

    }



    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            //分享的方法
            case R.id.iv_share_note:
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(NoteDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if ("1".equals(isshare)) {
                    ShowUtil.showToast(this, "尊：抱歉！发布者不允许此信息分享至其他平台。");
                    return;
                }
                if ("1".equals(sharefree)|| noteEntity.userid.equals(SharedPreferencesUtil.getUid(this))) {
                    HashMap map = new HashMap();
                    map.put("noteid", noteId);
                    map.put("repostid", repostid);
                    PopuUtil.initSharePopup(this, mHandler, map);
                    return;
                } else {
                    HashMap map = new HashMap();
                    map.put("paytype", Constants.share);
                    map.put("money", paynum);
                    PopuUtil.initPayPopu(this, this, map);
                }
                break;
            case R.id.iv_dashang:
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(NoteDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    HashMap map1 = new HashMap();
                    map1.put("paytype", Constants.dasahng);
                    PopuUtil.initPayPopu(this, this, map1);
                }
                break;
            case R.id.iv_video_img:
                starPlayAct();

                break;
            case R.id.iv_audio_img:
                starPlayAct();

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_ZF_but:
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(NoteDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    transmitNote();
                }
                break;
            case R.id.ll_PL_but:
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, CommentActivity.class);
                    intent.putExtra("noteid", noteId);
                    intent.putExtra("repostid", repostid);
                    startActivityForResult(intent, 2);
                }
                break;
            case R.id.ll_QX_but:
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, EmoTionActivity.class);
                    intent.putExtra("noteId", noteId);
                    startActivityForResult(intent, 3);
                }
                break;
            case R.id.ll_Zan_but:
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(getApplicationContext()))) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (noteEntity.getIszan().equals("1")) {//已经点赞
                        tv_zan_but.performClick();
                        ShowUtil.showToast(getApplicationContext(), "取消点赞！");
                        tv_iszan.setCompoundDrawables(nozan, null, null, null);
                        String s;
                        if (noteEntity.getZancount().equals("0")) {
                            s = String.valueOf(Integer.parseInt(noteEntity.getZancount()));
                        } else {
                            s = String.valueOf(Integer.parseInt(noteEntity.getZancount()) - 1);
                        }
                        noteEntity.setZancount(s);
                        noteEntity.setIszan("0");
                    } else if (noteEntity.getIszan().equals("0")) {//还未点赞
                        tv_zan_but.performClick();
                        ShowUtil.showToast(getApplicationContext(), "点赞成功！");
                        tv_iszan.setCompoundDrawables(iszan, null, null, null);
                        String s = String.valueOf(Integer.parseInt(noteEntity.getZancount()) + 1);
                        noteEntity.setZancount(s);
                        noteEntity.setIszan("1");
                    }
                    mNoteRequestBase.postNoteLike(NoteDetailsActivity.this, noteId, repostid, new WZHttpListener() {
                        public void onSuccess(String content, String isUrl) {
                            tv_zan_but.performClick();
//                            try {
//                                JSONObject json = new JSONObject(content);
//                                if (json.getString("result").equals("1")) {
//                                    tv_zan_but.performClick();
//                                    ShowUtil.showToast(getApplicationContext(), json.getString("message"));
//                                    if (noteEntity.getIszan().equals("1")) {
//                                        tv_iszan.setCompoundDrawables(null, null, nozan, null);
//                                        String s = String.valueOf(Integer.parseInt(noteEntity.getZancount()) - 1);
//                                        noteEntity.setZancount(s);
//                                        noteEntity.setIszan("0");
//                                    } else {
//                                        tv_iszan.setCompoundDrawables(null, null, iszan, null);
//                                        String s = String.valueOf(Integer.parseInt(noteEntity.getZancount()) + 1);
//                                        noteEntity.setZancount(s);
//                                        noteEntity.setIszan("1");
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }

                        @Override
                        public void onFailure(String content) {

                        }
                    });
                }
                break;
            case R.id.iv_user_img:
                intent = new Intent(NoteDetailsActivity.this, UserDetailsActivity.class);
                intent.putExtra("detailuserid", userid);
                startActivity(intent);
                break;
            case R.id.tv_delete_note:
                if (isDelete == 1) {
                    //请求删除接口
                    PopuUtil.initDelNotePopu(this, mNoteRequestBase, noteId, NoteDetailsActivity.this);
                } else if (isDelete == 2) {
                    //请求举报接口
                    intent = new Intent(NoteDetailsActivity.this, UserReportActivity.class);
                    intent.putExtra("noteId", noteId);
                    intent.putExtra("repostid", repostid);
                    startActivity(intent);
//                    PopuUtil.initJubaoNotePopu(this, mNoteRequestBase, noteId, repostid, NoteDetailsActivity.this);
                }
                break;
        }
    }

    private void starPlayAct() {
        Intent intent = new Intent(NoteDetailsActivity.this, Play_videoActivity.class);
        intent.putExtra("url", RequestPath.SERVER_PATH + iv_video_and_audio_url);
        intent.putExtra("mediatype", notetype);
        LogUtils.e("图片被点击");
        startActivity(intent);
    }

    /**
     * Title:  lineAnimation<br>
     * Description: TODO  xia<br>
     *
     * @param index
     * @author xuzhuchao
     * @since JDK 1.7
     */
    private void lineAnimation(int index) {
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        Animation animation = new TranslateAnimation(one * currIndex, one * index, 0, 0);
        currIndex = index;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        prsv_ScrollView.onRefreshComplete();
        Gson gson = new Gson();
        NoteInfoBean noteInfoBean;
        LogUtils.e(isUrl);
        switch (isUrl) {
            case RequestPath.POST_TOBACKORDER:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    if (jsonObject.getString("result").equals("1")) {
                        ShowUtil.showToast(NoteDetailsActivity.this, "已申请退款，请您等候系统通知...");
                    } else {
                        ShowUtil.showToast(NoteDetailsActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_ZHUANFA:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String message = jsonObject.getString("message");
                    if (TextUtils.isEmpty(message)) {
                        ShowUtil.showToast(this, message);
                        LogUtils.e("share", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //帖子分享成功后的权限
            case RequestPath.POST_NOTE_SHARESUCC:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String message = jsonObject.getString("message");
                    tv_zhuanfa_but.performClick();
                    if (TextUtils.isEmpty(message)) {
                        ShowUtil.showToast(this, message);
                        LogUtils.e("share", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.GET_NOTEINFO://帖子详情
                LogUtils.e("//帖子详情" + content);
                noteBean = gson.fromJson(content, NoteBean.class);
                if (noteBean == null) {
                    return;
                }
                if (noteBean.getResult().equals("1") && noteBean.getData().size() > 0) {
                    noteEntity = noteBean.getData().get(0);
                    iv_video_and_audio_url = noteEntity.getNotevideo();
                    userid = noteEntity.getUserid();
                    noteFee = noteEntity.getIsfree();
                    isself = noteEntity.getIsself();
                    paynum = noteEntity.getPaynum();
                    //如果分享的支付成功   不需要再次取数据
                    if (!isSharePay) {
                        sharefree = noteBean.getSharefree();
                    }
                    isshare = noteEntity.getIsshare();
                    setUIData(noteEntity);
                } else {
                    initNoExitLayout();
                }
                break;
            case RequestPath.POST_EDITNOTEZAN://点赞,取消点赞
                LogUtils.e("//点赞" + content);
                break;
            case RequestPath.POST_DELNOTE://删除帖子
                LogUtils.e("//删除" + content);
                try {
                    JSONObject json = new JSONObject(content);
                    if (json.getString("result").equals("1")) {
                        SharedPreferencesUtil.setUserDelNoteId(NoteDetailsActivity.this, noteId);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case RequestPath.POST_ADDADVICE://举报帖子
                LogUtils.e("//举报" + content);
                try {
                    JSONObject json = new JSONObject(content);
                    if (json.getString("result").equals("1")) {
                        ShowUtil.showToast(this, "举报成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //打赏成功后给用户提示
            case RequestPath.POST_DASHANG:
                try {
                    JSONObject json = new JSONObject(content);
                    if (json.getString("result").equals("1")) {
                        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTEREWARDLIST, noteId, repostid, pagerIndex, NoteDetailsActivity.this);
                        tv_shang_but.performClick();
                        ShowUtil.showToast(this, json.getString("message").toString());
                    } else {
                        ShowUtil.showToast(this, json.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                noteInfoBean = gson.fromJson(content, NoteInfoBean.class);
                if (noteInfoBean.getResult().equals("1")) {
                    setListData(noteInfoBean, isWho);
                }
                break;
        }
    }

    @Override
    public void onFailure(String content) {
        LogUtils.e("信息详情  界面 接口请求错误 = " + content);
    }

    /**
     * 转发列表等 数据填充
     */
    private void setListData(NoteInfoBean noteInfoBean, int isWho) {
        switch (isWho) {
            case 1://返回转发信息
                if (pagerIndex > 1) {
                    if (mListData_ZF != null && noteInfoBean.getData().size() > 0) {
                        mListData_ZF.addAll(noteInfoBean.getData());
                        mAdapter.setmDatas(mListData_ZF, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex--;
                        ShowUtil.showToast(this, "没有更多数据了");
                    }
                } else {
                    mListData_ZF = noteInfoBean.getData();
                    mAdapter.setmDatas(mListData_ZF, isWho);
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case 2://返回评论信息
                if (pagerIndex > 1) {
                    if (mListData_PL != null && noteInfoBean.getData().size() > 0) {
                        mListData_PL.addAll(noteInfoBean.getData());
                        mAdapter.setmDatas(mListData_PL, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex--;
                        ShowUtil.showToast(this, "没有更多评论了");
                    }
                } else {
                    mListData_PL = noteInfoBean.getData();
                    mAdapter.setmDatas(mListData_PL, isWho);
                    mAdapter.notifyDataSetChanged();
                }

                break;
            case 3:
                if (pagerIndex > 1) {
                    if (mListData_QX != null && noteInfoBean.getData().size() > 0) {
                        mListData_QX.addAll(noteInfoBean.getData());
                        mAdapter.setmDatas(mListData_QX, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex--;
                        ShowUtil.showToast(this, "没有更多数据了");
                    }
                } else {
                    mListData_QX = noteInfoBean.getData();
                    mAdapter.setmDatas(mListData_QX, isWho);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case 4:
                if (pagerIndex > 1) {
                    if (mListData_Zan != null && noteInfoBean.getData().size() > 0) {
                        mListData_Zan.addAll(noteInfoBean.getData());
                        mAdapter.setmDatas(mListData_Zan, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex--;
                        ShowUtil.showToast(this, "没有更多数据了");
                    }
                } else {
                    mListData_Zan = noteInfoBean.getData();
                    mAdapter.setmDatas(mListData_Zan, isWho);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case 5:
                if (pagerIndex > 1) {
                    if (mListData_DS != null && noteInfoBean.getData().size() > 0) {
                        mListData_DS.addAll(noteInfoBean.getData());
                        mAdapter.setmDatas(mListData_DS, isWho);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex--;
                        ShowUtil.showToast(this, "没有更多数据了");
                    }
                } else {
                    mListData_DS = noteInfoBean.getData();
                    mAdapter.setmDatas(mListData_DS, isWho);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
        tv_zhuanfa_but.setText("送给朋友们\n" + noteInfoBean.getRepost_Total());
        tv_zan_but.setText("赞\n" + noteInfoBean.getZan_Total());
        tv_pinglun_but.setText("评论\n" + noteInfoBean.getComment_Total());
        tv_shang_but.setText("赏\n" + noteInfoBean.getReward_Total());
        tv_qingxu_but.setText("情绪\n" + noteInfoBean.getMood_Total());
    }

    /**
     * 填充数据
     **/
    private void setUIData(final NoteBean.NoteEntity noteEntity) {
        if (noteEntity.getIszan().equals("0")) {
            tv_iszan.setCompoundDrawables(nozan, null, null, null);
        } else if (noteEntity.getIszan().equals("1")) {
            tv_iszan.setCompoundDrawables(iszan, null,null , null);
        }
        nickname = noteEntity.getUsernickname();
        tv_nickname.setText(noteEntity.getUsernickname());
        notecontent = noteEntity.getContent();
        tv_note_title.setText(notecontent);
        tv_note_content.setText(noteEntity.getContent().replace("#换行#", "\r\n"));
        if (TextUtils.isEmpty(noteEntity.getHotword())) {
            tv_is_reci.setText("");
        } else {
            tv_is_reci.setText("创造热词：" + noteEntity.getHotword());
        }
        if (noteEntity.getIsself().equals("0")) {
            tv_is_yuanchuang.setText("原创");
        } else {
            tv_is_yuanchuang.setText("非原创");
        }
        tv_dengji.setText(noteEntity.getUserlevel());
        tv_price.setText("¥" + noteEntity.getPaynum());
        tv_income.setText("¥" + noteEntity.getShouyi());
        tv_day.setText(DateTiemUtils.editTime(noteEntity.getAddtime()));
        tv_read_num.setText("阅读：" + noteEntity.getClickcount());

        if (noteEntity.getUserid().equals(SharedPreferencesUtil.getUid(this))) {
            isDelete = 1;
            tv_delete_note.setText("删除");
        } else {
            isDelete = 2;
            tv_delete_note.setText("举报");
        }
        notetype = noteEntity.getNotetype();


        switch (notetype) {
            case "4"://文字
                if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                    nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), this);
                    mGirdViewAdapter.notifyDataSetChanged();
                    nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                } else {
                    tv_note_content.setVisibility(View.VISIBLE);
                    if (true) {
                        tv_note_content.setText(noteEntity.getContent().replace("#换行#", "\r\n"));
                    } else {
                        SpannableStringBuilder builder = new SpannableStringBuilder(noteEntity.getOldusernickname() + "：" + noteEntity.getContent().replace("#换行#", "\r\n"));
                        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
                        builder.setSpan(blueSpan, 0, noteEntity.getOldusernickname().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv_note_content.setText(builder);
                    }
                }
                break;
            case "1"://视频文
                in_audio_video_ui.setVisibility(View.VISIBLE);
                iv_play.setVisibility(View.VISIBLE);
                iv_video_img.setVisibility(View.VISIBLE);
                imgViewSetData(noteEntity.getNotevideopic(), iv_video_img);
                iv_play.setImageResource(R.mipmap.note_play);
                break;
            case "2"://音频文
                tv_audio_long.setVisibility(View.VISIBLE);
                in_audio_video_ui.setVisibility(View.VISIBLE);
                iv_audio_img.setVisibility(View.VISIBLE);
                iv_audio_img.setImageResource(R.mipmap.ic_music);
               // tv_audio_long.setText("00:00:00");
                break;
            case "3"://图文
                nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                    mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), NoteDetailsActivity.this/*,mBitmap,bitmapDisplayConfig*/);
                    mGirdViewAdapter.notifyDataSetChanged();
                    nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                }
                break;
            case "5"://图文混排
                web_tuwen.setVisibility(View.VISIBLE);
                web_tuwen.getSettings().setBlockNetworkImage(false);
                web_tuwen.getSettings().setLoadsImagesAutomatically(true);
                //支持js
                web_tuwen.getSettings().setJavaScriptEnabled(true);

                web_tuwen.loadUrl(RequestPath.SERVER_PATH + "/site/webcontent.aspx?noteid=" + noteEntity.getNoteid());


                break;
        }

        nsgv_world_list_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] url = new String[noteEntity.getList().size()];
                for (int i = 0; i < noteEntity.getList().size(); i++) {
                    url[i] = noteEntity.getList().get(i).getUrl();
                }
                Intent intent = new Intent(NoteDetailsActivity.this, ImgShowActivity.class);
                intent.putExtra("index", position);
                intent.putExtra("imgUrl", url);
                startActivity(intent);
            }
        });

        imgurl = noteEntity.getUserface();
        imgViewSetData(imgurl, iv_user_img);


    }


    private void imgViewSetData(String url, ImageView iv) {
        view = iv;
        Glide.with(WeValueApplication.applicationContext)
                .load(RequestPath.SERVER_PATH + url)
                .placeholder(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }

    //转发帖子的方法
    private void transmitNote() {
        Intent intent = null;
        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, TransmitNoteActivity.class);
            intent.putExtra("repostid", repostid);
            intent.putExtra("noteId", noteId);
            intent.putExtra("nickname", nickname);
            intent.putExtra("imgurl", imgurl);
            intent.putExtra("notecontent", notecontent);
            intent.putExtra("isself", isself);
            intent.putExtra("noteFee", noteFee);
            intent.putExtra("paynum", paynum);
            intent.putExtra("repostfrom", repostfrom);
            startActivityForResult(intent, 1);
        }
    }

    //打赏帖子的方法
    private void dashang() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteId);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("rewardmoney", money);
        map.put("noteFee", noteFee);
        map.put("spendtype", spendtype);
        map.put("orderno", orderno);
        NetworkRequest.postRequest(RequestPath.POST_DASHANG, map, this);
    }

    //支付成功后的方法回调
    @Override
    public void paySucceed(HashMap map) {
        String paytype = (String) map.get("paytype");
        switch (paytype) {
            case Constants.dasahng:
                money = (String) map.get("money");
                spendtype = (String) map.get("spendtype");
                orderno = (String) map.get("orderno");
                dashang();
                break;
            case Constants.transmit:
                isSharePay = true;
                this.shareMap = new HashMap();
                this.shareMap.put("money", map.get("money"));
                this.shareMap.put("paymoney", map.get("money"));
                this.shareMap.put("orderno", map.get("orderno"));
                this.shareMap.put("spendtype", map.get("spendtype"));
                this.shareMap.put("paytype", map.get("paytype"));

                HashMap shareMap = new HashMap();
                shareMap.put("noteid", noteId);
                shareMap.put("repostid", repostid);
                PopuUtil.initSharePopup(this, mHandler, shareMap);
                break;
        }
    }

    @Override
    public void payStart(String startType) {

    }

    @Override
    public void payFail(String failString) {

    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (view != null) {
//            Glide.clear(view);
//        }
//    }

    /*转发 评论 发表情绪时候接受回调信息*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String url = "";
            switch (requestCode) {
                case 1://转发
                    sharefree = "1";
//                    isSharePay = true;
                    tv_zhuanfa_but.performClick();
                    url = RequestPath.GET_NOTEREPOSTLIST;
                    break;
                case 2://评论
                    tv_pinglun_but.performClick();
                    url = RequestPath.GET_NOTECOMMLIST;
                    break;
                case 3://情绪
                    tv_qingxu_but.performClick();
                    url = RequestPath.GET_NOTEMOODLIST;
                    break;
                case 4://点赞
//                    url = RequestPath.GET_NOTEZANLIST;
                    return;
                case 5://打赏
//                    url = RequestPath.GET_NOTEREWARDLIST;
                    return;
                default:
                    return;
            }
            mNoteRequestBase.getNoteInfoRepostlist(url, noteId, repostid, 1, NoteDetailsActivity.this);
        }
    }

    /*帖子不存在的布局设计*/
    private void initNoExitLayout() {
        findViewById(R.id.ll_not_exit).setVisibility(View.VISIBLE);
    }

    /*分享成功回填第三方信息*/
    private void bindShareSucc(HashMap map) {
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteId);
        map.put("repostid", repostid);
        map.put("shareto", "1");
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("isself", isself);
        NetworkRequest.postRequest(RequestPath.POST_NOTE_SHARESUCC, map, this);
    }

    /*分享失败申请退回第三方资金*/
    private void refundMoney(HashMap map) {
        ShowUtil.showToast(NoteDetailsActivity.this, "开始申请退款!");
        NetworkRequest.postRequest(RequestPath.POST_TOBACKORDER, map, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (view != null) {
            Glide.clear(view);
        }
    }
}
