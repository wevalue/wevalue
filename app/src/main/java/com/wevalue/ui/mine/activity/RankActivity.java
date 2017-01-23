package com.wevalue.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.base.BaseActivity;

//周排行榜的activity
public class RankActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_earnings_rank;
    private LinearLayout ll_transmit_rank;
    private LinearLayout ll_content_rank;
    private ImageView iv_back;
    private TextView tv_head_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pai_hang_bang);
        initView();
    }
    private void initView() {
        ll_earnings_rank = (LinearLayout) findViewById(R.id.ll_earnings_rank);
        ll_transmit_rank = (LinearLayout) findViewById(R.id.ll_transmit_rank);
        ll_content_rank = (LinearLayout) findViewById(R.id.ll_content_rank);
        ll_content_rank.setOnClickListener(this);
        ll_transmit_rank.setOnClickListener(this);
        ll_earnings_rank.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_title.setText("周排行榜");
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RankActivity.this, RankDetailsActivity.class);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_content_rank:
                intent.putExtra("type", "3");
                startActivity(intent);
                break;
            case R.id.ll_transmit_rank:
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.ll_earnings_rank:
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
        }
    }
}
