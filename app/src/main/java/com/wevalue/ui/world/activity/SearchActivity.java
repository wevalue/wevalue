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
 * Created by xuhua on 2016/8/16.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener, PopClickInterface {

    private TextView tv_head_title;
    private ArrayList<String> World;//世界
    private EditText ed_sousuo;
    private ImageView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        infoView();
        initHualunData();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_head_title:
                String position = "";
                switch (tv_head_title.getText().toString().trim()) {
                    case "信息":
                        position = "2";
                        break;
                    case "用户":
                        position = "1";
                        break;
                }
//                PopuUtil.initSearchPopu(this, tv_head_title, position, this);
                initHuaLunPicker(World);
                pvOptions.show();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private void infoView() {
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_back = (ImageView) findViewById(R.id.tv_back);
        tv_head_title.setOnClickListener(this);
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
                    String s1 = tv_head_title.getText().toString().trim();
                    String content = ed_sousuo.getText().toString().trim();
                    LogUtils.e("content =++++++=" + content);
                    if (TextUtils.isEmpty(content)) {
                        ShowUtil.showToast(SearchActivity.this, "搜索内容不得为空");
                        return false;
                    }
                    Intent intent = new Intent(SearchActivity.this, SearchJieguoActivity.class);
                    if (s1.equals("用户")) {
                        intent.putExtra("isWho", 1);
                    } else if (s1.equals("信息")) {
                        intent.putExtra("isWho", 2);
                    } else if (s1.equals("综合")) {
                        intent.putExtra("isWho", 3);
                    }
                    intent.putExtra("content", content);
                    startActivity(intent);
                    return true;
                }

//                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    String s1 = tv_head_title.getText().toString().trim();
//                    String content = ed_sousuo.getText().toString().trim();
//                    LogUtils.e("content =++++++=" + content);
//                    if (TextUtils.isEmpty(content)) {
//                        ShowUtil.showToast(SearchActivity.this, "搜索内容不得为空");
//                        return false;
//                    }
//                    Intent intent = news Intent(SearchActivity.this, SearchJieguoActivity.class);
//                    if (s1.equals("用户")) {
//                        intent.putExtra("isWho", 1);
//                    } else if (s1.equals("信息")) {
//                        intent.putExtra("isWho", 2);
//                    } else if (s1.equals("综合")) {
//                        intent.putExtra("isWho", 3);
//                    }
//                    intent.putExtra("content", content);
//                    startActivity(intent);
//                    return true;
//                }
                return false;
            }
        });
    }

    @Override
    public void onClickOk(String content) {

    }

    private OptionsPickerView pvOptions;

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
                tv_head_title.setText(World.get(options1));
            }
        });
    }

    /**
     * 初始化数据
     **/
    public void initHualunData() {
        World = new ArrayList<>();
        World.add("综合");
        World.add("信息");
        World.add("用户");
    }
}


