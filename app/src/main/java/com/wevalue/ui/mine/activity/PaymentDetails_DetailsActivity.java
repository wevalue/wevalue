package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.FriendsNoteDetailsActivity;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.details.activity.RepostNoteDetailActivity;
import com.wevalue.ui.world.activity.ImgShowActivity;
import com.wevalue.ui.world.adapter.WorldListGridViewAdapter;
import com.wevalue.utils.Constants;
import com.wevalue.utils.DateTiemUtils;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.view.NoScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PaymentDetails_DetailsActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {
    private ImageView iv_back;
    private TextView tv_head_title;
    private String orderno;
    private String money;
    private String title;
    private String type;//收支类型
    private String ordertype;//订单类型


    private NoteBean.NoteEntity noteInfo;
    private NoteBean.UserBean userInfo;
    private String outstate;//体现状态
    private TextView tv_withdraw_succ;
    private String notetype;//帖子类型
    private ImageView iv_user_img;
    private ImageView iv_play;
    private TextView tv_nickname;
    private TextView tv_dengji;
    private TextView tv_jianjie;
    private TextView tv_title;
    private TextView tv_paynum;
    private LinearLayout ll_withdraw;
    private TextView tv_content_content;
    private TextView tv_img_content;
    ImageView iv_video_img;//视频 图片
    ImageView iv_audio_img;//音频 图片
    private NoScrollGridView nsgv_world_list_gridview;
    private LinearLayout ll_imgAndAudioAndVideo_ui;
    View in_audio_video_ui;//视频音频的图片区域;
    private WorldListGridViewAdapter mGirdViewAdapter;
    private RelativeLayout rl_note_content;
    private String addtime;//时间
    private ImageView iv_transmit_userface;
    private TextView tv_transmit_username;
    private TextView tv_transmit_content;
    private LinearLayout ll_transmit_info;
    private TextView degree1;
    private TextView degree2;
    private TextView degree3;
    private TextView tv_tixianzhong;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouzhi_details);
        orderno = getIntent().getStringExtra("orderno");
        ordertype = getIntent().getStringExtra("ordertype");
        money = getIntent().getStringExtra("money");
        title = getIntent().getStringExtra("title");
        addtime = getIntent().getStringExtra("addtime");
        type = getIntent().getStringExtra("type");
        ordertype = getIntent().getStringExtra("ordertype");
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText(title);
        iv_transmit_userface = (ImageView) findViewById(R.id.iv_transmit_userface);

        tv_withdraw_succ = (TextView) findViewById(R.id.tv_withdraw_succ);
        iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_dengji = (TextView) findViewById(R.id.tv_dengji);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_jianjie.setText(DateTiemUtils.editTime(addtime));
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_paynum = (TextView) findViewById(R.id.tv_paynum);
        ll_withdraw = (LinearLayout) findViewById(R.id.ll_withdraw);
        tv_content_content = (TextView) findViewById(R.id.tv_content_content);
        tv_img_content = (TextView) findViewById(R.id.tv_img_content);
        iv_video_img = (ImageView) findViewById(R.id.iv_video_img);
        iv_audio_img = (ImageView) findViewById(R.id.iv_audio_img);
        nsgv_world_list_gridview = (NoScrollGridView) findViewById(R.id.nsgv_world_list_gridview);
        ll_imgAndAudioAndVideo_ui = (LinearLayout) findViewById(R.id.ll_imgAndAudioAndVideo_ui);
        in_audio_video_ui = findViewById(R.id.in_audio_video_ui);
        rl_note_content = (RelativeLayout) findViewById(R.id.rl_note_content);
        rl_note_content.setOnClickListener(this);
        tv_transmit_username = (TextView) findViewById(R.id.tv_transmit_username);
        tv_transmit_username.setOnClickListener(this);
        tv_transmit_content = (TextView) findViewById(R.id.tv_transmit_content);
        tv_transmit_content.setOnClickListener(this);
        ll_transmit_info = (LinearLayout) findViewById(R.id.ll_transmit_info);
        ll_transmit_info.setOnClickListener(this);
        tv_title.setText(title);
//        tv_jianjie.setText(addtime);
        if (type.equals("0")) {
            tv_paynum.setText("- ¥" + money);
        } else {
            tv_paynum.setText("+ ¥" + money);
        }
        degree1 = (TextView) findViewById(R.id.degree1);
        degree2 = (TextView) findViewById(R.id.degree2);
        degree3 = (TextView) findViewById(R.id.degree3);

        tv_tixianzhong = (TextView) findViewById(R.id.tv_tixianzhong);
        distinguishPayType();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_note_content:

                if(noteInfo.getIsfree().equals("1")){
                    Intent intent = new Intent(this, FriendsNoteDetailsActivity.class);
                    intent.putExtra("noteId", noteInfo.getNoteid());
                    intent.putExtra("repostid", noteInfo.getRepostid());
                    startActivity(intent);
                }else {

                    Intent intent = new Intent(this, NoteDetailsActivity.class);
                    intent.putExtra("noteId", noteInfo.getNoteid());
                    intent.putExtra("repostid", noteInfo.getRepostid());
                    startActivity(intent);
                }
                break;
            case R.id.ll_transmit_info:
                Intent intent1 = new Intent(this, RepostNoteDetailActivity.class);
                intent1.putExtra("noteId", noteInfo.getNoteid());
                intent1.putExtra("repostid", noteInfo.getRepostid());
                startActivity(intent1);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /*获取订单类型   处理不同信息 */
    private void distinguishPayType() {
        if(TextUtils.isEmpty(ordertype)){
            return;
        }
        switch (ordertype) {
            case Constants.buypermission:
            case Constants.charge:
                initChargeAndBuyPermissonInfo();
                break;
            case Constants.transmit:
            case Constants.share:
            case Constants.release:
            case Constants.dasahng:
            case Constants.withdraw:
                queryPaymentDetail();
                break;
        }
    }

    /*查询支付订单信息*/
    private void queryPaymentDetail() {
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("orderno", orderno);
        map.put("ordertype", ordertype);
        NetworkRequest.postRequest(RequestPath.POST_PENDISTINFO, map, this);
    }


    private void imgViewSetData(String url, ImageView iv) {
        Glide.with(this)
                .load(RequestPath.SERVER_PATH + url)
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .crossFade()
                .into(iv);
        view = iv;
    }

    /*提现申请调用的方法*/
    private void initWithdrawInfo(String content) {
        Drawable drawable = getResources().getDrawable(R.mipmap.me_withdrawdegree);
        Drawable drawable_2 = getResources().getDrawable(R.mipmap.icon_tixian_shibai);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_dengji.setText(SharedPreferencesUtil.getUserleve(this));

        ll_withdraw.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(content);
            //  "outstate": "2"0支出 1收入 2申请提现 3同意申请未提现 4提现完成  5关闭订单
            outstate = jsonObject.getString("outstate");
            LogUtils.e("outstate", outstate);
            if (outstate.equals("3")) {
                degree2.setBackgroundResource(R.color.blue);
                tv_tixianzhong.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_tixianzhong.setCompoundDrawables(null, drawable, null, null);
            } else if (outstate.equals("4")) {
                degree2.setBackgroundResource(R.color.blue);
                degree3.setBackgroundResource(R.color.blue);
                tv_tixianzhong.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_withdraw_succ.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_tixianzhong.setCompoundDrawables(null, drawable, null, null);
                tv_withdraw_succ.setCompoundDrawables(null, drawable, null, null);
            } else if (outstate.equals("5")) {
                degree2.setBackgroundResource(R.color.blue);
                degree3.setBackgroundResource(R.color.blue);
                tv_tixianzhong.setTextColor(getResources().getColor(R.color.login_text_blue));
                tv_withdraw_succ.setTextColor(getResources().getColor(R.color.orange));
                tv_tixianzhong.setCompoundDrawables(null, drawable, null, null);
                tv_withdraw_succ.setCompoundDrawables(null, drawable_2, null, null);
                tv_withdraw_succ.setText("订单关闭！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("outstate", "解析异常！");
        }
    }

    /*充值&购买权限 申请调用的方法*/
    private void initChargeAndBuyPermissonInfo() {
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_dengji.setText(SharedPreferencesUtil.getUserleve(this));
        imgViewSetData(SharedPreferencesUtil.getAvatar(this), iv_user_img);
    }

    /*打赏和发布的帖子支出详情*/
    private void initReleaseAndDaShangNoteInfo(String content) {
        Gson gson = new Gson();
        NoteBean paymodel = gson.fromJson(content, NoteBean.class);
        if (paymodel.getResult().equals("1") && null != paymodel.getData()) {
            rl_note_content.setVisibility(View.VISIBLE);
            noteInfo = paymodel.getData().get(0);
            notetype = noteInfo.getNotetype();
            if (type.equals("0")) {
                //支出信息
                tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
                tv_dengji.setText(SharedPreferencesUtil.getUserleve(this));
                imgViewSetData(SharedPreferencesUtil.getAvatar(this), iv_user_img);
            } else if (type.equals("1")) {
                //收入信息
                if (ordertype.equals(Constants.release)) {
                    //发布的收益  没有userinfo
                    tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
                    tv_dengji.setText(SharedPreferencesUtil.getUserleve(this));
                    imgViewSetData(SharedPreferencesUtil.getAvatar(this), iv_user_img);
                } else {
                    userInfo = paymodel.getUserinfo().get(0);
                    tv_nickname.setText(noteInfo.getUsernickname());
                    tv_dengji.setText(noteInfo.getUserlevel());
                    imgViewSetData(noteInfo.getUserface(), iv_user_img);
                }
            }
            tv_img_content.setText(noteInfo.getContent());
            switch (notetype) {
                case "4"://文字
                    if (noteInfo.getList() != null && noteInfo.getList().size() > 0) {
                        tv_content_content.setVisibility(View.GONE);
                        in_audio_video_ui.setVisibility(View.GONE);
                        ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                        nsgv_world_list_gridview.setVisibility(View.VISIBLE);
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteInfo.getList(), this);
                        mGirdViewAdapter.notifyDataSetChanged();
                        nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    } else {
                        tv_content_content.setVisibility(View.VISIBLE);
                        tv_content_content.setText(noteInfo.getContent());
                        ll_imgAndAudioAndVideo_ui.setVisibility(View.GONE);
                    }
                    break;
                case "1"://视频文
                    tv_content_content.setVisibility(View.GONE);
                    in_audio_video_ui.setVisibility(View.VISIBLE);
                    ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                    iv_play.setVisibility(View.VISIBLE);
                    iv_video_img.setVisibility(View.VISIBLE);
                    nsgv_world_list_gridview.setVisibility(View.GONE);
                    imgViewSetData(noteInfo.getNotevideopic(), iv_video_img);
                    iv_play.setImageResource(R.mipmap.note_play);
                    break;
                case "2"://音频文
                    tv_content_content.setVisibility(View.GONE);
                    in_audio_video_ui.setVisibility(View.VISIBLE);
                    ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                    nsgv_world_list_gridview.setVisibility(View.GONE);
                    iv_audio_img.setImageResource(R.mipmap.ic_music);
                    break;
                case "3"://图文
                    tv_content_content.setVisibility(View.GONE);
                    in_audio_video_ui.setVisibility(View.GONE);
                    ll_imgAndAudioAndVideo_ui.setVisibility(View.VISIBLE);
                    nsgv_world_list_gridview.setVisibility(View.VISIBLE);

                    if (noteInfo.getList() != null && noteInfo.getList().size() > 0) {
                        int l = noteInfo.getList().size();
                        mGirdViewAdapter = new WorldListGridViewAdapter(noteInfo.getList(), this);
                        mGirdViewAdapter.notifyDataSetChanged();
                        nsgv_world_list_gridview.setAdapter(mGirdViewAdapter);
                    }
                    break;
            }
            nsgv_world_list_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                    String[] url = new String[noteInfo.getList().size()];
                    for (int i = 0; i < noteInfo.getList().size(); i++) {
                        url[i] = noteInfo.getList().get(i).getUrl();
                    }
                    Intent intent = new Intent(PaymentDetails_DetailsActivity.this, ImgShowActivity.class);
                    intent.putExtra("index", index);
                    intent.putExtra("imgUrl", url);
                    startActivity(intent);
                }
            });
        }
    }

    /*分享和转发帖子的支出详情*/
    private void initTransmitAndShareNoteInfo(String content) {
        Gson gson = new Gson();
        NoteBean noteBean = gson.fromJson(content, NoteBean.class);
        if (noteBean.getResult().equals("1")) {
            ll_transmit_info.setVisibility(View.VISIBLE);
            if (noteBean.getData()!=null&&noteBean.getData().size()>0) {
                noteInfo = noteBean.getData().get(0);
                userInfo = noteBean.getUserinfo().get(0);
                tv_nickname.setText(noteInfo.getUsernickname());
                tv_dengji.setText(noteInfo.getUserlevel());
                imgViewSetData(noteInfo.getUserface(), iv_user_img);
                String s = noteInfo.getOlduserface();
                LogUtils.e("jpg", s);
                imgViewSetData(noteInfo.getOlduserface(), iv_transmit_userface);
                tv_transmit_username.setText(noteInfo.getOldusernickname());
                tv_transmit_content.setText(noteInfo.getRepostcontent() + "//" + noteInfo.getContent());
            }
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (ordertype) {
            case Constants.withdraw:
                initWithdrawInfo(content);
                break;
            case Constants.release:
            case Constants.dasahng:
                initReleaseAndDaShangNoteInfo(content);
                break;
            case Constants.transmit:
            case Constants.share:
                initTransmitAndShareNoteInfo(content);
                break;
        }
    }

    @Override
    public void onFailure(String content) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (view != null) {
            Glide.clear(view);
        }
    }
}
