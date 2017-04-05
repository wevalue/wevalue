package com.wevalue.ui.details.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NoteBean;
import com.wevalue.model.NoteInfoBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.adapter.NoteCommentAdapter;
import com.wevalue.ui.details.adapter.ReplyCommentActivity;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.activity.TransmitNoteActivity;
import com.wevalue.ui.world.adapter.WorldListGridViewAdapter;
import com.wevalue.utils.Constants;
import com.wevalue.utils.ImageUitls;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.ShareHelper;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.AudioView;
import com.wevalue.view.FlowLayout;
import com.wevalue.view.HeadView;
import com.wevalue.view.NoScrollGridView;
import com.wevalue.view.NoScrollListview;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by xox on 2017/3/7.
 */

public class NoteDetailActivity extends BaseActivity implements WZHttpListener, View.OnClickListener, AdapterView.OnItemClickListener, PayInterface {


    private HeadView head_view;
    private LinearLayout layout_cover; //遮挡层布局id
    private PullToRefreshScrollView pullLayout; //遮挡层布局id
    private RoundedImageView iv_user_img; //头像
    private ImageView iv_user_v; //
    private TextView tv_nickname; //昵称
    private TextView tv_grade;//等级
    private TextView tv_weizhi;//微值号
    private TextView tv_income;//收益
    private TextView tv_price;//发布价格

    private TextView tv_note_title;//标题
    private TextView tv_zhuangfaren;//转发信息的作者

    private AudioView note_audio;//语音
    private JCVideoPlayerStandard note_video;//视频
    private NoScrollGridView nsgv_world_list_gridview;//图片
    private WebView web_tuwen; //图文web
    private TextView tv_note_content;//内容

    private TextView tv_original;//原创
    private TextView tv_read_num;//阅读数
    private TextView tv_delete_note;//举报或者删除
    private TextView tv_reci;//热词

    private ImageView iv_dashang;//打赏
    private TextView tv_dashang_count;//打赏数量
    private FlowLayout fl_layout;//打赏数量

    private TextView tv_comment;//写评论
    private TextView tv_comment_num;//评论数量

    private NoScrollListview lv_comment;//评论列表布局
    private LinearLayout layout_no_comment;//没有评论显示的布局

    private LinearLayout layout_forward;//底部转发layout //免费的隐藏 不让转发
    private TextView tv_forward;//转发给朋友们
    private TextView tv_forward_num;//转发数量


    private List<NoteInfoBean.NoteInfoEntity> mListDataPl;//评论数据集合
    private List<NoteInfoBean.NoteInfoEntity> mListDataDs;//打赏数据集合
    private NoteCommentAdapter mAdapter;

    private NoteRequestBase mNoteRequestBase;//接口集合
    private NoteBean.NoteEntity noteEntity; //帖子详情实体类

    private int pagerIndex = 1; //评论加载页数  第几页

    private String repostid = "0";//转发id
    private String noteId;//帖子id
    private String repostfrom = "3";////1世界 2影响力 3其他

    private int isDelete = 0;//  1=自己的帖子,显示删除, 2 = 别人的帖子,显示举报
    private String sharefree = "0";//是否免费分享
    private String isfree = "0";//是否免费
    private HashMap payHashMap;// 支付成功后返回的支付信息集合
    private String commid = "-1";// 点赞的评论id
    private String myUserid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        context = this;
        myUserid = SharedPreferencesUtil.getUid(this);
        loadingDialog.show();
        getIntentData();
        initView();
        initCommtenAdapter();
        getNetworkData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myUserid = SharedPreferencesUtil.getUid(this);
    }

    private void getNetworkData() {
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(this);
        //获取帖子详情数据
        mNoteRequestBase.getNoteInfo(noteId, repostid, this);
        //获取帖子评论列表数据
        mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, this);
        //获取帖子打赏数据
        getNoteRewardList(RequestPath.GET_NOTEREWARDLIST, noteId, repostid, 1, this);
    }

    /**
     * 获取从上个页面带过来的数据
     */
    private void getIntentData() {
        if (getIntent() != null) {
            repostid = getIntent().getStringExtra("repostid");
            noteId = getIntent().getStringExtra("noteId");
            if (!TextUtils.isEmpty(getIntent().getStringExtra("repostfrom"))) {
                repostfrom = getIntent().getStringExtra("repostfrom");
            }
        } else {
            noteId = "1";
        }
        if (TextUtils.isEmpty(repostid))repostid = "0";
    }

    private void initPullToRefreshScrollView() {
        pullLayout = (PullToRefreshScrollView) findViewById(R.id.pullLayout);
        pullLayout.setFocusable(false);
        pullLayout.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                pagerIndex = 1;
                getNetworkData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //获取帖子评论列表数据
                pagerIndex++;
                mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, NoteDetailActivity.this);
            }
        });

    }

    private void initView() {
        initPullToRefreshScrollView();
        layout_cover = find(R.id.layout_cover);
        layout_cover.setVisibility(View.INVISIBLE);
        head_view = find(R.id.head_view);
        head_view.bindViews("详情");
        head_view.iv_back.setOnClickListener(this);

        iv_user_v = find(R.id.iv_user_v);
        iv_user_img = find(R.id.iv_user_img);
        tv_nickname = find(R.id.tv_nickname);
        tv_grade = find(R.id.tv_grade);
        tv_weizhi = find(R.id.tv_weizhi);
        tv_income = find(R.id.tv_income);
        tv_price = find(R.id.tv_price);

        tv_note_title = find(R.id.tv_note_title);
        tv_zhuangfaren = find(R.id.tv_zhuangfaren);
        tv_note_content = find(R.id.tv_note_content);
        note_audio = find(R.id.note_audio);
        note_video = find(R.id.note_video);

        nsgv_world_list_gridview = find(R.id.nsgv_world_list_gridview);
        web_tuwen = find(R.id.web_tuwen);

        tv_original = find(R.id.tv_original);
        tv_read_num = find(R.id.tv_read_num);
        tv_delete_note = find(R.id.tv_delete_note);
        tv_reci = find(R.id.tv_reci);
        iv_dashang = find(R.id.iv_dashang);
        tv_dashang_count = find(R.id.tv_dashang_count);
        fl_layout = find(R.id.fl_layout);


        tv_comment = find(R.id.tv_comment);
        tv_comment_num = find(R.id.tv_comment_num);
        tv_forward = find(R.id.tv_forward);
        tv_forward_num = find(R.id.tv_forward_num);
        layout_forward = find(R.id.layout_forward);

        lv_comment = find(R.id.lv_comment);
        lv_comment.setFocusable(false);
        layout_no_comment = find(R.id.layout_no_comment);
        layout_no_comment.setOnClickListener(this);
    }


    @Override
    public void onSuccess(String content, String isUrl) {
        if (loadingDialog.isShowing()) loadingDialog.dismiss();
        pullLayout.onRefreshComplete();
        Gson gson = new Gson();
        switch (isUrl) {
            case RequestPath.GET_NOTEINFO://帖子详情
                try {
                    LogUtils.e("//帖子详情" + content);
                    NoteBean noteBean = gson.fromJson(content, NoteBean.class);
                    sharefree = noteBean.getSharefree();
                    if (noteBean.result.equals("1") && noteBean.getData().size() > 0) {
                        noteEntity = noteBean.getData().get(0);
                        if (noteEntity == null) return;
                        setUIData(noteEntity);
                    } else ShowUtil.showToast(this, "帖子被外星人收藏了");
                } catch (Exception e) {
                    e.printStackTrace();
                    ShowUtil.showToast(this, "帖子被外星人收藏了");
                }
                break;
            case RequestPath.GET_NOTEREWARDLIST://获取打赏信息
                NoteInfoBean noteInfoBeanDs = gson.fromJson(content, NoteInfoBean.class);
                if (noteInfoBeanDs != null)
                    mListDataDs = noteInfoBeanDs.getData();
                if (mListDataDs != null && mListDataDs.size() > 0) {
                    fl_layout.removeAllViews();
                    for (int i = 0; i < mListDataDs.size(); i++) {
                        RoundedImageView view = new RoundedImageView(context);
                        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(120, 120);
                        view.setLayoutParams(params);
                        view.setOval(true);
                        ImageUitls.setHead(mListDataDs.get(i).getUserface(), view);
                        view.setPadding(8, 15, 8, 0);
                        view.setLayoutParams(params);
                        fl_layout.addView(view);
                    }
                    tv_dashang_count.setText(mListDataDs.size() + "人打赏");
                } else {
                    tv_dashang_count.setText("");
                }

                break;
            case RequestPath.GET_NOTECOMMLIST://评论列表
                NoteInfoBean commentBean = gson.fromJson(content, NoteInfoBean.class);
                List<NoteInfoBean.NoteInfoEntity> list = recombinantData(commentBean);
                if (pagerIndex > 1) {
                    layout_no_comment.setVisibility(View.GONE);
                    if (mListDataPl != null && commentBean.getData().size() > 0) {
                        mListDataPl.addAll(list);
                        mAdapter.setmDatas(mListDataPl, 2);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        pagerIndex--;
                        ShowUtil.showToast(this, "没有更多评论了");
                    }
                } else {
                    mListDataPl = list;
                    mAdapter.setmDatas(mListDataPl, 2);
                    mAdapter.notifyDataSetChanged();

                    if (mListDataPl != null && mListDataPl.size() > 0)
                        layout_no_comment.setVisibility(View.INVISIBLE);
                    else {
                        layout_no_comment.setVisibility(View.VISIBLE);
                    }
                }


                break;

            case RequestPath.POST_DELNOTE:
                delectNote(content);
                break;
            //帖子分享成功后的权限
            case RequestPath.POST_NOTE_SHARESUCC:
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String message = jsonObject.getString("message");
                    if (TextUtils.isEmpty(message)) {
                        ShowUtil.showToast(context, message);
                        LogUtils.e("message", message);
                    }else {
                        sharefree = "1";
                        ShowUtil.showToast(context, "分享成功!");
                        //分享成功  重新获取帖子详情数据
                        mNoteRequestBase.getNoteInfo(noteId, repostid, NoteDetailActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 删除帖子
     *
     * @param content
     */
    private void delectNote(String content) {
        try {
            JSONObject jsonObject = new JSONObject(content);
            String message = jsonObject.getString("message");
            String result = jsonObject.getString("result");
            if ("1".equals(result)) {
                ShowUtil.showToast(this, message);
                finish();
            } else {
                ShowUtil.showToast(this, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {
        pullLayout.onRefreshComplete();
        if (loadingDialog.isShowing()) loadingDialog.dismiss();
    }

    /**
     * 数据重组  因为返回的数据 评论和回复评论在一起混合着 所以需要把回复的评论归类重组
     *
     * @param noteInfoBean
     * @return
     */
    private List<NoteInfoBean.NoteInfoEntity> recombinantData(NoteInfoBean noteInfoBean) {
        /****获取 数据集合 新建一个空集合， 把所有的回复找出来并放入集合里面
         * 最后再统一删除以免改变list.size()大小让数据混乱
         ***/
        List<NoteInfoBean.NoteInfoEntity> removeList = new ArrayList<>();
        List<NoteInfoBean.NoteInfoEntity> entityList = noteInfoBean.getData();
        for (int i = 0; i < entityList.size(); i++) {
            NoteInfoBean.NoteInfoEntity entity = entityList.get(i);
            String commid = entityList.get(i).getCommid();
            List<NoteInfoBean.NoteInfoEntity> replyList = new ArrayList<>();
            for (int j = i + 1; j < entityList.size(); j++) {
                String repalyid = entityList.get(j).getReplycommid();
                //评论的id和回复的id相等时 那这个就是此评论的回复
                if (commid.equals(repalyid)) {
                    replyList.add(entityList.get(j));
                    removeList.add(entityList.get(j));
                }
            }
            entity.setReplycomm(replyList);
        }
        //统一删除重复的数据
        entityList.removeAll(removeList);
        return entityList;
    }

    /**
     * 初始化评论的适配器
     **/
    private void initCommtenAdapter() {
        mListDataPl = new ArrayList<>();
        mAdapter = new NoteCommentAdapter(this, mListDataPl, new NoteCommentAdapter.ReplyCommentInterface() {
            @Override
            public void replyComment(NoteInfoBean.NoteInfoEntity noteInfo) {
                Intent intent = null;
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(NoteDetailActivity.this))) {
                    intent = new Intent(NoteDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                intent = new Intent(context, ReplyCommentActivity.class);
                intent.putExtra("noteid", noteId);
                intent.putExtra("repostid", noteInfo.getRepostid());
                intent.putExtra("replyuserid", noteInfo.getCommuserid());
                intent.putExtra("replycommid", noteInfo.getCommid());
                startActivityForResult(intent, 2);
            }

            @Override
            public void fabulouComment(NoteInfoBean.NoteInfoEntity noteInfoEntity) {
                Intent intent = null;
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(NoteDetailActivity.this))) {
                    intent = new Intent(NoteDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                commid = noteInfoEntity.getCommid();

                String zanNums = noteInfoEntity.getZannums();
                int nums = getInt(zanNums) + 1;
                noteInfoEntity.setZannums(nums + "");
                zanPay();
            }
        });
        lv_comment.setAdapter(mAdapter);
        lv_comment.setSelected(false);
        lv_comment.setSelector(R.color.white);
    }

    private int getInt(String num) {
        try {
            int nums = Integer.parseInt(num);
            return nums;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


    /**
     * //处理转发内容加 @作者：
     *
     * @param noteEntity
     */
    private void textChange(NoteBean.NoteEntity noteEntity) {
        String context_1 = "@" + noteEntity.getOldusernickname() + "：" + noteEntity.getTitle();
        SpannableStringBuilder style_1 = new SpannableStringBuilder(context_1);
        style_1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0,
                noteEntity.getOldusernickname().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_zhuangfaren.setVisibility(View.VISIBLE);
        tv_zhuangfaren.setText(style_1);
    }
//    /**转发帖子的信息 **/
//    private void setRepostNotData(NoteBean.NoteEntity noteEntity){}
//    /**原帖子的信息**/
//    private void setPrimaryNoteData(NoteBean.NoteEntity noteEntity){}

    /**
     * 填充数据: 转发帖子的信息 和 原帖子的信息 不同
     * 主要区别在于 作者，收益，转发量，评论量不同
     **/
    public void setUIData(final NoteBean.NoteEntity noteEntity) {
        if (noteEntity == null) {

            return;
        }
        //作者信息
        if ("1".equals(noteEntity.getUserv()))
            iv_user_v.setVisibility(View.VISIBLE);
        else iv_user_v.setVisibility(View.INVISIBLE);
        ImageUitls.setHead(noteEntity.getUserface(), iv_user_img);
        tv_nickname.setText(noteEntity.getUsernickname());
        tv_grade.setText(noteEntity.getUserlevel());
        tv_price.setText("价格 " + noteEntity.getPaynum());
        tv_income.setText("收益 " + noteEntity.getShouyi());
        tv_weizhi.setText("微值号 " + noteEntity.getUsernumber());
        tv_weizhi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ShowUtil.showToast(context,"帖子ID: "+noteEntity.getNoteid());
                return false;
            }
        });
        tv_note_content.setText(noteEntity.getContent());
        tv_read_num.setText(noteEntity.getClickcount());
        //判断是否是转发信息
        if (repostid.equals("0")) {
            //帖子信息   如果是转发的信息 那转发信息显示在标题上 标题往哪写呢？
            tv_note_title.setText(noteEntity.getTitle());
            tv_zhuangfaren.setVisibility(View.GONE);
        } else {
            tv_note_title.setText(noteEntity.getRepostcontent());
            //处理转发内容加 @作者：
            textChange(noteEntity);
        }
        isFree(noteEntity);
        isDeleteText(tv_delete_note, noteEntity);
        isOriginal(tv_original, noteEntity);
        isShare(noteEntity);//是否可以分享
        isReCi(tv_reci, noteEntity);
        tv_delete_note.setOnClickListener(this);
        iv_dashang.setOnClickListener(this);

        initNotTextView(noteEntity);
        //点击监听
        iv_user_img.setOnClickListener(this);
        tv_delete_note.setOnClickListener(this);

        iv_dashang.setOnClickListener(this);
        tv_comment.setOnClickListener(this);
        tv_forward.setOnClickListener(this);

        tv_comment_num.setText("评论数\t" + noteEntity.getCommcount());
        String repostCount = noteEntity.getRepostcount();
        if (TextUtils.isEmpty(repostCount) || "0".equals(repostCount)) {
            tv_forward_num.setVisibility(View.GONE);
            tv_forward_num.setText(noteEntity.getRepostcount() + "人已转发");
        } else {
            tv_forward_num.setText(noteEntity.getRepostcount() + "人已转发");
            tv_forward_num.setVisibility(View.VISIBLE);
        }
        /**所有数据已设置 显示整体布局**/
        layout_cover.setVisibility(View.VISIBLE);
    }
   private void  isFree(NoteBean.NoteEntity noteEntity){
       isfree = noteEntity.getIsfree();
       if ("1".equals(isfree)){
           tv_price.setVisibility(View.GONE);
           tv_income.setVisibility(View.GONE);
           layout_forward.setVisibility(View.GONE);
       }else {
           tv_price.setVisibility(View.VISIBLE);
           tv_income.setVisibility(View.VISIBLE);
           layout_forward.setVisibility(View.VISIBLE);
       }
   }
    /**
     * 判断是否有热词
     */
    private void isReCi(TextView tv_reci, NoteBean.NoteEntity noteEntity) {
        if (!TextUtils.isEmpty(noteEntity.getHotword()) && !"".equals(noteEntity.getHotword())) {
            tv_reci.setText("热词：" + noteEntity.getHotword());
            tv_reci.setVisibility(View.VISIBLE);
        } else {
            tv_reci.setText("");
            tv_reci.setVisibility(View.GONE);
        }
    }

    private void isShare(NoteBean.NoteEntity noteEntity) {
        String isShare = noteEntity.getIsshare();
        if ("1".equals(isShare)) {
            head_view.iv_share_note.setVisibility(View.GONE);
        } else {
            if (myUserid.equals(noteEntity.getUserid())){
                head_view.iv_share_note.setVisibility(View.VISIBLE);
            }else if (!"1".equals(noteEntity.getIsfree())){
                head_view.iv_share_note.setVisibility(View.VISIBLE);
            } else {
                head_view.iv_share_note.setVisibility(View.GONE);
            }
            head_view.iv_share_note.setOnClickListener(this);
        }
    }

    /**
     * 判断是否原创
     */
    private void isOriginal(TextView tv_original, NoteBean.NoteEntity noteEntity) {
        if (noteEntity.getIsself().equals("0")) {
            tv_original.setText("原创");
            tv_original.setVisibility(View.VISIBLE);
        } else {
            tv_original.setText("非原创");//如果非原创 也不显示
            tv_original.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否是自己的帖子 自己的帖子显示删除
     */
    private void isDeleteText(TextView tv_delete_note, NoteBean.NoteEntity noteEntity) {
        if (noteEntity.getUserid().equals(SharedPreferencesUtil.getUid(this))) {
            isDelete = 1;
            tv_delete_note.setText("删除");
        } else {
            isDelete = 2;
            tv_delete_note.setText("举报");
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.iv_user_img://去作者详情
                if (noteEntity != null) {
                    intent = new Intent(this, UserDetailsActivity.class);
                    intent.putExtra("detailuserid", noteEntity.getUserid());
                    startActivity(intent);
                }
                break;
            case R.id.tv_delete_note://删除
                if (isDelete == 1) {
                    //请求删除接口
                    PopuUtil.initDelNotePopu(this, mNoteRequestBase, noteId, this);
                } else if (isDelete == 2) {
                    //请求举报接口
                    intent = new Intent(this, UserReportActivity.class);
                    intent.putExtra("noteId", noteId);
                    intent.putExtra("repostid", repostid);
                    startActivity(intent);
                }
                break;
            case R.id.iv_dashang://打赏
                dashangPay();
                break;
            case R.id.layout_no_comment://去评论
            case R.id.tv_comment://去评论
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
            case R.id.tv_forward:// 转发
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (noteEntity != null && noteEntity.getIsfree().equals("1")) {
                        ShowUtil.showToast(context, "免费的信息不能转发");
                    } else {
                        transmitNote();
                    }
                }
                break;
            case R.id.iv_share_note:// 分享
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    checkShare();
                }
                break;

        }
    }

    private void dashangPay() {

        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            if (!noteEntity.getUserid().equals(myUserid)) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("paytype", Constants.dasahng);
                hashMap.put("spendtype", Constants.suiyinpay);
                hashMap.put("money", null);//打赏不需要传金额 所以为null
                PopuUtil.initPayfom(this, hashMap, this);
            } else {
                ShowUtil.showToast(this, "不能孤芳自赏哦！");
            }
        }
    }

    private void checkShare() {
        myUserid = SharedPreferencesUtil.getUid(this);
        if ("1".equals(sharefree) || noteEntity.getUserid().equals(myUserid)) {
            shareNote();
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("paytype", Constants.share);
            hashMap.put("spendtype", Constants.suiyinpay);
            hashMap.put("money", noteEntity.getPaynum());//
            PopuUtil.initPayfom(this, hashMap, this);
        }
    }

    private void initNotTextView(NoteBean.NoteEntity noteEntity) {
        note_audio.setVisibility(View.GONE);
        note_video.setVisibility(View.GONE);
        nsgv_world_list_gridview.setVisibility(View.GONE);
        web_tuwen.setVisibility(View.GONE);

        String noteType = noteEntity.getNotetype();
        switch (noteType) {
            case "1"://显示视频
                head_view.setTranTitle();
                note_video.setVisibility(View.VISIBLE);
                note_video.setUp(RequestPath.SERVER_WEB_PATH + noteEntity.getNotevideo()
                        , "");
                ImageUitls.setImg(RequestPath.SERVER_WEB_PATH + noteEntity.getNotevideopic(), note_video.thumbImageView);
                break;
            case "2"://显示音频
                note_audio.setVisibility(View.VISIBLE);
                note_audio.player(RequestPath.SERVER_WEB_PATH + noteEntity.getNotevideo());
                break;
            case "3"://显示图片
                if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                    nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    WorldListGridViewAdapter mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), this/*,mBitmap,bitmapDisplayConfig*/);
                    mGirdViewAdapter.notifyDataSetChanged();
                    nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    nsgv_world_list_gridview.setOnItemClickListener(this);
                }
                break;
            case "4"://显示文字 因为早期纯文字帖子也能带图片 所以也要判断图片
                if (noteEntity.getList_1() != null && noteEntity.getList_1().size() > 0) {
                    nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                    WorldListGridViewAdapter mGirdViewAdapter = new WorldListGridViewAdapter(noteEntity.getList_1(), this/*,mBitmap,bitmapDisplayConfig*/);
                    mGirdViewAdapter.notifyDataSetChanged();
                    nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    nsgv_world_list_gridview.setOnItemClickListener(this);
                }
                break;
            case "5"://显示图文
                tv_note_content.setVisibility(View.GONE);
                web_tuwen.setFocusable(false);
                web_tuwen.setVisibility(View.VISIBLE);
                web_tuwen.getSettings().setBlockNetworkImage(false);
                web_tuwen.getSettings().setLoadsImagesAutomatically(true);
                web_tuwen.getSettings().setDomStorageEnabled(true);
                //支持js
                web_tuwen.getSettings().setJavaScriptEnabled(true);
                String url = RequestPath.SERVER_WEB_PATH + "/site/webcontent.aspx?noteid=" + noteEntity.getNoteid();
                web_tuwen.loadUrl(url);
                break;
        }
    }

    //图片点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String[] url = new String[noteEntity.getList().size()];
        for (int i = 0; i < noteEntity.getList().size(); i++) {
            url[i] = noteEntity.getList().get(i).getUrl();
        }
        Intent intent = new Intent(this, ImgShowActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("imgUrl", url);
        startActivity(intent);
    }

    //转发帖子的方法
    private void transmitNote() {
        Intent intent = null;
        if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            if (noteEntity == null) return;
            intent = new Intent(this, TransmitNoteActivity.class);
            intent.putExtra("repostid", repostid);
            intent.putExtra("noteId", noteId);
            intent.putExtra("nickname", noteEntity.getUsernickname());
            intent.putExtra("imgurl", noteEntity.getUserface());
            intent.putExtra("notecontent", noteEntity.getContent());
            intent.putExtra("isself", noteEntity.isself);
            intent.putExtra("noteFee", noteEntity.getIsfree());
            intent.putExtra("paynum", noteEntity.getPaynum());
            intent.putExtra("repostfrom", repostfrom);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * 支付完成后 调用此方法打赏
     *
     * @param money     打赏金额
     * @param spendtype 支付类型 支付宝 微信等、
     * @param orderno   订单号
     */
    private void dashang(String money, String spendtype, String orderno) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteId);
        map.put("repostid", repostid);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("noteFee", noteEntity.getIsfree());
        map.put("rewardmoney", money);
        map.put("spendtype", spendtype);
        map.put("orderno", orderno);
        NetworkRequest.postRequest(RequestPath.POST_DASHANG, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                ShowUtil.showToast(context, "打赏成功");
                getNoteRewardList(RequestPath.GET_NOTEREWARDLIST, noteId, repostid, 1, NoteDetailActivity.this);
                //获取帖子详情数据
                mNoteRequestBase.getNoteInfo(noteId, repostid, NoteDetailActivity.this);
            }

            @Override
            public void onFailure(String content) {

            }
        });
    }

    /**
     * 获取打赏数据
     *
     * @param url
     * @param noteid
     * @param repostid
     * @param index
     * @param wZHttpListener
     */
    public void getNoteRewardList(String url, String noteid, String repostid, int index, WZHttpListener wZHttpListener) {
        Map<String, String> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteid);
        map.put("pagenum", "100");//因为打赏取得是所有数据 所以给100 打赏可能超不过100
        map.put("pageindex", String.valueOf(index));
        map.put("repostid", repostid);
        NetworkRequest.getRequest(url, map, wZHttpListener);
    }

    /**
     * 分享帖子
     */
    private void shareNote() {
        HashMap shareMap = new HashMap();
        shareMap.put("noteid", noteId);
        shareMap.put("repostid", repostid);
        shareMap.put("title", ShareHelper.getTitle(noteEntity.getTitle()));
        shareMap.put("content", ShareHelper.getTitle(noteEntity.getContent()));
        shareMap.put("imgUrl", noteEntity.getUserface());
        PopuUtil.initSharePopup(this, mHandler, shareMap);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if ("1".equals(sharefree)) {
                        ShowUtil.showToast(context, "分享成功!");
                        return;
                    }
                    bindShareSucc();
                    break;
                case 2:
                    ShowUtil.showToast(context, "分享失败!");
                    if ("1".equals(sharefree)) {
                        return;
                    }
                    refundMoney(payHashMap);
                    break;
                case 3:
                    ShowUtil.showToast(context, "取消分享!");
                    if ("1".equals(sharefree)) {
                        return;
                    }
                    refundMoney(payHashMap);
                    break;
            }
        }
    };

    /*分享成功回填第三方信息*/
    private void bindShareSucc() {
        //如果是自己的帖子则没有以下逻辑
        if (noteEntity.getUserid().equals(myUserid))return;
        HashMap map = new HashMap();
        map.putAll(payHashMap);
        map.put("code", RequestPath.CODE);
        map.put("noteid", noteId);
        map.put("repostid", repostid);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("shareto", "1"); //1  qq  2 weixin  3  weibo
        map.put("isfree", noteEntity.getIsfree()); // 是否免费
        map.put("isself", noteEntity.isself);  //是否原创
        map.put("paymoney", payHashMap.get("money"));  //是否原创
        NetworkRequest.postRequest(RequestPath.POST_NOTE_SHARESUCC, map, this);
    }

    /*分享失败申请退回第三方资金*/
    private void refundMoney(HashMap payHashMap) {
        //如果是自己的帖子则没有以下逻辑
        if (noteEntity.getUserid().equals(myUserid))return;

        Map<String, Object> map = payHashMap;
        map.putAll(payHashMap);
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        NetworkRequest.postRequest(RequestPath.POST_TOBACKORDER, map, this);
    }

    /**
     * 支付成功
     *
     * @param map
     */
    @Override
    public void paySucceed(HashMap map) {
        //支付类型 打赏 转发 分享
        String paytype = (String) map.get("paytype");
        String money = (String) map.get("money");
        //支付方式 微信支付 支付宝
        String spendtype = (String) map.get("spendtype");
        String orderno = (String) map.get("orderno");
        payHashMap = map;

        switch (paytype) {
            case Constants.dasahng:
                dashang(money, spendtype, orderno);
                break;
            case Constants.share:
                shareNote();
                Log.e("paySucceed", "sharefree = " + sharefree);
                break;
            case Constants.zan:
                onClickZan(money, orderno);
                break;
        }
    }

    @Override
    public void payStart(String startType) {

    }

    @Override
    public void payFail(String failString) {
        ShowUtil.showToast(context, failString);
    }

    public void onClickZan(String money, String orderno) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("noteid", noteId);
        map.put("repostid", repostid);
        map.put("commentid", commid);
        map.put("orderno", orderno);
        NetworkRequest.postRequest(RequestPath.POST_ZAN, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                ShowUtil.showToast(context, "点赞成功");
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
                //获取帖子详情数据
                mNoteRequestBase.getNoteInfo(noteId, repostid, NoteDetailActivity.this);
            }

            @Override
            public void onFailure(String content) {
                ShowUtil.showToast(context, content);
            }
        });
    }

    /**
     * 点赞支付  money ==0.02 但是这值其实是后台配置的 但不能传0
     */
    private void zanPay() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("paytype", Constants.zan);
        hashMap.put("spendtype", Constants.suiyinpay);
        hashMap.put("money", "0.02");//打赏不需要传金额 所以为null
        PopuUtil.initPayfom(this, hashMap, this);
//
//        hashMap.put("paytype", Constants.zan);
//        hashMap.put("spendtype", Constants.suiyinpay);
//        hashMap.put("money", "0.02");//
//        PaymentBase1 paymentBase1 = new PaymentBase1(this, this, hashMap);
//        paymentBase1.initOrderInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String url = "";
            switch (requestCode) {
                case 1://转发  转发后生成了一条评论 所以要刷新品论信息
                    //获取帖子详情数据
                    sharefree = "1";
                    mNoteRequestBase.getNoteInfo(noteId, repostid, this);
                    //获取帖子评论列表数据
                    mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, this);
                    if (head_view != null && head_view.iv_share_note != null) {
                        head_view.iv_share_note.performClick();
                    }
                    break;
                case 2://评论
                    //获取帖子评论列表数据
                    mNoteRequestBase.getNoteInfoRepostlist(RequestPath.GET_NOTECOMMLIST, noteId, repostid, pagerIndex, this);
                    //获取帖子详情数据
                    mNoteRequestBase.getNoteInfo(noteId, repostid, NoteDetailActivity.this);
                    break;
                default:
                    return;
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (note_audio != null)
            note_audio.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (note_audio != null) {
            note_audio.stopMusic();
            note_audio = null;
        }
        if (note_video != null)
            note_video = null;
    }
}
