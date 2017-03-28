package com.wevalue.ui.world.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

import java.util.ArrayList;


/**
 * 搜索界面
 *
 * Created by xuhua on 2016/8/16.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, PopClickInterface {

    private TextView tv_clear;
    private ArrayList<String> World;//世界
    private EditText ed_sousuo;
    private ImageView tv_back;
    private int isWho = 3 ;//默认是综合 =3 ， 1 用户 ，  2 信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        infoView();
        initView();
    }
    TextView tv_zonghe,tv_info,tv_user;
    private void initView() {
        tv_zonghe = (TextView) findViewById(R.id.tv_zonghe);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_user = (TextView) findViewById(R.id.tv_user);

        tv_zonghe.setOnClickListener(this);
        tv_info.setOnClickListener(this);
        tv_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                finish();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_zonghe:
                tv_zonghe.setTextColor(getResources().getColor(R.color.white));
                tv_zonghe.setBackgroundResource(R.drawable.shape_round5_frame_right_full_blue);
                tv_info.setBackgroundResource(R.drawable.shape_frame_blue);
                tv_info.setTextColor(getResources().getColor(R.color.main_color));
                tv_user.setBackgroundResource(R.drawable.shape_round5_frame_left_blue);
                tv_user.setTextColor(getResources().getColor(R.color.main_color));
                isWho = 3;
                break;
            case R.id.tv_info:
                tv_zonghe.setTextColor(getResources().getColor(R.color.main_color));
                tv_zonghe.setBackgroundResource(R.drawable.shape_round5_frame_right_blue);
                tv_info.setBackgroundResource(R.drawable.shape_frame_full_blue);
                tv_info.setTextColor(getResources().getColor(R.color.white));
                tv_user.setBackgroundResource(R.drawable.shape_round5_frame_left_blue);
                tv_user.setTextColor(getResources().getColor(R.color.main_color));
                isWho = 2;
                break;
            case R.id.tv_user:
                tv_zonghe.setTextColor(getResources().getColor(R.color.main_color));
                tv_zonghe.setBackgroundResource(R.drawable.shape_round5_frame_right_blue);

                tv_info.setBackgroundResource(R.drawable.shape_frame_blue);
                tv_info.setTextColor(getResources().getColor(R.color.main_color));

                tv_user.setBackgroundResource(R.drawable.shape_round5_frame_left_full_blue);
                tv_user.setTextColor(getResources().getColor(R.color.white));

                isWho = 1;
                break;
        }
    }

    private void infoView() {
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_back = (ImageView) findViewById(R.id.tv_back);
        tv_clear.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        ed_sousuo = (EditText) findViewById(R.id.ed_sousuo);
        ed_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 11) {
                    ShowUtil.showToast(SearchActivity.this, "十一字以内！");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String content = ed_sousuo.getText().toString().trim();
                    LogUtils.e("content =++++++=" + content);
                    if (TextUtils.isEmpty(content)) {
                        ShowUtil.showToast(SearchActivity.this, "搜索内容不得为空");
                        return false;
                    }
                    Intent intent = new Intent(SearchActivity.this, SearchJieguoActivity.class);
                    intent.putExtra("isWho", isWho);
                    intent.putExtra("content", content);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClickOk(String content) {

    }


}


