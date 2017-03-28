package com.wevalue.ui.details.adapter;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.NoScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ReplyCommentActivity extends BaseActivity implements WZHttpListener, View.OnClickListener {
    private NoScrollGridView nsgv_send_note_gridview;
    private TextView tv_back;
    private TextView tv_head_title;
    private TextView tv_send_note;
    private EditText et_content;

    private int width;
    private int height;

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
        setContentView(R.layout.activity_comments);
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
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
//        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
//        tv_head_title.setText("回复评论");
//        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
//        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("回复评论");
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
    }

    private void submit() {
        // validate
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content) && MainActivity.mSelectedImage.size() == 0) {
            Toast.makeText(this, "回复内容不能为空哦", Toast.LENGTH_SHORT).show();
            return;
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
        mProgressDialog.show();
        NetworkRequest.postRequest(RequestPath.POST_ADDCOMMENT, map, this);
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
