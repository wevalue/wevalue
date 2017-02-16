package com.wevalue.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.ChannelBean;
import com.wevalue.ui.login.adapter.DragAdapter;
import com.wevalue.ui.login.adapter.TypeChoice_AlltypeAdapter;
import com.wevalue.ui.world.activity.MyWishActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.DragGrid;
import com.wevalue.view.NoScrollGridView;
import com.wevalue.youmeng.StatisticsConsts;

import java.util.ArrayList;
import java.util.List;

/** 我的频道
 * Created by Administrator on 2016-07-08.
 */
public class TypeChoiceActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView iv_back;
    private TextView tv_nickname;
    private TextView tv_next_step;
    private TextView tv_edit_type;
    private TextView tv_send_note;
    private TypeChoice_AlltypeAdapter otherAdapter;
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    DragAdapter userAdapter;
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;

    /**
     * 其它栏目的GRIDVIEW
     */
    private NoScrollGridView otherGridView;
    /**
     * 用户栏目的GRIDVIEW
     */
    private DragGrid userGridView;

    private List<ChannelBean.Channel> otherChannelList;
    private List<ChannelBean.Channel> userChannelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_choice);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_next_step = (TextView) findViewById(R.id.tv_next_step);
        tv_edit_type = (TextView) findViewById(R.id.tv_edit_type);
        tv_send_note = (TextView) findViewById(R.id.tv_send_note);
//        tv_head_title.setText("选择分类");
        tv_send_note.setVisibility(View.VISIBLE);
        tv_send_note.setText("编辑");

        otherGridView = (NoScrollGridView) findViewById(R.id.otherGridView);
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);


        iv_back.setOnClickListener(this);
        tv_send_note.setOnClickListener(this);
        tv_next_step.setOnClickListener(this);
        tv_edit_type.setOnClickListener(this);
        tv_edit_type.setVisibility(View.GONE);
        tv_nickname.setText(SharedPreferencesUtil.getNickname(this));
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Gson gson = new Gson();
        ChannelBean channelBean = gson.fromJson(SharedPreferencesUtil.getAllChannel(this), ChannelBean.class);
        if (channelBean != null && channelBean.getResult() != null && channelBean.getResult().equals("1")) {
            if (channelBean.getData() != null) {
                otherChannelList = channelBean.getData();
                userChannelList = new ArrayList<>();
                String userLike = SharedPreferencesUtil.getUserlike(this);
                if (!TextUtils.isEmpty(userLike)) {
                    String[] typeArray = userLike.split(",");
                    for (int i = 0; i < typeArray.length; i++) {
                        String name = typeArray[i];
                        for (int j = 0; j < otherChannelList.size(); j++) {
                            if (otherChannelList.get(j).getTypename().equals(name)) {
                                if (name.equals("推荐")) {
                                    userChannelList.add(0, otherChannelList.get(j));
                                }else {
//                                    if (name.equals("视频")) {
//                                        userChannelList.add(1, otherChannelList.get(j));
//                                    } else {
//                                        userChannelList.add(otherChannelList.get(j));
//                                    }
                                    userChannelList.add(otherChannelList.get(j));
                                }
                                otherChannelList.remove(j);
                                break;
                            }
                        }
                    }
                } else {
                    String[] typeArray = {"推荐", "视频", "地区", "搞笑", "财经", "体育", "星座"};
                    for (int i = 0; i < typeArray.length; i++) {
                        String name = typeArray[i];
                        for (int j = 0; j < otherChannelList.size(); j++) {
                            if (otherChannelList.get(j).getTypename().equals(name)) {
                                if (name.equals("推荐")) {
                                    userChannelList.add(0, otherChannelList.get(j));
                                } else {
                                    userChannelList.add(otherChannelList.get(j));
                                }
                                otherChannelList.remove(j);
                                break;
                            }
                        }

                    }
                }

                otherAdapter = new TypeChoice_AlltypeAdapter(otherChannelList, TypeChoiceActivity.this);
                userAdapter = new DragAdapter(TypeChoiceActivity.this, userChannelList, tv_send_note);

                otherAdapter.notifyDataSetChanged();
                otherGridView.setAdapter(otherAdapter);

                userAdapter.notifyDataSetChanged();
                userGridView.setAdapter(userAdapter);
            }
        }
    }

    /**
     * 我的频道删除按钮点击事件
     */
    public void delClick(int position) {

        otherChannelList.add(userChannelList.get(position));
        userChannelList.remove(position);
        otherAdapter.notifyDataSetChanged();
        userAdapter.notifyDataSetChanged();
    }


    /**
     * 点击事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next_step:

                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(this))) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, MyWishActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_send_note:
                if (userAdapter == null) {
                    return;
                }
                setUserLike();
                if (userAdapter.getIsEdit()) {
                    userAdapter.setIsEdit(false);
                    userAdapter.notifyDataSetChanged();
//                    finish();
                } else {
                    userAdapter.setIsEdit(true);
                    userAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    /**
     * 保存  选择好的频道
     */
    public void setUserLike() {
        if (userChannelList == null || userChannelList.size() == 0) {
            return;
        }
        String str = "";
        for (int i = 0; i < userChannelList.size(); i++) {
            if (i == userChannelList.size() - 1) {
                str += userChannelList.get(i).getTypename();
            } else {
                str += userChannelList.get(i).getTypename() + ",";
            }
        }
        LogUtils.e("str==" + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        SharedPreferencesUtil.setUserlike(this, str);
    }

    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0，1 的不可以进行任何操作
                if (userAdapter.getIsEdit()) {
                    if (position != 0 && position != 1) {
                        final ImageView moveImageView = getView(view);
                        if (moveImageView != null) {
                            TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                            final int[] startLocation = new int[2];
                            newTextView.getLocationInWindow(startLocation);
                            final ChannelBean.Channel channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                            otherAdapter.setVisible(false);
                            //添加到最后一个
                            otherAdapter.addItem(channel);
                            LogUtils.e("我的频道--------item点击了--");
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    try {
                                        int[] endLocation = new int[2];
                                        //获取终点的坐标
                                        LogUtils.e("删除的handler  我的频道----------");
                                        otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                        MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                        userAdapter.setRemove(position);
                                        LogUtils.e("删除的handler  我的频道");
                                    } catch (Exception localException) {
                                    }
                                }
                            }, 50L);
                        } else {

                            LogUtils.e("  moveImageView--------是 null--");
                        }
                    }
                }

                break;
            case R.id.otherGridView:
                if (userAdapter.getCount() == 20) {
                    LogUtils.e("  所有频道--------item点击了--");
                    ShowUtil.showToast(this, "最多选择二十个频道!");
                } else {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelBean.Channel channel = ((TypeChoice_AlltypeAdapter) parent.getAdapter()).getItem(position);
                        userAdapter.setVisible(false);
                        //添加到最后一个
                        userAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                    otherAdapter.setRemove(position);
                                    setUserLike();
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelBean.Channel moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }
}
