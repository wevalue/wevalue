package com.wevalue.ui.details.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.activity.PicChoiceActivity;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.ActionSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends BaseActivity implements WZHttpListener, View.OnClickListener {
    private TextView tv_back;
    private TextView tv_send_note;
    private EditText et_content;
    private Long suijishu;
    private static final String PHOTO_FILE_NAME = "wevalue_img.jpg";
    private int width;
    private int height;
    private String noteId;
    private String repostId;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        noteId = getIntent().getStringExtra("noteid");
        repostId = getIntent().getStringExtra("repostid");

        /**获取频幕宽高*/
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在发布评论...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
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
                if (charSequence.length() == 100) {
                    ShowUtil.showToast(CommentActivity.this, "100字以内");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void submit() {
        // validate
        List<File> imgFileLst = new ArrayList<>();
        for (int i = 0; i < MainActivity.mSelectedImage.size(); i++) {
            File imgFile = new File(MainActivity.mSelectedImage.get(i));
            imgFileLst.add(imgFile);
        }
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content) && imgFileLst.isEmpty()) {
            Toast.makeText(this, "评论内容不能为空哦", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO validate success, do something

        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("noteid", noteId);
        map.put("commcontent", content);
        map.put("commstate", "0");

        map.put("repostid", repostId);

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
                Intent intent = new Intent(CommentActivity.this, PicChoiceActivity.class);
                intent.putExtra("sendAct", 2);
                intent.putExtra("choiceLimit", 1);
                startActivity(intent);
            }
        }).show();
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
            case R.id.tv_add_pic:
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
