package com.wevalue.ui.we.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.adapter.NoScrollGridView_2_Adapter;
import com.wevalue.ui.world.activity.PicChoiceActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.utils.ZipBitmapUtil;
import com.wevalue.view.ActionSheetDialog;
import com.wevalue.view.NoScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ReplyCommentActivity extends BaseActivity implements WZHttpListener, View.OnClickListener {
    private NoScrollGridView nsgv_send_note_gridview;
    private TextView tv_back;
    private TextView tv_head_title;
    private TextView tv_nickname;
    private TextView tv_send_note;
    private EditText et_content;
    private Long suijishu;
    private static final String PHOTO_FILE_NAME = "wevalue_img.jpg";
    private int width;
    private int height;
    private Bitmap bitmap;
    private List<Bitmap> mImgList;
    private NoScrollGridView_2_Adapter mAdapter;
    private String noteId;
    private String repostId;
    private String messState;
    private String replyuserid;
    private String replycommid;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment);
        noteId = getIntent().getStringExtra("noteid");
        repostId = getIntent().getStringExtra("repostid");
        messState = getIntent().getStringExtra("messstate");
        replycommid = getIntent().getStringExtra("replycommid");
        replyuserid = getIntent().getStringExtra("replyuserid");
        /**获取频幕宽高*/
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在回复评论...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initGridViewData();
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("回复评论");
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
        tv_send_note.setOnClickListener(this);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        et_content.setHint(R.string.hint_send_note_content_2);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 300) {
                    ShowUtil.showToast(ReplyCommentActivity.this, "100字以内");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nsgv_send_note_gridview = (NoScrollGridView) findViewById(R.id.nsgv_send_note_gridview);
        nsgv_send_note_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == MainActivity.mSelectedImage.size()) {
                    addImg("手机拍照", "相册选择");
                }
            }
        });
        initGridViewData();
    }

    private void submit() {
        // validate
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content) && MainActivity.mSelectedImage.size() == 0) {
            Toast.makeText(this, "回复内容不能为空哦", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something
        List<File> imgFileLst = new ArrayList<>();
        for (int i = 0; i < MainActivity.mSelectedImage.size(); i++) {
            File imgFile = new File(MainActivity.mSelectedImage.get(i));
            imgFileLst.add(imgFile);
        }
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("repostid", repostId);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("noteid", noteId);
        map.put("commcontent", content);
        map.put("commstate", "0");
        map.put("replyuserid", replyuserid);
        map.put("replycommid", replycommid);

        if (imgFileLst != null && imgFileLst.size() > 0) {
            for (int i = 0; i < imgFileLst.size(); i++) {
                map.put("noteimg" + (i + 1), imgFileLst.get(i));
            }
        }
        mProgressDialog.show();
        NetworkRequest.postRequest(RequestPath.POST_ADDCOMMENT, map, this);
    }

    private void addImg(String str, String str2) {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(str, ActionSheetDialog.SheetItemColor.Grey,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                suijishu = System.currentTimeMillis();
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera", suijishu + PHOTO_FILE_NAME)));
                                startActivityForResult(intent, 2);
                            }
                        }).addSheetItem(str2, ActionSheetDialog.SheetItemColor.Grey, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent(ReplyCommentActivity.this, PicChoiceActivity.class);
                intent.putExtra("sendAct", 2);
                intent.putExtra("choiceLimit", 1);
                startActivity(intent);
            }
        }).show();
    }

    /**
     * 初始化gridview数据
     */
    private void initGridViewData() {
        if (MainActivity.mSelectedImage == null) {
            MainActivity.mSelectedImage = new LinkedList<>();
        }
        if (mImgList != null && mAdapter != null) {
            for (int i = mImgList.size(); i < MainActivity.mSelectedImage.size(); i++) {
                LogUtils.e("url --" + MainActivity.mSelectedImage.get(i));
                Bitmap b = BitmapFactory.decodeFile(MainActivity.mSelectedImage.get(i));
                Bitmap newB = ZipBitmapUtil.reduce(b, width, height, true);
                mImgList.add(newB);
            }
            mAdapter.setmList(mImgList);
            mAdapter.notifyDataSetChanged();
        } else {
            mImgList = new ArrayList<>();
            for (int i = 0; i < MainActivity.mSelectedImage.size(); i++) {
                LogUtils.e("url --" + MainActivity.mSelectedImage.get(i));
                Bitmap b = BitmapFactory.decodeFile(MainActivity.mSelectedImage.get(i));
                Bitmap newB = ZipBitmapUtil.reduce(b, width, height, true);
                mImgList.add(newB);
            }
            mAdapter = new NoScrollGridView_2_Adapter(this);
            mAdapter.setmList(mImgList);
            mAdapter.notifyDataSetChanged();
            nsgv_send_note_gridview.setAdapter(mAdapter);
            nsgv_send_note_gridview.setVisibility(View.VISIBLE);
        }
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
            mAdapter.setmList(mImgList);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  1 = 拍照, 2 = 选择照片, 3 = 录音 ,4= 本地音频, 5 =视频录制, 6 = 本地视频
        if (resultCode == RESULT_OK) {
            LogUtils.e("---------------" + requestCode);
            switch (requestCode) {
                case 2:
                    File tempFile = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/", suijishu + PHOTO_FILE_NAME);
                    LogUtils.e("123321", tempFile.getAbsolutePath());
                    MainActivity.mSelectedImage.add(tempFile.getAbsolutePath());
                    try {
                        //FileInputStream is = news FileInputStream(tempFile);
                        Bitmap b = BitmapFactory.decodeFile(tempFile.getAbsolutePath().trim());
                        bitmap = ZipBitmapUtil.reduce(b, width, height, true);
                        if (WeValueApplication.phoneName.equals("samsung")) {
                            bitmap = ZipBitmapUtil.rotate(b, 90);
                        }
                        mImgList.add(bitmap);
                        mAdapter.setmList(mImgList);
                        mAdapter.notifyDataSetChanged();
                        LogUtils.e("123321", bitmap.getByteCount() + "size");
//                        iv_img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        JSONObject object = null;
        String message = null;
        String result = "";
        try {
            object = new JSONObject(content);
            message = object.getString("message");
            result = object.getString("result");
            ShowUtil.showToast(this, message);
            setResult(RESULT_OK);
            mProgressDialog.dismiss();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String content) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_send_note:
                submit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.mSelectedImage != null) {
            MainActivity.mSelectedImage.clear();
        }
    }
}
