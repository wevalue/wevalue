package com.wevalue.ui.world.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.EmotionBean;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.world.adapter.GridAdapter;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhua on 2016/8/15.
 */
public class EmoTionActivity extends BaseActivity implements View.OnClickListener, WZHttpListener {


    private TextView tv_send_note;
    private TextView tv_back;
    private TextView tv_nickname;
    private TextView tv_head_title;
    private NoteRequestBase mNoteRequestBase;


    private EditText ed_zhuanfa;
    private ImageView iv_back;
    public GridView gridView;
    private String noteId;

    //    private List<String> name;
    private List<EmotionBean> name;
    private GridAdapter adapter;
    private int index = -1;
    private String repostid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        if (getIntent() != null) {
            noteId = getIntent().getStringExtra("noteId");
            repostid = getIntent().getStringExtra("repostid");
        } else {
            noteId = "1";
        }
        mNoteRequestBase = NoteRequestBase.getNoteRequestBase(this);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        gridView = (GridView) findViewById(R.id.id_gridView);
        ed_zhuanfa = (EditText) findViewById(R.id.ed_zhuanfa);
        ed_zhuanfa.setFocusable(false);
        ed_zhuanfa.setFocusableInTouchMode(false);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
/*标题控件*/
        tv_head_title.setText("阅读后情绪");
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        tv_send_note.setText("发表");
        tv_back.setText("取消");

        tv_send_note.setOnClickListener(this);
        tv_back.setOnClickListener(this);

        name = new ArrayList<>();
        name.add(new EmotionBean("喜爱", "0",R.mipmap.biaoqing_xiai));
        name.add(new EmotionBean("感动", "0",R.mipmap.biaoqing_gandong));
        name.add(new EmotionBean("同情", "0",R.mipmap.biaoqing_tongqing));
        name.add(new EmotionBean("愤怒", "0",R.mipmap.biaoqing_fennu));
        name.add(new EmotionBean("恐惧", "0",R.mipmap.biaoqing_kongju));
        name.add(new EmotionBean("炫酷", "0",R.mipmap.biaoqing_xuanku));
        name.add(new EmotionBean("搞笑", "0",R.mipmap.biaoqing_gaoxiao));
        name.add(new EmotionBean("震撼", "0",R.mipmap.biaoqing_zhenhan));
        adapter = new GridAdapter(this, name);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                for (int i = 0; i < name.size(); i++) {
                    if (i != position) {
                        name.get(i).setIsClick("0");
                    } else {
                        name.get(i).setIsClick("1");
                        ed_zhuanfa.setText(name.get(i).getName());
                    }
                }
                index = position;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.ed_zhuanfa:
                break;
            case R.id.tv_send_note:
                if (index != -1) {
                    mNoteRequestBase.postNoteEmotion(repostid, noteId, name.get(index).getName(), this);
                } else {
                    ShowUtil.showToast(this, "请选择情绪");
                }
                break;

        }
    }

    @Override
    public void onSuccess(String content, String isUrl) {

        try {
            JSONObject json = new JSONObject(content);
            if (json.getString("result").equals("1")) {
                ShowUtil.showToast(this, json.getString("message"));
                setResult(RESULT_OK);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onFailure(String content) {

    }
}

