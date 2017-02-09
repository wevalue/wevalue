package com.wevalue.ui.release;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.ChannelBean;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.payment.PaymentBase;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.activity.PicChoiceActivity;
import com.wevalue.ui.world.activity.Play_videoActivity;
import com.wevalue.ui.world.adapter.NoScrollGridViewAdapter;
import com.wevalue.utils.ButtontimeUtil;
import com.wevalue.utils.Constants;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PicCompressUtil.Luban;
import com.wevalue.utils.PicCompressUtil.OnCompressListener;
import com.wevalue.utils.PopuUtil;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.view.NoScrollGridView;
import com.wevalue.youmeng.StatisticsConsts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
*
 * Created by Administrator on 2016-06-30.
 * 发布信息的活动窗体
 * */


public class ReleaseNoteActivity extends BaseActivity implements View.OnClickListener, PayInterface {
    private TextView tv_send_note;
    private TextView tv_back;
    private TextView tv_nickname;
    private TextView tv_choice_tilte; //选择领域，标签
    private EditText et_edit_reci; //热词
    private TextView tv_is_shoufei;//是否收费
    private TextView tv_is_yuanchuang; //是否原创

    private EditText et_title;  //发布标题
    private EditText et_content;  //发布内容
   // private TextView tv_is_type; // 发布世界 朋友圈
   // private TextView tv_qingxu;

    private LinearLayout layout_free;
    private ImageView iv_video_img;
    private ImageView iv_paly;

    private RelativeLayout rl_videoAndAudio;
    private RelativeLayout ll_isTongyi;
    private NoScrollGridView nsgv_send_note_gridview;
    private NoScrollGridViewAdapter mAdapter;
    private List<Bitmap> mImgList;
    //是否同意分享
    private ImageView rgb_isShare;
    //发布的是否是免费信息
    private boolean isFree; // 免费 isFree = true  收费  isFree = false

    private int isClickType = 0; //1 = 图片 , 2 = 音频; 3 = 视频;
    private static final String PHOTO_FILE_NAME = "wevalue_img.jpg";
    private Long suijishu;
    private Bitmap bitmap;
    private int width;
    private int height;

    private ArrayList<String> mTitleDatas;//標題
    private ArrayList<String> mID; //帖子标签列表
//    private ArrayList<String> mYCDatas;//是否原創
//    private ArrayList<String> mSFDatas;// 是否收費
//    private ArrayList<String> mFWDatas;//範圍
    private ArrayList<String> mQXDatas;//情緒

    private int isClickWho;
    private String fileUrl;

    private OptionsPickerView pvOptions;
    private List<ChannelBean.Channel> allChannelList;
    private int isSendType;
    //发布时用户选择的内容标签
    private String noteClass = "1";

    private MediaPlayer mMediaPlayer;//媒体播放器
    private String money;// 帖子价格
    private String orderno;//订单编号
    private String spendtype;//支付类型
    private ProgressDialog mProgressDialog;
    private String filenum = "0";
    List<Integer> integerList;
    private String releaseType = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);
//        initReceiver();
        /**获取频幕宽高*/
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        initView();
        initData();
        initHualunData();
        LogUtils.e(ShowUtil.getYear());
        LogUtils.e(ShowUtil.getYear());
        integerList = new ArrayList<>();


    }


    /**
     * 初始化控件
     */
    private void initView() {
        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_choice_tilte = (TextView) findViewById(R.id.tv_choice_tilte);
        tv_is_yuanchuang = (TextView) findViewById(R.id.tv_is_yuanchuang);
        tv_is_yuanchuang.setTag(false);
        tv_is_shoufei = (TextView) findViewById(R.id.tv_is_shoufei);
        tv_is_shoufei.setTag(false);
        layout_free = (LinearLayout) findViewById(R.id.layout_free);
        rgb_isShare = (ImageView) findViewById(R.id.rgb_isShare);
        rgb_isShare.setTag(true);
        et_edit_reci = (EditText) findViewById(R.id.et_edit_reci);
        et_edit_reci.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = et_edit_reci.getText().toString();
                if (s.contains("\'")) {
                    et_edit_reci.setText(s.replace("\'", ""));
                }
                if (s.contains("\"")) {
                    et_edit_reci.setText(s.replace("\"", ""));
                }
                if (s.contains("\\")) {
                    et_edit_reci.setText(s.replace("\\", ""));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_video_img = (ImageView) findViewById(R.id.iv_video_img);
        iv_paly = (ImageView) findViewById(R.id.iv_paly);
        rl_videoAndAudio = (RelativeLayout) findViewById(R.id.rl_videoAndAudio);
        ll_isTongyi = (RelativeLayout) findViewById(R.id.ll_isTongyi);
        nsgv_send_note_gridview = (NoScrollGridView) findViewById(R.id.nsgv_send_note_gridview);
        rgb_isShare.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_send_note.setOnClickListener(this);
        tv_choice_tilte.setOnClickListener(this);
        tv_is_yuanchuang.setOnClickListener(this);
        tv_is_shoufei.setOnClickListener(this);
//        tv_is_type.setOnClickListener(this);
        //tv_qingxu.setOnClickListener(this);
        iv_paly.setOnClickListener(this);
        nsgv_send_note_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == MainActivity.mSelectedImage.size()) {
                    addImg("手机拍照", "相册选择");
                }
            }
        });
        isSendType = getIntent().getIntExtra("isSendType", -1);
        LogUtils.e("isSendType = " + isSendType);

        switch (isSendType) {
            case 1://视频
                fileUrl = getIntent().getStringExtra("fileUrl");
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(fileUrl);
                Bitmap video_img_bitmap = media.getFrameAtTime();
                iv_video_img.setImageBitmap(video_img_bitmap);
                et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
                et_content.setHint("说点什么…（300字以内）");
                et_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 300) {
                            ShowUtil.showToast(ReleaseNoteActivity.this, "300字以内");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                LogUtils.e("fileUrl = " + fileUrl);
                rl_videoAndAudio.setVisibility(View.VISIBLE);
                break;
            case 2://音频
                fileUrl = getIntent().getStringExtra("fileUrl");
                rl_videoAndAudio.setVisibility(View.VISIBLE);
                et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
                et_content.setHint("说点什么…（300字以内）");
                et_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 300) {
                            ShowUtil.showToast(ReleaseNoteActivity.this, "300字以内");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                mMediaPlayer = new MediaPlayer();
                iv_video_img.setImageResource(R.mipmap.default_head);
                break;
            case 3://图片
                et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(300)});
                et_content.setHint("说点什么…（300字以内）");
                et_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 300) {
                            ShowUtil.showToast(ReleaseNoteActivity.this, "300字以内");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                initGridViewData();
                break;
            case 4://文字

                et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2000)});
                et_content.setHint("说点什么…（2000字以内）");
                et_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() == 2000) {
                            ShowUtil.showToast(ReleaseNoteActivity.this, "2000字以内");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                initGridViewData();
                //如果发表文字 则隐藏加号  加号在nsgv_send_note_gridview里面写着
                nsgv_send_note_gridview.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSendType == 3 || isSendType == 4) {
            initGridViewData();
        }
        switch (isSendType) {
            case 1:
                releaseType = "视频";
                break;
            case 2:
                releaseType = "音频";
                break;
            case 3:
                releaseType = "图片";
                break;
            case 4:
                releaseType = "文字";
                break;
        }
        MobclickAgent.onEvent(this, StatisticsConsts.event_releaseType, releaseType);

    }

    /**
     * 初始化gridview数据
     */
    private void initGridViewData() {
        if (MainActivity.mSelectedImage == null) {
            MainActivity.mSelectedImage = new LinkedList<>();
        }
        if (mImgList != null && mAdapter != null) {
            mAdapter.setmList(MainActivity.mSelectedImage);
            mAdapter.notifyDataSetChanged();
        } else {
            mImgList = new ArrayList<>();
            mAdapter = new NoScrollGridViewAdapter(ReleaseNoteActivity.this);
            mAdapter.setmList(MainActivity.mSelectedImage);
            mAdapter.notifyDataSetChanged();
            nsgv_send_note_gridview.setAdapter(mAdapter);
            nsgv_send_note_gridview.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 初始化 车轮控件
     */
    private void initHuaLunPicker(ArrayList<String> list) {
        //选项选择器
        pvOptions = new OptionsPickerView(this);
        pvOptions.setPicker(list);
        pvOptions.setLabels("", "");
        pvOptions.setCyclic(false, false, false);
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3) {
                switch (isClickWho) {
                    case 1:
                        noteClass = mID.get(options1);
                        tv_choice_tilte.setText(mTitleDatas.get(options1));
                        break;
                    case 2:
//                        tv_is_yuanchuang.setText(mYCDatas.get(options1));
                        break;
                    case 3:
//                        tv_is_shoufei.setText(mSFDatas.get(options1));
//                        if (mSFDatas.get(options1).equals("付费")) {
//                            isFree = false;
//                        } else {
//                            isFree = true;
//                        }
//                        tv_is_shoufei.setTag(isFree);
                        break;
                    case 4:
                        //tv_is_type.setText(mFWDatas.get(options1));
                        break;
                    case 5:
                        //tv_qingxu.setText(mQXDatas.get(options1));
                        break;
                }
            }
        });
    }

    /**
     * 初始化数据
     **/
    public void initHualunData() {

//        mYCDatas = new ArrayList<>();
//        mYCDatas.add("原创");
//        mYCDatas.add("非原创");
//
//        mSFDatas = new ArrayList<>();
//        mSFDatas.add("付费");
//        mSFDatas.add("免费");
//
//        mFWDatas = new ArrayList<>();
//        mFWDatas.add("世界");
//        mFWDatas.add("朋友们");

        mQXDatas = new ArrayList<>();
        mQXDatas.add("喜爱");
        mQXDatas.add("感动");
        mQXDatas.add("同情");
        mQXDatas.add("愤怒");
        mQXDatas.add("恐惧");
        mQXDatas.add("炫酷");
        mQXDatas.add("搞笑");
        mQXDatas.add("震撼");
    }

    /**
     * 删除选中的图片
     */
    public void itemDeleteClick(int index) {
        if (MainActivity.mSelectedImage.size() > index) {
            MainActivity.mSelectedImage.remove(index);
        }
        if (mImgList.size() > index) {
            mImgList.remove(index);
            mAdapter.setmList(MainActivity.mSelectedImage);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rgb_isShare:
                boolean tag = (boolean) rgb_isShare.getTag();
                if (tag) {
                    rgb_isShare.setTag(false);
                    rgb_isShare.setImageResource(R.mipmap.icon_fabu_weigouxuan);
                    ShowUtil.showToast(this, "尊，分享至其他平台也可以扩大您的影响力！");
                } else {
                    rgb_isShare.setTag(true);
                    rgb_isShare.setImageResource(R.mipmap.iconfont_duihao);
                }
                break;
            case R.id.tv_back:
                MainActivity.mSelectedImage.clear();
                finish();
                break;
            case R.id.tv_send_note:
                tv_send_note.setEnabled(false);
                if (TextUtils.isEmpty(et_title.getText().toString().trim()) && TextUtils.isEmpty(fileUrl) && MainActivity.mSelectedImage.size() == 0) {
                    ShowUtil.showToast(ReleaseNoteActivity.this, "发布标题不能为空");
                    tv_send_note.setEnabled(true);
                    return;
                }
                //如果发布的是纯文字 则必须写内容
                if (isSendType==4)
                if (TextUtils.isEmpty(et_content.getText().toString().trim()) && TextUtils.isEmpty(fileUrl) && MainActivity.mSelectedImage.size() == 0) {
                    ShowUtil.showToast(ReleaseNoteActivity.this, "发布内容不能为空");
                    tv_send_note.setEnabled(true);
                    return;
                }
                if (!isFree) {
                    if (TextUtils.isEmpty(tv_choice_tilte.getText().toString().trim())) {
                        ShowUtil.showToast(ReleaseNoteActivity.this, "请选择标签");
                        tv_send_note.setEnabled(true);
                        return;
                    }
                }
                if (!ButtontimeUtil.isFastDoubleClick()) {
                    if (isFree) {
                        //发布免费信息 获取0元的预订单
                        HashMap map = new HashMap();
                        map.put("money", "0");
                        map.put("paytype", Constants.release);
                        map.put("spendtype", Constants.suiyinpay);
                        PaymentBase paymentBase = new PaymentBase(this, this, map);
                        paymentBase.initOrderInfo();
                    } else {
                        HashMap map = new HashMap();
                        map.put("paytype", Constants.release);
                        PopuUtil.initPayPopu(this, this, map);
                    }
                }
                break;
            case R.id.tv_choice_tilte://标签
                hideInput();
                isClickWho = 1;
                initHuaLunPicker(mTitleDatas);
                pvOptions.show();
                break;
            case R.id.tv_is_yuanchuang://原创
              boolean isYuan = (boolean) tv_is_yuanchuang.getTag();
                if (isYuan){
                    tv_is_yuanchuang.setBackgroundResource(R.mipmap.original_1);
                }else {
                    tv_is_yuanchuang.setBackgroundResource(R.mipmap.original_2);
                }
                tv_is_yuanchuang.setTag(!isYuan);

                break;
            case R.id.tv_is_shoufei://收费
                //isFree  false 收费发布世界  true 免费发朋友圈
                isFree = (boolean) tv_is_shoufei.getTag();
                if (!isFree) {

                    isFree = true;
                    tv_is_shoufei.setBackgroundResource(R.mipmap.release_type_2);
                    tv_is_shoufei.setTag(isFree);

                    tv_is_yuanchuang.setEnabled(false);
                    tv_choice_tilte.setEnabled(false);
                    layout_free.setVisibility(View.INVISIBLE);
                    ll_isTongyi.setVisibility(View.INVISIBLE);
                } else {
                    isFree = false;
                    tv_is_shoufei.setTag(isFree);
                    tv_is_shoufei.setBackgroundResource(R.mipmap.release_type_1);
                    tv_is_yuanchuang.setEnabled(true);
                    tv_choice_tilte.setEnabled(true);
                    layout_free.setVisibility(View.VISIBLE);
                    ll_isTongyi.setVisibility(View.VISIBLE);
                }

                break;
//            case R.id.tv_is_type://范围
//                isClickWho = 4;
//                initHuaLunPicker(mFWDatas);
//                pvOptions.show();
//                break;
//            case R.id.tv_qingxu://情緒
//                hideInput();
//                isClickWho = 5;
//                initHuaLunPicker(mQXDatas);
//                pvOptions.show();
//                break;
            case R.id.iv_paly://播放
                if (isSendType == 2) {
                    try {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        } else {
                            play(fileUrl);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (isSendType == 1) {
                    Intent in = new Intent(this, Play_videoActivity.class);
                    in.putExtra("url", fileUrl);
                    startActivity(in);
                }
                break;
        }
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return size;
    }

    /**
     * 初始化播放器
     */
    private void play(String fileurl) throws IOException {
        LogUtils.e("fileurl = " + fileurl);
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(fileurl);
        mMediaPlayer.prepare();
        mMediaPlayer.start();//播放
    }

    List<File> imgFileLst;
    File videoAndAudioFile;

    /**
     * 准备 上传的文件
     */
    private void getFileData() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在发布...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && mProgressDialog.isShowing()) {
                    ShowUtil.showToast(ReleaseNoteActivity.this, "信息正在发布，请稍等哒~");
                    return true;
                }
                return false;
            }
        });
        if (!ReleaseNoteActivity.this.isFinishing())
        mProgressDialog.show();
        imgFileLst = new ArrayList<>();
        LogUtils.e("imgFileLst123", "开始准备文件" + String.valueOf(isSendType));
        switch (isSendType) {
            case 1:
                videoAndAudioFile = new File(fileUrl);
                sendNote(videoAndAudioFile, imgFileLst);
                break;
            case 2:
                videoAndAudioFile = new File(fileUrl);
                sendNote(videoAndAudioFile, imgFileLst);
                break;
            case 3:
                reduceBitmap();
                break;
            case 4:
                reduceBitmap();
                break;
        }

    }

    /**
     * 发布帖子
     */
    private void sendNote(File videoFile, List<File> imgFileLst) {
        LogUtils.e("imgFileLst123", "开始发送");
        String url = RequestPath.POST_RELEASE;
        String content = et_content.getText().toString().trim();
        String title = et_title.getText().toString().trim();
        Map<String, Object> map = new HashMap<>();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("notetype", isSendType + "");
        map.put("noteclass", noteClass);

        if (TextUtils.isEmpty(content)) {
            switch (isSendType) {
                case 1://视频
                    content = "视频";
                    break;
                case 2://音频
                    content = "音乐";
                    break;
                case 3://图片
                    content = "图片";
                    break;
                case 4://图片
                    content = "图片";
                    break;
            }
        }
        map.put("title", content);
        map.put("content", content);
        map.put("orderno", orderno);
        map.put("spendtype", spendtype);
        map.put("cityname", SharedPreferencesUtil.getLocationCity(this));

        boolean isYuan = (boolean) tv_is_yuanchuang.getTag();
        if (isYuan) {
            map.put("isself", "0");
        } else {
            map.put("isself", "1");
        }
        //是否收费
        if (!isFree) {
            map.put("isfree", "0"); //付费
            map.put("notezone", "0");
        } else {
            map.put("isfree", "1");//免费
            map.put("notezone", "1");
        }
        map.put("hotword", et_edit_reci.getText().toString().trim());
       // map.put("notemood", tv_qingxu.getText().toString().trim()); //去掉情绪
        String isShare = (boolean)rgb_isShare.getTag()? "0" : "1";
        map.put("isshare", isShare);
        map.put("paynum", money);
        if (videoFile != null) {
            map.put("notefile", videoFile);
            filenum = "1";
        }
        if (imgFileLst != null && imgFileLst.size() > 0) {
            for (int i = 0; i < imgFileLst.size(); i++) {
                LogUtils.e("notefile", "cuizai ");
                map.put("noteimg" + (i + 1), imgFileLst.get(i));
            }
            filenum = String.valueOf(imgFileLst.size());
        }
        LogUtils.e("imgFileLst123", filenum);
        map.put("filenum", filenum);
        NetworkRequest.postRequest(url, map, new WZHttpListener() {
            @Override
            public void onSuccess(String content, String isUrl) {
                LogUtils.e("发布帖子接口content = " + content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getString("result").equals("1")) {
                        MobclickAgent.onEvent(ReleaseNoteActivity.this, StatisticsConsts.event_releaseTypeDone, releaseType);
                        String uplevel = obj.getString("uplevel");
                        mProgressDialog.dismiss();
                        ShowUtil.showToast(ReleaseNoteActivity.this, obj.getString("message"));
                        if (!TextUtils.isEmpty(uplevel)) {
                            ShowUtil.showToast(ReleaseNoteActivity.this, uplevel);
                        }
                        new PaymentBase(ReleaseNoteActivity.this).getsuiyin();
                        finish();
                        MainActivity.mSelectedImage.clear();
                    } else {
                        mProgressDialog.dismiss();
                        ShowUtil.showToast(ReleaseNoteActivity.this, obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String content) {
                mProgressDialog.dismiss();
                ShowUtil.showToast(ReleaseNoteActivity.this, content);
                LogUtils.e("SHOWTOAST", content);
            }
        });
    }

    /**
     * 初始化标题数据
     */

    private void initData() {
        Gson gson = new Gson();
        ChannelBean channelBean = gson.fromJson(SharedPreferencesUtil.getAllChannel(this), ChannelBean.class);
        mTitleDatas = new ArrayList<>();
        mID = new ArrayList<>();
        String s = "";
        if (channelBean.getResult().equals("1")) {
            allChannelList = channelBean.getData();
            for (int i = 0; i < allChannelList.size(); i++) {
                s += allChannelList.get(i).getTypename();
                if (allChannelList.get(i).getTypename().equals("推荐")) {
                    allChannelList.remove(i);
                } else if (allChannelList.get(i).getTypename().equals("视频")) {
                    allChannelList.remove(i);
                } else {
                    if (!allChannelList.get(i).getId().equals("3")) {
                        mTitleDatas.add(allChannelList.get(i).getTypename());
                        mID.add(allChannelList.get(i).getId());
                    }
                }
            }
            LogUtils.e(s);
        }
    }

    /**
     * 选择拍照或相册
     */
    private void addImg(String str, String str2) {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(str, ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                int code = ContextCompat.checkSelfPermission(ReleaseNoteActivity.this,
                                        Manifest.permission.CAMERA);
                                if (code != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ReleaseNoteActivity.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            100);
                                } else {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    suijishu = System.currentTimeMillis();
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", suijishu + PHOTO_FILE_NAME)));
                                    startActivityForResult(intent, 1);
                                }


                            }
                        }).addSheetItem(str2, ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(ReleaseNoteActivity.this, PicChoiceActivity.class);
                intent.putExtra("sendAct", 2);
                intent.putExtra("choiceLimit", 9);

                startActivity(intent);
            }
        }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  1 = 拍照, 2 = 选择照片, 3 = 录音 ,4= 本地音频, 5 =视频录制, 6 = 本地视频
        LogUtils.e("------1111---------" + requestCode);
        if (resultCode == RESULT_OK) {
            LogUtils.e("---------------" + requestCode);
            switch (requestCode) {
                case 1:
                    File tempFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/", suijishu + PHOTO_FILE_NAME);
                    LogUtils.e("123321", tempFile.getAbsolutePath());
                    MainActivity.mSelectedImage.add(tempFile.getAbsolutePath());
                    try {
                        mAdapter.setmList(MainActivity.mSelectedImage);
                        mAdapter.notifyDataSetChanged();
                        LogUtils.e("123321", bitmap.getByteCount() + "size");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 同意
                    LogUtils.e("-----onRequestPermissionsResult  // 同意-----  ");

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    suijishu = System.currentTimeMillis();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", suijishu + PHOTO_FILE_NAME)));
                    startActivityForResult(intent, 1);

                } else {
                    //拒绝
                    LogUtils.e(" ----onRequestPermissionsResult // //拒绝-----  ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseNoteActivity.this);
                    builder.setTitle("帮助");
                    builder.setMessage("当前应用缺少必要权限。" +
                            "请点击“设置”-“权限”-打开所需权限。" +
                            "最后点击两次后退按钮，即可返回。");

                    // 拒绝, 退出应用
                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//							setResult(10102);
//							finish();
                        }
                    });

                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    });

                    builder.show();

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * Title:  saveMyBitmap<br>
     * Description: TODO  保存图片<br>
     *
     * @param bitName
     * @param mBitmap
     * @since JDK 1.7
     */
    public File saveMyBitmap(String bitName, Bitmap mBitmap) {

        File f = new File(bitName);
        File path = new File(getSDPath() + "/微值/");
        if (!path.exists()) {
            path.mkdirs();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    /**
     * Title: getSDPath<br>
     * Description: TODO 获取SD卡路径<br>
     *
     * @return
     * @since JDK 1.7
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        LogUtils.e("sddir = " + sdDir.toString());
        return sdDir.toString();
    }

    /**
     * Title: hasSdcard<br>
     * Description: TODO 判断SD卡是否可用<br>
     *
     * @since JDK 1.7
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.mSelectedImage.clear();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isSendType == 2) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.seekTo(0);
        }
    }

    @Override
    protected void onDestroy() {
        if (isSendType == 2) {
            mMediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void paySucceed(HashMap map) {
        money = (String) map.get("money");
        spendtype = (String) map.get("spendtype");
        orderno = (String) map.get("orderno");
        getFileData();
    }

    @Override
    public void payStart(String startType) {

    }

    @Override
    public void payFail(String failString) {

    }

    /**
     * Title: hideInput<br>
     * Description: TODO 隐藏键盘输入法<br>
     *
     * @since JDK 1.7
     */
    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    int position_2 = 0;

    protected void reduceBitmap() {
        if (MainActivity.mSelectedImage.size() == 0) {
            sendNote(null, null);
            return;
        }
        File file = new File(MainActivity.mSelectedImage.get(position_2));
        LogUtils.e("filesize", String.valueOf(file.length()));
        if (file.length() / 1024 / 1024 < 1) {
            //图片小于1m  则直接上传图片
            Bitmap b = BitmapFactory.decodeFile(MainActivity.mSelectedImage.get(position_2));
            imgFileLst.add(saveMyBitmap(getSDPath() + "/微值/" + "noteimg" + (position_2 + 1) + ".jpg", b));
            if (imgFileLst.size() == MainActivity.mSelectedImage.size()) {
                LogUtils.e("imgFileLst123", "成功压碎图片准备上传" + String.valueOf(imgFileLst.size()));
                sendNote(null, imgFileLst);
            } else {
                position_2++;
                reduceBitmap();
            }
        } else {
            Luban.get(this)
                    .load(file)
                    .putGear(Luban.THIRD_GEAR)
                    .setFilename("noteimg" + (position_2 + 1))
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            LogUtils.e("imgFileLst123", "开始压缩图片" + position_2);
                        }

                        @Override
                        public void onSuccess(File file) {
                            imgFileLst.add(file);
                            LogUtils.e("imgFileLst123", "成功压碎图片" + String.valueOf(imgFileLst.size()));
                            if (imgFileLst.size() == MainActivity.mSelectedImage.size()) {
                                LogUtils.e("imgFileLst123", "成功压碎图片准备上传" + String.valueOf(imgFileLst.size()));
                                sendNote(null, imgFileLst);
                            } else {
                                position_2++;
                                reduceBitmap();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.e("imgFileLst123", "压缩图片失败" + position_2);
                        }
                    }).launch();
        }


    }


}


