package com.wevalue.ui.add.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.ui.world.activity.SearchActivity;
import com.wevalue.ui.world.activity.SearchJieguoActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.ShowUtil;

/**添加好友
 * Created by Administrator on 2016-08-11.
 */
public class AddFriendsActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_back;
    private TextView tv_head_title;
    private RelativeLayout rl_saoyisao;
    private RelativeLayout rl_tongxunlu;
    private EditText et_search_friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("添加好友");
        rl_tongxunlu = (RelativeLayout) findViewById(R.id.rl_tongxunlu);
        rl_saoyisao = (RelativeLayout) findViewById(R.id.rl_saoyisao);
        et_search_friends = (EditText) findViewById(R.id.et_search_friends);

        et_search_friends.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    String content = et_search_friends.getText().toString().trim();
                    LogUtils.e("content =++++++="+content);
                    if (TextUtils.isEmpty(content)) {
                        ShowUtil.showToast(AddFriendsActivity.this,"所有内容不得为空");
                        return false;
                    }
                    Intent intent = new Intent(AddFriendsActivity.this, SearchJieguoActivity.class);
                    intent.putExtra("isWho", 1);
                    intent.putExtra("content", content);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        rl_tongxunlu.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rl_saoyisao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_back:
                finish();
            break;
            case R.id.rl_saoyisao:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
                } else {//已授权
                    intent = new Intent(this, CaptureActivity.class);
                    startActivity(intent);
                }
            break;
            case R.id.rl_tongxunlu:
                intent = new Intent(this, AddFromContactsActivity.class);
                startActivity(intent);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK&&requestCode==1){
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivity(intent);
        }
    }
}
