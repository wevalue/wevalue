package com.wevalue.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wevalue.R;

/**
 * Created by xox on 2017/3/8.
 */

public class HeadView extends LinearLayout{

    public RelativeLayout title_layout;

    public ImageView iv_back; //返回
    public ImageView iv_search;//搜索

    public TextView tv_head_title;//标题

    public TextView tv_head_right;//右边文字
    public ImageView iv_share_note;//分享
    public ImageView iv_more;//更多

    public HeadView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_head ,this,true);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_head,this,true);
    }

    public void bindViews( String title) {
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_head_title = (TextView) findViewById(R.id.tv_head_title);
        tv_head_right = (TextView) findViewById(R.id.tv_head_right);
        iv_share_note = (ImageView) findViewById(R.id.iv_share_note);
        iv_more = (ImageView) findViewById(R.id.iv_more);

        tv_head_title.setText(title);
        iv_search.setVisibility(GONE);
        tv_head_right.setVisibility(GONE);
        iv_share_note.setVisibility(GONE);
        iv_more.setVisibility(GONE);
        iv_search.setVisibility(GONE);
    }

    /**
     * 透明背景 只有返回和分享
     */
    public void setTranTitle(){
        tv_head_title.setVisibility(GONE);
        iv_share_note.setVisibility(VISIBLE);
        title_layout.setBackgroundResource(R.color.transparent);
        iv_back.setImageResource(R.mipmap.iv_back_black);
        iv_share_note.setImageResource(R.mipmap.iv_shared_black);
    }

    public ImageView getBack() {
        return iv_back;
    }
    public ImageView getSearch() {
        return iv_search;
    }

    public TextView getTitle() {
        return tv_head_title;
    }

    public ImageView getMore() {
        return iv_more;
    }

    public TextView getTvRight() {
        return tv_head_right;
    }

    public ImageView getShareNote() {
        return iv_share_note;
    }
}
